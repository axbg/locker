package locker.service.impl;

import locker.event.OperationMode;
import locker.object.Preference;
import locker.service.CryptoService;
import locker.service.PreferenceService;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PreferenceServiceImpl implements PreferenceService {
    private static final String PREFERENCE_FILE_LOCATION = ".locker";
    private static final String PREFERENCE_FILE_BACKUP_PASSWORD = "no-localhost-name";
    private static final String EXPORTED_PREFERENCES_FILE_NAME = "locker_exported_preferences.bin";

    private final Path lockerHomePath;
    private final CryptoService cryptoService;
    private final Map<String, Preference> preferences;
    private final String preferenceFilePassword;

    public PreferenceServiceImpl(CryptoService cryptoService) {
        this.lockerHomePath = Path.of(System.getProperty("user.home"), PREFERENCE_FILE_LOCATION);
        this.cryptoService = cryptoService;
        this.preferences = new LinkedHashMap<>();

        String password = PREFERENCE_FILE_BACKUP_PASSWORD;
        try {
            password = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException ignored) {
        }

        this.preferenceFilePassword = password;

        this.loadPreferencesFromDisk(this.preferenceFilePassword, this.lockerHomePath, false);
    }

    @Override
    public List<String> getPreferencesNames() {
        return new ArrayList<>(this.preferences.keySet());
    }

    @Override
    public Preference getPreference(String name) {
        return this.preferences.get(name);
    }

    @Override
    public void savePreference(Preference preference) {
        this.cryptoService.initCipher(this.preferenceFilePassword, OperationMode.ENCRYPT);
        String encryptedPreference = this.cryptoService.doNameOperation(preference.toString()) + "\n";

        try {
            Files.write(this.lockerHomePath, encryptedPreference.getBytes(),
                    this.lockerHomePath.toFile().exists() ? StandardOpenOption.APPEND : StandardOpenOption.CREATE_NEW);
        } catch (IOException ignored) {
        }

        this.preferences.put(preference.getName(), preference);
    }

    @Override
    public void removePreference(String name) {
        Map<String, Preference> filteredPreferences = this.preferences.values().stream()
                .filter(preference -> !name.equals(preference.getName()))
                .collect(Collectors.toMap(Preference::getName, preference -> preference));

        this.savePreferencesToDisk(filteredPreferences, this.preferenceFilePassword, this.lockerHomePath);
        this.preferences.remove(name);
    }

    @Override
    public boolean importPreferences(String password, File file) {
        this.loadPreferencesFromDisk(this.preferenceFilePassword, Path.of(file.getAbsolutePath()), true);
        return true;
    }

    @Override
    public boolean exportPreferences(String password, File file) {
        this.savePreferencesToDisk(this.preferences, this.preferenceFilePassword, Path.of(file.getAbsolutePath(), EXPORTED_PREFERENCES_FILE_NAME));
        return true;
    }

    private void loadPreferencesFromDisk(String password, Path path, boolean save) {
        this.cryptoService.initCipher(password, OperationMode.DECRYPT);

        if (path.toFile().exists()) {
            try {
                for (String line : Files.readAllLines(path)) {
                    String decryptedLine = this.cryptoService.doNameOperation(line);
                    Preference preference = new Preference(decryptedLine);

                    if (save) {
                        this.savePreference(preference);
                        this.cryptoService.initCipher(password, OperationMode.DECRYPT);
                    } else {
                        this.preferences.put(preference.getName(), preference);
                    }
                }
            } catch (IOException ignored) {
            }
        }
    }

    private void savePreferencesToDisk(Map<String, Preference> preferences, String password, Path path) {
        this.cryptoService.initCipher(password, OperationMode.ENCRYPT);

        byte[] content = preferences.values().stream()
                .map(preference -> this.cryptoService.doNameOperation(preference.toString()) + "\n")
                .reduce("", (result, preference) -> result + preference)
                .getBytes();

        try {
            Files.write(path, content);
        } catch (IOException ignored) {
        }
    }
}

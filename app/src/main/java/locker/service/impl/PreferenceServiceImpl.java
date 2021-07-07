package locker.service.impl;

import locker.event.OperationMode;
import locker.object.Preference;
import locker.service.CryptoService;
import locker.service.PreferenceService;
import org.springframework.stereotype.Service;

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

@Service
public class PreferenceServiceImpl implements PreferenceService {
    private static final String PREFERENCE_FILE_LOCATION = ".locker";
    private static final String PREFERENCE_FILE_BACKUP_PASSWORD = "no-localhost-name";

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

        this.loadAvailablePreferences();
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
        this.cryptoService.initCipher(this.preferenceFilePassword, OperationMode.ENCRYPT);
        byte[] content = this.preferences.values().stream()
                .filter(preference -> !name.equals(preference.getName()))
                .map(preference -> this.cryptoService.doNameOperation(preference.toString()) + "\n")
                .reduce("", (result, preference) -> result + preference)
                .getBytes();

        try {
            Files.write(this.lockerHomePath, content);
        } catch (IOException ignored) {
        }

        this.preferences.remove(name);
    }

    private void loadAvailablePreferences() {
        this.cryptoService.initCipher(this.preferenceFilePassword, OperationMode.DECRYPT);

        if (this.lockerHomePath.toFile().exists()) {
            try {
                for (String line : Files.readAllLines(lockerHomePath)) {
                    String decryptedLine = this.cryptoService.doNameOperation(line);
                    this.preferences.put(decryptedLine.split("\r\n")[0], new Preference(decryptedLine));
                }
            } catch (IOException ignored) {
            }
        }
    }
}

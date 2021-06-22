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
import java.util.ArrayList;
import java.util.HashMap;
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
        this.preferences = new HashMap<>();

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
    }

    @Override
    public void removePreference(String name) {
    }

    // format: name/nsource/ndestination/npassword/nopMode
    private void loadAvailablePreferences() {
        this.cryptoService.initCipher(this.preferenceFilePassword, OperationMode.DECRYPT);

        if (this.lockerHomePath.toFile().exists()) {
            try {
                for (String line : Files.readAllLines(lockerHomePath)) {
                    String decryptedLine = this.cryptoService.doNameOperation(line);
                    this.preferences.put(decryptedLine.split("\n")[0], new Preference(decryptedLine));
                }
            } catch (IOException ignored) {
            }
        }
    }
}

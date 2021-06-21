package locker.service.impl;

import locker.object.Preference;
import locker.service.CryptoService;
import locker.service.PreferenceService;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

@Service
public class PreferenceServiceImpl implements PreferenceService {
    private static final String PREFERENCE_FILE_LOCATION = "~/.locker";
    private static final String PREFERENCE_FILE_BACKUP_PASSWORD = "no-localhost-name";

    private final CryptoService cryptoService;
    private final List<String> availablePreferences;
    private final String preferenceFilePassword;

    public PreferenceServiceImpl(CryptoService cryptoService) {
        this.cryptoService = cryptoService;
        this.availablePreferences = this.loadAvailablePreferences();

        String password = PREFERENCE_FILE_BACKUP_PASSWORD;
        try {
            password = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException ignored) {
        }

        this.preferenceFilePassword = password;
    }

    @Override
    public List<String> getAvailablePreferences() {
        return this.availablePreferences;
    }

    @Override
    public Preference getPreference(String name) {
        return null;
    }

    @Override
    public void savePreference(Preference preference) {
    }

    @Override
    public void removePreference(int position) {
    }

    private List<String> loadAvailablePreferences() {
        return null;
    }
}

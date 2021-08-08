package locker.service.impl;

import locker.event.OperationMode;
import locker.exception.AppException;
import locker.object.Preference;
import locker.service.CryptoService;
import locker.service.PreferenceService;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PreferenceServiceImpl implements PreferenceService {
    public static final String PREFERENCE_DECRYPTION_ERROR = "Error occurred during preferences decryption";
    public static final String PREFERENCE_BAD_FILE_ERROR = "The imported file does not contain preferences";

    private static final String PREFERENCE_FILE_LOCATION = ".locker";
    private static final String PREFERENCE_FILE_BACKUP_PASSWORD = "no-localhost-name";
    private static final String EXPORTED_PREFERENCES_FILE_NAME = "locker_exported_preferences.bin";

    private final Path lockerHomePath;
    private final CryptoService cryptoService;
    private final String preferenceFilePassword;
    private final Map<String, Preference> preferences;

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
    }

    @Override
    public void loadInitialPreferences() throws AppException {
        this.loadPreferencesFromDisk(this.preferenceFilePassword, this.lockerHomePath, false, null);
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
    public void savePreference(Preference preference) throws AppException {
        this.cryptoService.initCipher(this.preferenceFilePassword, OperationMode.ENCRYPT);

        String stringPreference = preference.toString();
        String hash = new String(this.cryptoService.computeHash(stringPreference.getBytes()), StandardCharsets.ISO_8859_1);

        String encryptedHash = this.cryptoService.doNameOperation(hash);
        String encryptedPreference = this.cryptoService.doNameOperation(stringPreference);
        byte[] content = (encryptedHash + "\n" + encryptedPreference + "\n").getBytes();

        try {
            Files.write(this.lockerHomePath, content, this.lockerHomePath.toFile().exists() ? StandardOpenOption.APPEND : StandardOpenOption.CREATE_NEW);
        } catch (IOException ex) {
            throw new AppException("Error occurred while saving preference");
        }

        this.preferences.put(preference.getName(), preference);
    }

    @Override
    public void removePreference(String name) throws AppException {
        Map<String, Preference> filteredPreferences = this.preferences.values().stream()
                .filter(preference -> !name.equals(preference.getName()))
                .collect(Collectors.toMap(Preference::getName, preference -> preference));

        this.savePreferencesToDisk(filteredPreferences, this.preferenceFilePassword, this.lockerHomePath);
        this.preferences.remove(name);
    }

    @Override
    public void importPreferences(String password, File file, String prefix) throws AppException {
        prefix = (prefix != null && !prefix.isBlank()) ? (" - " + prefix) : "";
        this.loadPreferencesFromDisk(password, Path.of(file.getAbsolutePath()), true, prefix);
    }

    @Override
    public void exportPreferences(String password, File file) throws AppException {
        this.savePreferencesToDisk(this.preferences, password, Path.of(file.getAbsolutePath(), EXPORTED_PREFERENCES_FILE_NAME));
    }

    private void loadPreferencesFromDisk(String password, Path path, boolean save, String prefix) throws AppException {
        this.cryptoService.initCipher(password, OperationMode.DECRYPT);

        byte[] hash = null;
        boolean readFirst = false;

        if (path.toFile().exists()) {
            try {
                List<String> lines = Files.readAllLines(path);

                if (lines.isEmpty()) {
                    throw new AppException(PREFERENCE_DECRYPTION_ERROR);
                }

                for (String line : lines) {
                    String decryptedLine = this.cryptoService.doNameOperation(line);

                    if (decryptedLine == null) {
                        throw new AppException(PREFERENCE_DECRYPTION_ERROR);
                    }

                    if (!readFirst) {
                        hash = decryptedLine.getBytes(StandardCharsets.ISO_8859_1);
                    } else {
                        byte[] computedHash = this.cryptoService.computeHash(decryptedLine.getBytes());

                        if (!Arrays.equals(hash, computedHash)) {
                            throw new AppException(PREFERENCE_DECRYPTION_ERROR);
                        }

                        Preference preference = new Preference(decryptedLine);

                        if (save) {
                            preference.setName(preference.getName() + prefix);
                            this.savePreference(preference);
                            this.cryptoService.initCipher(password, OperationMode.DECRYPT);
                        } else {
                            this.preferences.put(preference.getName(), preference);
                        }
                    }

                    readFirst = !readFirst;
                }
            } catch (IOException ex) {
                throw new AppException(PREFERENCE_BAD_FILE_ERROR);
            }
        }
    }

    private void savePreferencesToDisk(Map<String, Preference> preferences, String password, Path path) throws AppException {
        this.cryptoService.initCipher(password, OperationMode.ENCRYPT);

        byte[] content = preferences.values().stream()
                .map(preference -> {
                    byte[] hash = this.cryptoService.computeHash(preference.toString().getBytes());
                    return this.cryptoService.doNameOperation(new String(hash, StandardCharsets.ISO_8859_1)) + "\n"
                            + this.cryptoService.doNameOperation(preference.toString()) + "\n";

                })
                .reduce("", (result, preference) -> result + preference)
                .getBytes();
        try {
            Files.write(path, content);
        } catch (IOException ex) {
            throw new AppException("Error occurred while updating preferences");
        }
    }
}

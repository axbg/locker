package locker.service;

import locker.exception.DecryptionException;
import locker.object.Preference;

import java.io.File;
import java.util.List;

public interface PreferenceService {
    List<String> getPreferencesNames();

    Preference getPreference(String name);

    void loadInitialPreferences() throws DecryptionException;

    void savePreference(Preference preference);

    void removePreference(String name);

    void importPreferences(String password, File file, String postfix) throws DecryptionException;

    void exportPreferences(String password, File file);
}

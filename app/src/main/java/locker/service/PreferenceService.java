package locker.service;

import locker.object.Preference;

import java.io.File;
import java.util.List;

public interface PreferenceService {
    List<String> getPreferencesNames();

    Preference getPreference(String name);

    void savePreference(Preference preference);

    void removePreference(String name);

    boolean importPreferences(String password, File file, String postfix);

    boolean exportPreferences(String password, File file);
}

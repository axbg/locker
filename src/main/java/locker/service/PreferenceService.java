package locker.service;

import locker.object.Preference;

import java.util.List;

public interface PreferenceService {
    List<String> getPreferencesNames();

    Preference getPreference(String name);

    void savePreference(Preference preference);

    void removePreference(String name);
}

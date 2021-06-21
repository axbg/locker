package locker.service;

import locker.object.Preference;

import java.util.List;

public interface PreferenceService {
    List<String> getAvailablePreferences();

    Preference getPreference(String name);

    void savePreference(Preference preference);

    void removePreference(int position);
}

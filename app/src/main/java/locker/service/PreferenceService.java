package locker.service;

import locker.exception.AppException;
import locker.object.Preference;

import java.io.File;
import java.util.List;

public interface PreferenceService {
    List<String> getPreferencesNames();

    Preference getPreference(String name);

    void loadInitialPreferences() throws AppException;

    void savePreference(Preference preference) throws AppException;

    void removePreference(String name) throws AppException;

    void importPreferences(String password, File file, String postfix) throws AppException;

    void exportPreferences(String password, File file) throws AppException;
}

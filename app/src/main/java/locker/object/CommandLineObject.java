package locker.object;

import locker.event.OperationMode;

import java.nio.file.Path;

public class CommandLineObject {
    Path source;
    Path destination;
    OperationMode operationMode;
    Preference preference;
    Preference pairPreference;
    String preferenceName;

    public Path getSource() {
        return source;
    }

    public void setSource(Path source) {
        this.source = source;
    }

    public Path getDestination() {
        return destination;
    }

    public void setDestination(Path destination) {
        this.destination = destination;
    }

    public OperationMode getOperationMode() {
        return operationMode;
    }

    public void setOperationMode(OperationMode operationMode) {
        this.operationMode = operationMode;
    }

    public Preference getPreference() {
        return preference;
    }

    public void setPreference(Preference preference) {
        this.preference = preference;
    }

    public Preference getPairPreference() {
        return pairPreference;
    }

    public void setPairPreference(Preference pairPreference) {
        this.pairPreference = pairPreference;
    }

    public String getPreferenceName() {
        return preferenceName;
    }

    public void setPreferenceName(String preferenceName) {
        this.preferenceName = preferenceName;
    }
}

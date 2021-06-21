package locker.object;

import locker.event.OperationMode;

public class Preference {
    private final String name;
    private final String source;
    private final String destination;
    private final String password;
    private final OperationMode operationMode;

    public Preference(String name, String source, String destination, String password, OperationMode operationMode) {
        this.name = name;
        this.source = source;
        this.destination = destination;
        this.password = password;
        this.operationMode = operationMode;
    }

    public String getName() {
        return name;
    }

    public String getSource() {
        return source;
    }

    public String getDestination() {
        return destination;
    }

    public String getPassword() {
        return password;
    }

    public OperationMode getOperationMode() {
        return operationMode;
    }
}

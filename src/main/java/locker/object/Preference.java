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

    public Preference(String content) {
        String[] properties = content.split("\n");

        this.name = properties[0];
        this.source = properties[1];
        this.destination = properties[2];
        this.password = properties[3];
        this.operationMode = ("1").equals(properties[4]) ? OperationMode.ENCRYPT : OperationMode.DECRYPT;
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

    @Override
    public String toString() {
        return name + "\n" + source + "\n" + destination + "\n" + password
                + "\n" + (operationMode == OperationMode.ENCRYPT ? "1" : "0");
    }
}

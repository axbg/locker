package locker.object;

import locker.event.OperationMode;

public class Preference {
    private static final String SEPARATOR = "\r\n";

    private final String source;
    private final String destination;
    private final String password;
    private final OperationMode operationMode;

    private String name;

    public Preference(String name, String source, String destination, String password, OperationMode operationMode) {
        this.name = name;
        this.source = source;
        this.destination = destination;
        this.password = password;
        this.operationMode = operationMode;
    }

    public Preference(String content) {
        String[] properties = content.split(SEPARATOR);

        this.name = properties[0];
        this.source = properties[1];
        this.destination = properties[2];
        this.password = properties[3];
        this.operationMode = ("1").equals(properties[4]) ? OperationMode.ENCRYPT : OperationMode.DECRYPT;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        return name + SEPARATOR + source + SEPARATOR + destination + SEPARATOR + password
                + SEPARATOR + (operationMode == OperationMode.ENCRYPT ? "1" : "0");
    }
}

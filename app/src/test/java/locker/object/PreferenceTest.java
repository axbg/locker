package locker.object;

import locker.event.OperationMode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PreferenceTest {
    private static final String SEPARATOR = "\r\n";

    @Test
    public void toStringTest() {
        Preference preference = new Preference("Test Preference", "Test Source", "Test Destination",
                "Test Password", OperationMode.ENCRYPT);

        String expected = preference.getName() + SEPARATOR + preference.getSource() + SEPARATOR + preference.getDestination()
                + SEPARATOR + preference.getPassword() + SEPARATOR
                + (preference.getOperationMode() == OperationMode.ENCRYPT ? "1" : "0");

        Assertions.assertEquals(expected, preference.toString());
    }
}

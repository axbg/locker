package packaging;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Application {
    private static final String SHA_256 = "SHA-256";
    private static final String JAR_LOCATION = "app/target/app-1.0.jar";
    private static final String REPO_ROOT = "./locker.jar";
    private static final String README_LOCATION = "./README.md";

    public static void main(String[] args) {
        try (RandomAccessFile raf = new RandomAccessFile(Path.of(README_LOCATION).toFile(), "rw")) {
            MessageDigest messageDigest = MessageDigest.getInstance(SHA_256);
            Path originalPath = Path.of(JAR_LOCATION);

            String jarHash = hexToString(messageDigest.digest(Files.readAllBytes(originalPath)));

            Files.copy(Path.of(JAR_LOCATION), Path.of(REPO_ROOT), StandardCopyOption.REPLACE_EXISTING);

            byte b;
            long length = raf.length() - 1;

            do {
                length -= 1;
                raf.seek(length);
                b = raf.readByte();
            } while (b != 10);

            raf.write(jarHash.getBytes());
            System.out.println("Moved jar and updated README");
        } catch (IOException | NoSuchAlgorithmException e) {
            System.out.println(createMessage(e));
            e.printStackTrace();
        }
    }

    private static String createMessage(Exception ex) {
        if (ex instanceof IOException) {
            return "Error occurred while copying jar into repository root";
        } else if (ex instanceof NoSuchAlgorithmException) {
            return SHA_256 + " hashing algorithm was not found";
        }

        return "An unknown exception occurred";
    }

    private static String hexToString(byte[] content) {
        StringBuilder builder = new StringBuilder();

        for (byte b : content) {
            builder.append(String.format("%02X", b));
        }

        return builder.toString();
    }
}
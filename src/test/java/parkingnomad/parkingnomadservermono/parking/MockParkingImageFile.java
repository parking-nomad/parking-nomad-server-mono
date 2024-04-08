package parkingnomad.parkingnomadservermono.parking;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class MockParkingImageFile {
    public static final File FILE = new File("src/test/resources/coffee.png");

    public static MultipartFile generate() {
        try {
            final FileInputStream fileInputStream = new FileInputStream(FILE.getAbsolutePath());
            return new MockMultipartFile("images", "fileName.webp", "image/webp", fileInputStream);
        } catch (IOException exception) {
            throw new RuntimeException();
        }
    }

}

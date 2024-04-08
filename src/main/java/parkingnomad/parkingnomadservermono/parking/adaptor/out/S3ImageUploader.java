package parkingnomad.parkingnomadservermono.parking.adaptor.out;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import parkingnomad.parkingnomadservermono.common.exception.base.BadRequestException;
import parkingnomad.parkingnomadservermono.parking.exception.parking.ImageUploadFailException;
import parkingnomad.parkingnomadservermono.parking.exception.parking.InvalidFileNameException;
import parkingnomad.parkingnomadservermono.parking.exception.parking.InvalidFileTypeException;
import parkingnomad.parkingnomadservermono.parking.application.port.out.ImageUploader;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import static parkingnomad.parkingnomadservermono.parking.exception.parking.ParkingErrorCode.INVALID_FILE_NAME;
import static parkingnomad.parkingnomadservermono.parking.exception.parking.ParkingErrorCode.INVALID_FILE_TYPE;

@Component
public class S3ImageUploader implements ImageUploader {

    public static final String FILE_EXT_DELIMITER = ".";
    public static final String CONTENT_TYPE = "image/webp";
    private final AmazonS3 amazonS3;
    @Value("${s3.bucket}")
    private String bucket;

    public S3ImageUploader(final AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    @Override
    public String upload(final MultipartFile multipartFile) {
        try {
            final PutObjectRequest putObjectRequest = generatePutObjectRequest(multipartFile);
            amazonS3.putObject(putObjectRequest);
            return putObjectRequest.getKey();
        } catch (BadRequestException exception) {
            throw exception;
        } catch (Exception exception) {
            throw new ImageUploadFailException(exception.getMessage());
        }
    }

    private PutObjectRequest generatePutObjectRequest(final MultipartFile multipartFile) throws IOException {
        final String contentType = multipartFile.getContentType();
        validate(contentType);
        final String originalFileName = multipartFile.getOriginalFilename();
        final String fileName = generateFileName(originalFileName);
        final InputStream inputStream = multipartFile.getInputStream();
        final ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(contentType);
        objectMetadata.setContentLength(multipartFile.getBytes().length);
        return new PutObjectRequest(bucket, fileName, inputStream, objectMetadata);
    }

    private void validate(final String contentType) {
        if (Strings.isBlank(contentType) || !contentType.equals(CONTENT_TYPE)) {
            throw new InvalidFileTypeException(INVALID_FILE_TYPE, contentType);
        }
    }

    private String generateFileName(final String originalFileName) {
        if (Strings.isBlank(originalFileName) || !originalFileName.contains(FILE_EXT_DELIMITER)) {
            throw new InvalidFileNameException(INVALID_FILE_NAME, originalFileName);
        }
        final int delimiterIndex = originalFileName.lastIndexOf(FILE_EXT_DELIMITER);
        final String extension = originalFileName.substring(delimiterIndex);
        return UUID.randomUUID() + extension;
    }
}

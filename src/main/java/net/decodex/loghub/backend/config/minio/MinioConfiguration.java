package net.decodex.loghub.backend.config.minio;

import com.jlefebure.spring.boot.minio.MinioConfigurationProperties;
import com.jlefebure.spring.boot.minio.MinioException;
import com.jlefebure.spring.boot.minio.MinioService;
import io.minio.MinioClient;
import io.minio.errors.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Configuration
public class MinioConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(com.jlefebure.spring.boot.minio.MinioConfiguration.class);

    @Value("${minio.public-key}")
    String publicKey;

    @Value("${minio.private-key}")
    String secretKey;

    @Value("${minio.bucket}")
    String bucket;

    @Value("${minio.host}")
    String host;

    @Value("${minio.check-bucket}")
    boolean checkBucket;

    @Value("${minio.create-bucket}")
    boolean createBucket;

    @Value("${minio.connect-timeout}")
    int connectTimeout;

    @Value("${minio.read-timeout}")
    int readTimeout;

    @Value("${minio.write-timeout}")
    int writeTimeout;

    @Bean
    public MinioClient minioClient() throws InvalidEndpointException, InvalidPortException, IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, InternalException, NoResponseException, InvalidBucketNameException, XmlPullParserException, ErrorResponseException, InvalidResponseException, com.jlefebure.spring.boot.minio.MinioException {

        MinioClient minioClient = null;
        try {
            minioClient = new MinioClient(host, publicKey, secretKey);
            minioClient.setTimeout(connectTimeout, writeTimeout, readTimeout);
        } catch (InvalidEndpointException | InvalidPortException e) {
            LOGGER.error("Error while connecting to Minio", e);
            throw e;
        }

        if (checkBucket) {
            try {
                LOGGER.debug("Checking if bucket {} exists", bucket);
                boolean b = minioClient.bucketExists(bucket);
                if (!b) {
                    if (createBucket) {
                        try {
                            minioClient.makeBucket(bucket);
                        } catch (RegionConflictException e) {
                            throw new com.jlefebure.spring.boot.minio.MinioException("Cannot create bucket", e);
                        }
                    } else {
                        throw new InvalidBucketNameException(bucket, "Bucket does not exists");
                    }
                }
            } catch (InvalidBucketNameException | NoSuchAlgorithmException | InsufficientDataException | IOException |
                     InvalidKeyException | NoResponseException | XmlPullParserException | ErrorResponseException |
                     InternalException | InvalidResponseException | MinioException e) {
                LOGGER.error("Error while checking bucket", e);
                throw e;
            }
        }

        return minioClient;
    }

    @Bean
    public MinioService minioService() {
        return new MinioService();
    }
}

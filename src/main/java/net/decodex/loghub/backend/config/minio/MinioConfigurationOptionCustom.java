package net.decodex.loghub.backend.config.minio;

import com.jlefebure.spring.boot.minio.MinioConfigurationProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class MinioConfigurationOptionCustom extends MinioConfigurationProperties {
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

    @Override
    public Duration getConnectTimeout() {
        return Duration.ofSeconds(connectTimeout);
    }

    @Override
    public Duration getWriteTimeout() {
        return Duration.ofSeconds(writeTimeout);
    }

    @Override
    public Duration getReadTimeout() {
        return Duration.ofSeconds(readTimeout);
    }

    @Override
    public String getUrl() {
        return host;
    }

    @Override
    public String getAccessKey() {
        return publicKey;
    }

    @Override
    public String getSecretKey() {
        return secretKey;
    }

    @Override
    public String getBucket() {
        return bucket;
    }

    @Override
    public String getMetricName() {
        return super.getMetricName();
    }
}

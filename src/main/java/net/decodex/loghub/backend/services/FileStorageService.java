package net.decodex.loghub.backend.services;

import com.jlefebure.spring.boot.minio.MinioException;
import com.jlefebure.spring.boot.minio.MinioService;
import io.minio.ObjectStat;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.decodex.loghub.backend.domain.dto.FileDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileStorageService {

    private final MinioService minioService;

    @Value("${app.url}")
    private String baseUrl;

    public FileDto addFile(String fileName, MultipartFile file) throws IOException, MinioException {
        var extension = getFileExtension(file);
        if(extension == null) {
            extension = "";
        } else {
            extension = "."+extension;
        }
        Path path = Path.of(fileName+extension);
        minioService.upload(path, file.getInputStream(), file.getContentType());
        var metadata = minioService.getMetadata(path);
        return metaDataToDto(metadata);
    }

    public void deleteFile(String filename) throws MinioException {
        Path path = Path.of(filename);
        minioService.remove(path);
    }

    public FileDto getFile(String filename) throws MinioException {
        Path path = Path.of(filename);
        var metadata = minioService.getMetadata(path);

        InputStream inputStream = minioService.get(path);
        InputStreamResource inputStreamResource = new InputStreamResource(inputStream);

        return FileDto.builder()
                .fileName(metadata.name())
                .fileSize(metadata.length())
                .contentType(metadata.contentType())
                .createdTime(LocalDateTime.ofInstant(metadata.createdTime().toInstant(), ZoneId.systemDefault()))
                .stream(inputStreamResource)
                .build();
    }

    public FileDto getFileDetails(String fileName) throws MinioException {
        Path path = Path.of(fileName);
        var metadata = minioService.getMetadata(path);
        return metaDataToDto(metadata);
    }

    public String getBasePublicUrl() {
        return baseUrl + "/api/v1/files/public/view";
    }

    public String getBasePrivateUrl() {
        return baseUrl + "/api/v1/files/view";
    }

    private String getFileExtension(MultipartFile file) {
        // Get the original filename
        String originalFilename = file.getOriginalFilename();
        if (originalFilename != null && !originalFilename.isEmpty()) {
            // Extract file extension
            int lastIndex = originalFilename.lastIndexOf('.');
            if (lastIndex != -1 && lastIndex < originalFilename.length() - 1) {
                return originalFilename.substring(lastIndex + 1);
            }
        }
        return null; // No extension found
    }

    private FileDto metaDataToDto(ObjectStat metadata) {
        return FileDto.builder()
                .fileName(metadata.name())
                .fileSize(metadata.length())
                .contentType(metadata.contentType())
                .createdTime(LocalDateTime.ofInstant(metadata.createdTime().toInstant(), ZoneId.systemDefault()))
                .build();
    }
}

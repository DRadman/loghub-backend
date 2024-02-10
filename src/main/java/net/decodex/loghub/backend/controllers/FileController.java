package net.decodex.loghub.backend.controllers;

import com.jlefebure.spring.boot.minio.MinioException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import net.decodex.loghub.backend.domain.dto.FileDto;
import net.decodex.loghub.backend.enums.ResourceType;
import net.decodex.loghub.backend.exceptions.specifications.ForbiddenActionException;
import net.decodex.loghub.backend.exceptions.specifications.ResourceNotFoundException;
import net.decodex.loghub.backend.repositories.DebugFileRepository;
import net.decodex.loghub.backend.services.AuthenticationService;
import net.decodex.loghub.backend.services.FileStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
public class FileController {

    private final FileStorageService fileStorageService;
    private final AuthenticationService authenticationService;
    private final DebugFileRepository debugFileRepository;

    @Value("${app.url}")
    private String baseUrl;

    @GetMapping("/public/view/{file}")
    @Operation(summary = "View a Public File")
    public ResponseEntity<InputStreamResource> viewPublicFile(@PathVariable String file) {
        FileDto source = null;
        try {
            source = fileStorageService.getFile(file);
        } catch (MinioException e) {
            throw new ResourceNotFoundException("File", "name", file);
        }
        if (file.startsWith(ResourceType.DEBUG_FILE.name())) {
            throw new ForbiddenActionException("Resource is Private");
        }
        return ResponseEntity
                .ok()
                .contentType(MediaType.parseMediaType(source.getContentType()))
                .contentLength(source.getFileSize())
                .header("Content-disposition", "attachment; filename=" + source.getFileName())
                .body(source.getStream());
    }

    @GetMapping("/public/download/{file}")
    @Operation(summary = "Download a Public File")
    public ResponseEntity<InputStreamResource> downloadPublicFile(@PathVariable String file) {
        FileDto source = null;
        try {
            source = fileStorageService.getFile(file);
        } catch (MinioException e) {
            throw new ResourceNotFoundException("File", "name", file);
        }
        if (file.startsWith(ResourceType.DEBUG_FILE.name())) {
            throw new ForbiddenActionException("Resource is Private");
        }
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(source.getFileSize())
                .header("Content-disposition", "attachment; filename=" + source.getFileName())
                .body(source.getStream());
    }

    @GetMapping("/public/{file}/detail")
    @Operation(summary = "Get Public File Detail")
    public FileDto getPublicFileDetail(@PathVariable String file) {
        if (file.startsWith(ResourceType.DEBUG_FILE.name())) {
            throw new ForbiddenActionException("Resource is Private");
        }
        try {
            var details = fileStorageService.getFileDetails(file);
            details.setUrl(baseUrl + "/api/v1/files/public/view/" + file);
            return details;
        } catch (MinioException e) {
            throw new ResourceNotFoundException("File", "name", file);
        }
    }

    @GetMapping("/view/{file}")
    @Operation(summary = "View a File")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<InputStreamResource> viewFile(@PathVariable String file, Principal principal) {
        if (file.startsWith(ResourceType.DEBUG_FILE.name())) {
            var user = authenticationService.getLoggedUser(principal.getName());
            var debugFile = debugFileRepository.findByFile(file);
            if (debugFile.isPresent()) {
                if (!user.getOrganization().getProjects().contains(debugFile.get().getProject())) {
                    throw new ForbiddenActionException("Resource is Private");
                }
            }
        }
        FileDto source = null;
        try {
            source = fileStorageService.getFile(file);
        } catch (MinioException e) {
            throw new ResourceNotFoundException("File", "name", file);
        }
        return ResponseEntity
                .ok()
                .contentType(MediaType.parseMediaType(source.getContentType()))
                .contentLength(source.getFileSize())
                .header("Content-disposition", "attachment; filename=" + source.getFileName())
                .body(source.getStream());
    }

    @GetMapping("/download/{file}")
    @Operation(summary = "Download a File")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<InputStreamResource> downloadFile(@PathVariable String file, Principal principal) {
        if (file.startsWith(ResourceType.DEBUG_FILE.name())) {
            var user = authenticationService.getLoggedUser(principal.getName());
            var debugFile = debugFileRepository.findByFile(file);
            if (debugFile.isPresent()) {
                if (!user.getOrganization().getProjects().contains(debugFile.get().getProject())) {
                    throw new ForbiddenActionException("Resource is Private");
                }
            }
        }
        FileDto source = null;
        try {
            source = fileStorageService.getFile(file);
        } catch (MinioException e) {
            throw new ResourceNotFoundException("File", "name", file);
        }
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(source.getFileSize())
                .header("Content-disposition", "attachment; filename=" + source.getFileName())
                .body(source.getStream());
    }

    @GetMapping("/{file}/detail")
    @Operation(summary = "Get File Detail")
    @SecurityRequirement(name = "Bearer Authentication")
    public FileDto getFileDetail(@PathVariable String file, Principal principal) {
        if (file.startsWith(ResourceType.DEBUG_FILE.name())) {
            var user = authenticationService.getLoggedUser(principal.getName());
            var debugFile = debugFileRepository.findByFile(file);
            if (debugFile.isPresent()) {
                if (!user.getOrganization().getProjects().contains(debugFile.get().getProject())) {
                    throw new ForbiddenActionException("Resource is Private");
                }
            }
        }
        try {
            var details = fileStorageService.getFileDetails(file);
            details.setUrl(baseUrl + "/api/v1/files/view/" + file);
            return details;
        } catch (MinioException e) {
            throw new ResourceNotFoundException("File", "name", file);
        }
    }
}

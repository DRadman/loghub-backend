package net.decodex.loghub.backend.services;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import net.decodex.loghub.backend.domain.dto.FileDto;
import net.decodex.loghub.backend.domain.dto.PlatformDto;
import net.decodex.loghub.backend.domain.dto.requests.PlatformRequestDto;
import net.decodex.loghub.backend.domain.mappers.PlatformMapper;
import net.decodex.loghub.backend.enums.ResourceType;
import net.decodex.loghub.backend.exceptions.specifications.OrganizationNotPresentException;
import net.decodex.loghub.backend.exceptions.specifications.ResourceNotFoundException;
import net.decodex.loghub.backend.repositories.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlatformService {

    private final FileStorageService fileStorageService;
    private final ProjectService projectService;

    private final PlatformRepository platformRepository;
    private final ProjectRepository projectRepository;

    private final PlatformMapper platformMapper;
    public List<PlatformDto> findAll() {
        return platformRepository.findAll().stream().map(platformMapper::toDto).collect(Collectors.toList());
    }

    public PlatformDto findById(String platformId) {
        var platformOp = platformRepository.findById(platformId);
        if (platformOp.isEmpty()) {
            throw new ResourceNotFoundException("Platform", "platformId", platformId);
        }

        return platformMapper.toDto(platformOp.get());
    }

    public PlatformDto createPlatform(PlatformRequestDto dto) {
        var platform = platformMapper.toEntity(dto);
        platform = platformRepository.save(platform);
        return platformMapper.toDto(platform);
    }

    public PlatformDto updatePlatform(PlatformRequestDto dto, String platformId) {
        var platformOp = platformRepository.findById(platformId);
        if (platformOp.isEmpty()) {
            throw new ResourceNotFoundException("Platform", "platformId", platformId);
        }

        var platform = platformOp.get();
        platform = platformMapper.partialUpdate(dto, platform);
        platform = platformRepository.save(platform);

        return platformMapper.toDto(platform);
    }

    public Object deletePlatform(String platformId) {
        var platformOp = platformRepository.findById(platformId);
        if (platformOp.isEmpty()) {
            throw new ResourceNotFoundException("Platform", "platformId", platformId);
        }

        var platform = platformOp.get();
        platformRepository.delete(platform);

        var deletedProjects = projectRepository.deleteByPlatform(platform);
       projectService.purgeProjects(deletedProjects);

        return ResponseEntity.noContent().build();
    }

    @SneakyThrows
    public FileDto updatePicture(MultipartFile file, String platformId) {
        var platformOp = platformRepository.findById(platformId);
        if (platformOp.isEmpty()) {
            throw new ResourceNotFoundException("Platform", "platformId", platformId);
        }

        var platform = platformOp.get();

        try {
            this.fileStorageService.deleteFile(platform.getIcon());
        } catch (NullPointerException e) {
            //Consume
        }

        var result = this.fileStorageService.addFile(ResourceType.PLATFORM + "_" + platform.getPlatformId(), file);
        platform.setIcon(result.getFileName());
        platform.setIconUrl(this.fileStorageService.getBasePublicUrl() + "/" + result.getFileName());
        result.setUrl(platform.getIconUrl());

        this.platformRepository.save(platform);

        return result;
    }

    public PlatformDto updatePlatformTags(List<String> dto, String platformId) {
        var platformOp = platformRepository.findById(platformId);
        if (platformOp.isEmpty()) {
            throw new ResourceNotFoundException("Platform", "platformId", platformId);
        }
        var platform = platformOp.get();
        platform.setDefaultTags(dto);
        platform = platformRepository.save(platform);

        return platformMapper.toDto(platform);
    }
}

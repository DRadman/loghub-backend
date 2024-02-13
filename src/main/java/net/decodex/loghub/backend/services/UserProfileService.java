package net.decodex.loghub.backend.services;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import net.decodex.loghub.backend.domain.dto.FileDto;
import net.decodex.loghub.backend.domain.dto.UserDto;
import net.decodex.loghub.backend.domain.dto.requests.ChangePasswordRequestDto;
import net.decodex.loghub.backend.domain.dto.requests.UserProfileChangeRequestDto;
import net.decodex.loghub.backend.domain.mappers.UserMapper;
import net.decodex.loghub.backend.enums.ResourceType;
import net.decodex.loghub.backend.exceptions.specifications.BadRequestException;
import net.decodex.loghub.backend.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final UserRepository userRepository;

    private final AuthenticationService authenticationService;
    private final FileStorageService fileStorageService;

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserDto updateUserProfile(UserProfileChangeRequestDto dto, Principal principal) {
        var user = authenticationService.getLoggedUser(principal.getName());
        user = userMapper.partialUpdate(dto, user);
        user = userRepository.save(user);
        return userMapper.toDto(user);
    }

    public UserDto changePassword(ChangePasswordRequestDto dto, Principal principal) {
        var user = authenticationService.getLoggedUser(principal.getName());
        if (passwordEncoder.matches(dto.getOldPassword(), user.getPassword())) {
            user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
            return userMapper.toDto(userRepository.save(user));
        } else {
            throw new BadRequestException("Old password is incorrect");
        }
    }

    @SneakyThrows
    public FileDto updatePicture(MultipartFile file, Principal principal) {
        var user = authenticationService.getLoggedUser(principal.getName());

        try {
            this.fileStorageService.deleteFile(user.getProfileIcon());
        } catch (NullPointerException e) {
            //Consume
        }

        var result = this.fileStorageService.addFile(ResourceType.USER + "_" + user.getUserId(), file);
        user.setProfileIcon(result.getFileName());
        user.setProfileIconUrl(this.fileStorageService.getBasePublicUrl() + "/" + result.getFileName());
        result.setUrl(user.getProfileIconUrl());

        this.userRepository.save(user);

        return result;
    }
}

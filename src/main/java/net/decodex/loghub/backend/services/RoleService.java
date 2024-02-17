package net.decodex.loghub.backend.services;

import lombok.RequiredArgsConstructor;
import net.decodex.loghub.backend.domain.dto.RoleDto;
import net.decodex.loghub.backend.domain.mappers.RoleMapper;
import net.decodex.loghub.backend.repositories.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    public List<RoleDto> findAll() {
        return roleRepository.findByIsInternal(false).stream().map(roleMapper::toDto).collect(Collectors.toList());
    }
}

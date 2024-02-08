package net.decodex.loghub.backend.repositories;

import net.decodex.loghub.backend.domain.models.Role;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RoleRepository extends CrudRepository<Role, String> {
    Optional<Role> findByName(String name);
}

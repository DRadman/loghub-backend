package net.decodex.loghub.backend.repositories;

import net.decodex.loghub.backend.domain.models.Role;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends MongoRepository<Role, String> {
    List<Role> findByIsInternal(@NonNull boolean isInternal);
    Optional<Role> findByName(String name);
}

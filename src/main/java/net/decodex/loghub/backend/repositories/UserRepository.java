package net.decodex.loghub.backend.repositories;

import net.decodex.loghub.backend.domain.models.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, String> {
    Optional<User> findByUsername(String username);
}

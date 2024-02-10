package net.decodex.loghub.backend.repositories;

import net.decodex.loghub.backend.domain.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByUsername(String username);
}

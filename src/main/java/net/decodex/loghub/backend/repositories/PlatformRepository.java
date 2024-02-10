package net.decodex.loghub.backend.repositories;

import net.decodex.loghub.backend.domain.models.Platform;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PlatformRepository extends MongoRepository<Platform, String> {
}

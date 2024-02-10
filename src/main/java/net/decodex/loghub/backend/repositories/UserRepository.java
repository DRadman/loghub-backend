package net.decodex.loghub.backend.repositories;

import net.decodex.loghub.backend.domain.models.Organization;
import net.decodex.loghub.backend.domain.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.lang.NonNull;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    List<User> findByFirstNameIgnoreCaseOrLastNameIgnoreCaseOrEmailIgnoreCaseAndRole_RoleIdInAndOrganization(@NonNull String firstName, @NonNull String lastName, @NonNull String email, @NonNull Collection<String> roleIds, @NonNull Organization organization);
    List<User> findByFirstNameContainsIgnoreCaseOrLastNameContainsIgnoreCaseOrEmailContainsIgnoreCaseAndOrganization(@NonNull String firstName, @NonNull String lastName, @NonNull String email, @NonNull Organization organization);
    List<User> findByRole_RoleIdInAndOrganization(@NonNull Collection<String> roleIds, @NonNull Organization organization);
    List<User> findByOrganization(@NonNull Organization organization);
    Optional<User> findByUsername(String username);
}

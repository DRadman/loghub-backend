package net.decodex.loghub.backend.repositories;

import net.decodex.loghub.backend.domain.models.Invitation;
import net.decodex.loghub.backend.domain.models.Organization;
import net.decodex.loghub.backend.domain.models.User;
import net.decodex.loghub.backend.enums.InvitationStatus;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface InvitationRepository extends CrudRepository<Invitation, String> {
    List<Invitation> findByRole_RoleIdInAndOrganization(@NonNull Collection<String> roleIds, @NonNull Organization organization);
    List<Invitation> findByEmailContainsIgnoreCaseAndOrganization(@NonNull String email, @NonNull Organization organization);
    List<Invitation> findByEmailContainsIgnoreCaseAndRole_RoleIdInAndOrganization(@NonNull String email, @NonNull Collection<String> roleIds, @NonNull Organization organization);
    List<Invitation> findByOrganization(@NonNull Organization organization);
    List<Invitation> findByOrganizationAndStatus(@NonNull Organization organization, @NonNull InvitationStatus status);

}

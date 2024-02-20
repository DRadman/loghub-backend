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
    List<Invitation> findByRole_RoleIdInAndOrganizationAndStatus(@NonNull Collection<String> roleIds,  @NonNull Organization organization, @NonNull InvitationStatus status);
    List<Invitation> findByEmailContainsIgnoreCaseAndOrganizationAndStatus(@NonNull String email, @NonNull Organization organization, @NonNull InvitationStatus status);
    List<Invitation> findByEmailContainsIgnoreCaseAndRole_RoleIdInAndOrganizationAndStatus(@NonNull String email, @NonNull Collection<String> roleIds, @NonNull Organization organization, @NonNull InvitationStatus stat);
    List<Invitation> findByOrganization(@NonNull Organization organization);
    List<Invitation> findByOrganizationAndStatus(@NonNull Organization organization, @NonNull InvitationStatus status);

}

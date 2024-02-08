package net.decodex.loghub.backend.repositories;

import net.decodex.loghub.backend.domain.models.Organization;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;

import java.util.Optional;

public interface OrganizationRepository extends CrudRepository<Organization, String>, QuerydslPredicateExecutor<Organization> {
    Optional<Organization> findBySlug(@NonNull String slug);
}

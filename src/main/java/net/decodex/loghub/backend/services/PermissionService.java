package net.decodex.loghub.backend.services;

import net.decodex.loghub.backend.domain.models.Permission;
import net.decodex.loghub.backend.enums.ResourceType;
import org.springframework.stereotype.Service;

import java.util.List;

import static net.decodex.loghub.backend.enums.PermissionValue.*;

@Service
public class PermissionService {

    public List<Permission> getSuperAdminPermissions() {
        return List.of(
                new Permission(ResourceType.PERMISSION, List.of(CREATE, READ, UPDATE, DELETE)),
                new Permission(ResourceType.COMMENT, List.of(CREATE, READ, UPDATE, DELETE)),
                new Permission(ResourceType.DEBUG_FILE, List.of(CREATE, READ, UPDATE, DELETE)),
                new Permission(ResourceType.ISSUE, List.of(CREATE, READ, UPDATE, DELETE)),
                new Permission(ResourceType.DEVICE_STATE, List.of(CREATE, READ, UPDATE, DELETE)),
                new Permission(ResourceType.LOG_ENTRY, List.of(CREATE, READ, UPDATE, DELETE)),
                new Permission(ResourceType.LOG_SESSION, List.of(CREATE, READ, UPDATE, DELETE)),
                new Permission(ResourceType.LOG_SOURCE, List.of(CREATE, READ, UPDATE, DELETE)),
                new Permission(ResourceType.ORGANIZATION, List.of(CREATE, READ, UPDATE, DELETE)),
                new Permission(ResourceType.PLATFORM, List.of(CREATE, READ, UPDATE, DELETE)),
                new Permission(ResourceType.PROJECT, List.of(CREATE, READ, UPDATE, DELETE)),
                new Permission(ResourceType.PROJECT_RELEASE, List.of(CREATE, READ, UPDATE, DELETE)),
                new Permission(ResourceType.ROLE, List.of(CREATE, READ, UPDATE, DELETE)),
                new Permission(ResourceType.TEAM, List.of(CREATE, READ, UPDATE, DELETE)),
                new Permission(ResourceType.USER, List.of(CREATE, READ, UPDATE, DELETE)),
                new Permission(ResourceType.INVITATION, List.of(CREATE, READ, UPDATE, DELETE))
        );
    }

    public List<Permission> getGuestPermissions() {
        return List.of(
                new Permission(ResourceType.PERMISSION, List.of(READ)),
                new Permission(ResourceType.COMMENT, List.of(READ)),
                new Permission(ResourceType.DEBUG_FILE, List.of(READ)),
                new Permission(ResourceType.ISSUE, List.of(READ)),
                new Permission(ResourceType.DEVICE_STATE, List.of(READ)),
                new Permission(ResourceType.LOG_ENTRY, List.of(READ)),
                new Permission(ResourceType.LOG_SESSION, List.of(READ)),
                new Permission(ResourceType.LOG_SOURCE, List.of(READ)),
                new Permission(ResourceType.ORGANIZATION, List.of(READ)),
                new Permission(ResourceType.PLATFORM, List.of(READ)),
                new Permission(ResourceType.PROJECT, List.of(READ)),
                new Permission(ResourceType.PROJECT_RELEASE, List.of(READ)),
                new Permission(ResourceType.ROLE, List.of(READ)),
                new Permission(ResourceType.TEAM, List.of(READ)),
                new Permission(ResourceType.USER, List.of(READ)),
                new Permission(ResourceType.INVITATION, List.of(READ))
        );
    }

    public List<Permission> getOwnerPermissions() {
        return List.of(
                new Permission(ResourceType.PERMISSION, List.of(READ)),
                new Permission(ResourceType.COMMENT, List.of(READ, CREATE, UPDATE, DELETE)),
                new Permission(ResourceType.DEBUG_FILE, List.of(READ, CREATE, UPDATE, DELETE)),
                new Permission(ResourceType.ISSUE, List.of(READ, CREATE, UPDATE, DELETE)),
                new Permission(ResourceType.DEVICE_STATE, List.of(READ, CREATE, UPDATE, DELETE)),
                new Permission(ResourceType.LOG_ENTRY, List.of(READ, CREATE, UPDATE, DELETE)),
                new Permission(ResourceType.LOG_SESSION, List.of(READ, CREATE, UPDATE, DELETE)),
                new Permission(ResourceType.LOG_SOURCE, List.of(READ, CREATE, UPDATE, DELETE)),
                new Permission(ResourceType.ORGANIZATION, List.of(READ, CREATE, UPDATE)),
                new Permission(ResourceType.PLATFORM, List.of(READ)),
                new Permission(ResourceType.PROJECT, List.of(READ, CREATE, UPDATE, DELETE)),
                new Permission(ResourceType.PROJECT_RELEASE, List.of(READ, CREATE, UPDATE, DELETE)),
                new Permission(ResourceType.ROLE, List.of(READ)),
                new Permission(ResourceType.TEAM, List.of(READ, CREATE, UPDATE, DELETE)),
                new Permission(ResourceType.USER, List.of(READ, CREATE, UPDATE, DELETE)),
                new Permission(ResourceType.INVITATION, List.of(CREATE, READ, UPDATE, DELETE))
        );
    }

    public List<Permission> getMemberPermissions() {
        return List.of(
                new Permission(ResourceType.PERMISSION, List.of(READ)),
                new Permission(ResourceType.COMMENT, List.of(READ, CREATE, UPDATE, DELETE)),
                new Permission(ResourceType.DEBUG_FILE, List.of(READ)),
                new Permission(ResourceType.ISSUE, List.of(READ, CREATE, UPDATE, DELETE)),
                new Permission(ResourceType.DEVICE_STATE, List.of(READ, CREATE, UPDATE, DELETE)),
                new Permission(ResourceType.LOG_ENTRY, List.of(READ, CREATE, UPDATE, DELETE)),
                new Permission(ResourceType.LOG_SESSION, List.of(READ, CREATE, UPDATE, DELETE)),
                new Permission(ResourceType.LOG_SOURCE, List.of(READ, CREATE, UPDATE, DELETE)),
                new Permission(ResourceType.ORGANIZATION, List.of(READ)),
                new Permission(ResourceType.PLATFORM, List.of(READ)),
                new Permission(ResourceType.PROJECT, List.of(READ)),
                new Permission(ResourceType.PROJECT_RELEASE, List.of(READ)),
                new Permission(ResourceType.ROLE, List.of(READ)),
                new Permission(ResourceType.TEAM, List.of(READ)),
                new Permission(ResourceType.USER, List.of(READ)),
                new Permission(ResourceType.INVITATION, List.of(READ))
        );
    }
}

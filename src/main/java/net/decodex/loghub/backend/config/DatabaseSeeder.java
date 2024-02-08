package net.decodex.loghub.backend.config;

import jakarta.annotation.PostConstruct;
import net.decodex.loghub.backend.domain.models.Role;
import net.decodex.loghub.backend.domain.models.User;
import net.decodex.loghub.backend.repositories.RoleRepository;
import net.decodex.loghub.backend.repositories.UserRepository;
import net.decodex.loghub.backend.services.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DatabaseSeeder {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct()
    public void seedDatabase() {
        seedRoles();
        seedUsers();
    }

    public void seedRoles() {
        seedSuperAdminRole();
        seedOwnerRole();
        seedMemberRole();
        seedGuestRole();
    }

    public void seedUsers() {
        var role = roleRepository.findByName("Super Admin");
        if (role.isPresent()) {
            var opUser = userRepository.findByUsername("admin@loghub.com");
            if (opUser.isEmpty()) {
                var user = new User();
                user.setUsername("admin@loghub.com");
                user.setPassword(passwordEncoder.encode("admin123"));
                user.setActivated(true);
                user.setEmail("admin@loghub.com");
                user.setFirstName("Admin");
                user.setLastName("Admin");
                user.setRole(role.get());
                userRepository.save(user);
            }
        }
    }

    private void seedSuperAdminRole() {
        var opRole = roleRepository.findByName("Super Admin");
        if (opRole.isEmpty()) {
            var role = new Role();
            role.setInternal(true);
            role.setName("Super Admin");
            role.setPermissions(permissionService.getSuperAdminPermissions());
            this.roleRepository.save(role);
        }
    }

    private void seedOwnerRole() {
        var opRole = roleRepository.findByName("Owner");
        if (opRole.isEmpty()) {
            var role = new Role();
            role.setInternal(false);
            role.setName("Owner");
            role.setPermissions(permissionService.getOwnerPermissions());
            this.roleRepository.save(role);
        }
    }

    private void seedMemberRole() {
        var opRole = roleRepository.findByName("Member");
        if (opRole.isEmpty()) {
            var role = new Role();
            role.setInternal(false);
            role.setName("Member");
            role.setPermissions(permissionService.getMemberPermissions());
            this.roleRepository.save(role);
        }
    }

    private void seedGuestRole() {
        var opRole = roleRepository.findByName("Guest");
        if (opRole.isEmpty()) {
            var role = new Role();
            role.setInternal(false);
            role.setName("Guest");
            role.setPermissions(permissionService.getGuestPermissions());
            this.roleRepository.save(role);
        }
    }
}

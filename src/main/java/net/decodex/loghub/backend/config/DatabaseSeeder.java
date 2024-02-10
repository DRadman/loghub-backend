package net.decodex.loghub.backend.config;

import jakarta.annotation.PostConstruct;
import net.decodex.loghub.backend.domain.models.Platform;
import net.decodex.loghub.backend.domain.models.Role;
import net.decodex.loghub.backend.domain.models.User;
import net.decodex.loghub.backend.enums.PlatformType;
import net.decodex.loghub.backend.repositories.PlatformRepository;
import net.decodex.loghub.backend.repositories.RoleRepository;
import net.decodex.loghub.backend.repositories.UserRepository;
import net.decodex.loghub.backend.services.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

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
    @Autowired
    private PlatformRepository platformRepository;

    @PostConstruct()
    public void seedDatabase() {
        seedRoles();
        seedUsers();
        seedPlatforms();
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

    private void seedPlatforms() {
        var platforms = platformRepository.findAll();
        if (platforms.isEmpty()) {
            var platformTypes = PlatformType.values();
            for (int i = 0; i < platformTypes.length; i++) {
                var currentType = platformTypes[i];
                var platform = new Platform();
                platform.setType(currentType);
                var tags = new ArrayList<String>();
                tags.add("handled");
                tags.add("browser");
                tags.add("browser.name");
                tags.add("transaction");
                tags.add("url");
                tags.add("user");
                tags.add("release");
                tags.add("sample_event");
                tags.add("package.name");

                if (currentType == PlatformType.EXPRESS || currentType == PlatformType.SERVER ||
                        currentType == PlatformType.NEST || currentType == PlatformType.GO ||
                        currentType == PlatformType.SPRING || currentType == PlatformType.CS ||
                        currentType == PlatformType.RUBY || currentType == PlatformType.RUST ||
                        currentType == PlatformType.LARAVEL || currentType == PlatformType.PYTHON) {
                    tags.add("runtime");
                    tags.add("runtime.name");
                    tags.add("server_name");
                } else {
                    tags.add("replyId");
                }

                if (currentType == PlatformType.ANDROID || currentType == PlatformType.IOS) {
                    tags.add("installer_store");
                }
                platform.setDefaultTags(tags);
            }
        }
    }
}

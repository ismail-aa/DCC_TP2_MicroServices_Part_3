package org.example.securityservice;

import org.example.securityservice.configuration.RsaKeys;
import org.example.securityservice.entities.AppRole;
import org.example.securityservice.entities.AppUser;
import org.example.securityservice.repository.RoleRepository;
import org.example.securityservice.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@EnableConfigurationProperties(RsaKeys.class)
public class SecurityServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecurityServiceApplication.class, args);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Initialize roles and users
    @Bean
    CommandLineRunner createUsers(UserRepository userRepo, RoleRepository roleRepo, PasswordEncoder passwordEncoder) {
        return args -> {
            // Create roles if they don't exist
            AppRole adminRole = roleRepo.findByRoleName("ADMIN")
                    .orElseGet(() -> roleRepo.save(new AppRole("ADMIN")));
            AppRole userRole = roleRepo.findByRoleName("USER")
                    .orElseGet(() -> roleRepo.save(new AppRole("USER")));

            // Create normal user if not exists
            if (userRepo.findByUsername("user1").isEmpty()) {
                AppUser user = new AppUser();
                user.setUsername("user1");
                user.setPassword(passwordEncoder.encode("1234")); // encrypted
                user.getRoles().add(userRole);
                userRepo.save(user);
            }

            // Create admin user if not exists
            if (userRepo.findByUsername("admin1").isEmpty()) {
                AppUser admin = new AppUser();
                admin.setUsername("admin1");
                admin.setPassword(passwordEncoder.encode("1234")); // encrypted
                admin.getRoles().add(adminRole);
                userRepo.save(admin);
            }
        };
    }
}

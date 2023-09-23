package com.reddit;

import com.reddit.entities.Role;
import com.reddit.enums.RoleType;
import com.reddit.repositories.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class Bootstrap implements ApplicationListener<ContextRefreshedEvent> {

    private final RoleRepository roleRepository;

    @Autowired
    public Bootstrap(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }


    private void saveRolesInDB() {
        List<Role> roles = new ArrayList<>();
        Role userRole = new Role(RoleType.ROLE_USER);
        Role adminRole = new Role(RoleType.ROLE_ADMIN);
        roles.add(userRole);
        roles.add(adminRole);
        if (roleRepository.count() == 0) {
            roleRepository.saveAll(roles);
            log.info("Roles save successfully");
        }
    }


    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        try {
            this.saveRolesInDB();
        } catch (Exception e) {
            log.error("Exception In On Application Event Service - ", e);
        }
    }
}

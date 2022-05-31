package com.itransition.itransitiontaskfour.common;

//Asatbek Xalimojnov 4/8/22 2:41 PM

import com.itransition.itransitiontaskfour.entity.Role;
import com.itransition.itransitiontaskfour.entity.User;
import com.itransition.itransitiontaskfour.repository.RoleRepository;
import com.itransition.itransitiontaskfour.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Component
public class DataLoader implements CommandLineRunner {

    @Value("${spring.sql.init.mode}")
    String initMode;

    final PasswordEncoder passwordEncoder;
    final RoleRepository roleRepo;
    final UserRepository userRepo;

    public DataLoader(PasswordEncoder passwordEncoder, RoleRepository roleRepo, UserRepository userRepo) {
        this.passwordEncoder = passwordEncoder;
        this.roleRepo = roleRepo;
        this.userRepo = userRepo;
    }


    @Override
    public void run(String... args) throws Exception {
        if (initMode.equals("always")) {

            Role user_role = new Role( "ROLE_USER");
            if (roleRepo.findByName("ROLE_USER")!=null){

            }else {
                Role saveUser = roleRepo.save(user_role);
            }

//            Set<Role> userRole = new HashSet<>();
//            userRole.add(saveUser);
//
//            User user1 = new User("Asadbek",
//                    "asatbekxalimjonov2000@gmail.com",
//                    LocalDateTime.now(),
//                    LocalDateTime.now(),
//                    false,
//                    passwordEncoder.encode("1111"),
//                    userRole);
//
//            User user2 = new User("Eldor",
//                    "ch.eldor1999@gmail.com",
//                    LocalDateTime.now(),
//                    LocalDateTime.now(),
//                    false,
//                    passwordEncoder.encode("2222"),
//                    userRole);
//
//            User user3 = new User("Saidbek",
//                    "saidbek.1997@gmail.com",
//                    LocalDateTime.now(),
//                    LocalDateTime.now(),
//                    false,
//                    passwordEncoder.encode("3333"),
//                    userRole);
//
//            userRepo.save(user1);
//            userRepo.save(user2);
//            userRepo.save(user3);

        }
    }
}

package com.itransition.itransitiontaskfour.service;


import com.itransition.itransitiontaskfour.entity.Role;
import com.itransition.itransitiontaskfour.entity.User;
import com.itransition.itransitiontaskfour.repository.RoleRepository;
import com.itransition.itransitiontaskfour.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService,
        ApplicationListener<AuthenticationSuccessEvent> {

    private final UserRepository userRepo;
    private final RoleRepository roleRepo;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepo.findByEmail(email);
        if (user == null) {
            log.error("User not found");
            throw new UsernameNotFoundException("User not found");
        } else {
            log.info("User found {} user ", email);
        }
        return user;
    }


    @Override
    public Map<String,String> save(User userDto) {

        Map<String,String> messages = new HashMap<>();
        User user = userRepo.findByEmail(userDto.getEmail());
        if (user==null) {
            try {

                User savingUser = new User();
                Role role_user = roleRepo.findByName("ROLE_USER");
                Set<Role> userRole = new HashSet<>();
                userRole.add(role_user);

                String password = userDto.getPassword();

                savingUser.setRegistrationTime(LocalDateTime.now());
                savingUser.setPassword(getEncoder().encode(password));
                savingUser.setRoles(userRole);
                savingUser.setEmail(userDto.getEmail());
                savingUser.setName(userDto.getName());

                userRepo.save(savingUser);
                messages.put("success", "Successfully registered");
                return messages;

            } catch (Exception ignored) {
            }
        }

        messages.put("error","This email already taken");
        return messages;
    }

    @Override
    public List<User> getAllUsers() {
        List<User> all = userRepo.findAll();
        Collections.sort(all);
        return all;
    }

    @Override
    public Map<String, String> delete(Long id,HttpServletRequest request) {
        Map<String,String> messages = new HashMap<>();
        try {
            if (userRepo.existsById(id)) {
                User user = userRepo.findById(id).get();
                userRepo.deleteById(id);
                Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                String username = ((UserDetails)principal).getUsername();

                if(user.getEmail().equals(username)) {
                    HttpSession session = request.getSession(false);
                    SecurityContextHolder.clearContext();
                    if (session != null) {
                        session.invalidate();
                    }
                }
                messages.put("success", "Successfully deleted");
                return messages;
            }
        }catch (Exception ignored){}
        messages.put("error","Deleting error");
        return messages;
    }

    @Override
    public Map<String, String> blockUser(Long id, HttpServletRequest request) {
        Map<String,String> messages = new HashMap<>();
        try {
            if (userRepo.findById(id).isPresent()) {
                User user = userRepo.findById(id).get();
                if (!user.isBlocked()) {
                    user.setBlocked(true);
                    userRepo.save(user);
                    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                    String username = ((UserDetails)principal).getUsername();

                    if(user.getEmail().equals(username)) {
                        HttpSession session = request.getSession(false);
                        SecurityContextHolder.clearContext();
                        if (session != null) {
                            session.invalidate();
                        }
                    }

                    messages.put("success", "Successfully blocked");
                    return messages;
                }
            }
        }catch (Exception ignored){}
        messages.put("error","Blocking error");
        return messages;
    }

    @Override
    public Map<String, String> unBlockUser(Long id) {
        Map<String,String> messages = new HashMap<>();
        try {
            if (userRepo.findById(id).isPresent()) {

                User user = userRepo.findById(id).get();
                if (user.isBlocked()) {
                    user.setBlocked(false);
                    userRepo.save(user);
                    messages.put("success", "Successfully UnBlocked");
                    return messages;
                }
            }
        }catch (Exception ignored){}
        messages.put("error","UnBlocking error");
        return messages;
    }

    @Override
    public Map<String, String> deleteAll(List<Long> users,HttpServletRequest request) {
        Map<String,String> messages = new HashMap<>();
        try {
            boolean isFound=false;
            for (Long id : users) {
                if (userRepo.existsById(id)) {
                    User user = userRepo.findById(id).get();
                    userRepo.deleteById(id);
                    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                    String username = ((UserDetails) principal).getUsername();

                    if (user.getEmail().equals(username)) {
                        isFound=true;
                    }
                }
            }
            messages.put("success", "Successfully deleted");

            if (isFound){
                HttpSession session = request.getSession(false);
                SecurityContextHolder.clearContext();
                if (session != null) {
                    session.invalidate();
                }
            }

            return messages;
        }catch (Exception ignored){}
        messages.put("error","Deleting error");
        return messages;
    }

    @Override
    public void blockAll(List<Long> usersId, HttpServletRequest request) {
        boolean isFound=false;
        try {
            for (Long id : usersId) {
                if (userRepo.findById(id).isPresent()) {
                    User user = userRepo.findById(id).get();
                    if (!user.isBlocked()) {
                        user.setBlocked(true);
                        userRepo.save(user);
                        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                        String username = ((UserDetails)principal).getUsername();

                        if(user.getEmail().equals(username)) {
                            isFound=true;
                        }

                    }
                }
            }
        }catch (Exception ignored){}

        if(isFound){
            HttpSession session = request.getSession(false);
            SecurityContextHolder.clearContext();
            if (session != null) {
                session.invalidate();
            }
        }

    }

    @Override
    public void unBlockAll(List<Long> usersId, HttpServletRequest request) {
        try {
            for (Long id : usersId) {
                if (userRepo.findById(id).isPresent()) {

                    User user = userRepo.findById(id).get();
                    if (user.isBlocked()) {
                        user.setBlocked(false);
                        userRepo.save(user);

                    }
                }
            }
        }catch (Exception ignored){}
    }

    public PasswordEncoder getEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        String userName = ((UserDetails) event.getAuthentication().
                getPrincipal()).getUsername();
        User currentUser = userRepo.findByEmail(userName);
        currentUser.setLastLoginTime(LocalDateTime.now());
        userRepo.save(currentUser);
    }


    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }
}

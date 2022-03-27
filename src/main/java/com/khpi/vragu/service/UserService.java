package com.khpi.vragu.service;

import com.khpi.vragu.config.PasswordConfig;
import com.khpi.vragu.domain.Role;
import com.khpi.vragu.domain.User;
import com.khpi.vragu.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private EmailSender emailSender;

    @Autowired
    private PasswordConfig passwordConfig;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByUsername(username);
    }

    public boolean addUser(User user) {
        User userFromDb = userRepo.findByUsername(user.getUsername());

        if (userFromDb != null) {
            return false;
        }

        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        user.setActivationCode(UUID.randomUUID().toString());
        user.setPassword(passwordConfig.getPasswordEncoder().encode(user.getPassword()));

        userRepo.save(user);

        sendMessage(user);

        return true;
    }

    private void sendMessage(User user) {
        if (!user.getEmail().isEmpty()) {
            String message = String.format("Hello %s! \n" +
                            "Welcome to VRagu. Please visit link: http://localhost:8080/activate/%s",
                    user.getUsername(),
                    user.getActivationCode()
            );
            emailSender.send(user.getEmail(), "Activation code", message);
        }
    }

    public boolean activateUser(String code) {
        User user = userRepo.findByActivationCode(code);

        if (user == null) {
            return false;
        }

        user.setActivationCode(null);
        userRepo.save(user);

        return true;
    }

    public void editUser(String username, Map<String, String> form, Long userId) {
        User user = userRepo.findById(userId);

        Set<Role> roles = Arrays.stream(Role.values())
                .filter(r -> form.containsKey(r.name()))
                .collect(Collectors.toSet());

        user.setRoles(roles);

        user.setUsername(username);
        userRepo.save(user);
    }

    public List<User> findAll() {
        return userRepo.findAll();
    }

    public User findById(Long id) {
        return userRepo.findById(id);
    }

    public void updateProfile(User user, String username, String email, String password) {
        boolean isEmailChanged = (email != null && !email.equals(user.getEmail())) ||
                (user.getEmail() != null && !user.getEmail().equals(email));

        if (isEmailChanged) {
            user.setEmail(email);
        }

        if (username != null && !username.isEmpty()) {
            user.setUsername(username);
        }

        if (password != null && !password.isEmpty()) {
            user.setPassword(password);
        }

        user.setActivationCode(UUID.randomUUID().toString());

        userRepo.save(user);

        if (isEmailChanged) {
            sendMessage(user);
        }
    }
}

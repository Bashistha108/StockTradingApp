package com.springapp.springapp.service;

import com.springapp.springapp.entity.User;
import com.springapp.springapp.entity.UserType;
import com.springapp.springapp.repository.UserRepository;
import com.springapp.springapp.repository.UserTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserTypeRepository userTypeRepository;
    private final PasswordEncoder passwordEncoder; //For Bcrypting

    @Autowired
    public UserService(UserRepository userRepository, UserTypeRepository userTypeRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userTypeRepository = userTypeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Integer instead of int to hold null.
    // Use Optional to avoid null pointer exception and to use isPresent... functions
    public User getUserById(Integer id) {
        try {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("User not found with id: " + id));
            System.out.println("User: " + user);
            return user;
        } catch (NoSuchElementException e) {
            System.out.println("Exception: " + e.getMessage());
            throw e;
        }
    }



    // Also update
    @Transactional
    public User saveUser(User user) {
        if (user.getUserId() != 0 && userRepository.existsById(user.getUserId())) {
            // Update existing user
            User existingUser = userRepository.findById(user.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            existingUser.setEmail(user.getEmail());
            existingUser.setFirstName(user.getFirstName());
            existingUser.setLastName(user.getLastName());
            // Re-hashing password before updating
            existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
            existingUser.setRegistrationDate(LocalDateTime.now());
            existingUser.setUserType(user.getUserType());
            // Save updated user (automatically merges the entity)
            return userRepository.save(existingUser);
        } else {
            // Save new user
            String hashedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(hashedPassword);
            user.setRegistrationDate(LocalDateTime.now());

            UserType userType = userTypeRepository.findById(user.getUserType().getUserTypeId())
                    .orElseThrow(() -> new RuntimeException("UserType not found"));
            user.setUserType(userType);
            userType.getUsers().add(user);

            return userRepository.save(user);
        }
    }

    @Transactional
    public void deleteUser(Integer id) {
        User user = userRepository.findById(id).orElseThrow(()->new NoSuchElementException("User not found"));
        user.setUserType(null);
        userRepository.delete(user);
        System.out.println("User with id: " + id + " deleted");
    }

    @Transactional
    public void deleteAllUsers() {
        userRepository.deleteAll();
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }



}
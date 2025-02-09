package org.moldavets.service.Impl;

import org.moldavets.data.Impl.UserRepositoryImpl;
import org.moldavets.data.UserRepository;
import org.moldavets.model.User;
import org.moldavets.service.EmailVerificationService;
import org.moldavets.service.UserService;

import java.util.UUID;

public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private EmailVerificationService emailVerificationService;

    public UserServiceImpl(UserRepository userRepository,
                           EmailVerificationService emailVerificationService) {
        this.userRepository = userRepository;
        this.emailVerificationService = emailVerificationService;
    }

    @Override
    public User createUser(String firstName,
                           String lastName,
                           String email,
                           String password,
                           String repeatPassword) {

        if(firstName == null || firstName.trim().length() == 0) {
            throw new IllegalArgumentException("User's first name cannot be empty");
        } else if(lastName == null || lastName.trim().length() == 0) {
            throw new IllegalArgumentException("User's last name cannot be empty");
        } else if(email == null || email.trim().length() == 0) {
            throw new IllegalArgumentException("User's email cannot be empty");
        } else if(password == null || password.trim().length() >= 9 || password.trim().length() == 0) {
            throw new IllegalArgumentException("User's password cannot be empty or longer than 8 characters");
        }

        User user = new User(firstName, lastName, email, UUID.randomUUID().toString());

        boolean isUserSaved = false;

        try {
            isUserSaved = userRepository.save(user);
        } catch (RuntimeException e) {
            throw new UserServiceException(e.getMessage());
        }

        if(!isUserSaved) {
            throw new UserServiceException("Could not to create user");
        }

        try {
            emailVerificationService.scheduleEmailConfirmation(user);
        } catch (RuntimeException e) {
            throw new UserServiceException(e.getMessage());
        }

        return user;
    }
}

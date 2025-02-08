package org.moldavets.service.Impl;

import org.moldavets.data.Impl.UserRepositoryImpl;
import org.moldavets.data.UserRepository;
import org.moldavets.model.User;
import org.moldavets.service.UserService;

import java.util.UUID;

public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
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

        boolean isUserSaved = userRepository.save(user);

        if(!isUserSaved) {
            throw new UserServiceException("Could not to create user");
        }

        return user;
    }
}

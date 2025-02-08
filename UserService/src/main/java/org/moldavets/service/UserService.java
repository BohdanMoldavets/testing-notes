package org.moldavets.service;

import org.moldavets.model.User;

public interface UserService {

    User createUser(String firstName,
                    String lastName,
                    String email,
                    String password,
                    String repeatPassword);

}

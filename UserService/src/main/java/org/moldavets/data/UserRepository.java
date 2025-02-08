package org.moldavets.data;

import org.moldavets.model.User;

public interface UserRepository {
    boolean save(User user);
}

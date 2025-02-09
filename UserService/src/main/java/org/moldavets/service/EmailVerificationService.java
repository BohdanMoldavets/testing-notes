package org.moldavets.service;

import org.moldavets.model.User;

public interface EmailVerificationService {
    void scheduleEmailConfirmation(User user);
}

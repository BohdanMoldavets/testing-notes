package org.moldavets.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.moldavets.data.UserRepository;
import org.moldavets.model.User;
import org.moldavets.service.Impl.EmailVerificationServiceException;
import org.moldavets.service.Impl.EmailVerificationServiceImpl;
import org.moldavets.service.Impl.UserServiceException;
import org.moldavets.service.Impl.UserServiceImpl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    UserServiceImpl userService;

    @Mock
    EmailVerificationServiceImpl emailVerificationService;

    @Mock
    UserRepository userRepository;


    String firstName;
    String lastName;
    String email;
    String password;
    String repeatPassword;

    @BeforeEach
    void init() {
        firstName = "Bohdan";
        lastName = "Moldavets";
        email = "bohdan.moldavets@gmail.com";
        password = "password";
        repeatPassword = "password";
    }

    @Test
    public void testCreateUser_whenUserDetailsProvided_returnsUserObject() {

        when(userRepository.save(Mockito.any(User.class))).thenReturn(true);

        User user = userService.createUser(firstName,lastName,email,password,repeatPassword);

        assertNotNull(user, "The createUser() should not to return null");
        assertEquals(firstName, user.getFirstName(), "User's first name is incorrect");
        assertEquals(lastName, user.getLastName(), "User's last name is incorrect");
        assertEquals(email, user.getEmail(), "User's email is incorrect");
        assertNotNull(user.getId(), "User's id is incorrect");

        Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any(User.class));
    }

    @DisplayName("Empty first name causes correct exception")
    @Test
    public void testCreateUser_whenUserFirstNameEmpty_throwsIllegalArgumentException() {
        String firstName = "";

        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,
                () -> userService.createUser(firstName,lastName,email,password,repeatPassword));

        assertEquals("User's first name cannot be empty", exception.getMessage());
    }

    @DisplayName("Empty last name causes correct exception")
    @Test
    public void testCreateUser_whenUserLastNameEmpty_throwsIllegalArgumentException() {
        String lastName = "";

        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,
                () -> userService.createUser(firstName,lastName,email,password,repeatPassword));

        assertEquals("User's last name cannot be empty", exception.getMessage());
    }

    @DisplayName("Empty email causes correct exception")
    @Test
    public void testCreateUser_whenUserEmailEmpty_throwsIllegalArgumentException() {
        String email = "";

        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,
                () -> userService.createUser(firstName,lastName,email,password,repeatPassword));

        assertEquals("User's email cannot be empty", exception.getMessage());
    }

    @DisplayName("Empty password causes correct exception")
    @Test
    public void testCreateUser_whenUserPasswordEmpty_throwsIllegalArgumentException() {
        String password = "";

        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,
                () -> userService.createUser(firstName,lastName,email,password,repeatPassword));

        assertEquals("User's password cannot be empty or longer than 8 characters", exception.getMessage());
    }

    @DisplayName("Password longer than 8 characters - must throw correct exception")
    @Test
    public void testCreateUser_whenUserPasswordLongerThanEightCharacters_throwsIllegalArgumentException() {
        String password = "123456789";

        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,
                () -> userService.createUser(firstName,lastName,email,password,repeatPassword));

        assertEquals("User's password cannot be empty or longer than 8 characters", exception.getMessage());
    }

    @DisplayName("If save() method causes RuntimeException, a UserService is thrown")
    @Test
    public void testCreateUser_whenSaveMethodThrowException_thenThrowsUserServiceException() {

        when(userRepository.save(any(User.class))).thenThrow(RuntimeException.class);

        assertThrows(UserServiceException.class,
                () -> userService.createUser(firstName,lastName,email,password,repeatPassword));
    }

    @DisplayName("Email notification exception is handled")
    @Test
    public void testCreateUser_whenEmailNotificationMethodThrowException_thenThrowsUserServiceException() {

        when(userRepository.save(any(User.class))).thenReturn(true);

        doThrow(EmailVerificationServiceException.class)
                .when(emailVerificationService).scheduleEmailConfirmation(any(User.class));

        assertThrows(UserServiceException.class,
                () -> userService.createUser(firstName,lastName,email,password,repeatPassword));

        verify(emailVerificationService).scheduleEmailConfirmation(any(User.class));
    }

}
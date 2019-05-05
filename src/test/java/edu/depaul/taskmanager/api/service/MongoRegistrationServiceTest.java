package edu.depaul.taskmanager.api.service;

import edu.depaul.taskmanager.api.model.RegistrationRequest;
import edu.depaul.taskmanager.api.model.User;
import edu.depaul.taskmanager.api.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class MongoRegistrationServiceTest {

    private MongoRegistrationService mongoRegistrationService;
    private UserRepository userRepository;
    private User newUser = User.newBuilder()
            .withEmail("email")
            .withFirstName("first")
            .withLastName("last")
            .withPassword("password")
            .build();
    private User expectedUser = User.newBuilder()
            .withEmail("email")
            .withPassword("password")
            .withFirstName("first")
            .withLastName("last")
            .withId("12345")
            .build();
    private RegistrationRequest registrationRequest = new RegistrationRequest("first", "last", "email", "password", "password");

    @Before
    public void setUp() {
        userRepository = mock(UserRepository.class);
        mongoRegistrationService = new MongoRegistrationService(userRepository);
        when(userRepository.save(any())).thenReturn(expectedUser);
    }

    @Test
    public void register_callsRepository_withUser() {
        mongoRegistrationService.register(registrationRequest);
        verify(userRepository).save(newUser);
    }

    @Test
    public void register_returnsEmpty_onPasswordMismatch() {
        RegistrationRequest badRegistration = new RegistrationRequest("first", "last", "email", "password", "nope");
        assertThat(mongoRegistrationService.register(badRegistration)).isEmpty();
    }

    @Test
    public void login_returnsNewUser_onSuccess() {
        Optional<User> newUser = mongoRegistrationService.register(registrationRequest);
        assertThat(newUser).isEqualTo(Optional.of(expectedUser));
    }
}

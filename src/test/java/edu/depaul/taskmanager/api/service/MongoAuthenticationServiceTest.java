package edu.depaul.taskmanager.api.service;

import edu.depaul.taskmanager.api.model.AuthenticationRequest;
import edu.depaul.taskmanager.api.model.Session;
import edu.depaul.taskmanager.api.model.User;
import edu.depaul.taskmanager.api.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static org.mockito.Mockito.*;

public class MongoAuthenticationServiceTest {

    private MongoAuthenticationService mongoAuthenticationService;
    private UserRepository userRepository;
    private User expectedUser = User.newBuilder()
            .withEmail("email")
            .withFirstName("first")
            .withLastName("last")
            .withId("12345")
            .build();

    @Before
    public void setUp() throws Exception {
        userRepository = mock(UserRepository.class);
        mongoAuthenticationService = new MongoAuthenticationService(userRepository);
        when(userRepository.findByEmailEqualsIgnoreCaseAndPasswordEquals(any(), any())).thenReturn(Optional.of(expectedUser));
    }

    @Test
    public void login_callsRepository() {
        mongoAuthenticationService.login(new AuthenticationRequest("email", "password"));
        verify(userRepository).findByEmailEqualsIgnoreCaseAndPasswordEquals("email", "password");
    }

    @Test
    public void login_returnsSession() {
        Optional<Session> login = mongoAuthenticationService.login(new AuthenticationRequest("email", "password"));
        Assertions.assertThat(login).isEqualTo(
                Optional.of(Session.newBuilder()
                        .withFirstName("first")
                        .withLastName("last")
                        .withUserId("12345")
                        .build())
        );
    }
}
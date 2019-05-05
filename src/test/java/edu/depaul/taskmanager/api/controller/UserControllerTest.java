package edu.depaul.taskmanager.api.controller;

import edu.depaul.taskmanager.api.model.AuthenticationRequest;
import edu.depaul.taskmanager.api.model.RegistrationRequest;
import edu.depaul.taskmanager.api.model.Session;
import edu.depaul.taskmanager.api.model.User;
import edu.depaul.taskmanager.api.service.AuthenticationService;
import edu.depaul.taskmanager.api.service.RegistrationService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class UserControllerTest {

    private UserController userController;
    private AuthenticationService authenticationService;
    private AuthenticationRequest expectedRequest;
    private RegistrationService registrationService;
    private RegistrationRequest registrationRequest;
    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        authenticationService = mock(AuthenticationService.class);
        registrationService = mock(RegistrationService.class);
        userController = new UserController(authenticationService, registrationService);
        setUpAuthenticationService();
        setUpRegistrationService();
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    private void setUpAuthenticationService() throws Exception {
        expectedRequest = new AuthenticationRequest("example@example.com", "abc123");
        when(authenticationService.login(any())).thenReturn(
                Optional.of(Session.newBuilder()
                        .withFirstName("first")
                        .withLastName("last")
                        .withUserId("12345")
                        .build())
        );
    }

    private void setUpRegistrationService() throws Exception {
        registrationRequest = new RegistrationRequest("first", "last", "email", "password", "password");
        when(registrationService.register(any())).thenReturn(
                Optional.of(User.newBuilder()
                        .withId("12345")
                        .withEmail("example@example.com")
                        .withPassword("password")
                        .withFirstName("first")
                        .withLastName("last")
                        .build())
        );
    }

    @Test
    public void login_callsAuthenticationService() {
        userController.login(expectedRequest);
        verify(authenticationService).login(expectedRequest);
    }

    @Test
    public void login_returnsSession_onSuccess() {
        ResponseEntity loginResponse = userController.login(expectedRequest);
        assertThat(loginResponse.getBody()).isEqualTo(Session.newBuilder()
                .withFirstName("first")
                .withLastName("last")
                .withUserId("12345")
                .build());
    }

    @Test
    public void login_returns200_onSuccess() {
        ResponseEntity loginResponse = userController.login(expectedRequest);
        assertThat(loginResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void login_returns401_onFailure() {
        reset(authenticationService);
        when(authenticationService.login(any())).thenReturn(Optional.empty());
        ResponseEntity loginResponse = userController.login(expectedRequest);
        assertThat(loginResponse.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void login_acceptsNetworkCalls() throws Exception {
        mockMvc.perform(post("/sessions")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "  \"email\": \"example@example.com\",\n" +
                        "  \"password\": \"abc123\"\n" +
                        "}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", equalTo("first")))
                .andExpect(jsonPath("$.lastName", equalTo("last")))
                .andExpect(jsonPath("$.userId", equalTo("12345")));
        verify(authenticationService).login(expectedRequest);
    }

    @Test
    public void register_callsRegistrationService() {
        userController.register(registrationRequest, UriComponentsBuilder.newInstance());
        verify(registrationService).register(registrationRequest);
    }

    @Test
    public void register_returnsSession_onSuccess() {
        ResponseEntity<Session> response = userController.register(registrationRequest, UriComponentsBuilder.newInstance());
        assertThat(response.getBody()).isEqualTo(Session.newBuilder()
            .withFirstName("first")
            .withLastName("last")
            .withUserId("12345")
            .build());
    }

    @Test
    public void register_returns201_onSuccess() {
        ResponseEntity<Session> response = userController.register(registrationRequest, UriComponentsBuilder.newInstance());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    public void register_returnsLocationHeader_onSuccess() {
        URI locationHeader = userController.register(registrationRequest, UriComponentsBuilder.newInstance()).getHeaders().getLocation();
        assertThat(locationHeader).isNotNull();
        assertThat(locationHeader.getPath()).isEqualTo("/users/12345");
    }

    @Test
    public void register_returns400_onFailure() {
        reset(registrationService);
        when(registrationService.register(any())).thenReturn(Optional.empty());

        ResponseEntity<Session> response = userController.register(registrationRequest, UriComponentsBuilder.newInstance());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void register_acceptsNetworkCalls() throws Exception {
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "  \"firstName\": \"first\",\n" +
                        "  \"lastName\": \"last\",\n" +
                        "  \"email\": \"example@example.com\",\n" +
                        "  \"password\": \"password\",\n" +
                        "  \"passwordConfirmation\": \"password\"\n" +
                        "}"))
                .andExpect(status().isCreated());
    }
}

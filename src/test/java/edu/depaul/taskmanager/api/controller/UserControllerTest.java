package edu.depaul.taskmanager.api.controller;

import edu.depaul.taskmanager.api.model.AuthenticationRequest;
import edu.depaul.taskmanager.api.model.Session;
import edu.depaul.taskmanager.api.service.AuthenticationService;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class UserControllerTest {

    private UserController userController;
    private AuthenticationService authenticationService;
    private AuthenticationRequest expectedRequest;
    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        authenticationService = mock(AuthenticationService.class);
        userController = new UserController(authenticationService);
        expectedRequest = new AuthenticationRequest("example@example.com", "abc123");
        when(authenticationService.login(any())).thenReturn(
                Optional.of(Session.newBuilder()
                        .withFirstName("first")
                        .withLastName("last")
                        .withUserId("12345")
                        .build())
        );

        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
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
                .andExpect(jsonPath("$.firstName", Matchers.equalTo("first")))
                .andExpect(jsonPath("$.lastName", Matchers.equalTo("last")))
                .andExpect(jsonPath("$.userId", Matchers.equalTo("12345")));
        verify(authenticationService).login(expectedRequest);
    }
}

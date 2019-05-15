package edu.depaul.taskmanager.api.controller;

import edu.depaul.taskmanager.api.model.Team;
import edu.depaul.taskmanager.api.model.TeamMembers;
import edu.depaul.taskmanager.api.service.TeamService;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TeamControllerTest {

    private TeamController teamController;
    private TeamService mockTeamService;
    private UriComponentsBuilder uriComponentsBuilder;
    private MockMvc mockMvc;
    private Team requestTeam = Team.newBuilder()
            .withName("my team")
            .withMembers(singletonList(TeamMembers.newBuilder().withId("1").build()))
            .build();

    private List<Team> expectedTeamList = asList(
            Team.newBuilder().withId("1").withName("one").build(),
            Team.newBuilder().withId("2").withName("two").build()
    );

    @Before
    public void setUp() throws Exception {
        uriComponentsBuilder = UriComponentsBuilder.newInstance();
        mockTeamService = mock(TeamService.class);
        teamController = new TeamController(mockTeamService);

        mockMvc = MockMvcBuilders.standaloneSetup(teamController).build();

        when(mockTeamService.saveTeam(any())).thenReturn(Team.newBuilder().withName("team name").withId("team-id").build());
        when(mockTeamService.getAllTeams()).thenReturn(expectedTeamList);
        when(mockTeamService.getTeamsBy(any())).thenReturn(expectedTeamList);
    }

    @Test
    public void createTeam_callsService_withName() {
        teamController.createTeam(requestTeam, uriComponentsBuilder);
        verify(mockTeamService).saveTeam(requestTeam);
    }

    @Test
    public void createTeam_returnsTeam_fromService() {
        ResponseEntity responseEntity = teamController.createTeam(requestTeam, uriComponentsBuilder);
        assertThat(responseEntity.getBody()).isInstanceOf(Team.class);
        assertThat(((Team) responseEntity.getBody()).getName()).isEqualTo("team name");
    }

    @Test
    public void createTeam_returns_201CREATED() {
        ResponseEntity responseEntity = teamController.createTeam(requestTeam, uriComponentsBuilder);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    public void createTeam_setsLocationHeader() {
        ResponseEntity responseEntity = teamController.createTeam(requestTeam, uriComponentsBuilder);
        assertThat(responseEntity.getHeaders().getLocation()).isNotNull();
        assertThat(responseEntity.getHeaders().getLocation().getPath()).isEqualTo("/teams/team-id");
    }

    @Test
    public void createTeam_acceptsNetworkCalls() throws Exception {
        mockMvc.perform(post("/teams")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "  \"teamName\": \"team name\",\n" +
                        "  \"members\": [\n" +
                        "    {\n" +
                        "      \"id\": \"123\"\n" +
                        "    }\n" +
                        "  ]\n" +
                        "}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", CoreMatchers.equalTo("team-id")))
                .andExpect(jsonPath("$.name", CoreMatchers.equalTo("team name")));
    }

    @Test
    public void getAllTeams_returnsTeamsFromService() {
        ResponseEntity responseEntity = teamController.getTeams(null);
        verify(mockTeamService).getAllTeams();
        assertThat(responseEntity.getBody()).isEqualTo(expectedTeamList);

    }

    @Test
    public void getAllTeams_returnsTeamsFromService_whenUserId() {
        ResponseEntity responseEntity = teamController.getTeams("id");
        verify(mockTeamService).getTeamsBy("id");
        assertThat(responseEntity.getBody()).isEqualTo(expectedTeamList);

    }

    @Test
    public void getAllTeams_acceptsNetworkRequests() throws Exception {
        mockMvc.perform(get("/teams"))
                .andExpect(status().isOk());
//                .andExpect(jsonPath("$.id", CoreMatchers.equalTo("team-id")))
//                .andExpect(jsonPath("$.name", CoreMatchers.equalTo("team name")));
    }
}

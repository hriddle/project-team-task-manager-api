package edu.depaul.taskmanager.api.service;


import edu.depaul.taskmanager.api.model.Team;
import edu.depaul.taskmanager.api.model.TeamMembers;
import edu.depaul.taskmanager.api.repository.TeamRepository;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TeamServiceTest {

    private TeamService teamService;
    private TeamRepository mockTeamRepository;
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
        mockTeamRepository = mock(TeamRepository.class);
        teamService = new TeamService(mockTeamRepository);
    }

    @Test
    public void createTeam_sendsTeamToRepository() {
        teamService.saveTeam(requestTeam);
        verify(mockTeamRepository).save(requestTeam);
    }

    @Test
    public void createTeam_returnsTeam_fromRepository() {
        Team expectedTeam = Team.newBuilder().withId("id").withName("my team").build();
        when(mockTeamRepository.save(any())).thenReturn(expectedTeam);
        Team actualTeam = teamService.saveTeam(requestTeam);
        assertThat(actualTeam).isEqualTo(expectedTeam);
    }

    @Test
    public void getAllTeams_returnsTeams_fromRepository() {
        when(mockTeamRepository.findAll()).thenReturn(expectedTeamList);
        List<Team> actualTeams = teamService.getAllTeams();
        assertThat(actualTeams).isEqualTo(expectedTeamList);
    }
}
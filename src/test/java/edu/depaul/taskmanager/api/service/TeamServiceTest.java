package edu.depaul.taskmanager.api.service;

import edu.depaul.taskmanager.api.model.Team;
import edu.depaul.taskmanager.api.model.TeamMember;
import edu.depaul.taskmanager.api.model.TeamMemberDetail;
import edu.depaul.taskmanager.api.model.User;
import edu.depaul.taskmanager.api.repository.TeamRepository;
import edu.depaul.taskmanager.api.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TeamServiceTest {

    private TeamService teamService;
    private TeamRepository mockTeamRepository;
    private UserRepository mockUserRepository;
    private Team requestTeam = Team.newBuilder()
            .withName("my team")
            .withMembers(singletonList(TeamMember.newBuilder().withId("1").build()))
            .build();
    private List<Team> expectedTeamList = asList(
            Team.newBuilder().withId("1").withName("one").build(),
            Team.newBuilder().withId("2").withName("two").build()
    );

    @Before
    public void setUp() throws Exception {
        mockTeamRepository = mock(TeamRepository.class);
        mockUserRepository = mock(UserRepository.class);
        teamService = new TeamService(mockTeamRepository, mockUserRepository);
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

    @Test
    public void getTeamMembers_findsTeam_fromRepository() {
        when(mockTeamRepository.findById(any())).thenReturn(Optional.of(Team.newBuilder().build()));
        teamService.getTeamMembers("1");
        verify(mockTeamRepository).findById("1");
    }

    @Test
    public void getTeamMembers_findsUsers_fromTeamList() {
        when(mockTeamRepository.findById(any())).thenReturn(Optional.of(Team.newBuilder()
                .withMembers(asList(
                        TeamMember.newBuilder().withId("123").build(),
                        TeamMember.newBuilder().withId("456").build(),
                        TeamMember.newBuilder().withId("789").build())
                )
                .build()
        ));
        teamService.getTeamMembers("1");
        verify(mockUserRepository).findAllById(asList("123", "456", "789"));
    }

    @Test
    public void getTeamMembers_TeamMemberDetails() {
        when(mockTeamRepository.findById(any())).thenReturn(Optional.of(Team.newBuilder()
                .withMembers(asList(
                        TeamMember.newBuilder().withId("123").build(),
                        TeamMember.newBuilder().withId("456").build(),
                        TeamMember.newBuilder().withId("789").build())
                )
                .build()
        ));
        when(mockUserRepository.findAllById(any())).thenReturn(asList(
                User.newBuilder().withId("123").withFirstName("fname0").withLastName("lname0").build(),
                User.newBuilder().withId("456").withFirstName("fname1").withLastName("lname1").build(),
                User.newBuilder().withId("789").withFirstName("fname2").withLastName("lname2").build())
        );
        List<TeamMemberDetail> teamMembers = teamService.getTeamMembers("1");
        assertThat(teamMembers).hasSize(3);
        assertThat(teamMembers.get(0).getId()).isEqualTo("123");
        assertThat(teamMembers.get(0).getFirstName()).isEqualTo("fname0");
        assertThat(teamMembers.get(0).getLastName()).isEqualTo("lname0");

        assertThat(teamMembers.get(1).getId()).isEqualTo("456");
        assertThat(teamMembers.get(1).getFirstName()).isEqualTo("fname1");
        assertThat(teamMembers.get(1).getLastName()).isEqualTo("lname1");

        assertThat(teamMembers.get(2).getId()).isEqualTo("789");
        assertThat(teamMembers.get(2).getFirstName()).isEqualTo("fname2");
        assertThat(teamMembers.get(2).getLastName()).isEqualTo("lname2");
    }
}
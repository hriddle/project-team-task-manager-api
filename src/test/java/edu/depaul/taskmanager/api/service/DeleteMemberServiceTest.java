package edu.depaul.taskmanager.api.service;


import edu.depaul.taskmanager.api.model.Task;
import edu.depaul.taskmanager.api.model.TaskList;
import edu.depaul.taskmanager.api.model.Team;
import edu.depaul.taskmanager.api.model.TeamMember;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static java.util.Arrays.asList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class DeleteMemberServiceTest {

    private DeleteMemberService deleteMemberService;
    private TeamService mockTeamService;
    private TaskListService mockTaskListService;

    private String teamId = "123";

    @Before
    public void setUp() throws Exception {
        mockTaskListService = mock(TaskListService.class);
        mockTeamService = mock(TeamService.class);
        deleteMemberService = new DeleteMemberService(mockTeamService, mockTaskListService);

        when(mockTeamService.findTeamById(any())).thenReturn(
                Optional.of(Team.newBuilder()
                        .withId(teamId)
                        .withMembers(asList(
                                TeamMember.newBuilder().withId("delete-me").build(),
                                TeamMember.newBuilder().withId("dont-delete-me").build()
                        ))
                        .build()));
        when(mockTaskListService.getAllTeamLists(any())).thenReturn(
                asList(
                        TaskList.newBuilder()
                                .withOwnerId(teamId)
                                .withTasks(asList(
                                        Task.newBuilder().withName("assigned").withAssignedUser("dont-delete-me").build(),
                                        Task.newBuilder().withName("someone else").withAssignedUser("delete-me").build(),
                                        Task.newBuilder().withName("unassigned").build()
                                ))
                                .build(),
                        TaskList.newBuilder()
                                .withOwnerId(teamId)
                                .build()
                )
        );
    }

    @Test
    public void removeTeamMember_removesMemberFromTeam() {
        deleteMemberService.removeTeamMember(teamId,"delete-me");
        verify(mockTeamService).findTeamById(teamId);
        verify(mockTeamService).saveTeam(Team.newBuilder()
                .withId(teamId)
                .withMembers(asList(
                        TeamMember.newBuilder().withId("dont-delete-me").build()
                )).build());
    }

    @Test
    public void removeTeamMember_unassignsTasksForTeamMember() {
        deleteMemberService.removeTeamMember(teamId,"delete-me");
        verify(mockTaskListService).getAllTeamLists(teamId);
        verify(mockTaskListService).saveTaskLists(
                asList(
                        TaskList.newBuilder()
                                .withOwnerId(teamId)
                                .withTasks(asList(
                                        Task.newBuilder().withName("assigned").withAssignedUser("dont-delete-me").build(),
                                        Task.newBuilder().withName("someone else").withAssignedUser(null).build(),
                                        Task.newBuilder().withName("unassigned").build()
                                ))
                                .build(),
                        TaskList.newBuilder()
                                .withOwnerId(teamId)
                                .build()
                )
        );

    }
}
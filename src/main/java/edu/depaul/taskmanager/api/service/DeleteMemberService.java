package edu.depaul.taskmanager.api.service;

import edu.depaul.taskmanager.api.model.Task;
import edu.depaul.taskmanager.api.model.TaskList;
import edu.depaul.taskmanager.api.model.Team;
import edu.depaul.taskmanager.api.model.TeamMember;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class DeleteMemberService {

    private TeamService teamService;
    private TaskListService taskListService;

    @Autowired
    public DeleteMemberService(TeamService teamService, TaskListService taskListService) {
        this.teamService = teamService;
        this.taskListService = taskListService;
    }

    public void removeTeamMember(String teamID, String memberID) {
        teamService.findTeamById(teamID).map(
                team -> {
                    List<TeamMember> newTeamMembers = team.getMembers().stream().filter(teamMember -> !teamMember.getId().equals(memberID)).collect(toList());
                    return Team.newBuilder(team).withMembers(newTeamMembers).build();
                }
        ).map(team -> teamService.saveTeam(team))
                .orElse(null);

        List<TaskList> updateTaskList = taskListService.getAllTeamLists(teamID).stream()
                .map(taskList -> {
                    if (taskList.getTasks() == null) {
                        return taskList;
                    }
                    List<Task> newTaskList = new ArrayList<>(taskList.getTasks().size());
                    for (Task task : taskList.getTasks()) {
                        if (memberID.equals(task.getAssignedUser())) {
                            newTaskList.add(Task.newBuilder(task).withAssignedUser(null).build());
                        } else {
                            newTaskList.add(task);
                        }
                    }
                    return TaskList.newBuilder(taskList).withTasks(newTaskList).build();
                }).collect(toList());

        taskListService.saveTaskLists(updateTaskList);
    }
}

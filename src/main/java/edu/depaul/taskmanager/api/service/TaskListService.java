package edu.depaul.taskmanager.api.service;

import edu.depaul.taskmanager.api.model.CompletionDetails;
import edu.depaul.taskmanager.api.model.Team;
import edu.depaul.taskmanager.api.model.Task;
import edu.depaul.taskmanager.api.model.TaskList;
import edu.depaul.taskmanager.api.repository.TaskListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

@Service
public class TaskListService {
    private TeamService teamService;

    private TaskListRepository taskListRepository;

    @Autowired
    public TaskListService(TeamService teamService, TaskListRepository taskListRepository) {
        this.teamService = teamService;
        this.taskListRepository = taskListRepository;
    }

    public TaskList createPersonalList(String userId, TaskList newTaskList) {
        return taskListRepository.save(TaskList.newBuilder(newTaskList).withOwnerId(userId).withTasks(emptyList()).build());
    }

    public TaskList createTeamList(String teamId, String listName) {
        return taskListRepository.save(TaskList.newBuilder().withName(listName).withOwnerId(teamId).withTasks(emptyList()).build());
    }

    public TaskList createTeamList(String teamId, TaskList newList) {
        return taskListRepository.save(TaskList.newBuilder(newList).withOwnerId(teamId).withTasks(emptyList()).build());
    }

    public List<TaskList> getAllPersonalLists(String userId) {
        return taskListRepository.findByOwnerId(userId);
    }

    public List<TaskList> getAllTeamLists(String teamId) {
        return taskListRepository.findByOwnerId(teamId);
    }

    public List<TaskList> getAllTeamListsByType(String teamId, String type) {
        return getAllTeamLists(teamId)
                .stream()
                .filter(list -> list.getListType().equals(type))
                .collect(Collectors.toList());
    }

    public List<TaskList> getAllTeamTasksByUserId(String userId) {
        List<Team> teams = teamService.getTeamsBy(userId);
        List<TaskList> returnTaskList = new ArrayList<>();
        for (Team team : teams) {
            List<TaskList> listTaskList = taskListRepository.findByOwnerId(team.getId());

            for (TaskList taskList : listTaskList) {
                List<Task> listTask = taskList.getTasks();
                List<Task> newTaskList =  new ArrayList<>();
                for (Task task : listTask) {
                    if (userId.equals(task.getAssignedUser())) {
                        if (task.getCompletionDetails() == null) {
                            newTaskList.add(Task.newBuilder(task).build());
                        }
                        else if (task.getCompletionDetails().getCompletedBy().length()<=0) {
                            newTaskList.add(Task.newBuilder(task).build());
                        }
                    }
                }
                if (newTaskList.size() > 0) {
                    returnTaskList.add(TaskList.newBuilder(taskList).withTasks(newTaskList).build());
                }
            }
        }

        return returnTaskList;
    }

    public List<Task> getTasksInList(String listId) {
        Optional<TaskList> taskList = taskListRepository.findById(listId);
        return taskList.isPresent() && taskList.get().getTasks() != null ? taskList.get().getTasks() : emptyList();
    }

    public List<Task> addTaskToList(String listId, Task task) {
        Optional<TaskList> maybeTaskList = taskListRepository.findById(listId);
        if (maybeTaskList.isPresent()) {
            TaskList taskList = maybeTaskList.get();
            List<Task> tasks = taskList.getTasks() != null ? new ArrayList<>(taskList.getTasks()) : new ArrayList<>();
            tasks.add(task);
            TaskList updatedTaskList = TaskList.newBuilder(taskList).withTasks(tasks).build();
            TaskList savedTaskList = taskListRepository.save(updatedTaskList);
            return savedTaskList.getTasks();
        } else {
            return null;
        }
    }

    public List<TaskList> saveTaskLists(List<TaskList> taskLists) {
        return taskListRepository.saveAll(taskLists);
    }

    public Task updateTask(String listId, int index, Task task) {
        Optional<TaskList> maybeTaskList = taskListRepository.findById(listId);
        if (maybeTaskList.isPresent()) {
            TaskList taskList = maybeTaskList.get();
            if (taskList.getTasks() != null && taskList.getTasks().size() > index) {
                List<Task> tasks = new ArrayList<>(taskList.getTasks());
                tasks.set(index, task);
                TaskList updatedTaskList = TaskList.newBuilder(taskList).withTasks(tasks).build();
                TaskList savedTaskList = taskListRepository.save(updatedTaskList);
                return savedTaskList.getTasks().get(index);
            }
        }
        return null;
    }

    public void deleteTask(String listId, int index) {
        Optional<TaskList> maybeTaskList = taskListRepository.findById(listId);
        if (maybeTaskList.isPresent()) {
            TaskList taskList = maybeTaskList.get();
            if (taskList.getTasks() != null && taskList.getTasks().size() > index) {
                List<Task> tasks = new ArrayList<>(taskList.getTasks());
                tasks.remove(index);
                TaskList updatedTaskList = TaskList.newBuilder(taskList).withTasks(tasks).build();
                taskListRepository.save(updatedTaskList);
            }
        }
    }
}

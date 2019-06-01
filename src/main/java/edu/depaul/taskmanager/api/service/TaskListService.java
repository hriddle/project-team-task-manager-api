package edu.depaul.taskmanager.api.service;

import edu.depaul.taskmanager.api.model.Task;
import edu.depaul.taskmanager.api.model.TaskList;
import edu.depaul.taskmanager.api.repository.TaskListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;

@Service
public class TaskListService {

    private TaskListRepository taskListRepository;

    @Autowired
    public TaskListService(TaskListRepository taskListRepository) {
        this.taskListRepository = taskListRepository;
    }

    public TaskList createPersonalList(String userId, String listName) {
        return taskListRepository.save(TaskList.newBuilder().withName(listName).withOwnerId(userId).withTasks(emptyList()).build());
    }

    public TaskList createTeamList(String teamId, String listName) {
        return taskListRepository.save(TaskList.newBuilder().withName(listName).withOwnerId(teamId).withTasks(emptyList()).build());
    }

    public List<TaskList> getAllPersonalLists(String userId) {
        return taskListRepository.findByOwnerId(userId);
    }

    public List<TaskList> getAllTeamLists(String teamId) {
        return taskListRepository.findByOwnerId(teamId);
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

package edu.depaul.taskmanager.api.service;

import edu.depaul.taskmanager.api.model.Task;
import edu.depaul.taskmanager.api.model.TaskList;
import edu.depaul.taskmanager.api.repository.TaskListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        return taskListRepository.save(TaskList.newBuilder().withName(listName).withOwnerId(userId).build());
    }

    public List<TaskList> getAllPersonalLists(String userId) {
        return taskListRepository.findByOwnerId(userId);
    }

    public List<Task> getTasksInList(String listId) {
        Optional<TaskList> taskList = taskListRepository.findById(listId);
        return taskList.isPresent() ? taskList.get().getTasks() : emptyList();
    }
}

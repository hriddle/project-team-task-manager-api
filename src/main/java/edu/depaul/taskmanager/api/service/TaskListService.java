package edu.depaul.taskmanager.api.service;

import edu.depaul.taskmanager.api.model.TaskList;
import edu.depaul.taskmanager.api.repository.TaskListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskListService {

    private TaskListRepository taskListRepository;

    @Autowired
    public TaskListService(TaskListRepository taskListRepository) {
        this.taskListRepository = taskListRepository;
    }

    public TaskList createPersonalList(String userId, String listName) {
        TaskList list = taskListRepository.save(TaskList.newBuilder().withName(listName).withOwnerId(userId).build());
        return list;
    }
}

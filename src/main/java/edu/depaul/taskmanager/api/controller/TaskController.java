package edu.depaul.taskmanager.api.controller;

import edu.depaul.taskmanager.api.model.Task;
import edu.depaul.taskmanager.api.service.TaskListService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TaskController {

    private TaskListService taskListService;

    public TaskController(TaskListService taskListService) {
        this.taskListService = taskListService;
    }

    @GetMapping("/lists/{listId}/tasks")
    public ResponseEntity<List<Task>> getTasksInList(@PathVariable String listId) {
        List<Task> tasks = taskListService.getTasksInList(listId);
        return ResponseEntity.ok().body(tasks);
    }

    @PostMapping("/lists/{listId}/tasks")
    public ResponseEntity<List<Task>> addTaskToList(@PathVariable String listId, @RequestBody Task task) {
        List<Task> tasks = taskListService.addTaskToList(listId, task);
        return ResponseEntity.status(HttpStatus.CREATED).body(tasks);
    }

    @PutMapping("/lists/{listId}/tasks/{index}")
    public ResponseEntity<Task> updateTask(@PathVariable String listId, @PathVariable int index, @RequestBody Task task) {
        Task updatedTask = taskListService.updateTask(listId, index, task);
        return ResponseEntity.ok(updatedTask);
    }

    @DeleteMapping("/lists/{listId}/tasks/{index}")
    public ResponseEntity deleteTask(@PathVariable String listId, @PathVariable int index) {
        taskListService.deleteTask(listId, index);
        return ResponseEntity.noContent().build();
    }
}

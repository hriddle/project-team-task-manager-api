package edu.depaul.taskmanager.api.controller;

import edu.depaul.taskmanager.api.model.Task;
import edu.depaul.taskmanager.api.model.TaskList;
import edu.depaul.taskmanager.api.service.TaskListService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TaskControllerTest {

    private TaskController controller;
    private TaskListService taskListService;

    private TaskList list = TaskList.newBuilder().withId("5678").withName("To Do List").withOwnerId("1234").build();
    private Task task1 = Task.newBuilder().withName("Task 1").withDueDate(LocalDateTime.now()).withAssignedUser("1234").build();
    private Task task2 = Task.newBuilder().withName("Task 2").build();
    private TaskList listWithTasks = TaskList.newBuilder(list).withTasks(asList(task1, task2)).build();
    private Task taskToAdd = Task.newBuilder().withName("New Task").build();
    private Task editedTask = Task.newBuilder().withName("Edited Task").build();

    @Before
    public void setUp() {
        taskListService = mock(TaskListService.class);
        controller = new TaskController(taskListService);

        when(taskListService.getTasksInList(any())).thenReturn(listWithTasks.getTasks());
        when(taskListService.addTaskToList(any(), any())).thenReturn(asList(task1, task2, taskToAdd));
        when(taskListService.updateTask(any(), anyInt(), any())).thenReturn(editedTask);
    }


    @Test
    public void getTasks_returns200_onSuccess() {
        ResponseEntity<List<Task>> response = controller.getTasksInList(listWithTasks.getId());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void getTasks_returnsAllTasks_onSuccess() {
        ResponseEntity<List<Task>> response = controller.getTasksInList(listWithTasks.getId());
        assertThat(response.getBody()).isEqualTo(asList(task1, task2));
    }

    @Test
    public void getTasks_returnsAnEmptyResultList_whenNoneExist() {
        reset(taskListService);
        when(taskListService.getTasksInList(any())).thenReturn(emptyList());

        ResponseEntity<List<Task>> response = controller.getTasksInList(listWithTasks.getId());
        assertThat(response.getBody()).hasSize(0);
    }

    @Test
    public void getTasks_callsTaskListService() {
        controller.getTasksInList(listWithTasks.getId());
        verify(taskListService).getTasksInList(listWithTasks.getId());
    }

    @Test
    public void addTaskToList_returns201_onCreation() {
        ResponseEntity<List<Task>> response = controller.addTaskToList(listWithTasks.getId(), taskToAdd);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    public void addTaskToList_returnsAllTasks_onSuccess() {
        ResponseEntity<List<Task>> response = controller.addTaskToList(listWithTasks.getId(), taskToAdd);
        assertThat(response.getBody()).isEqualTo(asList(task1, task2, taskToAdd));
    }

    @Test
    public void addTaskToList_callsTaskListService() {
        controller.addTaskToList(listWithTasks.getId(), taskToAdd);
        verify(taskListService).addTaskToList(listWithTasks.getId(), taskToAdd);
    }

    @Test
    public void updateTask_returns200_onSuccess() {
        ResponseEntity<Task> response = controller.updateTask(listWithTasks.getId(), 1, editedTask);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void updateTask_returnsUpdatedTask_onSuccess() {
        ResponseEntity<Task> response = controller.updateTask(listWithTasks.getId(), 1, editedTask);
        assertThat(response.getBody()).isEqualTo(editedTask);
    }

    @Test
    public void updateTask_callsTaskListService() {
        controller.updateTask(listWithTasks.getId(), 1, editedTask);
        verify(taskListService).updateTask(listWithTasks.getId(), 1, editedTask);
    }

    @Test
    public void deleteTask_returns204_onSuccess() {
        ResponseEntity response = controller.deleteTask(listWithTasks.getId(), 0);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    public void deleteTask_callsTaskListService() {
        controller.deleteTask(listWithTasks.getId(), 0);
        verify(taskListService).deleteTask(listWithTasks.getId(), 0);
    }
}

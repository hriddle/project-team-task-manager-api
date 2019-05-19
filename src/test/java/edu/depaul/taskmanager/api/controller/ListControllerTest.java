package edu.depaul.taskmanager.api.controller;

import edu.depaul.taskmanager.api.model.Task;
import edu.depaul.taskmanager.api.model.TaskList;
import edu.depaul.taskmanager.api.service.TaskListService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ListControllerTest {

    private ListController listController;
    private UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.newInstance();
    private TaskListService taskListService;

    private String userId = "1234";
    private String listName = "To Do List";
    private TaskList list = TaskList.newBuilder().withId("5678").withName(listName).withOwnerId(userId).build();
    private TaskList anotherList = TaskList.newBuilder().withId("9999").withName("Another List").withOwnerId(userId).build();
    private Task task1 = Task.newBuilder().withName("Task 1").build();
    private Task task2 = Task.newBuilder().withName("Task 2").build();
    private TaskList listWithTasks = TaskList.newBuilder(list).withTasks(asList(task1, task2)).build();
    private Task taskToAdd = Task.newBuilder().withName("Task 3").build();

    @Before
    public void setUp() {
        taskListService = mock(TaskListService.class);
        listController = new ListController(taskListService);

        when(taskListService.createPersonalList(any(), any())).thenReturn(list);
        when(taskListService.getAllPersonalLists(any())).thenReturn(asList(list, anotherList));
        when(taskListService.getTasksInList(any())).thenReturn(asList(task1, task2));
        when(taskListService.addTaskToList(any(), any())).thenReturn(asList(task1, task2, taskToAdd));
    }

    @Test
    public void createPersonalList_returns201_onCreation() {
        ResponseEntity<TaskList> response = listController.createPersonalList(userId, listName, uriComponentsBuilder);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    public void createPersonalList_returnsList_onSuccess() {
        ResponseEntity<TaskList> response = listController.createPersonalList(userId, listName, uriComponentsBuilder);
        assertThat(response.getBody()).isEqualTo(list);
    }

    @Test
    public void createPersonalList_returnsLocationHeader_onSuccess() {
        URI locationHeader = listController.createPersonalList(userId, listName, uriComponentsBuilder).getHeaders().getLocation();
        assertThat(locationHeader).isNotNull();
        assertThat(locationHeader.getPath()).isEqualTo("/users/1234/lists/5678");
    }

    @Test
    public void createPersonalList_callsTaskListService() {
        listController.createPersonalList(userId, listName, uriComponentsBuilder);
        verify(taskListService).createPersonalList(userId, listName);
    }

    @Test
    public void getPersonalLists_returns200_onSuccess() {
        ResponseEntity<List<TaskList>> response = listController.getPersonalLists(userId);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void getPersonalLists_returnsAllTaskLists_onSuccess() {
        ResponseEntity<List<TaskList>> response = listController.getPersonalLists(userId);
        assertThat(response.getBody()).containsExactlyInAnyOrder(list, anotherList);
    }

    @Test
    public void getPersonalLists_returnsAnEmptyResultList_whenNoneExist() {
        reset(taskListService);
        when(taskListService.getAllPersonalLists(any())).thenReturn(emptyList());

        ResponseEntity<List<TaskList>> response = listController.getPersonalLists(userId);
        assertThat(response.getBody()).hasSize(0);
    }

    @Test
    public void getPersonalLists_callsTaskListService() {
        listController.createPersonalList(userId, listName, uriComponentsBuilder);
        verify(taskListService).createPersonalList(userId, listName);
    }

    @Test
    public void getTasks_returns200_onSuccess() {
        ResponseEntity<List<Task>> response = listController.getTasksInList(listWithTasks.getId());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void getTasks_returnsAllTasks_onSuccess() {
        ResponseEntity<List<Task>> response = listController.getTasksInList(listWithTasks.getId());
        assertThat(response.getBody()).isEqualTo(asList(task1, task2));
    }

    @Test
    public void getTasks_returnsAnEmptyResultList_whenNoneExist() {
        reset(taskListService);
        when(taskListService.getTasksInList(any())).thenReturn(emptyList());

        ResponseEntity<List<Task>> response = listController.getTasksInList(listWithTasks.getId());
        assertThat(response.getBody()).hasSize(0);
    }

    @Test
    public void getTasks_callsTaskListService() {
        listController.getTasksInList(listWithTasks.getId());
        verify(taskListService).getTasksInList(listWithTasks.getId());
    }

    @Test
    public void addTaskToList_returns201_onCreation() {
        ResponseEntity<List<Task>> response = listController.addTaskToList(listWithTasks.getId(), taskToAdd);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    public void addTaskToList_returnsAllTasks_onSuccess() {
        ResponseEntity<List<Task>> response = listController.addTaskToList(listWithTasks.getId(), taskToAdd);
        assertThat(response.getBody()).isEqualTo(asList(task1, task2, taskToAdd));
    }

    @Test
    public void addTaskToList_callsTaskListService() {
        listController.addTaskToList(listWithTasks.getId(), taskToAdd);
        verify(taskListService).addTaskToList(listWithTasks.getId(), taskToAdd);
    }
}

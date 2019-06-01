package edu.depaul.taskmanager.api.controller;

import edu.depaul.taskmanager.api.model.TaskList;
import edu.depaul.taskmanager.api.service.TaskListService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

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

    private String id = "1234";
    private String listName = "To Do List";
    private TaskList list = TaskList.newBuilder().withId("5678").withName(listName).withOwnerId(id).build();
    private TaskList anotherList = TaskList.newBuilder().withId("9999").withName("Another List").withOwnerId(id).build();

    @Before
    public void setUp() {
        taskListService = mock(TaskListService.class);
        listController = new ListController(taskListService);

        when(taskListService.createPersonalList(any(), any())).thenReturn(list);
        when(taskListService.createTeamList(any(), any())).thenReturn(list);
        when(taskListService.getAllPersonalLists(any())).thenReturn(asList(list, anotherList));
        when(taskListService.getAllTeamLists(any())).thenReturn(asList(list, anotherList));
    }

    @Test
    public void createPersonalList_returns201_onCreation() {
        ResponseEntity<TaskList> response = listController.createPersonalList(id, listName, uriComponentsBuilder);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    public void createPersonalList_returnsList_onSuccess() {
        ResponseEntity<TaskList> response = listController.createPersonalList(id, listName, uriComponentsBuilder);
        assertThat(response.getBody()).isEqualTo(list);
    }

    @Test
    public void createPersonalList_returnsLocationHeader_onSuccess() {
        URI locationHeader = listController.createPersonalList(id, listName, uriComponentsBuilder).getHeaders().getLocation();
        assertThat(locationHeader).isNotNull();
        assertThat(locationHeader.getPath()).isEqualTo("/users/1234/lists/5678");
    }

    @Test
    public void createPersonalList_callsTaskListService() {
        listController.createPersonalList(id, listName, uriComponentsBuilder);
        verify(taskListService).createPersonalList(id, listName);
    }

    @Test
    public void createTeamList_returns201_onCreation() {
        ResponseEntity<TaskList> response = listController.createTeamList(id, listName, uriComponentsBuilder);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    public void createTeamList_returnsList_onSuccess() {
        ResponseEntity<TaskList> response = listController.createTeamList(id, listName, uriComponentsBuilder);
        assertThat(response.getBody()).isEqualTo(list);
    }

    @Test
    public void createTeamList_returnsLocationHeader_onSuccess() {
        URI locationHeader = listController.createTeamList(id, listName, uriComponentsBuilder).getHeaders().getLocation();
        assertThat(locationHeader).isNotNull();
        assertThat(locationHeader.getPath()).isEqualTo("/teams/1234/lists/5678");
    }

    @Test
    public void createTeamList_callsTaskListService() {
        listController.createTeamList(id, listName, uriComponentsBuilder);
        verify(taskListService).createTeamList(id, listName);
    }

    @Test
    public void getPersonalLists_returns200_onSuccess() {
        ResponseEntity<List<TaskList>> response = listController.getPersonalLists(id);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void getPersonalLists_returnsAllTaskLists_onSuccess() {
        ResponseEntity<List<TaskList>> response = listController.getPersonalLists(id);
        assertThat(response.getBody()).containsExactlyInAnyOrder(list, anotherList);
    }

    @Test
    public void getPersonalLists_returnsAnEmptyResultList_whenNoneExist() {
        reset(taskListService);
        when(taskListService.getAllPersonalLists(any())).thenReturn(emptyList());

        ResponseEntity<List<TaskList>> response = listController.getPersonalLists(id);
        assertThat(response.getBody()).hasSize(0);
    }

    @Test
    public void getPersonalLists_callsTaskListService() {
        listController.getPersonalLists(id);
        verify(taskListService).getAllPersonalLists(id);
    }

    @Test
    public void getTeamLists_returns200_onSuccess() {
        ResponseEntity<List<TaskList>> response = listController.getTeamLists(id);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void getTeamLists_returnsAllTaskLists_onSuccess() {
        ResponseEntity<List<TaskList>> response = listController.getTeamLists(id);
        assertThat(response.getBody()).containsExactlyInAnyOrder(list, anotherList);
    }

    @Test
    public void getTeamLists_returnsAnEmptyResultList_whenNoneExist() {
        when(taskListService.getAllTeamLists(any())).thenReturn(emptyList());

        ResponseEntity<List<TaskList>> response = listController.getTeamLists(id);
        assertThat(response.getBody()).hasSize(0);
    }

    @Test
    public void getTeamLists_callsTaskListService() {
        listController.getTeamLists(id);
        verify(taskListService).getAllTeamLists(id);
    }
}

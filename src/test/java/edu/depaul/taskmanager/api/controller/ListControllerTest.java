package edu.depaul.taskmanager.api.controller;

import edu.depaul.taskmanager.api.model.TaskList;
import edu.depaul.taskmanager.api.service.TaskListService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ListControllerTest {

    private ListController listController;
    private UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.newInstance();
    private TaskListService taskListService;

    private String userId = "1234";
    private String listName = "To Do List";
    private TaskList list = TaskList.newBuilder().withId("5678").withName(listName).withOwnerId(userId).build();

    @Before
    public void setUp() {
        taskListService = mock(TaskListService.class);
        listController = new ListController(taskListService);

        when(taskListService.createPersonalList(any(), any())).thenReturn(
                TaskList.newBuilder().withId("5678").withName(listName).withOwnerId(userId).build()
        );
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
}

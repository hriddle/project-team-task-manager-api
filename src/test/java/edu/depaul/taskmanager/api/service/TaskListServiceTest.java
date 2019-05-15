package edu.depaul.taskmanager.api.service;

import edu.depaul.taskmanager.api.model.TaskList;
import edu.depaul.taskmanager.api.repository.TaskListRepository;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TaskListServiceTest {

    private TaskListService service;
    private TaskListRepository repository;

    private String userId = "1234";
    private String listId = "5678";
    private String listName = "To Do List";
    private TaskList taskList = TaskList.newBuilder().withId(listId).withName(listName).withOwnerId(userId).build();
    private TaskList anotherTaskList = TaskList.newBuilder().withId("9999").withName("Another List").withOwnerId(userId).build();

    @Before
    public void setUp() {
        repository = mock(TaskListRepository.class);
        service = new TaskListService(repository);

        when(repository.save(any())).thenReturn(taskList);
        when(repository.findByOwnerId(any())).thenReturn(Arrays.asList(taskList, anotherTaskList));
    }

    @Test
    public void createPersonalList_callsRepository() {
        service.createPersonalList(userId, listName);
        verify(repository).save(TaskList.newBuilder().withName(listName).withOwnerId(userId).build());
    }

    @Test
    public void createPersonalList_returnsCreatedList() {
        TaskList createdList = service.createPersonalList(userId, listName);
        assertThat(createdList).isEqualTo(taskList);
    }

    @Test
    public void getAllPersonalLists_callsRepository() {
        service.getAllPersonalLists(userId);
        verify(repository).findByOwnerId(userId);
    }

    @Test
    public void getAllPersonalLists_returnsAList() {
        List<TaskList> lists = service.getAllPersonalLists(userId);
        assertThat(lists).containsExactlyInAnyOrder(taskList, anotherTaskList);
    }
}
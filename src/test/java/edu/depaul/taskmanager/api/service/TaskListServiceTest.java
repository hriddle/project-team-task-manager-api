package edu.depaul.taskmanager.api.service;

import edu.depaul.taskmanager.api.model.Task;
import edu.depaul.taskmanager.api.model.TaskList;
import edu.depaul.taskmanager.api.repository.TaskListRepository;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.Optional.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TaskListServiceTest {

    private TaskListService service;
    private TaskListRepository repository;

    private String id = "1234";
    private String listId = "5678";
    private String listName = "To Do List";
    private TaskList taskList = TaskList.newBuilder().withId(listId).withName(listName).withOwnerId(id).withTasks(emptyList()).build();
    private TaskList anotherTaskList = TaskList.newBuilder().withId("9999").withName("Another List").withOwnerId(id).withTasks(emptyList()).build();
    private Task task1 = Task.newBuilder().withName("Task 1").withDueDate(null).build();
    private Task task2 = Task.newBuilder().withName("Task 2").withDueDate(LocalDateTime.of(2019, 12, 1, 12, 0, 0)).build();
    private Task taskToAdd = Task.newBuilder().withName("Task 3").build();
    private TaskList taskListWithTasks = TaskList.newBuilder(taskList).withTasks(asList(task1, task2)).build();
    private TaskList taskListWithoutTasks = TaskList.newBuilder().withId(listId).withName(listName).withOwnerId(id).build();
    private Task editedTask = Task.newBuilder().withName("Edited Task").withDueDate(LocalDateTime.of(2019, 12, 31, 12, 0, 0)).build();

    @Before
    public void setUp() {
        repository = mock(TaskListRepository.class);
        service = new TaskListService(repository);

        when(repository.save(any())).thenReturn(taskList);
        when(repository.findByOwnerId(any())).thenReturn(Arrays.asList(taskList, anotherTaskList));
        when(repository.findById(any())).thenReturn(of(taskListWithTasks));
    }

    @Test
    public void createPersonalList_callsRepository() {
        service.createPersonalList(id, listName);
        verify(repository).save(TaskList.newBuilder().withName(listName).withOwnerId(id).withTasks(emptyList()).build());
    }

    @Test
    public void createPersonalList_returnsCreatedList() {
        TaskList createdList = service.createPersonalList(id, listName);
        assertThat(createdList).isEqualTo(taskList);
    }

    @Test
    public void createTeamList_callsRepository() {
        service.createPersonalList(id, listName);
        verify(repository).save(TaskList.newBuilder().withName(listName).withOwnerId(id).withTasks(emptyList()).build());
    }

    @Test
    public void createTeamList_returnsCreatedList() {
        TaskList createdList = service.createTeamList(id, listName);
        assertThat(createdList).isEqualTo(taskList);
    }

    @Test
    public void getAllPersonalLists_callsRepository() {
        service.getAllPersonalLists(id);
        verify(repository).findByOwnerId(id);
    }

    @Test
    public void getAllPersonalLists_returnsAList() {
        List<TaskList> lists = service.getAllPersonalLists(id);
        assertThat(lists).containsExactlyInAnyOrder(taskList, anotherTaskList);
    }

    @Test
    public void getAllTeamLists_callsRepository() {
        service.getAllTeamLists(taskList.getId());
        verify(repository).findByOwnerId(taskList.getId());
    }

    @Test
    public void getAllTeamLists_returnsAList() {
        List<TaskList> lists = service.getAllTeamLists(taskList.getId());
        assertThat(lists).containsExactlyInAnyOrder(taskList, anotherTaskList);
    }

    @Test
    public void getTasksInList_callsRepository() {
        service.getTasksInList(taskListWithTasks.getId());
        verify(repository).findById(taskListWithTasks.getId());
    }

    @Test
    public void getTasksInList_returnsAList() {
        List<Task> tasks = service.getTasksInList(taskListWithTasks.getId());
        assertThat(tasks).isEqualTo(taskListWithTasks.getTasks());
    }

    @Test
    public void getTasksInList_returnsAnEmptyList_whenNoneExist() {
        when(repository.findById(any())).thenReturn(of(taskList));
        List<Task> tasks = service.getTasksInList(taskList.getId());
        assertThat(tasks).isEqualTo(emptyList());
    }

    @Test
    public void addTaskToList_updatesTaskListWithNewTask() {
        service.addTaskToList(taskListWithTasks.getId(), taskToAdd);
        TaskList updatedTaskList = TaskList.newBuilder(taskListWithTasks).withTasks(asList(task1, task2, taskToAdd)).build();
        verify(repository).save(updatedTaskList);
    }

    @Test
    public void addTaskToList_returnsListOfTasks() {
        when(repository.save(any())).thenReturn(TaskList.newBuilder(taskList).withTasks(asList(task1, task2, taskToAdd)).build());
        List<Task> tasks = service.addTaskToList(taskListWithTasks.getId(), taskToAdd);
        assertThat(tasks).containsExactly(task1, task2, taskToAdd);
    }

    @Test
    public void addTaskToList_canAddTaskToNullList() {
        when(repository.findById(taskListWithoutTasks.getId())).thenReturn(of(taskListWithoutTasks));
        service.addTaskToList(taskListWithoutTasks.getId(), taskToAdd);
        verify(repository).save(TaskList.newBuilder()
                .withId(taskListWithoutTasks.getId())
                .withName(taskListWithoutTasks.getName())
                .withOwnerId(taskListWithoutTasks.getOwnerId())
                .withTasks(singletonList(taskToAdd))
                .build()
        );
    }

    @Test
    public void updateTask_callsRepositorySave() {
        when(repository.save(any())).thenReturn(taskListWithTasks);

        service.updateTask(taskListWithTasks.getId(), 0, editedTask);
        verify(repository).save(TaskList.newBuilder(taskListWithTasks).withTasks(asList(editedTask, task2)).build());

        service.updateTask(taskListWithTasks.getId(), 1, editedTask);
        verify(repository).save(TaskList.newBuilder(taskListWithTasks).withTasks(asList(task1, editedTask)).build());
    }

    @Test
    public void updateTask_returnsUpdatedTask() {
        when(repository.save(any())).thenReturn(TaskList.newBuilder(taskList).withTasks(asList(task1, editedTask)).build());
        Task task = service.updateTask(taskListWithTasks.getId(), 1, editedTask);
        assertThat(task.getName()).isEqualTo(editedTask.getName());
        assertThat(task.getDueDate()).isEqualTo(editedTask.getDueDate());
    }

    @Test
    public void updateTask_returnsNullIfThereAreNoTasks() {
        when(repository.findById(any())).thenReturn(of(TaskList.newBuilder().withId("1").withTasks(null).build()));
        Task task = service.updateTask(taskListWithoutTasks.getId(), 0, editedTask);
        assertThat(task).isNull();
    }

    @Test
    public void updateTask_returnsNullIfIndexIsInvalid() {
        when(repository.findById(any())).thenReturn(of(taskListWithTasks));
        Task task = service.updateTask(taskListWithTasks.getId(), taskListWithTasks.getTasks().size(), editedTask);
        assertThat(task).isNull();
    }

    @Test
    public void deleteTask_callsRepositorySave() {
        service.deleteTask(taskListWithTasks.getId(), 0);
        verify(repository).save(TaskList.newBuilder(taskListWithTasks).withTasks(singletonList(task2)).build());

        service.deleteTask(taskListWithTasks.getId(), 1);
        verify(repository).save(TaskList.newBuilder(taskListWithTasks).withTasks(singletonList(task1)).build());
    }
}
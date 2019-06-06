package edu.depaul.taskmanager.api.controller;

import edu.depaul.taskmanager.api.model.TaskList;
import edu.depaul.taskmanager.api.service.TaskListService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
public class ListController {

    private TaskListService taskListService;

    public ListController(TaskListService taskListService) {
        this.taskListService = taskListService;
    }

    @PostMapping("/users/{userId}/lists")
    public ResponseEntity<TaskList> createPersonalList(@PathVariable String userId, @RequestBody TaskList newTaskList, UriComponentsBuilder uriComponentsBuilder) {
        TaskList list = taskListService.createPersonalList(userId, newTaskList);
        return ResponseEntity
                .created(uriComponentsBuilder.path("/users/{userId}/lists/{listId}").buildAndExpand(userId, list.getId()).toUri())
                .body(list);
    }

    @GetMapping("/users/{userId}/lists")
    public ResponseEntity<List<TaskList>> getPersonalLists(@PathVariable String userId) {
        List<TaskList> lists = taskListService.getAllPersonalLists(userId);
        return ResponseEntity.ok().body(lists);
    }

    @GetMapping("/teams/{teamId}/lists")
    public ResponseEntity<List<TaskList>> getTeamLists(@PathVariable String teamId, @RequestParam(defaultValue = "list") String type) {
        List<TaskList> lists = taskListService.getAllTeamListsByType(teamId, type);
        return ResponseEntity.ok().body(lists);
    }

    @PostMapping("/teams/{teamId}/lists")
    public ResponseEntity<TaskList> createTeamList(@PathVariable String teamId, @RequestBody TaskList newList, UriComponentsBuilder uriComponentsBuilder) {
        TaskList list = taskListService.createTeamList(teamId, newList);
        return ResponseEntity
                .created(uriComponentsBuilder.path("/teams/{teamId}/lists/{listId}").buildAndExpand(teamId, list.getId()).toUri())
                .body(list);
    }
}

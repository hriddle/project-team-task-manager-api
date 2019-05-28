package edu.depaul.taskmanager.api.controller;

import edu.depaul.taskmanager.api.model.Team;
import edu.depaul.taskmanager.api.model.TeamMember;
import edu.depaul.taskmanager.api.model.TeamMemberDetail;
import edu.depaul.taskmanager.api.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/teams")
public class TeamController {

    private TeamService teamService;

    @Autowired
    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @PostMapping()
    public ResponseEntity createTeam(@RequestBody Team team, UriComponentsBuilder uriComponentsBuilder) {
        Team createdTeam = teamService.saveTeam(team);
        return ResponseEntity.created(uriComponentsBuilder.path("/teams/{teamId}")
                .buildAndExpand(createdTeam.getId()).toUri())
                .body(createdTeam);
    }

    @GetMapping()
    public ResponseEntity getTeams(@RequestParam(required = false) String userId) {
        if (userId == null) {
            return ResponseEntity.ok(teamService.getAllTeams());
        } else {
            return ResponseEntity.ok(teamService.getTeamsBy(userId));
        }
    }

    @PutMapping("/{teamID}/{memberID}")
    public ResponseEntity addNewMember(@PathVariable String teamID, @PathVariable String memberID,
                                       UriComponentsBuilder uriComponentsBuilder) {
        Team updatedTeam = teamService.addTeamMember(teamID, memberID);
        return ResponseEntity.created(uriComponentsBuilder.path("/teams/{teamId}")
                .buildAndExpand(updatedTeam.getId()).toUri())
                .body(updatedTeam);
    }

    @GetMapping("/{teamId}/members")
    public ResponseEntity getTeamMembers(@PathVariable String teamId) {
        List<TeamMemberDetail> teamMembers = teamService.getTeamMembers(teamId);
        return ResponseEntity.ok(teamMembers);
    }
}

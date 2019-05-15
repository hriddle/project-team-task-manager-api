package edu.depaul.taskmanager.api.service;

import edu.depaul.taskmanager.api.model.Team;
import edu.depaul.taskmanager.api.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeamService {

    private TeamRepository teamRepository;

    @Autowired
    public TeamService(TeamRepository teamRepository) {

        this.teamRepository = teamRepository;
    }

//    public Team createTeam(String teamName, String user) {
//        return teamRepository.save(Team.newBuilder()
//                .withName(teamName)
//                .withMembers(Collections.singletonList(TeamMembers.newBuilder().withId(user).build()))
//                .build());
//    }

    public Team saveTeam(Team team) {
        return teamRepository.save(team);
    }

    public List<Team> getAllTeams() {
        return teamRepository.findAll();
    }

    public List<Team> getTeamsBy(String userId) {
        return teamRepository.getTeamsByMembers_IdIn(userId);
    }
}

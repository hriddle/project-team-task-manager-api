package edu.depaul.taskmanager.api.service;

import edu.depaul.taskmanager.api.model.Team;
import edu.depaul.taskmanager.api.model.TeamMembers;
import edu.depaul.taskmanager.api.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public Team addTeamMember(String teamID, String newMemberID) {
        Optional<Team> tempTeam = teamRepository.findById(teamID);
        if (tempTeam.isPresent()) {
            Team updatedTeam = tempTeam.get();
			
            // Check if the new member is NOT already in the team
            boolean found = false;
            for (TeamMembers teamMembers : updatedTeam.getMembers()) {
                if (teamMembers.getId().equals(newMemberID)) {
                    found = true;
                    break;
                }
            }
            if (found == false)
            {
                updatedTeam.getMembers().add(TeamMembers.newBuilder().withId(newMemberID).build());
            }
            teamRepository.save(updatedTeam);
            return updatedTeam;
        }
        else {
            return null;
        }
    }

    public List<Team> getAllTeams() {
        return teamRepository.findAll();
    }

    public List<Team> getTeamsBy(String userId) {
        return teamRepository.getTeamsByMembers_IdIn(userId);
    }
}

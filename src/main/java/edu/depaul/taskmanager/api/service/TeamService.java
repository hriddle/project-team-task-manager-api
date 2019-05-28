package edu.depaul.taskmanager.api.service;

import edu.depaul.taskmanager.api.model.Team;
import edu.depaul.taskmanager.api.model.TeamMember;
import edu.depaul.taskmanager.api.model.TeamMemberDetail;
import edu.depaul.taskmanager.api.repository.TeamRepository;
import edu.depaul.taskmanager.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

@Service
public class TeamService {

    private TeamRepository teamRepository;
    private UserRepository userRepository;

    @Autowired
    public TeamService(TeamRepository teamRepository, UserRepository userRepository) {
        this.teamRepository = teamRepository;
        this.userRepository = userRepository;
    }

    public Team saveTeam(Team team) {
        return teamRepository.save(team);
    }

    public Team addTeamMember(String teamID, String newMemberID) {
        Optional<Team> tempTeam = teamRepository.findById(teamID);
        if (tempTeam.isPresent()) {
            Team updatedTeam = tempTeam.get();

            // Check if the new member is NOT already in the team
            boolean found = false;
            for (TeamMember teamMember : updatedTeam.getMembers()) {
                if (teamMember.getId().equals(newMemberID)) {
                    found = true;
                    break;
                }
            }
            if (found == false) {
                updatedTeam.getMembers().add(TeamMember.newBuilder().withId(newMemberID).build());
            }
            teamRepository.save(updatedTeam);
            return updatedTeam;
        } else {
            return null;
        }
    }

    public List<Team> getAllTeams() {
        return teamRepository.findAll();
    }

    public List<Team> getTeamsBy(String userId) {
        return teamRepository.getTeamsByMembers_IdIn(userId);
    }

    public List<TeamMemberDetail> getTeamMembers(String teamId) {
        List<TeamMember> teamMembers = teamRepository.findById(teamId)
                .map(Team::getMembers)
                .orElse(emptyList());

        List<String> membersIds = teamMembers.stream().map(TeamMember::getId).collect(toList());

        return StreamSupport.stream(userRepository.findAllById(membersIds).spliterator(), false)
                .map(user -> TeamMemberDetail.newBuilder()
                        .withId(user.getId())
                        .withFirstName(user.getFirstName())
                        .withLastName(user.getLastName())
                        .build())
                .collect(Collectors.toList());
    }
}

package edu.depaul.taskmanager.api.repository;

import edu.depaul.taskmanager.api.model.Team;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamRepository extends MongoRepository<Team, String> {
    List<Team> getTeamsByMembers_IdIn(String memberId);
}

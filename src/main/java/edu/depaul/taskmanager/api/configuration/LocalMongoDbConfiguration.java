package edu.depaul.taskmanager.api.configuration;

import com.mongodb.MongoClient;
import cz.jirutka.spring.embedmongo.EmbeddedMongoFactoryBean;
import edu.depaul.taskmanager.api.model.Task;
import edu.depaul.taskmanager.api.model.TaskList;
import edu.depaul.taskmanager.api.model.Team;
import edu.depaul.taskmanager.api.model.TeamMember;
import edu.depaul.taskmanager.api.model.User;
import edu.depaul.taskmanager.api.repository.TaskListRepository;
import edu.depaul.taskmanager.api.repository.TeamRepository;
import edu.depaul.taskmanager.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

@Configuration
@Profile("!cloud")
public class LocalMongoDbConfiguration {

    private static final String MONGO_DB_URL = "localhost";
    private static final String MONGO_DB_NAME = "team_task_manager";

    @Bean
    public MongoTemplate mongoTemplate() throws IOException {
        EmbeddedMongoFactoryBean mongo = new EmbeddedMongoFactoryBean();
        mongo.setBindIp(MONGO_DB_URL);
        MongoClient mongoClient = mongo.getObject();
        MongoTemplate mongoTemplate = new MongoTemplate(mongoClient, MONGO_DB_NAME);
        return mongoTemplate;
    }
}

@Configuration
@Profile("!cloud")
class PostStartupConfiguration {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskListRepository listRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Bean
    public ApplicationListener<ContextRefreshedEvent> loadDefaultData() {
        return event -> {
            User user1 = userRepository.save(User.newBuilder()
                    .withEmail("")
                    .withPassword("")
                    .withFirstName("Hello")
                    .withLastName("World")
                    .build());
            User user2 = userRepository.save(User.newBuilder()
                    .withEmail("abcdef")
                    .withPassword("")
                    .withFirstName("Abc")
                    .withLastName("Bcd")
                    .build());
            User user3 = userRepository.save(User.newBuilder()
                    .withEmail("ghijkl")
                    .withPassword("")
                    .withFirstName("Wxy")
                    .withLastName("Xyz")
                    .build());
            User user4 = userRepository.save(User.newBuilder()
                    .withEmail("noone")
                    .withPassword("")
                    .withFirstName("No")
                    .withLastName("One")
                    .build());
            listRepository.saveAll(asList(
                    TaskList.newBuilder()
                            .withName("List 1")
                            .withOwnerId(user1.getId())
                            .withTasks(asList(
                                    Task.newBuilder().withName("Task A").build(),
                                    Task.newBuilder().withName("Task B").withDueDate(LocalDateTime.of(2019, 7, 13, 19, 0, 0)).build(),
                                    Task.newBuilder().withName("Task C").build(),
                                    Task.newBuilder().withName("Task D").build(),
                                    Task.newBuilder().withName("Task E").withDueDate(LocalDateTime.of(2019, 6, 12, 17, 30, 0)).build(),
                                    Task.newBuilder().withName("Task F").build()
                            ))
                            .build(),
                    TaskList.newBuilder()
                            .withName("List 2")
                            .withOwnerId(user1.getId())
                            .withTasks(asList(
                                    Task.newBuilder().withName("Task G").build(),
                                    Task.newBuilder().withName("Task H").build()
                            ))
                            .build()
            ));
            List<Team> team = teamRepository.saveAll(asList(
                    Team.newBuilder()
                            .withName("Team")
                            .withMembers(asList(
                                    TeamMember.newBuilder().withId(user1.getId()).build(),
                                    TeamMember.newBuilder().withId(user2.getId()).build(),
                                    TeamMember.newBuilder().withId(user3.getId()).build()))
                            .build(),
                    Team.newBuilder().withName("Another Team").withMembers(emptyList()).build(),
                    Team.newBuilder().withName("And Another Team").withMembers(emptyList()).build(),
                    Team.newBuilder().withName("Yet Again Another Team").withMembers(emptyList()).build(),
                    Team.newBuilder().withName("Another Blah Team").withMembers(emptyList()).build(),
                    Team.newBuilder().withName("Heck Another Team").withMembers(emptyList()).build()
            ));

            listRepository.save(TaskList.newBuilder()
                    .withName("Legacy Team List")
                    .withOwnerId(team.get(0).getId())
                    .withTasks(asList(
                            Task.newBuilder().withName("Assigned to you").withAssignedUser(user1.getId()).build(),
                            Task.newBuilder().withName("Assigned to someone else").withAssignedUser(user3.getId()).build(),
                            Task.newBuilder().withName("Assigned to no one").build()))
                    .build());

            listRepository.save(TaskList.newBuilder()
                    .withName("New Team List")
                    .withOwnerId(team.get(0).getId())
                    .withListType("list")
                    .withTasks(asList(
                            Task.newBuilder().withName("Assigned to you").withAssignedUser(user1.getId()).build(),
                            Task.newBuilder().withName("Assigned to someone else").withAssignedUser(user3.getId()).build(),
                            Task.newBuilder().withName("Assigned to no one").build()))
                    .build());
        };
    }
}

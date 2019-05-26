package edu.depaul.taskmanager.api.configuration;

import com.mongodb.MongoClient;
import cz.jirutka.spring.embedmongo.EmbeddedMongoFactoryBean;
import edu.depaul.taskmanager.api.model.Task;
import edu.depaul.taskmanager.api.model.TaskList;
import edu.depaul.taskmanager.api.model.User;
import edu.depaul.taskmanager.api.repository.TaskListRepository;
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

import static java.util.Arrays.asList;

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

    @Bean
    public ApplicationListener<ContextRefreshedEvent> loadDefaultData() {
        return event -> {
            User user = userRepository.save(User.newBuilder()
                    .withEmail("")
                    .withPassword("")
                    .withFirstName("Hello")
                    .withLastName("World")
                    .build());
            listRepository.saveAll(asList(
                    TaskList.newBuilder()
                            .withName("List 1")
                            .withOwnerId(user.getId())
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
                            .withOwnerId(user.getId())
                            .withTasks(asList(
                                    Task.newBuilder().withName("Task G").build(),
                                    Task.newBuilder().withName("Task H").build()
                            ))
                            .build()
            ));
        };
    }
}

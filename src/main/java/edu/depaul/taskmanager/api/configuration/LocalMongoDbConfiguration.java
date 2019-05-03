package edu.depaul.taskmanager.api.configuration;

import com.mongodb.MongoClient;
import cz.jirutka.spring.embedmongo.EmbeddedMongoFactoryBean;
import edu.depaul.taskmanager.api.model.User;
import edu.depaul.taskmanager.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.io.IOException;

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

    @Bean
    public ApplicationListener<ContextRefreshedEvent> loadDefaultUser() {
        return event -> {
            userRepository.save(User.newBuilder()
                    .withEmail("example@example.com")
                    .withPassword("Testing1")
                    .withFirstName("Hello")
                    .withLastName("World")
                    .build());
        };
    }
}

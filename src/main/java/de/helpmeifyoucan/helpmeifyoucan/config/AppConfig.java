package de.helpmeifyoucan.helpmeifyoucan.config;

import com.mongodb.client.MongoDatabase;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import de.helpmeifyoucan.helpmeifyoucan.dao.Database;

@Configuration
public class AppConfig {

    
    @Bean
    public MongoDatabase setDatabase(DatabaseConfig config) {
        return new Database(config).getDatabase();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
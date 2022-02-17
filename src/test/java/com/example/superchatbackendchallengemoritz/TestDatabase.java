package com.example.superchatbackendchallengemoritz;

import javax.sql.DataSource;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

@Configuration
public class TestDatabase {
    static final PostgreSQLContainer<?> POSTGRE_SQL_CONTAINER;

    static {
        POSTGRE_SQL_CONTAINER =
                new PostgreSQLContainer<>(DockerImageName.parse("postgres:13.1-alpine"));
        POSTGRE_SQL_CONTAINER.start();
    }

    @Bean
    public DataSource getDataSource() {
        return DataSourceBuilder.create()
                .url(POSTGRE_SQL_CONTAINER.getJdbcUrl())
                .username(POSTGRE_SQL_CONTAINER.getUsername())
                .password(POSTGRE_SQL_CONTAINER.getPassword())
                .build();
    }
}

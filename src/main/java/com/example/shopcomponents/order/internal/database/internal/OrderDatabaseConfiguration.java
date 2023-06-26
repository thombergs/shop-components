package com.example.shopcomponents.order.internal.database.internal;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.repository.config.AbstractJdbcConfiguration;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;

@Configuration
@ComponentScan
@EnableJdbcRepositories
public class OrderDatabaseConfiguration extends AbstractJdbcConfiguration {
}

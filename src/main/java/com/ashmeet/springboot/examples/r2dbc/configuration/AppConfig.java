package com.ashmeet.springboot.examples.r2dbc.configuration;

import static com.ashmeet.springboot.examples.r2dbc.model.AppConstants.READ;
import static com.ashmeet.springboot.examples.r2dbc.model.AppConstants.WRITE;

import com.ashmeet.springboot.examples.r2dbc.deserializer.PgRowDeserializer;
import com.ashmeet.springboot.examples.r2dbc.router.RoutingConnectionFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import io.r2dbc.spi.ConnectionFactoryOptions.Builder;
import io.r2dbc.spi.Option;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.r2dbc.convert.R2dbcCustomConversions;
import org.springframework.data.r2dbc.dialect.PostgresDialect;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@Configuration
@EnableR2dbcRepositories(basePackages = "com.ashmeet.springboot.examples.r2dbc.repository")
public class AppConfig {

  @Value("${spring.r2dbc.url.reader}")
  private String dbUrlReader;

  @Value("${spring.r2dbc.url.writer}")
  private String dbUrlWriter;

  @Value("${spring.r2dbc.username}")
  private String dbUsername;

  @Value("${spring.r2dbc.password}")
  private String dbPassword;
  @Value("${spring.r2dbc.pool.max-size}")
  private String maxPoolSize;
  @Value("${spring.r2dbc.pool.max-idle-time}")
  private String maxPoolConnectionIdleTime;
  @Value("${spring.r2dbc.properties.schema}")
  private String dbSchema;

  @Bean
  public RoutingConnectionFactory connectionFactory() {
        /*
            This is based on AWS Aurora, where we can have different urls(reader and writer) with same user name and password
         */
    ConnectionFactoryOptions.Builder commonOptions = ConnectionFactoryOptions.builder()
        .option(ConnectionFactoryOptions.USER, dbUsername)
        .option(ConnectionFactoryOptions.PASSWORD, dbPassword);

    // db specific options
    addDBSpecificOptions(commonOptions);

    ConnectionFactory readerFactory = ConnectionFactories.get(commonOptions.from(ConnectionFactoryOptions.parse(dbUrlReader)).build());
    ConnectionFactory writerFactory = ConnectionFactories.get(commonOptions.from(ConnectionFactoryOptions.parse(dbUrlWriter)).build());

    Map<String, ConnectionFactory> factories = new HashMap<>();
    factories.put(READ, readerFactory);
    factories.put(WRITE, writerFactory);

    RoutingConnectionFactory connectionFactory = new RoutingConnectionFactory();
    connectionFactory.setTargetConnectionFactories(factories);
    connectionFactory.setDefaultTargetConnectionFactory(writerFactory);
    connectionFactory.afterPropertiesSet();

    return connectionFactory;

  }

  @Bean
  public R2dbcCustomConversions customConversions(ObjectMapper objectMapper) {
    List<Converter<?, ?>> converters = new ArrayList<>();

    converters.add(new PgRowDeserializer(objectMapper));
    return R2dbcCustomConversions.of(PostgresDialect.INSTANCE, converters);
  }


  // POSTGRESQL specific options
  private void addDBSpecificOptions(Builder commonOptions) {
    commonOptions.option(Option.valueOf("maxSize"), maxPoolSize);
    commonOptions.option(Option.valueOf("maxIdleTime"), maxPoolConnectionIdleTime);
    commonOptions.option(Option.valueOf("schema"), dbSchema);
  }

}

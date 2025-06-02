package dev.pichborith.grpc_demo.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.WriteConcern;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;

@Configuration
public class MongoConfig {

  @Value("${spring.data.mongodb.uri}")
  private String mongoUri;

  @Bean
  public MongoClient reactiveMongoClient() {
    return MongoClients.create(MongoClientSettings.builder()
                                 .applyConnectionString(new ConnectionString(mongoUri))
                                 .writeConcern(WriteConcern.MAJORITY)
                                 .build());
  }

  @Bean
  public ReactiveMongoTemplate reactiveMongoTemplate() {
    return new ReactiveMongoTemplate(reactiveMongoClient(), "grpc_demo");
  }

  @EventListener(ContextRefreshedEvent.class)
  public void init() {
    reactiveMongoTemplate().getCollectionNames()
      .flatMap(reactiveMongoTemplate()::dropCollection)
      .blockLast();
  }

}

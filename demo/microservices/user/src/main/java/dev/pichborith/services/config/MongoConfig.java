package dev.pichborith.services.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import io.opentelemetry.api.trace.Tracer;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;

@Configuration
@RequiredArgsConstructor
public class MongoConfig {

  @Value("${spring.data.mongodb.uri}")
  private String mongoUri;

  private final Tracer tracer;

  @Bean
  public MongoClient reactiveMongoClient() {
//    MongoCredential credential = MongoCredential.createCredential("username", "microservices_demo", "password".toCharArray());

    MongoClientSettings settings = MongoClientSettings.builder()
//      .applyToConnectionPoolSettings(builder -> builder
//        .maxSize(100)
//        .minSize(1)
//        .maxConnectionIdleTime(1_800_000, MILLISECONDS)
//        .maxConnectionLifeTime(600_000, MILLISECONDS)
//        .maxWaitTime(30_000, MILLISECONDS)
//      )
//      .applyToSocketSettings(builder -> builder
//        .connectTimeout(10_000,MILLISECONDS)
//        .readTimeout(10_000, MILLISECONDS)
//      )
//      .applyToClusterSettings(builder -> builder
//        .hosts(Collections.singletonList(new ServerAddress("localhost", 27017)))
//      )
//      .credential(credential)
      .applyConnectionString(new ConnectionString(mongoUri))
//      .addCommandListener(new OtelMongoListener())
      .build();
    return MongoClients.create(settings);
  }

  @Bean
  public ReactiveMongoTemplate reactiveMongoTemplate() {
    return new ReactiveMongoTemplate(reactiveMongoClient(), "microservices_demo");
  }

  @EventListener(ContextRefreshedEvent.class)
  public void init() {
    reactiveMongoTemplate().getCollectionNames()
      .flatMap(reactiveMongoTemplate()::dropCollection)
      .blockLast();
  }

}

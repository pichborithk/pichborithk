package dev.pichborith.demo.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import dev.pichborith.demo.util.OtelMongoListener;
import io.opentelemetry.api.trace.Tracer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;

@Configuration
public class MongoConfig {

  @Value("${spring.data.mongodb.uri.user}")
  private String userUri;
  @Value("${spring.data.mongodb.uri.item}")
  private String itemUri;

//  @Bean
//  public Tracer mongoTracer(OpenTelemetry openTelemetry) {
//    return openTelemetry.getTracer("MongoDriver");
//  }

  @Primary
  @Bean
  public MongoClient userMongoClient(Tracer mongoTracer) {
    MongoClientSettings settings = MongoClientSettings.builder()
                                     .applyConnectionString(new ConnectionString(userUri))
                                     .addCommandListener(new OtelMongoListener(mongoTracer))
                                     .build();
    return MongoClients.create(settings);
  }

  @Primary
  @Bean
  public ReactiveMongoTemplate reactiveMongoTemplate(MongoClient userMongoClient) {
    return new ReactiveMongoTemplate(userMongoClient, "webfluxdemodbuser");
  }

  @Bean
  public MongoClient itemMongoClient(Tracer mongoTracer) {
    MongoClientSettings settings = MongoClientSettings.builder()
                                     .applyConnectionString(new ConnectionString(itemUri))
                                     .addCommandListener(new OtelMongoListener(mongoTracer))
                                     .build();
    return MongoClients.create(settings);
  }

  @Bean(name = "itemMongoTemplate")
  public ReactiveMongoTemplate itemMongoTemplate(MongoClient itemMongoClient) {
    return new ReactiveMongoTemplate(itemMongoClient, "webfluxdemodbitem");
  }

}

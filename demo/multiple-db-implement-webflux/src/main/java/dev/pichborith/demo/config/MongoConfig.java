package dev.pichborith.demo.config;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.ReactiveMongoDatabaseFactory;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;

@Configuration
public class MongoConfig {

    @Primary
    @Bean
    public MongoClient userMongoClient() {
        return MongoClients.create("mongodb://localhost:27017/webfluxdemodb");
    }

    @Primary
    @Bean
    public ReactiveMongoTemplate reactiveMongoTemplate(ReactiveMongoDatabaseFactory userFactory) {
        return new ReactiveMongoTemplate(userMongoClient(), "webfluxdemodb");
    }

    @Bean
    public MongoClient itemMongoClient() {
        return MongoClients.create(
            "mongodb://localhost:27017/webfluxdemodbitem");
    }

    @Bean(name = "itemMongoTemplate")
    public ReactiveMongoTemplate itemMongoTemplate() {
        return new ReactiveMongoTemplate(itemMongoClient(), "webfluxdemodbitem");
    }
}

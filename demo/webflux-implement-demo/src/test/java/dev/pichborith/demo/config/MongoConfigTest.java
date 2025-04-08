package dev.pichborith.demo.config;

import dev.pichborith.demo.IntegrationTest;
import dev.pichborith.demo.repository.user.MongoUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;

import static org.assertj.core.api.Assertions.assertThat;

public class MongoConfigTest extends IntegrationTest {

  @Autowired
  ReactiveMongoTemplate reactiveMongoTemplate;

  @BeforeEach
  void cleanup() {
//    reactiveMongoTemplate.remove(MongoUser.class).all().block();
  }

  @Test
  void testSuccess() {
    reactiveMongoTemplate.save(MongoUser.builder().name("a").email("b").build()).block();

    assertThat(
      reactiveMongoTemplate.findAll(MongoUser.class)
        .collectList()
        .block()
        .get(0)
        .getName()
    ).isEqualTo("a");
  }
}

package dev.pichborith.demo;

import dev.pichborith.demo.testcontainers.MongoContainerWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@AutoConfigureWebTestClient
@ActiveProfiles({"default"})
@SpringBootTest(webEnvironment = RANDOM_PORT)
@Testcontainers
public class IntegrationTest {

  @Container
  private static final MongoContainerWrapper MONGO_CONTAINER = MongoContainerWrapper.getInstance();

  @DynamicPropertySource
  static void setProperties(DynamicPropertyRegistry registry) {
    String mongoUri = "mongodb://%s:%d/%s?connectTimeoutMS=300000&maxPoolSize=100" +
                        "&maxIdleTimeMS=1800000&waitQueueTimeoutMS=30000";
    registry.add("spring.data.mongodb.uri.user",
                 () -> String.format(
                   mongoUri, MONGO_CONTAINER.getHost(),
                   MONGO_CONTAINER.getMappedPort(27017),
                   "webfluxdemodbuser"
                 ));
//    registry.add("spring.data.mongodb.uri.user", MONGO_CONTAINER::getReplicaSetUrl);
    registry.add("spring.data.mongodb.uri.item",
                 () -> String.format(
                   mongoUri, MONGO_CONTAINER.getHost(),
                   MONGO_CONTAINER.getMappedPort(27017),
                   "webfluxdemodbitem"
                 ));
//    registry.add("spring.autoconfigure.exclude",
//                 () -> "org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration");
  }

  @Test
  void testSuccess() {
    System.out.println(MONGO_CONTAINER.getReplicaSetUrl());
  }
}

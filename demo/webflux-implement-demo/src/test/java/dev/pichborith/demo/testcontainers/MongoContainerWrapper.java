package dev.pichborith.demo.testcontainers;

import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.utility.DockerImageName;

public class MongoContainerWrapper extends MongoDBContainer {

  private static final DockerImageName DOCKER_IMAGE_NAME = DockerImageName.parse("mongo");
  private static final String DEFAULT_TAG = "4.0.8";
  private static MongoContainerWrapper container;

  private MongoContainerWrapper() {
    super(DOCKER_IMAGE_NAME.withTag(DEFAULT_TAG));
  }

  public static MongoContainerWrapper getInstance() {
    if (container == null) {
      container = new MongoContainerWrapper();
    }

    return container;
  }

  @Override
  public void start() {
    super.start();
    System.out.println("MongoDB start on " + container.getHost());
  }
}

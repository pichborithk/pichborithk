package dev.pichborith.grpc_reactor_demo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Document(collection = "users")
public class User {

  @Id
  String id;
  String name;
//  Role role;

}

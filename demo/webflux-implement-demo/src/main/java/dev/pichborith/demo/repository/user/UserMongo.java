package dev.pichborith.demo.repository.user;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Document(collation = "users")
public class UserMongo {

    @Id
    private String id;
    private String name;
    private String email;

}

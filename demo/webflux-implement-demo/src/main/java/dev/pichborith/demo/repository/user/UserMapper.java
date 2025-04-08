package dev.pichborith.demo.repository.user;

import dev.pichborith.demo.domain.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class UserMapper {

    public abstract MongoUser toPersistenceModel(User user);

    public abstract User toResponseModel(MongoUser mongoUser);
}
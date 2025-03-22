package dev.pichborith.demo.model.user;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class UserMapper {

    public abstract UserMongo toPersistenceModel(User user);

    public abstract User toResponseModel(UserMongo userMongo);
}
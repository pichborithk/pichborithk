package dev.pichborith.grpc_demo.mapper;

import dev.pichborith.grpc.UserRequest;
import dev.pichborith.grpc_demo.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface UserMapper {

  @Mapping(target = "id", ignore = true)
  User toPersistenceModel(UserRequest request);
}

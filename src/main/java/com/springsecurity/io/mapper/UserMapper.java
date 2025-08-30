package com.springsecurity.io.mapper;

import com.springsecurity.io.dto.UserRequest;
import com.springsecurity.io.entity.Users;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper extends EntityMapper<UserRequest,Users>{

    @Override
    Users toEntity(UserRequest userRequest);

    @Override
    UserRequest toDto(Users users);
}

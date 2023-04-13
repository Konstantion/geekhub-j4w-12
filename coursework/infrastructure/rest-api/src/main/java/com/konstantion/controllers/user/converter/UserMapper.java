package com.konstantion.controllers.user.converter;

import com.konstantion.controllers.user.dto.CreateUserRequestDto;
import com.konstantion.controllers.user.dto.LoginUserRequestDto;
import com.konstantion.controllers.user.dto.UserDto;
import com.konstantion.user.User;
import com.konstantion.user.model.CreateUserRequest;
import com.konstantion.user.model.LoginUserRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDto toDto(User user);

    CreateUserRequest toEntity(CreateUserRequestDto dto);
    LoginUserRequest toLoginUserRequest(LoginUserRequestDto dto);
}

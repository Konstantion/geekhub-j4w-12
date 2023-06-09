package com.konstantion.dto.mappers;

import com.konstantion.dto.user.*;
import com.konstantion.user.User;
import com.konstantion.user.model.*;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDto toDto(User entity);

    List<UserDto> toDto(List<User> entity);

    LoginUserRequest toEntity(LoginUserDto dto);

    RegistrationUserRequest toEntity(RegistrationUserDto dto);

    UpdateUserRequest toEntity(UpdateUserDto updateUserDto);

    UpdateUserRolesRequest toEntity(UpdateUserRolesDto updateUserRolesDto);

    CreateUserRequest toCreateUserRequest(CreateUserRequestDto request);
    RestoreUserRequest toRestoreUserRequest(RestoreUserRequestDto dto);
}

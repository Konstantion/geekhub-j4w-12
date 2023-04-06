package com.konstantion.dto.mappers;

import com.konstantion.dto.user.LoginUserDto;
import com.konstantion.dto.user.RegistrationUserDto;
import com.konstantion.dto.user.UpdateUserDto;
import com.konstantion.dto.user.UserDto;
import com.konstantion.user.User;
import com.konstantion.user.model.LoginUserRequest;
import com.konstantion.user.model.RegistrationUserRequest;
import com.konstantion.user.model.UpdateUserRequest;
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
}

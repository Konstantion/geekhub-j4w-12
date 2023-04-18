package com.konstantion.dto.user.converter;

import com.konstantion.dto.user.dto.CreateUserRequestDto;
import com.konstantion.dto.user.dto.LoginUserRequestDto;
import com.konstantion.dto.user.dto.UpdateUserRequestDto;
import com.konstantion.dto.user.dto.UserDto;
import com.konstantion.user.User;
import com.konstantion.user.model.CreateUserRequest;
import com.konstantion.user.model.LoginUserRequest;
import com.konstantion.user.model.UpdateUserRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDto toDto(User user);

    LoginUserRequest toLoginUserRequest(LoginUserRequestDto dto);

    List<UserDto> toDto(List<User> waitersByTableId);

    UpdateUserRequest toUpdateUserRequest(UpdateUserRequestDto requestDto);

    CreateUserRequest toCreateUserRequest(CreateUserRequestDto requestDto);
}

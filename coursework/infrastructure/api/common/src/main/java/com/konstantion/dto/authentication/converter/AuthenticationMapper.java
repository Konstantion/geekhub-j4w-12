package com.konstantion.dto.authentication.converter;

import com.konstantion.authentication.model.AuthenticationResponse;
import com.konstantion.dto.authentication.dto.AuthenticationResponseDto;
import com.konstantion.dto.table.dto.TableDto;
import com.konstantion.dto.user.dto.UserDto;
import com.konstantion.table.Table;
import com.konstantion.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.Map;

import static com.konstantion.utils.EntityNameConstants.*;

@Mapper
public interface AuthenticationMapper {
    AuthenticationMapper INSTANCE = Mappers.getMapper(AuthenticationMapper.class);

    default AuthenticationResponseDto toDto(AuthenticationResponse authenticationResponse) {
        if (authenticationResponse.type().equals(USER)
            && authenticationResponse.userDetails() instanceof User user) {

            return new AuthenticationResponseDto(
                    authenticationResponse.token(),
                    Map.of(authenticationResponse.type(), new UserDto(
                            user.getId(),
                            user.getFirstName(),
                            user.getLastName(),
                            user.getUsername(),
                            user.getPhoneNumber(),
                            user.getAge(),
                            user.getActive(),
                            user.getRoles(),
                            user.getCreatedAt(),
                            user.getPermissions()
                    )));
        } else if (authenticationResponse.type().equals(TABLE)
                   && authenticationResponse.userDetails() instanceof Table table) {
            return new AuthenticationResponseDto(
                    authenticationResponse.token(),
                    Map.of(authenticationResponse.type(),
                            new TableDto(
                                    table.getId(),
                                    table.getName(),
                                    table.getCapacity(),
                                    table.getTableType(),
                                    table.getHallId(),
                                    table.getOrderId(),
                                    table.getWaitersId(),
                                    table.getActive()
                            ))
            );
        } else {
            return new AuthenticationResponseDto(
                    authenticationResponse.token(),
                    Map.of(ENTITY, authenticationResponse.userDetails())
            );
        }
    }
}

package com.lottofun.lottofunrest.mapper;

import com.lottofun.lottofunrest.dto.UserDto;
import com.lottofun.lottofunrest.model.User;
import com.lottofun.lottofunrest.util.mapper.GenericMapper;

public class UserMapper {
    public static GenericMapper<User, UserDto> userAndUserDto() {
        return GenericMapper.createMap(
                user -> UserDto.builder()
                        .id(user.getId())
                        .username(user.getUsername())
                        .name(user.getName())
                        .surname(user.getSurname())
                        .balance(user.getBalance())
                        .build(),

                dto -> User.builder()
                        .id(dto.getId())
                        .username(dto.getUsername())
                        .name(dto.getName())
                        .surname(dto.getSurname())
                        .balance(dto.getBalance())
                        .build()
        );
    }
}

package app.mapper;

import app.config.MapperConfig;
import app.dto.UserRequestDto;
import app.dto.UserResponseDto;
import app.model.User;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    User toModel(UserRequestDto requestDto);

    UserResponseDto toDto(User user);
}

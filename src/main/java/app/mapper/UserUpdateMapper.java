package app.mapper;

import app.config.MapperConfig;
import app.dto.UserUpdateRequestDto;
import app.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(config = MapperConfig.class, componentModel = MappingConstants.ComponentModel.SPRING,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserUpdateMapper {
    void updateUserFromDto(UserUpdateRequestDto requestDto, @MappingTarget User user);
}

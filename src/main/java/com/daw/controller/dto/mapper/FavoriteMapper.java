package com.daw.controller.dto.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.daw.controller.dto.FavoriteDTO;
import com.daw.datamodel.entities.Favorite;

@Mapper(componentModel = "spring", uses = {UserSummaryMapper.class})
public interface FavoriteMapper {

    @Mapping(target = "userDTO", source = "user")
    @Mapping(target = "favoriteUserDTO", source = "favoriteUser")
    FavoriteDTO toDto(Favorite favorite);

    List<FavoriteDTO> toListDto(List<Favorite> favorites);
}

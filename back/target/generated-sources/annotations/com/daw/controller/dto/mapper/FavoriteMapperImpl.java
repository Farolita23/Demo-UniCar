package com.daw.controller.dto.mapper;

import com.daw.controller.dto.FavoriteDTO;
import com.daw.datamodel.entities.Favorite;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-27T10:23:55+0200",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class FavoriteMapperImpl implements FavoriteMapper {

    @Autowired
    private UserSummaryMapper userSummaryMapper;

    @Override
    public FavoriteDTO toDto(Favorite favorite) {
        if ( favorite == null ) {
            return null;
        }

        FavoriteDTO favoriteDTO = new FavoriteDTO();

        favoriteDTO.setUserDTO( userSummaryMapper.toDto( favorite.getUser() ) );
        favoriteDTO.setFavoriteUserDTO( userSummaryMapper.toDto( favorite.getFavoriteUser() ) );
        favoriteDTO.setId( favorite.getId() );

        return favoriteDTO;
    }

    @Override
    public List<FavoriteDTO> toListDto(List<Favorite> favorites) {
        if ( favorites == null ) {
            return null;
        }

        List<FavoriteDTO> list = new ArrayList<FavoriteDTO>( favorites.size() );
        for ( Favorite favorite : favorites ) {
            list.add( toDto( favorite ) );
        }

        return list;
    }
}

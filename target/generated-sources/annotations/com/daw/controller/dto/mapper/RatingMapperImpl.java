package com.daw.controller.dto.mapper;

import com.daw.controller.dto.RatingDTO;
import com.daw.datamodel.entities.Rating;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-27T00:31:05+0200",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class RatingMapperImpl implements RatingMapper {

    @Autowired
    private UserSummaryMapper userSummaryMapper;

    @Override
    public RatingDTO toDto(Rating rating) {
        if ( rating == null ) {
            return null;
        }

        RatingDTO ratingDTO = new RatingDTO();

        ratingDTO.setUserRateDTO( userSummaryMapper.toDto( rating.getUserRate() ) );
        ratingDTO.setRatedUserDTO( userSummaryMapper.toDto( rating.getRatedUser() ) );
        ratingDTO.setId( rating.getId() );
        ratingDTO.setRating( rating.getRating() );

        return ratingDTO;
    }

    @Override
    public List<RatingDTO> toListDto(List<Rating> ratings) {
        if ( ratings == null ) {
            return null;
        }

        List<RatingDTO> list = new ArrayList<RatingDTO>( ratings.size() );
        for ( Rating rating : ratings ) {
            list.add( toDto( rating ) );
        }

        return list;
    }

    @Override
    public Set<RatingDTO> toSetDto(Set<Rating> ratings) {
        if ( ratings == null ) {
            return null;
        }

        Set<RatingDTO> set = new LinkedHashSet<RatingDTO>( Math.max( (int) ( ratings.size() / .75f ) + 1, 16 ) );
        for ( Rating rating : ratings ) {
            set.add( toDto( rating ) );
        }

        return set;
    }
}

package com.daw.controller.dto.mapper;

import com.daw.controller.dto.RatingDTO;
import com.daw.datamodel.entities.Rating;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-28T19:24:49+0100",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.43.0.v20250819-1513, environment: Java 21.0.8 (Eclipse Adoptium)"
)
@Component
public class RatingMapperImpl implements RatingMapper {

    @Override
    public RatingDTO toDto(Rating rating) {
        if ( rating == null ) {
            return null;
        }

        RatingDTO ratingDTO = new RatingDTO();

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
}

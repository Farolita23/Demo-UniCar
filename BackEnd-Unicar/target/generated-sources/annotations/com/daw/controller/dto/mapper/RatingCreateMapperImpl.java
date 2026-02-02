package com.daw.controller.dto.mapper;

import com.daw.controller.dto.RatingCreateDTO;
import com.daw.datamodel.entities.Rating;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-02-02T13:17:27+0100",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.43.0.v20250819-1513, environment: Java 21.0.8 (Eclipse Adoptium)"
)
@Component
public class RatingCreateMapperImpl extends RatingCreateMapper {

    @Override
    public Rating toEntity(RatingCreateDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Rating rating = new Rating();

        rating.setUserRate( userById( dto.getIdUserRate() ) );
        rating.setRatedUser( userById( dto.getIdRatedUser() ) );
        rating.setRating( dto.getRating() );

        return rating;
    }
}

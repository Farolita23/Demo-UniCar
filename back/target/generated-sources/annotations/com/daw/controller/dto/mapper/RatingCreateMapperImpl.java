package com.daw.controller.dto.mapper;

import com.daw.controller.dto.RatingCreateDTO;
import com.daw.datamodel.entities.Rating;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-27T10:23:55+0200",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class RatingCreateMapperImpl implements RatingCreateMapper {

    @Override
    public Rating toEntity(RatingCreateDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Rating rating = new Rating();

        rating.setRating( dto.getRating() );

        return rating;
    }
}

package com.daw.controller.dto.mapper;

import com.daw.controller.dto.RatingUpdateDTO;
import com.daw.datamodel.entities.Rating;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-01T11:43:33+0100",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.43.0.v20250819-1513, environment: Java 21.0.8 (Eclipse Adoptium)"
)
@Component
public class RatingUpdateMapperImpl implements RatingUpdateMapper {

    @Override
    public void updateEntityFromDto(RatingUpdateDTO dto, Rating entity) {
        if ( dto == null ) {
            return;
        }

        entity.setRating( dto.getRating() );
    }
}

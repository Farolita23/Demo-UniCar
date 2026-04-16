package com.daw.controller.dto.mapper;

import com.daw.controller.dto.RatingUpdateDTO;
import com.daw.datamodel.entities.Rating;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-16T09:46:27+0200",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.44.0.v20251118-1623, environment: Java 25.0.1 (Eclipse Adoptium)"
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

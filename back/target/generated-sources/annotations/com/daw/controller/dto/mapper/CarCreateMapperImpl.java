package com.daw.controller.dto.mapper;

import com.daw.controller.dto.CarCreateDTO;
import com.daw.datamodel.entities.Car;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-16T09:46:27+0200",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.44.0.v20251118-1623, environment: Java 25.0.1 (Eclipse Adoptium)"
)
@Component
public class CarCreateMapperImpl implements CarCreateMapper {

    @Override
    public Car toEntity(CarCreateDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Car car = new Car();

        car.setCapacity( dto.getCapacity() );
        car.setColor( dto.getColor() );
        car.setLicensePlate( dto.getLicensePlate() );
        car.setModel( dto.getModel() );

        return car;
    }

    @Override
    public void updateEntityFromDto(CarCreateDTO dto, Car entity) {
        if ( dto == null ) {
            return;
        }

        entity.setCapacity( dto.getCapacity() );
        entity.setColor( dto.getColor() );
        entity.setLicensePlate( dto.getLicensePlate() );
        entity.setModel( dto.getModel() );
    }
}

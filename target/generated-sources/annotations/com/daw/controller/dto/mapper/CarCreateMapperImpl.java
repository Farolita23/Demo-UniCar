package com.daw.controller.dto.mapper;

import com.daw.controller.dto.CarCreateDTO;
import com.daw.datamodel.entities.Car;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-27T00:31:05+0200",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class CarCreateMapperImpl implements CarCreateMapper {

    @Override
    public Car toEntity(CarCreateDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Car car = new Car();

        car.setModel( dto.getModel() );
        car.setColor( dto.getColor() );
        car.setLicensePlate( dto.getLicensePlate() );
        car.setCapacity( dto.getCapacity() );

        return car;
    }

    @Override
    public void updateEntityFromDto(CarCreateDTO dto, Car entity) {
        if ( dto == null ) {
            return;
        }

        entity.setModel( dto.getModel() );
        entity.setColor( dto.getColor() );
        entity.setLicensePlate( dto.getLicensePlate() );
        entity.setCapacity( dto.getCapacity() );
    }
}

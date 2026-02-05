package com.daw.controller.dto.mapper;

import com.daw.controller.dto.CarCreateDTO;
import com.daw.datamodel.entities.Car;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-02-05T09:58:01+0100",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.43.0.v20250819-1513, environment: Java 21.0.8 (Eclipse Adoptium)"
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
}

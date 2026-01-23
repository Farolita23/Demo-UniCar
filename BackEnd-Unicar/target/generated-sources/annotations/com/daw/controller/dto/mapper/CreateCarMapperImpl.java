package com.daw.controller.dto.mapper;

import com.daw.controller.dto.CarCreateDTO;
import com.daw.datamodel.entities.Car;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-01-23T10:58:03+0100",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.43.0.v20250819-1513, environment: Java 21.0.8 (Eclipse Adoptium)"
)
@Component
public class CreateCarMapperImpl implements CreateCarMapper {

    @Override
    public CarCreateDTO toDto(Car car) {
        if ( car == null ) {
            return null;
        }

        CarCreateDTO carCreateDTO = new CarCreateDTO();

        carCreateDTO.setCapacity( car.getCapacity() );
        carCreateDTO.setColor( car.getColor() );
        carCreateDTO.setDriver( car.getDriver() );
        carCreateDTO.setLicensePlate( car.getLicensePlate() );
        carCreateDTO.setModel( car.getModel() );

        return carCreateDTO;
    }

    @Override
    public Car toEntity(CarCreateDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Car car = new Car();

        car.setCapacity( dto.getCapacity() );
        car.setColor( dto.getColor() );
        car.setDriver( dto.getDriver() );
        car.setLicensePlate( dto.getLicensePlate() );
        car.setModel( dto.getModel() );

        return car;
    }
}

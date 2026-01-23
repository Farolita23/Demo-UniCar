package com.daw.controller.dto.mapper;

import com.daw.controller.dto.CarDTO;
import com.daw.datamodel.entities.Car;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-01-23T11:00:21+0100",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.43.0.v20250819-1513, environment: Java 21.0.8 (Eclipse Adoptium)"
)
@Component
public class CarMapperImpl implements CarMapper {

    @Override
    public CarDTO toDto(Car car) {
        if ( car == null ) {
            return null;
        }

        CarDTO carDTO = new CarDTO();

        carDTO.setCapacity( car.getCapacity() );
        carDTO.setColor( car.getColor() );
        carDTO.setDriver( car.getDriver() );
        carDTO.setId( car.getId() );
        carDTO.setLicensePlate( car.getLicensePlate() );
        carDTO.setModel( car.getModel() );

        return carDTO;
    }

    @Override
    public List<CarDTO> toListDto(List<Car> cars) {
        if ( cars == null ) {
            return null;
        }

        List<CarDTO> list = new ArrayList<CarDTO>( cars.size() );
        for ( Car car : cars ) {
            list.add( toDto( car ) );
        }

        return list;
    }
}

package com.daw.controller.dto.mapper;

import com.daw.controller.dto.CarDTO;
import com.daw.datamodel.entities.Car;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-16T09:46:26+0200",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.44.0.v20251118-1623, environment: Java 25.0.1 (Eclipse Adoptium)"
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

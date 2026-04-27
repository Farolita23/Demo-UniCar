package com.daw.controller.dto.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

import com.daw.controller.dto.TripDTO;
import com.daw.datamodel.entities.Trip;

@Mapper(componentModel = "spring", uses = {UserSummaryMapper.class,
                                            CarMapper.class,
                                            CampusMapper.class,
                                            TownMapper.class})
public interface TripMapper {

    @Mapping(target = "carDTO",        source = "car")
    @Mapping(target = "townDTO",       source = "town")
    @Mapping(target = "campusDTO",     source = "campus")
    @Mapping(target = "driverDTO",     source = "car.driver")
    @Mapping(target = "passengersDTO", source = "passengers")
    @Mapping(target = "requestersDTO", source = "requesters")
    TripDTO toDto(Trip trip);

    List<TripDTO> toListDto(List<Trip> trips);

    default Page<TripDTO> toPageDto(Page<Trip> trips) {
        return trips.map(this::toDto);
    }

}

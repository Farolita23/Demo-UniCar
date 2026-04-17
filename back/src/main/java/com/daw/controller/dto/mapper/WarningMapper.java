package com.daw.controller.dto.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.daw.controller.dto.WarningDTO;
import com.daw.datamodel.entities.Warning;

@Mapper(componentModel = "spring", uses = {UserSummaryMapper.class})
public interface WarningMapper {

    @Mapping(target = "userDTO", source = "user")
    @Mapping(target = "adminDTO", source = "admin")
    WarningDTO toDto(Warning warning);

    List<WarningDTO> toListDto(List<Warning> warnings);
}

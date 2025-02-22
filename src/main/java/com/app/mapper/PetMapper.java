package com.app.mapper;

import com.app.domain.Pet;
import com.app.model.PetDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR, componentModel = MappingConstants.ComponentModel.SPRING)
public interface PetMapper {
    PetDto to(Pet pet);

    @Mapping(target = "id", ignore = true)
    Pet to(PetDto pet);

    List<PetDto> to(List<Pet> pet);
}
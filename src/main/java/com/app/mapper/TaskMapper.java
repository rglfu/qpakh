package com.app.mapper;

import com.app.domain.Task;
import com.app.model.TaskDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR, componentModel = MappingConstants.ComponentModel.SPRING)
public interface TaskMapper {
    TaskDto to(Task task);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "state", constant = "IN_PROGRESS")
    @Mapping(target = "result", ignore = true)
    Task to(TaskDto task);
}
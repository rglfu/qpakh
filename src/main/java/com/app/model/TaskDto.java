package com.app.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class TaskDto {
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private State state;
    @NotNull
    private Integer min;
    @NotNull
    private Integer max;
    @NotNull
    @Positive
    private Integer count;
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private List<Integer> result;
}
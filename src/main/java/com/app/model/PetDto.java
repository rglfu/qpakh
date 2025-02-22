package com.app.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PetDto {
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;
    @EqualsAndHashCode.Include
    @NotBlank
    @Size(max = 1023)
    private String name;
}
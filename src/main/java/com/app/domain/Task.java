package com.app.domain;

import com.app.model.State;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Entity
public class Task {
    @EqualsAndHashCode.Exclude
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @JdbcType(PostgreSQLEnumJdbcType.class)
    @Column(columnDefinition = "state")
    private State state;
    @NotNull
    @Immutable
    private Integer min;
    @NotNull
    @Immutable
    private Integer max;
    @NotNull
    @Positive
    @Immutable
    private Integer count;
    private List<Integer> result = new ArrayList<>();
}
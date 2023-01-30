package ru.job4j.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ru.job4j.handler.Operation;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "person")
public class Person {

    @Positive(message = "must be more then 0", groups = {Operation.OnUpdate.class})
    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull(message = "must be not null", groups = {Operation.OnUpdate.class, Operation.OnCreate.class})
    @Size(min = 5)
    private String login;

    @NotNull(message = "must be not null", groups = {Operation.OnUpdate.class, Operation.OnCreate.class})
    @Size(min = 7)
    private String password;
}

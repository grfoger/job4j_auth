package ru.job4j.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;
import ru.job4j.domain.Person;

import java.util.Collection;
import java.util.List;

public interface PersonRepository extends CrudRepository<Person, Integer> {
    Collection<Person> findAll();
    Person findByLogin(String login);
}

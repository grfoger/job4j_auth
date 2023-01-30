package ru.job4j.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.domain.Person;

import java.util.Collection;
import java.util.List;

public interface PersonRepository extends CrudRepository<Person, Integer> {
    Collection<Person> findAll();
    Person findByLogin(String login);

    @Transactional
    @Modifying
    @Query(value = "UPDATE Person SET login = ? WHERE id = ?", nativeQuery = true)
    void patchLogin(String login, int id);
    @Transactional
    @Modifying
    @Query(value = "UPDATE Person SET password = ? WHERE id = ?", nativeQuery = true)
    void patchPass(String password, int id);
}

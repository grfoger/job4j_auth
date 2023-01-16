package ru.job4j.service;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.job4j.domain.Person;
import ru.job4j.repository.PersonRepository;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class PersonService {

    private final PersonRepository repository;
    public List<Person> findAll() {
        return (List<Person>) repository.findAll();
    }

    public Optional<Person> findById(int id) {
        return repository.findById(id);
    }

    public Person save(Person person) {
        return repository.save(person);
    }

    public boolean update(Person person) {
        int id = person.getId();
        if (!repository.existsById(id) || !person.getLogin().equals(repository.findById(id).get().getLogin())) {
            return false;
        }
        repository.save(person);
        return true;
    }

    public boolean delete(Person person) {
        if (!repository.existsById(person.getId())) {
            return false;
        }
        repository.delete(person);
        return true;
    }
}

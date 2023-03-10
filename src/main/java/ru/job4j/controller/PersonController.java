package ru.job4j.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.domain.Person;
import ru.job4j.handler.Operation;
import ru.job4j.repository.PersonRepository;
import ru.job4j.service.PersonService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import static java.util.Map.entry;

@AllArgsConstructor
@RestController
@RequestMapping("/person")
public class PersonController {
    private final PersonService persons;
    private static final Logger LOGGER = LoggerFactory.getLogger(PersonController.class.getSimpleName());
    private final ObjectMapper objectMapper;

    private static final String IS_SHORT_MSG = """
        Your password or login so short.
        Please, do your login more then 4 characters
        and password more then 6 characters. Thanks.
        """;

    @GetMapping(value = {"/", "/all"})
    public ResponseEntity<List<Person>> findAll() {
        List<Person> list = this.persons.findAll();
        if (list.isEmpty()) {
            throw new NoSuchElementException("Have not users");
        }
        return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT)
                .contentLength(list.toString().length())
                .body(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Person> findById(@PathVariable int id) {
        if (id < 1) {
            throw new IllegalArgumentException("Wrong id");
        }
        Optional<Person> person = Optional.of(this.persons.findById(id).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Account is not found. Please, check id."
        )));
        return new ResponseEntity<>(
                person.orElse(new Person()),
                HttpStatus.OK
                );
    }

    @PostMapping("/")
    @Validated(Operation.OnCreate.class)
    public ResponseEntity<Person> create(@Valid @RequestBody Person person) {
        isWrongDataException(person);
        return new ResponseEntity<>(this.persons.save(person), HttpStatus.CREATED);
    }

    @PutMapping("/")
    @Validated(Operation.OnUpdate.class)
    public ResponseEntity<Void> update(@Valid @RequestBody Person person) {
        isWrongDataException(person);
        if (!this.persons.update(person)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

    private void isWrongDataException(Person person) {
        if (person.getLogin().length() < 5 || person.getPassword().length() < 7) {
            throw new IllegalArgumentException(IS_SHORT_MSG);
        }
        if (person.getPassword() == null || person.getLogin() == null) {
            throw new NullPointerException("Have not login or password");
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        if (id < 1) {
            throw new IllegalArgumentException("Wrong id");
        }
        Person person = new Person();
        person.setId(id);
        if (!this.persons.delete(person)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/sign-up")
    @Validated(Operation.OnCreate.class)
    public void signUp(@Valid @RequestBody Person person) {
        isWrongDataException(person);
        persons.save(person);
    }

    @PatchMapping("/edit/{id}")
    public void patch(@RequestBody Person person, @PathVariable int id) {
        person.setId(id);
        if ((person.getLogin() != null && person.getLogin().length() < 5)
        || (person.getLogin() != null && person.getPassword().length() < 7)) {
            throw new IllegalArgumentException(IS_SHORT_MSG);
        }
        persons.patch(person);
    }

    @ExceptionHandler(value = { IllegalArgumentException.class })
    public void exceptionHandler(Exception e, HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(Map.ofEntries(
                entry("message", e.getMessage()),
                entry("type", e.getClass())
        )));
        LOGGER.error(e.getLocalizedMessage());
    }
}

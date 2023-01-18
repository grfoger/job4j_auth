package ru.job4j.service;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.job4j.domain.Person;
import ru.job4j.repository.PersonRepository;

import java.util.Collections;

@AllArgsConstructor
@Service
public class PersonDetailsServiceImpl implements UserDetailsService {

    private PersonRepository persons;

   @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       Person person = persons.findByLogin(username);
       if (person == null) {
           throw new UsernameNotFoundException(username);
       }
       return new User(person.getLogin(), person.getPassword(), Collections.emptyList());
   }

}

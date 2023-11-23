package com.example.dataaccess;

import com.example.entities.Person;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonRepository extends ListCrudRepository<Person, Long> {
    Optional<Person> findPersonByName(String name);
}

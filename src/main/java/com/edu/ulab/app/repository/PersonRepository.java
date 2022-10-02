package com.edu.ulab.app.repository;

import com.edu.ulab.app.entity.Person;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonRepository extends CrudRepository<Person, Long> {

    @Query("SELECT p FROM Person p WHERE p.id = :id")
    Optional<Person> findById(long id);

}

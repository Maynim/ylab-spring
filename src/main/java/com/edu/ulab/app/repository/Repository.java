package com.edu.ulab.app.repository;

import com.edu.ulab.app.entity.BaseEntity;

import java.util.List;
import java.util.Optional;

public interface Repository<E extends BaseEntity> {

    E save(E entity);

    void delete(Long id);

    boolean update(E entity);

    Optional<E> findById(Long id);

    List<E> findAll();
}

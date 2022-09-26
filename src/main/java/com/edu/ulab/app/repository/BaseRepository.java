package com.edu.ulab.app.repository;

import com.edu.ulab.app.entity.BaseEntity;
import com.edu.ulab.app.storage.Storage;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

@Slf4j
@Deprecated
public abstract class BaseRepository<E extends BaseEntity> implements Repository<E> {

    private final String table;

    public BaseRepository(String table) {
        this.table = table;
    }

    @Override
    public E save(E entity){
        return Storage.insertInto(table, entity);
    }

    @Override
    public void delete(Long id) {
        Storage.deleteEntity(table, id);
    }

    @Override
    public boolean update(E entity) {
        return Storage.update(table, entity);
    }

    @Override
    public Optional<E> findById(Long id) {
        E entity = Storage.getEntity(table, id);
        return Optional.ofNullable(entity);
    }

    @Override
    public List<E> findAll() {
        return Storage.getEntityList(table);
    }
}

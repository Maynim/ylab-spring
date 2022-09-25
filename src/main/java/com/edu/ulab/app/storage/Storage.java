package com.edu.ulab.app.storage;

import com.edu.ulab.app.entity.BaseEntity;
import com.edu.ulab.app.exception.StorageException;

import java.util.*;

public class Storage {
    //todo создать хранилище в котором будут содержаться данные +
    // сделать абстракции через которые можно будет производить операции с хранилищем +
    // продумать логику поиска и сохранения +
    // продумать возможные ошибки +
    // учесть, что при сохранеии юзера или книги, должен генерироваться идентификатор +
    // продумать что у узера может быть много книг и нужно создать эту связь +
    // так же учесть, что методы хранилища принимают друго тип данных - учесть это в абстракции +

    private static final Map<String, Map<Long, ? extends BaseEntity>> maynimdb = new HashMap<>();
    private static final Map<String, Long> sequence = new HashMap<>();

    static {
        StorageUtil.generateStorage();
        StorageUtil.insertIntoStorage();
    }

    protected static void createNewTable(String table) {
        if (maynimdb.containsKey(table)) {
            throw new StorageException("Table already exists");
        }

        maynimdb.put(table, new TreeMap<>());
        sequence.put(table, 1L);
    }

    @SuppressWarnings("unchecked")
    public static <E extends BaseEntity> E insertInto(String table, E entity) {
        if (!maynimdb.containsKey(table)) {
            throw new StorageException("Table not found");
        }

        Map<Long, E> entityMap = (Map<Long, E>) maynimdb.get(table);
        Long id = sequence.get(table);

        sequence.put(table, id + 1);
        entity.setId(id);
        entityMap.put(id, entity);

        return entity;
    }

    @SuppressWarnings("unchecked")
    public static <E extends BaseEntity> E getEntity(String table, Long id) {
        if (!maynimdb.containsKey(table)) {
            throw new StorageException("Table not found");
        }

        Map<Long, E> entityMap = (Map<Long, E>) maynimdb.get(table);
        return entityMap.get(id);
    }

    @SuppressWarnings("unchecked")
    public static <E extends BaseEntity> List<E> getEntityList(String table) {
        if (!maynimdb.containsKey(table)) {
            throw new StorageException("Table not found");
        }

        Map<Long, E> entityMap = (Map<Long, E>) maynimdb.get(table);
        return new ArrayList<>(entityMap.values());
    }

    @SuppressWarnings("unchecked")
    public static <E extends BaseEntity> boolean update(String table, E entity) {
        if (!maynimdb.containsKey(table)) {
            throw new StorageException("Table not found");
        }

        if (maynimdb.get(table).get(entity.getId()) == null) {
            return false;
        }


        Map<Long, E> entityMap = (Map<Long, E>) maynimdb.get(table);
        entityMap.put(entity.getId(), entity);

        return true;
    }

    @SuppressWarnings("unchecked")
    public static <E extends BaseEntity> void deleteEntity(String table, Long id) {
        if (!maynimdb.containsKey(table)) {
            throw new StorageException("Table not found");
        }

        Map<Long, E> entityMap = (Map<Long, E>) maynimdb.get(table);
        entityMap.remove(id);
    }
}

package com.lashkevich.bank.dao;

import com.lashkevich.bank.entity.Entity;

import java.util.List;
import java.util.Optional;

public interface AbstractDAO<T extends Entity> {

    Optional<T> findById(long id);

    List<T> findAll();

    void save(T entity);

    void update(T entity);

    void delete(long id);

}

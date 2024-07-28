package com.lashkevich.bank.service;

import com.lashkevich.bank.dao.AbstractDAO;
import com.lashkevich.bank.entity.Entity;

import java.util.List;
import java.util.Optional;

public abstract class AbstractService<T extends Entity>
        implements Service<T> {

    protected abstract AbstractDAO<T> getDAO();

    @Override
    public Optional<T> findById(long id) {
        return getDAO().findById(id);
    }

    @Override
    public List<T> findAll() {
        return getDAO().findAll();
    }

    @Override
    public void save(T entity) {
        getDAO().save(entity);
    }

    @Override
    public void update(T entity) {
        getDAO().update(entity);
    }

    @Override
    public void delete(long id) {
        getDAO().delete(id);
    }

}

package com.lashkevich.bank.dao.mapper;

import com.lashkevich.bank.entity.Entity;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface EntityMapper<T extends Entity> {

    T map(ResultSet resultSet) throws SQLException;

}

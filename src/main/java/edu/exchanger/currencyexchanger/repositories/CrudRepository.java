package edu.exchanger.currencyexchanger.repositories;

import java.util.List;
import java.util.Optional;

public interface CrudRepository<T> {
    Optional<T> findById(int id);
    List<T> findAll();
    void save(T entity);
    void update(T entity);
    void delete(int id);

}

package interfaces;

import java.util.LinkedList;

public interface GenericService<T, ID> {

    T findById(ID id);

    LinkedList<T> findAll();

    void save(T entity);

    void update(T entity);

    void delete(T entity);

    void deleteById(ID id);
}

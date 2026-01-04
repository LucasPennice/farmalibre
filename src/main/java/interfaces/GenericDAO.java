package interfaces;

import java.util.LinkedList;

public interface GenericDAO<T, ID> {
    public T findById(ID id);

    public LinkedList<T> findAll();

    public void save(T entity);

    public void update(T entity);

    public void delete(T entity);

    public void deleteById(ID id);
}
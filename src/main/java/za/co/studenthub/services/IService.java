package za.co.studenthub.services;

import java.util.List;

public interface IService <T, id>{
    T create(T t);
    T read (id id);
    T update (T t);
    void delete (id id);
    List<T> getAll();
}

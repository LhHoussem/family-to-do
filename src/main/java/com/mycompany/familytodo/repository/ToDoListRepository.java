package com.mycompany.familytodo.repository;

import com.mycompany.familytodo.domain.ToDoList;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the ToDoList entity.
 */
@Repository
public interface ToDoListRepository extends MongoRepository<ToDoList, String> {
    @Query("{}")
    Page<ToDoList> findAllWithEagerRelationships(Pageable pageable);

    @Query("{}")
    List<ToDoList> findAllWithEagerRelationships();

    @Query("{'id': ?0}")
    Optional<ToDoList> findOneWithEagerRelationships(String id);
}

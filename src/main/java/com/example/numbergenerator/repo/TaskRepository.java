package com.example.numbergenerator.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.numbergenerator.model.Task;

@Repository
public interface TaskRepository extends CrudRepository<Task, Long>{

}

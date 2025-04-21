package com.example.todo.repositories;

import com.example.todo.entities.ToDoElement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ToDoElementRepository extends JpaRepository<ToDoElement, Long> {
}

package com.example.todo.repositories;

import com.example.todo.entities.ToDoElement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ToDoElementRepository extends JpaRepository<ToDoElement, Long> {
}
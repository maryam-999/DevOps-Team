package com.example.todo.controllers;

import com.example.todo.entities.*;
import com.example.todo.repositories.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class EditItemController {

    @Autowired
    ToDoListRepository toDoListRepository;

    @Autowired
    ToDoElementRepository toDoElementRepository;

    @GetMapping("/edit-list/{id}")
    ModelAndView editList(@PathVariable("id") Long listId) {
        ModelAndView modelAndView = new ModelAndView("edit-list-page");
        ToDoList list = toDoListRepository.findById(listId)
                .orElseThrow(() -> new IllegalArgumentException("ToDoList id: " + listId + " not found"));
        modelAndView.addObject("toDoList", list);
        return modelAndView;
    }

    @PostMapping("/edit-list-name/{id}")
    String editListName(@PathVariable("id") Long listId, @Valid ToDoList toDoList, BindingResult result, Model model) {
        ToDoList list = toDoListRepository.findById(listId)
                .orElseThrow(() -> new IllegalArgumentException("ToDoList id: " + listId + " not found"));
        list.setName(toDoList.getName());
        toDoListRepository.save(list);
        return "redirect:/";
    }

    @GetMapping("/delete-list/{id}")
    String deleteList(@PathVariable("id") Long listId) {
        ToDoList list = toDoListRepository.findById(listId)
                .orElseThrow(() -> new IllegalArgumentException("ToDoList id: " + listId + " not found"));
        toDoListRepository.delete(list);
        return "redirect:/";
    }

    @GetMapping("/delete-element/{id}")
    String deleteElement(@PathVariable("id") Long elementId) {
        ToDoElement element = toDoElementRepository.findById(elementId)
                .orElseThrow(() -> new IllegalArgumentException("ToDoElement id: " + elementId + " not found"));
        toDoElementRepository.delete(element);
        return "redirect:/";
    }


}
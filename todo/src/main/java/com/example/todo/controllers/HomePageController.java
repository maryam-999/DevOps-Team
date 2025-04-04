package com.example.todo.controllers;


import com.example.todo.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomePageController {

    @Autowired
    private ToDoListRepository toDoListRepository;

    @GetMapping("/")
    public ModelAndView startHomePage() {
        ModelAndView modelAndView = new ModelAndView("home");
        modelAndView.addObject("toDoLists", toDoListRepository.findAll());
        return modelAndView;
    }
}
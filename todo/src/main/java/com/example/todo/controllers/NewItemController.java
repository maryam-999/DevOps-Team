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
public class NewItemController {

    @Autowired
    private ToDoListRepository toDoListRepository;
    @Autowired
    private ToDoElementRepository toDoElementRepository;

    @GetMapping("/create-list")
    public ModelAndView showCreateListPage() {
        ModelAndView modelAndView = new ModelAndView("create-list-page");
        modelAndView.addObject("toDoList", new ToDoList());
        return modelAndView;
    }

    @PostMapping("/create-new-list")
    public String createListItem(@Valid ToDoList toDoList, BindingResult result, Model model) {

        ToDoList list = new ToDoList();
        list.setName(toDoList.getName());

        toDoListRepository.save(list);
        return "redirect:/";
    }

    @GetMapping("/create-element/{id}")
    public ModelAndView showCreateElementPage(@PathVariable("id") Long listId) {
        ModelAndView modelAndView = new ModelAndView("create-element-page");
        ToDoList toDoList = toDoListRepository.findById(listId)
                .orElseThrow(() -> new IllegalArgumentException("ToDoList id: " + listId + " not found"));
        ToDoElement toDoElement = new ToDoElement();
        toDoElement.setToDoList(toDoList);
        modelAndView.addObject("toDoList", toDoList);
        modelAndView.addObject("toDoElement", toDoElement);
        return modelAndView;
    }

    @PostMapping("/create-new-element/{id}")
    public String createElementItem(@PathVariable("id") Long listId, @Valid ToDoElement toDoElement, BindingResult result, Model model) {
        ToDoElement element = new ToDoElement();
        ToDoList toDoList = toDoListRepository.findById(listId)
                .orElseThrow(() -> new IllegalArgumentException("ToDoList id: " + listId + " not found"));
        element.setName(toDoElement.getName());
        element.setToDoList(toDoList);
        element.setIsDone(false);

        toDoElementRepository.save(element);
        return "redirect:/";
    }
}
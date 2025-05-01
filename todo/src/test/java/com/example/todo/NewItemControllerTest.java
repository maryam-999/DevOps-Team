package com.example.todo;
import com.example.todo.repositories.ToDoElementRepository;
import com.example.todo.repositories.ToDoListRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import com.example.todo.entities.ToDoList;
import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@AutoConfigureMockMvc
public class NewItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ToDoListRepository toDoListRepository;

    @Autowired
    private ToDoElementRepository toDoElementRepository;


    // Test d’intégration pour la création d'une nouvelle liste
    @Test
    public void testCreateNewList() throws Exception {
        // On simule l'ajout d'une liste via POST
        mockMvc.perform(post("/create-new-list")
                        .param("name", "Ma nouvelle liste"))
                .andExpect(status().is3xxRedirection()) // Redirection vers "/"
                .andExpect(redirectedUrl("/"));

        // Vérifie que la liste a bien été créée dans la BDD
        boolean exists = toDoListRepository.findAll()
                .stream()
                .anyMatch(list -> "Ma nouvelle liste".equals(list.getName()));

        assert exists;
    }

    @Test
    public void testCreateNewElement() throws Exception {
        // Étape 1 : Créer une liste
        ToDoList list = new ToDoList();
        list.setName("Liste test");
        list = toDoListRepository.save(list);
        Long finalListId = list.getId(); // Rendre l'ID final pour l'utiliser dans la lambda

        // Étape 2 : Ajouter un élément à la liste
        mockMvc.perform(post("/create-new-element/" + finalListId)
                        .param("name", "Nouvel élément"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        // Étape 3 : Vérifier que l'élément a bien été ajouté
        boolean exists = toDoElementRepository.findAll().stream()
                .anyMatch(element -> "Nouvel élément".equals(element.getName())
                        && element.getToDoList().getId().equals(finalListId));

        assertThat(exists).isTrue();
    }


    @AfterEach
    public void cleanUp() {
        toDoListRepository.deleteAll();
    }

}

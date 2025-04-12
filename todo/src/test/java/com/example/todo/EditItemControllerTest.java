package com.example.todo;

import com.example.todo.entities.ToDoList;
import com.example.todo.repositories.ToDoListRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class EditItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ToDoListRepository toDoListRepository;

    @AfterEach
    public void cleanUp() {
        toDoListRepository.deleteAll();
    }

    @Test
    public void testEditListName() throws Exception {
        // Étape 1 : Créer et sauvegarder une liste initiale
        ToDoList list = new ToDoList();
        list.setName("Ancien nom");
        list = toDoListRepository.save(list); // on récupère l'id

        // Étape 2 : Modifier le nom via une requête POST
        mockMvc.perform(post("/edit-list-name/" + list.getId())
                        .param("name", "Nouveau nom"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        // Étape 3 : Vérifier que le nom a bien été mis à jour
        ToDoList updatedList = toDoListRepository.findById(list.getId()).orElse(null);
        assertThat(updatedList).isNotNull();
        assertThat(updatedList.getName()).isEqualTo("Nouveau nom");
    }
}

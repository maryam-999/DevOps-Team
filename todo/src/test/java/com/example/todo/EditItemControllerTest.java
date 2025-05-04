package com.example.todo;

import com.example.todo.entities.ToDoElement;
import com.example.todo.entities.ToDoList;
import com.example.todo.repositories.ToDoElementRepository;
import com.example.todo.repositories.ToDoListRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class EditItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ToDoListRepository toDoListRepository;

    @Autowired
    private ToDoElementRepository toDoElementRepository;

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
    @Test
    public void testDeleteList() throws Exception {
        // Step 1: Create and save a list
        ToDoList list = new ToDoList();
        list.setName("List to delete");
        list = toDoListRepository.save(list); // Save and retrieve the ID

        // Step 2: Delete the list using the endpoint
        mockMvc.perform(get("/delete-list/" + list.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        // Step 3: Verify that the list has been deleted
        boolean exists = toDoListRepository.findById(list.getId()).isPresent();
        assertThat(exists).isFalse();
    }
    @Test
    public void testEditElement() throws Exception {
        // Étape 1 : Créer une liste et un élément initial
        ToDoList list = new ToDoList();
        list.setName("Liste test");
        list = toDoListRepository.save(list);

        ToDoElement element = new ToDoElement();
        element.setName("Élément initial");
        element.setToDoList(list);
        element = toDoElementRepository.save(element);
        Long elementId = element.getId();

        // Étape 2 : Modifier le nom de l'élément via le contrôleur
        String newName = "Élément modifié";
        mockMvc.perform(post("/edit-element-name/" + elementId)
                        .param("name", newName)) // Le paramètre doit correspondre au champ de ToDoElement
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        // Étape 3 : Vérifier que l'élément a bien été modifié
        ToDoElement updatedElement = toDoElementRepository.findById(elementId)
                .orElseThrow(() -> new IllegalArgumentException("Élément non trouvé"));

        assertThat(updatedElement.getName()).isEqualTo(newName);
        assertThat(updatedElement.getToDoList().getId()).isEqualTo(list.getId());
    }
}

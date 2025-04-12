package com.example.todo;
import com.example.todo.repositories.ToDoListRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class NewItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ToDoListRepository toDoListRepository;


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

    @AfterEach
    public void cleanUp() {
        toDoListRepository.deleteAll();
    }

}

package com.example.todolist.controller;

import com.example.todolist.model.Task;
import com.example.todolist.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class TaskControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TaskService taskService;

    @InjectMocks
    private TaskController taskController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public TaskControllerTest() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
        mockMvc = MockMvcBuilders.standaloneSetup(taskController).build();
    }

    @Test
    void testCreateTaskReturnsTaskWithId() throws Exception {
        // Simulated data for the test
        Task mockTask = new Task();
        mockTask.setId(1L);
        mockTask.setTitle("Comprar alimentos");
        mockTask.setDescription("Leche, pan y huevos");
        mockTask.setCompleted(false);

        // Configure mock service behavior
        when(taskService.createTask(org.mockito.ArgumentMatchers.any(Task.class))).thenReturn(mockTask);

        // Create JSON for the request (without including the ID)
        Task requestTask = new Task();
        requestTask.setTitle("Comprar alimentos");
        requestTask.setDescription("Leche, pan y huevos");
        requestTask.setCompleted(false);

        String taskJson = objectMapper.writeValueAsString(requestTask);

        // Perform POST request
        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taskJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.title").value("Comprar alimentos"))
                .andExpect(jsonPath("$.description").value("Leche, pan y huevos"))
                .andExpect(jsonPath("$.completed").value(false));
    }
}

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

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TaskControllerTest {
    private MockMvc mockMvc;

    @Mock
    private TaskService taskService;

    @InjectMocks
    private TaskController taskController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public TaskControllerTest() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(taskController)
                .alwaysDo(print()) // Agregamos esto para mejor debugging
                .build();
    }

    private Task createMockTask() {
        Task mockTask = new Task();
        mockTask.setId(1L);
        mockTask.setTitle("Comprar alimentos");
        mockTask.setDescription("Leche, pan y huevos");
        mockTask.setCompleted(false);
        return mockTask;
    }

    @Test
    void testCreateTaskReturnsTaskWithId() throws Exception {
        Task mockTask = createMockTask();
        when(taskService.createTask(any(Task.class))).thenReturn(mockTask);

        Task requestTask = new Task();
        requestTask.setTitle("Comprar alimentos");
        requestTask.setDescription("Leche, pan y huevos");
        requestTask.setCompleted(false);

        String taskJson = objectMapper.writeValueAsString(requestTask);

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taskJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Comprar alimentos"))
                .andExpect(jsonPath("$.description").value("Leche, pan y huevos"))
                .andExpect(jsonPath("$.completed").value(false));
    }

    @Test
    void testGetTaskById() throws Exception {
        Task mockTask = createMockTask();
        when(taskService.getTaskById(1L)).thenReturn(mockTask);

        mockMvc.perform(get("/api/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Comprar alimentos"))
                .andExpect(jsonPath("$.description").value("Leche, pan y huevos"))
                .andExpect(jsonPath("$.completed").value(false));
    }

    @Test
    void testGetAllTasks() throws Exception {
        List<Task> mockTasks = Arrays.asList(createMockTask());
        when(taskService.getAllTasks()).thenReturn(mockTasks);

        mockMvc.perform(get("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Comprar alimentos"))
                .andExpect(jsonPath("$[0].description").value("Leche, pan y huevos"))
                .andExpect(jsonPath("$[0].completed").value(false));
    }

    @Test
    void testUpdateTask() throws Exception {
        Task updatedMockTask = createMockTask();
        updatedMockTask.setTitle("Comprar alimentos actualizados");
        when(taskService.updateTask(eq(1L), any(Task.class))).thenReturn(updatedMockTask);

        Task requestTask = new Task();
        requestTask.setTitle("Comprar alimentos actualizados");
        requestTask.setDescription("Leche, pan y huevos");
        requestTask.setCompleted(false);

        String taskJson = objectMapper.writeValueAsString(requestTask);

        mockMvc.perform(put("/api/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taskJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Comprar alimentos actualizados"))
                .andExpect(jsonPath("$.description").value("Leche, pan y huevos"))
                .andExpect(jsonPath("$.completed").value(false));
    }

    @Test
    void testDeleteTask() throws Exception {
        doNothing().when(taskService).deleteTask(1L);

        mockMvc.perform(delete("/api/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent()); // Cambiado a 204 NO_CONTENT
    }


}
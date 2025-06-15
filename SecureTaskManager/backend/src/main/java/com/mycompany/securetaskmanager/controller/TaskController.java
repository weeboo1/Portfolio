package com.mycompany.securetaskmanager.controller;

import com.mycompany.securetaskmanager.audit.AuditService;
import com.mycompany.securetaskmanager.model.Task;
import com.mycompany.securetaskmanager.model.User;
import com.mycompany.securetaskmanager.repository.TaskRepository;
import com.mycompany.securetaskmanager.repository.UserRepository;
import com.mycompany.securetaskmanager.dto.TaskRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuditService auditService;

    // Получить все задачи пользователя
    @GetMapping
    public List<Task> getAllTasks() {
        String currentUsername = getCurrentUsername();
        return taskRepository.findByUserUsername(currentUsername);
    }

    // Создать задачу
    @PostMapping
    public ResponseEntity<?> createTask(@RequestBody TaskRequest request) {
        String currentUsername = getCurrentUsername();
        Optional<User> userOpt = userRepository.findByUsername(currentUsername);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setUser(userOpt.get());

        taskRepository.save(task);

        auditService.log("Пользователь " + currentUsername + " создал задачу: " + task.getTitle());

        return ResponseEntity.status(HttpStatus.CREATED).body(task);
    }

    // Обновить задачу
    @PutMapping("/{id}")
    public ResponseEntity<?> updateTask(@PathVariable Long id, @RequestBody TaskRequest request) {
        String currentUsername = getCurrentUsername();

        Optional<Task> taskOpt = taskRepository.findById(id);
        if (taskOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Task task = taskOpt.get();

        // Проверка владельца
        if (!task.getUser().getUsername().equals(currentUsername)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());

        taskRepository.save(task);

        auditService.log("Пользователь " + currentUsername + " обновил задачу ID " + id);

        return ResponseEntity.ok(task);
    }

    // Удалить задачу
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable Long id) {
        String currentUsername = getCurrentUsername();

        Optional<Task> taskOpt = taskRepository.findById(id);
        if (taskOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Task task = taskOpt.get();

        // Проверка владельца
        if (!task.getUser().getUsername().equals(currentUsername)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        taskRepository.delete(task);

        auditService.log("Пользователь " + currentUsername + " удалил задачу ID " + id);

        return ResponseEntity.noContent().build();
    }

    private String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }
}

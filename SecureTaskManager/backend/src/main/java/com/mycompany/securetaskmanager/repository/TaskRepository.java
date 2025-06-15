package com.mycompany.securetaskmanager.repository;

import com.mycompany.securetaskmanager.model.Task;
import com.mycompany.securetaskmanager.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByUser(User user);
    List<Task> findByUserUsername(String username);
}

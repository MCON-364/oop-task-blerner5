package edu.touro.las.mcon364.taskmanager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

class TaskManagerTest {
    private TaskRegistry registry;
    private TaskManager manager;

    @BeforeEach
    void setUp() {
        registry = new TaskRegistry();
        manager = new TaskManager(registry);
    }

    @Test
    @DisplayName("TaskManager.run() should execute AddTaskCommand")
    void testRunAddTaskCommand() {
        Task task = new Task("Test task", Priority.MEDIUM);
        Command command = new AddTaskCommand(registry, task);
        manager.run(command);
        assertTrue(registry.get("Test task").isPresent());
    }

    @Test
    @DisplayName("TaskManager.run() should execute RemoveTaskCommand")
    void testRunRemoveTaskCommand() {
        registry.add(new Task("Remove me", Priority.HIGH));
        Command command = new RemoveTaskCommand(registry, "Remove me");
        manager.run(command);
        assertTrue(registry.get("Remove me").isEmpty());
    }

    @Test
    @DisplayName("TaskManager.run() should execute UpdateTaskCommand")
    void testRunUpdateTaskCommand() {
        registry.add(new Task("Update me", Priority.LOW));
        Command command = new UpdateTaskCommand(registry, "Update me", Priority.HIGH);
        manager.run(command);
        assertEquals(Priority.HIGH, registry.get("Update me").orElseThrow().priority());
    }

    @Test
    @DisplayName("TaskManager.run() should handle multiple commands in sequence")
    void testRunMultipleCommands() {
        manager.run(new AddTaskCommand(registry, new Task("Task 1", Priority.HIGH)));
        manager.run(new AddTaskCommand(registry, new Task("Task 2", Priority.LOW)));
        manager.run(new UpdateTaskCommand(registry, "Task 2", Priority.MEDIUM));
        manager.run(new RemoveTaskCommand(registry, "Task 1"));
        assertTrue(registry.get("Task 1").isEmpty());
        assertTrue(registry.get("Task 2").isPresent());
        assertEquals(Priority.MEDIUM, registry.get("Task 2").orElseThrow().priority());
    }

    @Test
    @DisplayName("TaskManager should work with same registry instance")
    void testSharedRegistry() {
        Task task = new Task("Shared task", Priority.HIGH);
        manager.run(new AddTaskCommand(registry, task));
        assertTrue(registry.get("Shared task").isPresent());
    }
}
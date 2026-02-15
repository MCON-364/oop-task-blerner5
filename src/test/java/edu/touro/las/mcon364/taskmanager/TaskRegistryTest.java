package edu.touro.las.mcon364.taskmanager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

class TaskRegistryTest {
    private TaskRegistry registry;

    @BeforeEach
    void setUp() {
        registry = new TaskRegistry();
    }

    @Test
    @DisplayName("Adding a task should make it retrievable")
    void testAdd() {
        Task task = new Task("Test task", Priority.HIGH);
        registry.add(task);

        Task retrieved = registry.get("Test task").orElseThrow();
        assertEquals(task, retrieved);
    }

    @Test
    @DisplayName("Adding multiple tasks with same name should replace the old one")
    void testAddReplacement() {
        Task task1 = new Task("Test task", Priority.LOW);
        Task task2 = new Task("Test task", Priority.HIGH);

        registry.add(task1);
        registry.add(task2);

        Task retrieved = registry.get("Test task").orElseThrow();
        assertEquals(Priority.HIGH, retrieved.priority());
    }

    @Test
    @DisplayName("Getting non-existent task should return empty Optional")
    void testGetNonExistent() {
        assertTrue(registry.get("Non-existent").isEmpty());
    }

    @Test
    @DisplayName("Removing a task should make it no longer retrievable")
    void testRemove() {
        Task task = new Task("Test task", Priority.MEDIUM);
        registry.add(task);

        registry.remove("Test task");

        assertTrue(registry.get("Test task").isEmpty());
    }

    @Test
    @DisplayName("Removing non-existent task should not throw exception")
    void testRemoveNonExistent() {
        assertDoesNotThrow(() -> registry.remove("Non-existent"));
    }

    @Test
    @DisplayName("getAll should return all added tasks")
    void testGetAll() {
        Task task1 = new Task("Task 1", Priority.HIGH);
        Task task2 = new Task("Task 2", Priority.LOW);
        Task task3 = new Task("Task 3", Priority.MEDIUM);

        registry.add(task1);
        registry.add(task2);
        registry.add(task3);

        assertEquals(3, registry.getAll().size());
        assertTrue(registry.getAll().containsKey("Task 1"));
        assertTrue(registry.getAll().containsKey("Task 2"));
        assertTrue(registry.getAll().containsKey("Task 3"));
    }

    @Test
    @DisplayName("getAll on empty registry should return empty map")
    void testGetAllEmpty() {
        assertTrue(registry.getAll().isEmpty());
    }
}
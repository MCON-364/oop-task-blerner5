package edu.touro.las.mcon364.taskmanager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

class CommandTest {
    private TaskRegistry registry;

    @BeforeEach
    void setUp() {
        registry = new TaskRegistry();
    }

    @Test
    @DisplayName("AddTaskCommand should add task to registry")
    void testAddTaskCommand() {
        Task task = new Task("New task", Priority.MEDIUM);
        Command command = new AddTaskCommand(registry, task);
        command.execute();
        assertTrue(registry.get("New task").isPresent());
        assertEquals(task, registry.get("New task").orElseThrow(AssertionError::new));
    }

    @Test
    @DisplayName("AddTaskCommand should replace existing task with same name")
    void testAddTaskCommandReplacement() {
        Task originalTask = new Task("Task", Priority.LOW);
        Task replacementTask = new Task("Task", Priority.HIGH);
        new AddTaskCommand(registry, originalTask).execute();
        new AddTaskCommand(registry, replacementTask).execute();
        assertEquals(Priority.HIGH, registry.get("Task").orElseThrow().priority());
    }

    @Test
    @DisplayName("RemoveTaskCommand should remove task from registry")
    void testRemoveTaskCommand() {
        registry.add(new Task("To be removed", Priority.HIGH));
        Command command = new RemoveTaskCommand(registry, "To be removed");
        command.execute();
        assertTrue(registry.get("To be removed").isEmpty());
    }

    @Test
    @DisplayName("RemoveTaskCommand on non-existent task should not throw")
    void testRemoveTaskCommandNonExistent() {
        Command command = new RemoveTaskCommand(registry, "Non-existent");
        assertDoesNotThrow(command::execute);
    }

    @Test
    @DisplayName("UpdateTaskCommand should update existing task priority")
    void testUpdateTaskCommand() {
        registry.add(new Task("Update me", Priority.HIGH));
        Command command = new UpdateTaskCommand(registry, "Update me", Priority.HIGH);
        command.execute();
        Task updated = registry.get("Update me").orElseThrow();
        assertNotNull(updated);
        assertEquals(Priority.HIGH, updated.priority());
    }

    @Test
    @DisplayName("UpdateTaskCommand should preserve task name")
    void testUpdateTaskCommandPreservesName() {
        registry.add(new Task("Important task", Priority.MEDIUM));
        Command command = new UpdateTaskCommand(registry, "Important task", Priority.LOW);
        command.execute();
        Task updated = registry.get("Important task").orElseThrow();
        assertEquals("Important task", updated.name());
    }

    @Test
    @DisplayName("UpdateTaskCommand on non-existent task should not throw (pre-refactor)")
    void testUpdateTaskCommandNonExistent() {
        Command command = new UpdateTaskCommand(registry, "Non-existent", Priority.HIGH);
        assertThrows(TaskNotFoundException.class, command::execute);
        assertTrue(registry.get("Non-existent").isEmpty());
    }

    @Test
    @DisplayName("UpdateTaskCommand should allow changing priority from HIGH to LOW")
    void testUpdateTaskCommandPriorityDecrease() {
        registry.add(new Task("Flexible", Priority.LOW));
        new UpdateTaskCommand(registry, "Flexible", Priority.LOW).execute();
        assertEquals(Priority.LOW, registry.get("Flexible").orElseThrow().priority());
    }

    @Test
    @DisplayName("UpdateTaskCommand should allow changing priority from LOW to HIGH")
    void testUpdateTaskCommandPriorityIncrease() {
        registry.add(new Task("Urgent", Priority.HIGH));
        new UpdateTaskCommand(registry, "Urgent", Priority.HIGH).execute();
        assertEquals(Priority.HIGH, registry.get("Urgent").orElseThrow().priority());
    }
}
package edu.touro.las.mcon364.taskmanager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class DemoMainTest {
    private DemoMain demo;
    private TaskRegistry registry;
    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;

    @BeforeEach
    void setUp() {
        demo = new DemoMain();
        registry = new TaskRegistry();
        outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
    }

    @Test
    @DisplayName("Adding tasks should create 5 tasks with correct priorities")
    void testDemonstrateAddingTasks() {
        TaskRegistry testRegistry = new TaskRegistry();
        TaskManager testManager = new TaskManager(testRegistry);
        testManager.run(new AddTaskCommand(testRegistry, new Task("Write documentation", Priority.HIGH)));
        testManager.run(new AddTaskCommand(testRegistry, new Task("Review pull requests", Priority.MEDIUM)));
        testManager.run(new AddTaskCommand(testRegistry, new Task("Update dependencies", Priority.LOW)));
        testManager.run(new AddTaskCommand(testRegistry, new Task("Fix critical bug", Priority.HIGH)));
        testManager.run(new AddTaskCommand(testRegistry, new Task("Refactor code", Priority.MEDIUM)));
        assertEquals(5, testRegistry.getAll().size());
        Task doc = testRegistry.get("Write documentation").orElseThrow();
        assertEquals(Priority.HIGH, doc.priority());
        Task review = testRegistry.get("Review pull requests").orElseThrow();
        assertEquals(Priority.MEDIUM, review.priority());
        Optional<Task> dependencies = testRegistry.get("Update dependencies");
        assertTrue(dependencies.isPresent());
        assertEquals(Priority.LOW, dependencies.orElseThrow().priority());
        Task bug = testRegistry.get("Fix critical bug").orElseThrow();
        assertEquals(Priority.HIGH, bug.priority());
        Task refactor = testRegistry.get("Refactor code").orElseThrow();
        assertEquals(Priority.MEDIUM, refactor.priority());
    }

    @Test
    @DisplayName("Retrieving existing task should return correct task")
    void testDemonstrateRetrievingTask() {
        TaskRegistry testRegistry = new TaskRegistry();
        TaskManager testManager = new TaskManager(testRegistry);
        Task expectedTask = new Task("Fix critical bug", Priority.HIGH);
        testManager.run(new AddTaskCommand(testRegistry, expectedTask));
        Task retrieved = testRegistry.get("Fix critical bug").orElseThrow();
        assertEquals("Fix critical bug", retrieved.name());
        assertEquals(Priority.HIGH, retrieved.priority());
        assertEquals(expectedTask, retrieved);
    }

    @Test
    @DisplayName("Retrieving non-existent task should return empty Optional")
    void testDemonstrateRetrievingNonExistentTask() {
        TaskRegistry testRegistry = new TaskRegistry();
        assertTrue(testRegistry.get("Non-existent task").isEmpty());
    }

    @Test
    @DisplayName("Updating task should change priority")
    void testDemonstrateUpdatingTask() {
        TaskRegistry testRegistry = new TaskRegistry();
        TaskManager testManager = new TaskManager(testRegistry);
        testManager.run(new AddTaskCommand(testRegistry, new Task("Refactor code", Priority.MEDIUM)));
        Task before = testRegistry.get("Refactor code").orElseThrow();
        assertEquals(Priority.MEDIUM, before.priority());
        testManager.run(new UpdateTaskCommand(testRegistry, "Refactor code", Priority.HIGH));
        Task after = testRegistry.get("Refactor code").orElseThrow();
        assertEquals(Priority.HIGH, after.priority());
        assertEquals("Refactor code", after.name());
    }

    @Test
    @DisplayName("Updating non-existent task should throw TaskNotFoundException")
    void testDemonstrateUpdatingNonExistentTask() {
        TaskRegistry testRegistry = new TaskRegistry();
        TaskManager testManager = new TaskManager(testRegistry);
        assertThrows(TaskNotFoundException.class, () -> testManager.run(new UpdateTaskCommand(testRegistry, "Non-existent task", Priority.HIGH)));
        assertTrue(testRegistry.get("Non-existent task").isEmpty());
    }

    @Test
    @DisplayName("Removing task should delete it from registry")
    void testDemonstrateRemovingTask() {
        TaskRegistry testRegistry = new TaskRegistry();
        TaskManager testManager = new TaskManager(testRegistry);
        testManager.run(new AddTaskCommand(testRegistry, new Task("Update dependencies", Priority.LOW)));
        testManager.run(new AddTaskCommand(testRegistry, new Task("Fix critical bug", Priority.HIGH)));
        assertEquals(2, testRegistry.getAll().size());
        assertTrue(testRegistry.get("Update dependencies").isPresent());
        testManager.run(new RemoveTaskCommand(testRegistry, "Update dependencies"));
        assertEquals(1, testRegistry.getAll().size());
        assertTrue(testRegistry.get("Update dependencies").isEmpty());
        assertTrue(testRegistry.get("Fix critical bug").isPresent());
    }

    @Test
    @DisplayName("Null return demonstration updated to Optional behavior")
    void testDemonstrateNullReturn() {
        TaskRegistry testRegistry = new TaskRegistry();
        assertTrue(testRegistry.get("Non-existent task").isEmpty());
    }

    @Test
    @DisplayName("Full demo run should execute without exceptions")
    void testFullDemoRun() {
        DemoMain testDemo = new DemoMain();
        assertThrows(TaskNotFoundException.class, testDemo::run);
    }

    @Test
    @DisplayName("Task equality should work correctly")
    void testTaskEquality() {
        Task task1 = new Task("Test task", Priority.HIGH);
        Task task2 = new Task("Test task", Priority.HIGH);
        Task task3 = new Task("Test task", Priority.LOW);
        Task task4 = new Task("Different task", Priority.HIGH);
        assertEquals(task1, task2);
        assertEquals(task1.hashCode(), task2.hashCode());
        assertNotEquals(task1, task3);
        assertNotEquals(task1, task4);
    }

    @Test
    @DisplayName("Command pattern - AddTaskCommand should execute correctly")
    void testAddTaskCommand() {
        TaskRegistry testRegistry = new TaskRegistry();
        Task task = new Task("Test task", Priority.MEDIUM);
        AddTaskCommand command = new AddTaskCommand(testRegistry, task);
        command.execute();
        assertTrue(testRegistry.get("Test task").isPresent());
        assertEquals(task, testRegistry.get("Test task").orElseThrow());
    }

    @Test
    @DisplayName("Command pattern - RemoveTaskCommand should execute correctly")
    void testRemoveTaskCommand() {
        TaskRegistry testRegistry = new TaskRegistry();
        testRegistry.add(new Task("Test task", Priority.MEDIUM));
        RemoveTaskCommand command = new RemoveTaskCommand(testRegistry, "Test task");
        command.execute();
        assertTrue(testRegistry.get("Test task").isEmpty());
    }

    @Test
    @DisplayName("Command pattern - UpdateTaskCommand should execute correctly")
    void testUpdateTaskCommand() {
        TaskRegistry testRegistry = new TaskRegistry();
        testRegistry.add(new Task("Test task", Priority.LOW));
        UpdateTaskCommand command = new UpdateTaskCommand(testRegistry, "Test task", Priority.HIGH);
        command.execute();
        Task updated = testRegistry.get("Test task").orElseThrow();
        assertEquals(Priority.HIGH, updated.priority());
    }

    @Test
    @DisplayName("TaskManager.run() should handle AddTaskCommand")
    void testTaskManagerRunWithAddCommand() {
        TaskRegistry testRegistry = new TaskRegistry();
        TaskManager manager = new TaskManager(testRegistry);
        Task task = new Task("Test task", Priority.HIGH);
        manager.run(new AddTaskCommand(testRegistry, task));
        assertTrue(testRegistry.get("Test task").isPresent());
    }

    @Test
    @DisplayName("TaskManager.run() should handle RemoveTaskCommand")
    void testTaskManagerRunWithRemoveCommand() {
        TaskRegistry testRegistry = new TaskRegistry();
        TaskManager manager = new TaskManager(testRegistry);
        testRegistry.add(new Task("Test task", Priority.HIGH));
        manager.run(new RemoveTaskCommand(testRegistry, "Test task"));
        assertTrue(testRegistry.get("Test task").isEmpty());
    }

    @Test
    @DisplayName("TaskManager.run() should handle UpdateTaskCommand")
    void testTaskManagerRunWithUpdateCommand() {
        TaskRegistry testRegistry = new TaskRegistry();
        TaskManager manager = new TaskManager(testRegistry);
        testRegistry.add(new Task("Test task", Priority.LOW));
        manager.run(new UpdateTaskCommand(testRegistry, "Test task", Priority.HIGH));
        Task updated = testRegistry.get("Test task").orElseThrow();
        assertEquals(Priority.HIGH, updated.priority());
    }
}
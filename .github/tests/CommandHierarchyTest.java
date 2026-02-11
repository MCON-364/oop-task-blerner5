package edu.touro.las.mcon364.taskmanager;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class CommandHierarchyTest {

    @Test
    void knownCommandsMustBePermitted() {
        Set<String> permitted =
                Arrays.stream(Command.class.ermittedSubclasses())
                        .map(Class::getSimpleName)
                        .collect(Collectors.toSet());

        assertTrue(permitted.contains("AddTaskCommand"),
                "AddTaskCommand must be permitted by Command");
        assertTrue(permitted.contains("RemoveTaskCommand"),
                "RemoveTaskCommand must be permitted by Command");
        assertTrue(permitted.contains("UpdateTaskCommand"),
                "UpdateTaskCommand must be permitted by Command");
    }
}


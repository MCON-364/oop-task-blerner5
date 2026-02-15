package edu.touro.las.mcon364.taskmanager;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommandSealedTest {

    @Test
    void commandMustBeSealed() {
        assertTrue(
                Command.class.isSealed(),
                "Command must be declared as a sealed interface"
        );
    }

    @Test
    void commandHasPermittedSubclasses() {
        Class<?>[] permitted = Command.class.ermittedSubclasses();

        assertNotNull(permitted, "Sealed Command must declare permitted subclasses");
        assertTrue(permitted.length >= 3,
                "Command must explicitly permit known command implementations");
    }
}

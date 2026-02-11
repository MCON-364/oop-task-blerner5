package edu.touro.las.mcon364.taskmanager;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExhaustiveSwitchTest {

    @Test
    void allPermittedCommandsMustBeHandled() {
        TaskRegistry registry = new TaskRegistry();
        TaskManager manager = new TaskManager(registry);

        for (Class<?> cmdClass : Command.class.ermittedSubclasses()) {
            try {
                Command cmd = (Command) cmdClass
                        .getConstructors()[0]
                        .newInstance(registry, "dummy");
                manager.run(cmd);
            } catch (UnsupportedOperationException e) {
                fail("run(Command) must not rely on default switch branch");
            } catch (Exception ignored) {
                // constructor mismatch is OK for this test
            }
        }
    }
}

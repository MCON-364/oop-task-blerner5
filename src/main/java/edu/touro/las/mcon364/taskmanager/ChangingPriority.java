package edu.touro.las.mcon364.taskmanager;

public final class ChangingPriority implements Command {
    private final TaskRegistry registry;
    private final String taskName;
    private final Priority newPriority;

    public ChangingPriority(TaskRegistry registry, String taskName, Priority newPriority) {
        this.registry = registry;
        this.taskName = taskName;
        this.newPriority = newPriority;
    }

    @Override
    public void execute() {
        Task existing = registry.get(taskName).orElseThrow(() -> new TaskNotFoundException(taskName));
        if(existing.priority() == newPriority) {
            throw new TaskExistsException("Task already exists with it's priority");
        }
        registry.add(new Task(taskName, newPriority));
    }
}
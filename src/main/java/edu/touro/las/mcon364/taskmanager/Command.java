package edu.touro.las.mcon364.taskmanager;

public  sealed interface Command  permits AddTaskCommand, RemoveTaskCommand, UpdateTaskCommand, ChangingPriority {
    void execute();
}

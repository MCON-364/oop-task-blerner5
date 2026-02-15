package edu.touro.las.mcon364.taskmanager;

public class TaskExistsException extends RuntimeException{
    public TaskExistsException(String message){
        super(message);
    }
}

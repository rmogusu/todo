package models;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Task {

    private String description;
    private static ArrayList<Task> instances = new ArrayList<>();
    private boolean completed;
    private LocalDateTime createdAt;
    private int id;

    public Task(String description){
        this.description = description;
        this.completed = false;
        this.createdAt = LocalDateTime.now();
        instances.add(this);
        this.id = instances.size();
    }

    public String getDescription() {
        return description;
    }

    public static ArrayList<Task> getAll(){
        return instances;
    }

    public static void clearAllTasks(){
        instances.clear();
    }

    public boolean getCompleted(){
        return this.completed;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public int getId() {
        return id;
    }

    public static Task findById(int id){
        return instances.get(id-1); //why minus 1? See if you can figure it out.
    }

    public void update(String content) {
        this.description = content;
    }

    public void deleteTask(){
        instances.remove(id-1); //same reason
    }
}
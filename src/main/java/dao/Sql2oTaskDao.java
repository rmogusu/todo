package dao;

import models.Task;
import org.sql2o.*;
import java.util.List;

public class Sql2oTaskDao implements TaskDao { //implementing our interface

    private final Sql2o sql2o;

    public Sql2oTaskDao(Sql2o sql2o){
        this.sql2o = sql2o; //making the sql2o object available everywhere so we can call methods in it
    }

    @Override
    public void add(Task task) {
        String sql = "INSERT INTO tasks (description) VALUES (:description)"; //raw sql
        try(Connection con = sql2o.open()){ //try to open a connection
            int id = (int) con.createQuery(sql, true) //make a new variable
                    .bind(task) //map my argument onto the query so we can use information from it
                    .executeUpdate() //run it all
                    .getKey(); //int id is now the row number (row “key”) of db
            task.setId(id); //update object to set id now from database
        } catch (Sql2oException ex) {
            System.out.println(ex); //oops we have an error!
        }
    }

    @Override
    public List<Task> getAll() {
        try(Connection con = sql2o.open()){
            return con.createQuery("SELECT * FROM tasks") //raw sql
                    .executeAndFetch(Task.class); //fetch a list
        }
    }

    @Override
    public Task findById(int id) {
        try(Connection con = sql2o.open()){
            return con.createQuery("SELECT * FROM tasks WHERE id = :id")
                    .addParameter("id", id) //key/value pair, key must match above
                    .executeAndFetchFirst(Task.class); //fetch an individual item
        }
    }
}
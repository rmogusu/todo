package dao;

import models.Category;
import models.Task;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;
import java.util.List;

public class Sql2oCategoryDao implements CategoryDao {

    private final Sql2o sql2o;

    public Sql2oCategoryDao(Sql2o sql2o){
        this.sql2o = sql2o;
    }



    @Override
    public void add(Category category) {
        String sql = "INSERT INTO categories (name) VALUES (:name)";
        try(Connection con = sql2o.open()){
            int id = (int) con.createQuery(sql, true)
                    .bind(category)
                    .executeUpdate()
                    .getKey();
            category.setId(id);
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }
    }

    @Override
    public List<Category> getAll() {
        try(Connection con = sql2o.open()){
            return con.createQuery("SELECT * FROM categories")
                    .executeAndFetch(Category.class);
        }
    }

    @Override
    public Category findById(int id) {
        try(Connection con = sql2o.open()){
            return con.createQuery("SELECT * FROM categories WHERE id = :id")
                    .addParameter("id", id)
                    .executeAndFetchFirst(Category.class);
        }
    }

    @Override
    public void update(int id, String newName){
        String sql = "UPDATE categories SET name = :name WHERE id=:id";
        try(Connection con = sql2o.open()){
            con.createQuery(sql)
                    .addParameter("name", newName)
                    .addParameter("id", id)
                    .executeUpdate();
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }
    }

    @Override
    public void deleteById(int id) {
        String sql = "DELETE from categories WHERE id=:id"; //raw sql
        try (Connection con = sql2o.open()) {
            con.createQuery(sql)
                    .addParameter("id", id)
                    .executeUpdate();
        } catch (Sql2oException ex){
            System.out.println(ex);
        }
    }

    @Override
    public void clearAllCategories() {
        String sql = "DELETE from categories"; //raw sql
        try (Connection con = sql2o.open()) {
            con.createQuery(sql)
                    .executeUpdate();
        } catch (Sql2oException ex){
            System.out.println(ex);
        }
    }

    @Override
    public List<Task> getAllTasksByCategory(int categoryId) {
        try(Connection con = sql2o.open()){
            return con.createQuery("SELECT * FROM tasks WHERE categoryId = :categoryId")
                    .addParameter("categoryId", categoryId)
                    .executeAndFetch(Task.class);
        }
    }
}
package dao;

import models.Category;
import models.Task;
import org.junit.*;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class Sql2oCategoryDaoTest {
    private static Sql2oCategoryDao categoryDao; //these variables are now static.
    private static Sql2oTaskDao taskDao; //these variables are now static.
    private static Connection conn; //these variables are now static.

    @BeforeClass //changed to @BeforeClass (run once before running any tests in this file)
    public static void setUp() throws Exception { //changed to static
     String connectionString = "jdbc:postgresql://ec2-34-233-186-251.compute-1.amazonaws.com:5432/dbgll9r0ej4bu0";
      Sql2o sql2o = new Sql2o(connectionString, "aynkrphzycrnur", "7bc55376845b26f63479282896ffd8a6b7394a399e38b6edd99d7f9c52d4ae39");
//        String connectionString = "jdbc:postgresql://localhost:5432/todolist_test";
//      Sql2o sql2o = new Sql2o(connectionString, "rose", "wambua");
        categoryDao = new Sql2oCategoryDao(sql2o);
        taskDao = new Sql2oTaskDao(sql2o);
        conn = sql2o.open(); // open connection once before this test file is run
    }

    @After // run after every test
    public void tearDown() throws Exception { //I have changed
        System.out.println("clearing database");
        categoryDao.clearAllCategories(); // clear all categories after every test
        taskDao.clearAllTasks(); // clear all tasks after every test
    }

    @AfterClass // changed to @AfterClass (run once after all tests in this file completed)
    public static void shutDown() throws Exception { //changed to static and shutDown
        conn.close(); // close connection once after this entire test file is finished
        System.out.println("connection closed");
    }

    @Test
    public void addingCategorySetsId() throws Exception {
        Category category = setupNewCategory();
        int originalCategoryId = category.getId();
        categoryDao.add(category);
        assertNotEquals(originalCategoryId, category.getId());
    }

    @Test
    public void existingCategoriesCanBeFoundById() throws Exception {
        Category category = setupNewCategory();
        categoryDao.add(category);
        Category foundCategory = categoryDao.findById(category.getId());
        assertEquals(category, foundCategory);
    }

    @Test
    public void addedCategoriesAreReturnedFromGetAll() throws Exception {
        Category category = setupNewCategory();
        categoryDao.add(category);
        assertEquals(1, categoryDao.getAll().size());
    }

    @Test
    public void noCategoriesReturnsEmptyList() throws Exception {
        assertEquals(0, categoryDao.getAll().size());
    }

    @Test
    public void updateChangesCategoryContent() throws Exception {
        String initialDescription = "Yardwork";
        Category category = new Category (initialDescription);
        categoryDao.add(category);
        categoryDao.update(category.getId(),"Cleaning");
        Category updatedCategory = categoryDao.findById(category.getId());
        assertNotEquals(initialDescription, updatedCategory.getName());
    }

    @Test
    public void deleteByIdDeletesCorrectCategory() throws Exception {
        Category category = setupNewCategory();
        categoryDao.add(category);
        categoryDao.deleteById(category.getId());
        assertEquals(0, categoryDao.getAll().size());
    }

    @Test
    public void clearAllClearsAllCategories() throws Exception {
        Category category = setupNewCategory();
        Category otherCategory = new Category("Cleaning");
        categoryDao.add(category);
        categoryDao.add(otherCategory);
        int daoSize = categoryDao.getAll().size();
        categoryDao.clearAllCategories();
        assertTrue(daoSize > 0 && daoSize > categoryDao.getAll().size());
    }

    @Test
    public void getAllTasksByCategoryReturnsTasksCorrectly() throws Exception {
        Category category = setupNewCategory();
        categoryDao.add(category);
        int categoryId = category.getId();
        Task newTask = new Task("mow the lawn", categoryId);
        Task otherTask = new Task("pull weeds", categoryId);
        Task thirdTask = new Task("trim hedge", categoryId);
        taskDao.add(newTask);
        taskDao.add(otherTask); //we are not adding task 3 so we can test things precisely.
        assertEquals(2, categoryDao.getAllTasksByCategory(categoryId).size());
        assertTrue(categoryDao.getAllTasksByCategory(categoryId).contains(newTask));
        assertTrue(categoryDao.getAllTasksByCategory(categoryId).contains(otherTask));
        assertFalse(categoryDao.getAllTasksByCategory(categoryId).contains(thirdTask)); //things are accurate!
    }

    // helper method
    public Category setupNewCategory(){
        return new Category("Yardwork");
    }
}
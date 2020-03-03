package dao;

import models.Task;
import org.sql2o.*;
import org.junit.*;
import static org.junit.Assert.*;

public class Sql2oTaskDaoTest {
    private static Sql2oTaskDao taskDao;                //these variables are now static.
    private static Connection conn;                     //these variables are now static.

    @BeforeClass
    public static void setUp() throws Exception {
        String connectionString = "jdbc:postgresql://localhost:5432/todolist_test"; // connect to postgres test database
        Sql2o sql2o = new Sql2o(connectionString, "rose", "wambua");         // changed user and pass to null
        taskDao = new Sql2oTaskDao(sql2o);
        conn = sql2o.open();                                                        // open connection once before this test file is run
    }

    @After
    public void tearDown() throws Exception {
        System.out.println("clearing database");
        taskDao.clearAllTasks();

    }

    @AfterClass
    public static void shutDown() throws Exception{
        conn.close();
        System.out.println("connection closed");
    }


    @Test
    public void addingTaskSetsId() throws Exception {
        Task task = setupNewTask();
        int originalTaskId = task.getId();
        taskDao.add(task);
        assertNotEquals(originalTaskId, task.getId()); //how does this work?
    }

    @Test
    public void existingTasksCanBeFoundById() throws Exception {
        Task task = setupNewTask();
        taskDao.add(task); //add to dao (takes care of saving)
        Task foundTask = taskDao.findById(task.getId()); //retrieve
        assertEquals(task, foundTask); //should be the same
    }

    @Test
    public void addedTasksAreReturnedFromgetAll() throws Exception {
        Task task = setupNewTask();
        taskDao.add(task);
        assertEquals(1, taskDao.getAll().size());
    }

    @Test
    public void noTasksReturnsEmptyList() throws Exception {
        assertEquals(0, taskDao.getAll().size());
    }

    @Test
    public void updateChangesTaskContent() throws Exception {
        String initialDescription = "mow the lawn";
        Task task = setupNewTask();
        taskDao.add(task);

        taskDao.update(task.getId(),"brush the cat",1);
        Task updatedTask = taskDao.findById(task.getId()); //why do I need to refind this?
        assertNotEquals(initialDescription, updatedTask.getDescription());
    }

    @Test
    public void deleteByIdDeletesCorrectTask() throws Exception {
        Task task = setupNewTask();
        taskDao.add(task);
        taskDao.deleteById(task.getId());
        assertEquals(0, taskDao.getAll().size());
    }

    @Test
    public void clearAllClearsAll() throws Exception {
        Task task = setupNewTask();
        Task otherTask = new Task("brush the cat", 2);
        taskDao.add(task);
        taskDao.add(otherTask);
        int daoSize = taskDao.getAll().size();
        taskDao.clearAllTasks();
        assertTrue(daoSize > 0 && daoSize > taskDao.getAll().size()); //this is a little overcomplicated, but illustrates well how we might use `assertTrue` in a different way.
    }

    @Test
    public void categoryIdIsReturnedCorrectly() throws Exception {
        Task task = setupNewTask();
        int originalCatId = task.getCategoryId();
        taskDao.add(task);
        assertEquals(originalCatId, taskDao.findById(task.getId()).getCategoryId());
    }

    //define the following once and then call it as above in your tests.
    public Task setupNewTask(){
        return new Task("Mow the lawn", 1);
    }
}
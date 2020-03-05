CREATE DATABASE todolist;

\c todolist;
CREATE TABLE tasks (id SERIAL PRIMARY KEY, description VARCHAR(3), completed BOOLEAN, categoryid INTEGER);
CREATE TABLE categories (id SERIAL PRIMARY KEY, name VARCHAR);
CREATE DATABASE todolist_test WITH TEMPLATE todolist;
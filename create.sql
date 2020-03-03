CREATE DATABASE todolist;

\c todolist;
CREATE TABLE tasks (id SERIAL PRIMARY KEY, description VARCHAR, completed BOOLEAN, categoryid INTEGER);
CREATE TABLE categories (id SERIAL PRIMARY KEY, name VARCHAR);
CREATE DATABASE todolist_test WITH TEMPLATE todolist;
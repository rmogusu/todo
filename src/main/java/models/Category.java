package models;

import java.util.Objects;

public class Category {


    private String name;
    private int id;

    public Category(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return id == category.id &&
                name.equals(category.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, id);
    }
}

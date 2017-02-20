package ro.ubbcluj.cs.exam.domain;

import io.realm.RealmObject;

public class Item extends RealmObject {
    private int id;
    private String name;
    private int quantity;
    private String status;

    public Item() {
    }

    public Item(int id, String name, int quantity, String status) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}

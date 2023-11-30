package Entites;

import java.io.Serializable;

public class Book implements Serializable {

    private int id;
    private String name;
    private String author;
    private String genre;
    private int amount;

    public Book() {
    }

    public Book(int id, String name, String author, String genre, int amount) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.genre = genre;
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}

package model;

public class Book {
    private int id;
    private String book;
    private int number;

    // ★ 追加
    private boolean alreadyLent;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getBook() {
        return book;
    }
    public void setBook(String book) {
        this.book = book;
    }

    public int getNumber() {
        return number;
    }
    public void setNumber(int number) {
        this.number = number;
    }

    // ★ 追加
    public boolean isAlreadyLent() {
        return alreadyLent;
    }
    public void setAlreadyLent(boolean alreadyLent) {
        this.alreadyLent = alreadyLent;
    }
}
package com.example.kamhi.ex13.data;

/**
 * Created by Kamhi on 19/5/2017.
 */

public class Item {
    private long id;
    private int number;
    private int color;

    public Item(long id, int number, int color) {
        this.id = id;
        this.number = number;
        this.color = color;
    }

    public Item(int number, int color) {
        this(-1,number,color);
    }

    public long getId() {
        return id;
    }

    public int getNumber() {
        return number;
    }

    public int getColor() {
        return color;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setColor(int color) {
        this.color = color;
    }

}

package com.codecool.Model.MobData;

import com.codecool.Model.Board;
import com.codecool.Model.Point;

public abstract class MobData {

    Board board;
    private int energy = 100;
    private Point position;
    int health = 3;
    int speed = 10;
    int damage = 1;
    private String breed;
    String[] foodList;
    private String asText;

    public MobData(Point position, String breed, Board board, String text) {
        this.position = position;
        this.breed = breed;
        this.board = board;
        this.asText = text;
    }

    public String getAsText() {
        return asText;
    }

    public void dealDamage(int damage) {
        this.health -= damage;
    }

    public void decreaseEnergy(int value) {
        energy -= value;
    }

    public void increaseEnergy(int value) {
        energy += value;
    }

    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String[] getFoodList() {
        return foodList;
    }

    public void setFoodList(String[] foodList) {
        this.foodList = foodList;
    }

    public Board getBoard() {
        return board;
    }
}

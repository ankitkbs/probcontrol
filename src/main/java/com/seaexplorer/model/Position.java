package com.seaexplorer.model;

public class Position {
    private int x;
    private int y;
    private Direction direction;

    public Position(int x, int y, Direction direction) {
        this.x = x;
        this.y = y;
        this.direction = direction;
    }

    public void moveForward() {
        switch (direction) {
            case NORTH -> y++;
            case SOUTH -> y--;
            case EAST -> x++;
            case WEST -> x--;
        }
    }

    public void turnLeft() {
        this.direction = this.direction.turnLeft();
    }

    public void turnRight() {
        this.direction = this.direction.turnRight();
    }

    public void moveBackward() {
        switch (direction) {
            case NORTH -> y--;
            case SOUTH -> y++;
            case EAST -> x--;
            case WEST -> x++;
        }
    }


    public int getX() { return x; }
    public int getY() { return y; }
    public Direction getDirection() { return direction; }
}

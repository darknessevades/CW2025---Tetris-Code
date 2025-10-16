package com.comp2042;

import com.comp2042.logic.bricks.Brick;

// Each tetris piece can have 4 rotation positions, this class checks for the piece's current rotation.

public class BrickRotator {

    private Brick brick;
    private int currentShape = 0; // default rotation for the tetris piece

    public NextShapeInfo getNextShape() {
        int nextShape = (currentShape + 1) % brick.getShapeMatrix().size();
        return new NextShapeInfo(brick.getShapeMatrix().get(nextShape), nextShape);
    }

    public int[][] getCurrentShape() {
        return brick.getShapeMatrix().get(currentShape);
    } // get the current rotation of brick

    public void setCurrentShape(int currentShape) {
        this.currentShape = currentShape;
    } // update the rotation index if confirmed to be valid

    public void setBrick(Brick brick) {
        this.brick = brick;
        currentShape = 0;
    } // set a new piece, reset the rotation index to default


}

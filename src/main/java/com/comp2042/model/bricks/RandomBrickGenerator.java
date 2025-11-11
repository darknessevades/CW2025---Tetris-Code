package com.comp2042.model.bricks;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Generates random Tetris bricks with a look-ahead buffer.
 * Implements the BrickGenerator interface to provide random brick selection.
 */
public class RandomBrickGenerator implements BrickGenerator {

    private static final int INITIAL_BUFFER_SIZE = 2;
    private static final int MIN_BUFFER_SIZE = 1;

    private final List<Brick> brickList;
    private final Deque<Brick> nextBricks;

    /**
     * Creates a new RandomBrickGenerator and initializes the brick pool.
     * Fills the buffer with initial random bricks.
     */
    public RandomBrickGenerator() {
        this.brickList = initializeBrickPool();
        this.nextBricks = new ArrayDeque<>();
        fillBuffer(INITIAL_BUFFER_SIZE);
    }

    @Override
    public Brick getBrick() {
        ensureBufferSize();
        return nextBricks.poll();
    }

    @Override
    public Brick getNextBrick() {
        return nextBricks.peek();
    }

    /**
     * Initializes the pool of all available brick types.
     *
     * @return a list containing one instance of each brick type.
     */
    private List<Brick> initializeBrickPool() {
        List<Brick> pool = new ArrayList<>();
        pool.add(new IBrick());
        pool.add(new JBrick());
        pool.add(new LBrick());
        pool.add(new OBrick());
        pool.add(new SBrick());
        pool.add(new TBrick());
        pool.add(new ZBrick());
        return pool;
    }

    /**
     * Fills the buffer with a specified number of random bricks.
     *
     * @param count the number of bricks to add to the buffer.
     */
    private void fillBuffer(int count) {
        for (int i = 0; i < count; i++) {
            nextBricks.add(getRandomBrick());
        }
    }

    /**
     * Ensures the buffer maintains the minimum required size.
     * Adds a new brick if the buffer is too small.
     */
    private void ensureBufferSize() {
        if (nextBricks.size() <= MIN_BUFFER_SIZE) {
            nextBricks.add(getRandomBrick());
        }
    }

    /**
     * Selects a random brick from the brick pool.
     *
     * @return a randomly selected brick.
     */
    private Brick getRandomBrick() {
        int randomIndex = ThreadLocalRandom.current().nextInt(brickList.size());
        return brickList.get(randomIndex);
    }
}
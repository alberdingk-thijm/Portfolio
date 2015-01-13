package com.wheel.tim.settlementgenerator;

/**
 * Created by Tim on 12/01/2015.
 */
public interface Rollable {

    /**
     * Return a randomly-generated number between two values as though it were produced
     * by a polyhedral die.
     * @return an integer
     */
    public int roll();
}

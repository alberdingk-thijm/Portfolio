package com.wheel.tim.settlementgenerator;

import java.util.Random;

/**
 * Created by Tim on 08/01/2015.
 */
public enum SettleEnum implements Rollable{
    BURG (10, 100),
    TOWN (100, 500),
    CITY (500, 1500),
    CAPITAL (1500, 10000);

    private final int minPop;
    private final int maxPop;

    SettleEnum(int minPop, int maxPop) {
        this.minPop = minPop;
        this.maxPop = maxPop;
    }

    private final int numRolls = 10;

    public int getMinPop() {
        return minPop;
    }

    public int getMaxPop() {
        return maxPop;
    }

    public int roll() {
        return new Random().nextInt(maxPop +1) + minPop;
    }

    public int popRoll() {
        int pop = 0;
        for (int i=0; i <= numRolls; i++ ) {
            pop += roll();
        }
        return pop;
    }
}

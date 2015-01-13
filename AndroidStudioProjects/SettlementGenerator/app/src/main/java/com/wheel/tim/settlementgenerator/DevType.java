package com.wheel.tim.settlementgenerator;

/**
 * Created by Tim on 12/01/2015.
 */
public enum DevType {
    BURG (4),
    TOWN (5),
    CITY (6),
    CAPITAL (6);

    private final int maxRoll;

    DevType(int maxRoll) {
        this.maxRoll = maxRoll;
    }

    private final int range = 4;
}

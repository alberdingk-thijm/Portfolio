package com.wheel.tim.settlementgenerator;

import java.util.ArrayList;

/**
 * Created by Tim on 12/01/2015.
 */
public class DevelopmentList<T> extends ArrayList {

    private SettleEnum type;

    public DevelopmentList(SettleEnum type) {
        super();
        this.type = type;
    }

    @Override
    public boolean add(Object object) {
        return super.add(object);
    }
}

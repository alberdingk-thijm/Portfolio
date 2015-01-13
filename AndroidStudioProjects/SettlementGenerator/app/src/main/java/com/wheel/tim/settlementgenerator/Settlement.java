package com.wheel.tim.settlementgenerator;

/**
 * Created by Tim on 08/01/2015.
 * A Settlement object stores the basic information about a settlement's SettleEnum (enum) and
 * population (int).
 */
public class Settlement implements SettlementType{

    private SettleEnum type;
    private int pop;
    private DevelopmentList<Development> developments;

    /**
     * Constructor for a new Settlement object. A Settlement has a type (burg, town, city or
     * capital) and generates a population number based on its type. It can also store a number
     * of Development objects.
     * @param type
     */
    public Settlement(SettleEnum type) {
        this.type = type;
        this.pop = type.popRoll();
        this.developments = new DevelopmentList<>(type);
    }

    /**
     * Add a Development to the Settlement.
     * @param dev
     * @return void
     */
    public void addDevelopment(Development dev) {
        this.developments.add(dev);
    }

    public SettleEnum getType() {
        return type;
    }

    public int getPop() {
        return pop;
    }

    public static void main(String[] args) {
        System.out.println(new Settlement(SettleEnum.valueOf("CITY")).getPop());
    }
}

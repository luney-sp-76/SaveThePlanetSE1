package com.savetheplanet.Main;

import java.util.Arrays;

public class FundableSquare extends Square {

    private int fieldSize;
    private int cost;
    private int devCost;
    private int devLevel;
    private int[] ratesCosts;
    private Player owner;
    private int value;

    public FundableSquare(String name, int field, String[] data) {
        super(name, field);
        setFieldSize(Integer.parseInt(data[2]));
        setCost(Integer.parseInt(data[3]));
        setDevCost(Integer.parseInt(data[4]));
        setValue(Integer.parseInt(data[5]));
        setRatesCosts(Arrays.stream(data[6].split("\\D")).mapToInt(Integer::parseInt).toArray());
        setOwner(null);
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public int getDevCost() {
        if (this.getDevLevel() >= 3) {
            return (int) (Math.round((devCost * 2.4) / 100) * 100);
        } else {
            return this.devCost;
        }
    }

    public int getValue() {

        if (devLevel < 4)
            return value + this.devLevel;

        return (value + this.devLevel) + 1;


    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setDevCost(int devCost) {
        this.devCost = devCost;
    }

    public int getDevLevel() {
        return devLevel;
    }

    public void setDevLevel(int devLevel) {


        this.devLevel = devLevel;
    }

    public int[] getRatesCosts() {
        return ratesCosts;
    }

    public void setRatesCosts(int[] ratesCosts) {
        this.ratesCosts = ratesCosts;
    }

    public int getRatesBill() {
        return ratesCosts[getDevLevel()];
    }

    public int getFieldSize() {
        return fieldSize;
    }

    public void setFieldSize(int fieldSize) {
        this.fieldSize = fieldSize;
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    @Override
    public String toString() {
        return "FundableSquare{" +
                super.toString() +
                "fieldSize=" + fieldSize +
                ", cost=" + cost +
                ", devCost=" + devCost +
                ", devLevel=" + devLevel +
                ", rates=" + getRatesBill() +
                ", owner='" + owner + '\'' +
                "} ";
    }
}

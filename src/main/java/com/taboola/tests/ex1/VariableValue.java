package com.taboola.tests.ex1;

import java.util.HashMap;

/**
 * Created by michael on 5/29/18.
 */
public class VariableValue implements Value {
    private String name;
    private double value = 0.0;

    public VariableValue(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }


    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public void incrementBy(int i) {
        value += i;
    }

    public void decrementBy(int i) {
        value -= i;
    }

    @Override
    public String toString() {
        return name  + "=" + value;
    }
}

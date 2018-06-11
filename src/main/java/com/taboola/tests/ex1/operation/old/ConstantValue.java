package com.taboola.tests.ex1.operation.old;

import com.taboola.tests.ex1.Value;

/**
 * Created by michael on 5/29/18.
 */
public class ConstantValue implements Value {

    private final double value;

    public ConstantValue(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double val) {
        throw new UnsupportedOperationException();
    }

    public void incrementBy(int i) {
        throw new UnsupportedOperationException();
    }

    public void decrementBy(int i) {
        throw new UnsupportedOperationException();
    }
}

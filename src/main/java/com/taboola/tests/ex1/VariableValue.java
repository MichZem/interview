package com.taboola.tests.ex1;

/**
 * Created by michael on 5/29/18.
 */
public class VariableValue implements Value {
    private String name;
    private double value = 0.0;

    public VariableValue(String name) {
        this(name, 0.0);
    }
    public VariableValue(String name, double value) {
        this.name = name;
        this.value = value;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VariableValue that = (VariableValue) o;

        if (Double.compare(that.value, value) != 0) return false;
        return name != null ? name.equals(that.name) : that.name == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = name != null ? name.hashCode() : 0;
        temp = Double.doubleToLongBits(value);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}

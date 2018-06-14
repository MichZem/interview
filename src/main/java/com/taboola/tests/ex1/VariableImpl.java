package com.taboola.tests.ex1;

/**
 * Created by michael on 6/14/18. <br/>
 * Very trivial implementation of Variable with some basic operation like incrementBy/decrementBy <br/>
 *
 */
public class VariableImpl implements Variable {
    private String name;
    private Double value;

    public VariableImpl(String name, Double value) {
        this.name = name;
        this.value = value;
    }

    public VariableImpl(String name, int value) {
        this(name, new Double(value));
    }

    public String getName() {
        return name;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(double val) {
        this.value = val;
    }

    public void incrementBy(int i) {
        value += i;
    }

    public void decrementBy(int i) {
        value -= i;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VariableImpl variable = (VariableImpl) o;

        if (!name.equals(variable.name)) return false;
        return value.equals(variable.value);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + value.hashCode();
        return result;
    }
}

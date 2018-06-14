package com.taboola.tests.ex1;

/**
 * Created by michael on 6/14/18. <br/>
 * Represents a simple variable, like <b><i>i=10.0</i></b>
 */
public interface Variable {

    String getName();
    Double getValue();
    void setValue(double val);
    void incrementBy(int i);
    void decrementBy(int i);
}

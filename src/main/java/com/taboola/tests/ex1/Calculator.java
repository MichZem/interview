package com.taboola.tests.ex1;

import java.util.Collection;

/**
 * Created by michael on 6/12/18.
 */
public interface Calculator {

    Collection<Variable> processEquationLine(String line);

    String getVariableStatus();

}

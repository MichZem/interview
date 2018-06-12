package com.taboola.tests.ex1;

import java.util.Collection;
import java.util.List;

/**
 * Created by michael on 6/12/18.
 */
public interface Calculator {

    Collection<VariableValue> processEquationLine(String line);
    List<Collection<VariableValue>> getIntermediateResult();
    Collection<VariableValue> getFinalResult();

}

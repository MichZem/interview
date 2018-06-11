package com.taboola.tests.ex1.operation;

import com.taboola.tests.ex1.Value;
import com.taboola.tests.ex1.VariableValue;
import com.taboola.tests.ex1.operation.ArythmeticOperation;

/**
 * Created by michael on 5/30/18.
 */
public class MinusMinusOperation implements ArythmeticOperation {

    private final VariableValue variableValue;

    public MinusMinusOperation(VariableValue variableValue) {
        this.variableValue = variableValue;
    }

    public Value execute() {
        variableValue.setValue(variableValue.getValue()-1);
        return variableValue;
    }
}

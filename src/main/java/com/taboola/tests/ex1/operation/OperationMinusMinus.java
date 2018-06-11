package com.taboola.tests.ex1.operation;

import com.taboola.tests.ex1.Value;
import com.taboola.tests.ex1.VariableValue;

/**
 * Created by michael on 5/30/18.
 */
public class OperationMinusMinus implements ArythmeticOperation {
    private final VariableValue variableValue;

    public OperationMinusMinus(VariableValue variableValue) {
        this.variableValue = variableValue;
    }

    public Value execute() {
        Value rc = variableValue;
        variableValue.setValue(variableValue.getValue()-1);
        return rc;
    }
}

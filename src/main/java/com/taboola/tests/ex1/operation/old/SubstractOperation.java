package com.taboola.tests.ex1.operation.old;

import com.taboola.tests.ex1.Value;
import com.taboola.tests.ex1.operation.ArythmeticOperation;

/**
 * Created by michael on 5/30/18.
 */
public class SubstractOperation implements ArythmeticOperation {

    private final Value b;
    private final Value a;

    public SubstractOperation(Value a, Value b) {
        this.a = a;
        this.b = b;
    }

    public Value execute() {
        return new ConstantValue(a.getValue()-b.getValue());
    }
}

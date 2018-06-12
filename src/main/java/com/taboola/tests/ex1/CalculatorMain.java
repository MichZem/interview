package com.taboola.tests.ex1;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Scanner;

/**
 * Created by michael on 5/29/18.
 */
public class CalculatorMain  {
    
    private static Logger log = LoggerFactory.getLogger(CalculatorMain.class);


    public static void main(String[] args) {

        Calculator calculatorMain = new CalculatorImpl();
        String line = null;
        Scanner scanner = new Scanner(System.in);
        do {
            log.debug("Please enter your equation line (like i=0  or j = ++i + 4 *5 ");
            line = scanner.nextLine();
            Collection<VariableValue> result = calculatorMain.processEquationLine(line);
            log.debug("result = " + result);
        }
        while(!"".equals(line));
    }

}

package com.taboola.tests.ex1;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

/**
 * Created by michael on 6/3/18.
 */
public class CalculatoTest {

    private static Logger log = LoggerFactory.getLogger(CalculatoTest.class);

    @Test
    public void testSimpleAssigment() {
        CalculatorImpl calculator = new CalculatorImpl();
        Collection<Variable> result = calculator.processEquationLine("i=0");

        Assert.assertEquals("Only one variable is expected : i=0" , 1, result.size());
        Assert.assertTrue("Only one variable is expected : i=0" , result.contains(new VariableImpl("i", 0)));

    }

    @Test
    public void testDoubleAssigment() {
        CalculatorImpl calculator = new CalculatorImpl();
        Collection<Variable> result = calculator.processEquationLine("i=0");
        Assert.assertEquals("Only one variable is expected : i=0" , 1, result.size());
        Assert.assertTrue("Only one variable is expected : i=0" , result.contains(new VariableImpl("i", 0)));


        result = calculator.processEquationLine("j=i++ + 4");
        Assert.assertEquals("Only TWO variables are expected : i=1 and j=4" , 2, result.size());
        Assert.assertTrue("Expected i=1" , result.contains(new VariableImpl("i", 1)));
        Assert.assertTrue("Expected j=4" , result.contains(new VariableImpl("j", 4)));

    }


    /**
     * i = 0                ==> status : i=0                    <br/>
     * j = ++i              ==> status : i=1 , j=1              <br/>
     * x = i++ + 5          ==> status : i=2 , j=1, x=6         <br/>
     * y = 5 + 3 * 10       ==> status : i=2 , j=1, x=6, y=35   <br/>
     * i += y               ==> status : i=37 , j=1, x=6, y=35  <br/>
     * <br/>
     Should return <b><i>(i=37,j=1,x=6,y=35)</i></b>
     */
    @Test
    public void testTaboolaAssigment() {
        Calculator calculator = new CalculatorImpl();
        Collection<Variable> result = calculator.processEquationLine("i=0");

        calculator.processEquationLine("j=++i");
        calculator.processEquationLine("x=i++ + 5");
        calculator.processEquationLine("y= 5 + 3*10");
        result = calculator.processEquationLine("i+=y");

        Assert.assertEquals("i,j,x,y are expected (4 variables) " , 4, result.size());
        Assert.assertTrue("i=37" , result.contains(new VariableImpl("i", 37)));
        Assert.assertTrue("j=1" , result.contains(new VariableImpl("j", 1)));
        Assert.assertTrue("expected  x=6" , result.contains(new VariableImpl("x", 6)));
        Assert.assertTrue("expected  y=35" , result.contains(new VariableImpl("y", 35)));

        String resultAsStr = calculator.getVariableStatus();
        log.info(resultAsStr);
    }

    /**
     * Run the following equations and ensure we have all the intermediate results as described : <br/>
     * i=5              ==> i=5 <br/>
     * j=i++ *3         ==> i=6 j=15 <br/>
     * k=i++ - 3*j      ==> i=7 j=15 k=-39 <br/>
     *
     */
    @Test
    public void testIntermediateResult() {
        Calculator calculator = new CalculatorImpl();
        Collection<Variable> result = calculator.processEquationLine("i=5");
        Assert.assertTrue(result.contains(new VariableImpl("i", 5)));

        result = calculator.processEquationLine("j=i++ * 3");
        Assert.assertTrue(result.contains(new VariableImpl("i", 6)));
        Assert.assertTrue(result.contains(new VariableImpl("j", 15)));

        result = calculator.processEquationLine("k=i++ - 3*j");
        Assert.assertTrue(result.contains(new VariableImpl("i", 7)));
        Assert.assertTrue(result.contains(new VariableImpl("j", 15)));
        Assert.assertTrue(result.contains(new VariableImpl("k", -39)));
    }


    /**
     * Check intermediate results and ensure we use also the minus-minus operator <br/>
     * i=5              ==> i=5 <br/>
     * j=i-- *3         ==> i=4 j=15 <br/>
     * k=--i - 3*j      ==> i=3 j=15 k=-42 <br/>
     *
     */
    @Test
    public void testMinusMinusOperation() {
        Calculator calculator = new CalculatorImpl();
        Collection<Variable> result = calculator.processEquationLine("i=5");
        Assert.assertTrue(result.contains(new VariableImpl("i", 5)));

        result = calculator.processEquationLine("j=i-- * 3");
        Assert.assertTrue(result.contains(new VariableImpl("i", 4)));
        Assert.assertTrue(result.contains(new VariableImpl("j", 15)));

        result = calculator.processEquationLine("k=--i - 3*j");
        Assert.assertTrue(result.contains(new VariableImpl("i", 3)));
        Assert.assertTrue(result.contains(new VariableImpl("j", 15)));
        Assert.assertTrue(result.contains(new VariableImpl("k", -42)));
    }

    /**
     * Check that our code support multiple occurence of same variable in the equation like here : <br/>
     * i=5          ==> (i=5)       <br/>
     * j=3*i+i      ==>  (i=5 , j=20) <br/>
     */
    @Test
    public void testMultipleOccurenceOfVariable() {
        Calculator calculator = new CalculatorImpl();
        Collection<Variable> result = calculator.processEquationLine("i=5");
        Assert.assertTrue(result.contains(new VariableImpl("i", 5)));

        result = calculator.processEquationLine("j=i*3+i");
        Assert.assertTrue(result.contains(new VariableImpl("i", 5)));
        Assert.assertTrue(result.contains(new VariableImpl("j", 20)));
    }


    /**
     * Check that we catch an IllegalArgumentException in case equation is not resolvable ... <br/>
     * i=5              ==> i=5 <br/>
     * j=i-- *3 + 2*k         ==> ERROR her : we cannot resolve that, since k is undefined at this stage <br/>

     *
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidEquations() {
        Calculator calculator = new CalculatorImpl();
        Collection<Variable> result = calculator.processEquationLine("i=5");
        Assert.assertTrue(result.contains(new VariableImpl("i", 5)));

        // Here, we are supposed to get an Exception since the variable k on the right side of the equation has no value ...
        result = calculator.processEquationLine("j=i-- * 3 + 2*k");

    }




}

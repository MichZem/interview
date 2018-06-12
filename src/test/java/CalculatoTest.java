import com.taboola.tests.ex1.Calculator;
import com.taboola.tests.ex1.CalculatorMain;
import com.taboola.tests.ex1.VariableValue;
import com.taboola.tests.ex1.operation.CalculatorImpl;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Map;

/**
 * Created by michael on 6/3/18.
 */
public class CalculatoTest {

    private static Logger log = LoggerFactory.getLogger(CalculatoTest.class);

    @Test
    public void testSimpleAssigment() {
        CalculatorImpl calculator = new CalculatorImpl();
        Collection<VariableValue> result = calculator.processEquationLine("i=0");

        Assert.assertEquals("Only one variable is expected : i=0" , 1, result.size());
        Assert.assertTrue("Only one variable is expected : i=0" , result.contains(new VariableValue("i", 0)));

    }

    @Test
    public void testDoubleAssigment() {
        CalculatorImpl calculator = new CalculatorImpl();
        Collection<VariableValue> result = calculator.processEquationLine("i=0");
        Assert.assertEquals("Only one variable is expected : i=0" , 1, result.size());
        Assert.assertTrue("Only one variable is expected : i=0" , result.contains(new VariableValue("i", 0)));


        result = calculator.processEquationLine("j=i++ + 4");
        Assert.assertEquals("Only TWO variables are expected : i=1 and j=4" , 2, result.size());
        Assert.assertTrue("Expected i=1" , result.contains(new VariableValue("i", 1)));
        Assert.assertTrue("Expected j=4" , result.contains(new VariableValue("j", 4)));

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
        Collection<VariableValue> result = calculator.processEquationLine("i=0");

        calculator.processEquationLine("j=++i");
        calculator.processEquationLine("x=i++ + 5");
        calculator.processEquationLine("y= 5 + 3*10");
        result = calculator.processEquationLine("i+=y");

        Assert.assertEquals("i,j,x,y are expected (4 variables) " , 4, result.size());
        Assert.assertTrue("Only one variable is expected : i=37" , result.contains(new VariableValue("i", 37)));
        Assert.assertTrue("Only one variable is expected : j=1" , result.contains(new VariableValue("j", 1)));
        Assert.assertTrue("Only one variable is expected : x=6" , result.contains(new VariableValue("x", 6)));
        Assert.assertTrue("Only one variable is expected : y=35" , result.contains(new VariableValue("y", 35)));
    }

    @Test
    public void testTaboolaAssigment_1() {
        Calculator calculator = new CalculatorImpl();
        Collection<VariableValue> result = calculator.processEquationLine("i=5");
        result = calculator.processEquationLine("j=i++ * 3");
        result = calculator.processEquationLine("k=i++ - 3*j");

        Assert.assertTrue(result.contains(new VariableValue("i", 7)));
        Assert.assertTrue(result.contains(new VariableValue("j", 15)));
        Assert.assertTrue(result.contains(new VariableValue("k", -39)));
    }
}
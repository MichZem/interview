import com.taboola.tests.ex1.CalculatorMain;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Created by michael on 6/3/18.
 */
public class CalculatorMainTest {

    private static Logger log = LoggerFactory.getLogger(CalculatorMainTest.class);

    @Test
    public void testSimpleAssigment() {
        CalculatorMain calculatorMain = new CalculatorMain();
        Map<String, Double> result = calculatorMain.processLine("i=0");

        Assert.assertEquals("Only one variable is expected : i=0" , 1, result.size());
        Assert.assertTrue("Only one variable is expected : i=0" , result.get("i").equals(Double.parseDouble("0")));

    }

    @Test
    public void testDoubleAssigment() {
        CalculatorMain calculatorMain = new CalculatorMain();
        Map<String, Double> result_1 = calculatorMain.processLine("i=0");
        Assert.assertEquals("Only one variable is expected : i=0" , 1, result_1.size());
        Assert.assertTrue("Only one variable is expected : i=0" , result_1.get("i").equals(Double.parseDouble("0")));


        Map<String, Double> result_2 = calculatorMain.processLine("j=i++ + 4");
        Assert.assertEquals("Only TWO variables are expected : i=1 and j=4" , 2, result_2.size());
        Assert.assertTrue("Expected i=1" , result_2.get("i").equals(Double.parseDouble("1")));
        Assert.assertTrue("Expected i=1" , result_2.get("j").equals(Double.parseDouble("4")));
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
        CalculatorMain calculatorMain = new CalculatorMain();
        Map<String, Double> result_1 = calculatorMain.processLine("i=0");
        System.out.println(calculatorMain.getVariableStatus());
        Map<String, Double> result_2 = calculatorMain.processLine("j=++i");
        Map<String, Double> result_3 = calculatorMain.processLine("x=i++ + 5");
        Map<String, Double> result_4 = calculatorMain.processLine("y= 5 + 3*10");
        Map<String, Double> result_5 = calculatorMain.processLine("i+=y");

        Assert.assertEquals("i,j,x,y are expected (4 variables) " , 4, result_5.size());
        Assert.assertTrue("Only one variable is expected : i=37" , result_5.get("i").equals(Double.parseDouble("37")));
        Assert.assertTrue("Only one variable is expected : j=1" , result_5.get("j").equals(Double.parseDouble("1")));
        Assert.assertTrue("Only one variable is expected : x=6" , result_5.get("x").equals(Double.parseDouble("6")));
        Assert.assertTrue("Only one variable is expected : y=35" , result_5.get("y").equals(Double.parseDouble("35")));
    }

    @Test
    public void testTaboolaAssigment_1() {
        CalculatorMain calculatorMain = new CalculatorMain();
        calculatorMain.processLine("i=5");
        log.info(calculatorMain.getVariableStatus());
        calculatorMain.processLine("j=i++ * 3");
        log.info(calculatorMain.getVariableStatus());
        calculatorMain.processLine("k=i++ - 3*j");
        log.info(calculatorMain.getVariableStatus());
    }
}

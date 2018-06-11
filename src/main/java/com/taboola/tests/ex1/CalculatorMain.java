package com.taboola.tests.ex1;


import com.taboola.tests.aaaaa.SimpleCalc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.security.InvalidParameterException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by michael on 5/29/18.
 */
public class CalculatorMain implements Calculator {
    
    private static Logger log = LoggerFactory.getLogger(CalculatorMain.class);

    /**
     * Find operator like i++ , --i , ++j or j--
     */
    final static String operatorRegex = "\\+\\+\\w+|\\w+\\+\\+|\\-\\-\\w+|\\w+\\-\\-";

    final Pattern operatorPattern = Pattern.compile(operatorRegex, Pattern.MULTILINE);

    /**
     * Find variable (which are defined by "any series of char " <br/>
     *
     */
    final Pattern variablePattern = Pattern.compile("\\w+", Pattern.MULTILINE);

    private HashMap<String, VariableValue> variableMap = new HashMap<String, VariableValue>();


    public static void main(String[] args) {



        CalculatorMain calculatorMain = new CalculatorMain();

        String line = null;
        Scanner scanner = new Scanner(System.in);
        do {
            log.debug("Please enter your equation line (like i=0  or j = ++i + 4 *5 ");
            line = scanner.nextLine();
            calculatorMain.handleLine(line);
        }
        while(!"".equals(line));

    }


    /**
     * Process the given line  <br/>
     * We expect for very stricted syntax : <br/>
     * <b><i>y = a*x*x - 4*bx + 2*c</i></b>
     *
     * @param line
     */
    public void handleLine(String line) {
        log.debug("Got line " + line);

        String[] splitValues = line.split("=");
        String leftSide = splitValues[0];
        String rightSide = splitValues[1];
        VariableValue variableValue = new VariableValue(leftSide);
        String lineAfterOperatorResolution = processOperatorInLine(rightSide);

        SimpleCalc.mainOriginal(new String[] {lineAfterOperatorResolution});

        variableMap.put(variableValue.getName(), variableValue);

    }

    /**
     * Find all the operator in the input line and execute them <br/>
     * Then, replace the different variables by their known values <br/>
     *
     * For ex: <br/>
     *   <b><i>i++</i></b> + 4*(<b><i>--j</i></b>) <br/>
     * @param line
     * @return
     */
    private String processOperatorInLine(String line) {
        String newLine = line;
        Matcher matcher = operatorPattern.matcher(line);
        while (matcher.find()) {

            log.debug("Full match: " + matcher.group(0));
            for (int i = 1; i <= matcher.groupCount(); i++) {
                log.debug("Group " + i + ": " + matcher.group(i));
            }

            // Replace the first occurence of the operator. In addition, add quote to your operator (aka Pattern.quote)
            // as the expected argument of the replaceFirst is a regex ...
            newLine = newLine.replaceFirst(Pattern.quote(matcher.group()), executeOperator(matcher.group()));
            log.debug("newLine = " + newLine);

            matcher = operatorPattern.matcher(newLine);
        }

        log.debug("processOperatorInLine : line = " + line);
        log.debug("processOperatorInLine : newLine = " + newLine);
        return newLine;
    }

    /**
     * Execute the operator instruction <br/>
     * Like : <br/>
     * <li> i++ (assuming i=3) should return 4 and assign i=4</li>
     * <li> ++i (assuming i=3) should return 3 and assign i=4</li>
     * @param operatorDirective
     * @return
     */
    private String executeOperator(String operatorDirective) {
        if(operatorDirective.endsWith("++")) {
            String variableName = operatorDirective.substring(0, operatorDirective.length()-2);
            VariableValue variable = variableMap.get(variableName);
            String result = variable.getValue() + "";
            variable.setValue(variable.getValue() + 1);
            return result;
        }
        else  if(operatorDirective.startsWith("++")) {
            String variableName = operatorDirective.substring("++".length());
            VariableValue variable = variableMap.get(variableName);
            if(variable == null) {
                throw new IllegalArgumentException("variableName was not assigned yet. variableName " + variableName);
            }
            variable.setValue(variable.getValue() + 1);
            String result = variable.getValue() + "";
            return result;
        }


        return operatorDirective;
    }

    /**
     * We assume here that each expression / variable etc is separated by a space <br/>
     * like here:   j = i++ + 5 + 3 * 2
     * @param expression
     * @return
     */
    private double handleExpression(String expression) {
        String[] expressionTokens = expression.split(" ");

        // Like "i++"
        if(expressionTokens.length == 1) {
            if(expression.startsWith("++")) {
                String variableName = expression.substring(2);
                VariableValue variable = variableMap.get(variableName);
                if(variable != null) {

                    return 0.0; // (new PlusPlusOperation(variable));
                }
                else {
                    throw new InvalidParameterException("expression is invalid : " + expression);
                }

            }
        }
        // Like "i++ + 5 + 3 * 2"
        else {
            for (String token : expressionTokens) {
                handleExpression(token);
            }
        }

        return 66666;
    }

    /**
     * Return true/false whether the given text represents an integer or not.
     * @param text
     * @return
     */
    private boolean isNumeric(String text) {
        try {
            Double.parseDouble(text);
            return true;
        }
        catch(NumberFormatException e) {
            return false;
        }
    }

    /**
     * Process equation line and returns the state of each variable till now.
     * @param line of stricted syntax like : k = 2*x + y*y - z++
     * @return Map of <b><i>variable name ==> Double value</i></b>
     */
    public Map<String, Double> processLine(String line) {
        String[] splitValues = line.split("=");


        String leftSideOfLine = splitValues[0];
        String rightSideOfLine = splitValues[1];
        String variableName = leftSideOfLine;

        // We have here an equation of type i += 6  or j -= j*2
        if(leftSideOfLine.endsWith("+")) {
            variableName = variableName.substring(0, leftSideOfLine.length()-1);
            rightSideOfLine = variableName + " +" + rightSideOfLine;
        }
        else if(leftSideOfLine.endsWith("-")) {
            variableName = variableName.substring(0, leftSideOfLine.length()-1);
            rightSideOfLine = variableName + " i" + rightSideOfLine;
        }

        VariableValue variableValue = new VariableValue(variableName);

        String lineAfterOperatorResolution = processOperatorInLine(rightSideOfLine);

        lineAfterOperatorResolution = replaceVariableByCurrentValue(lineAfterOperatorResolution);
        // Remove all space from line, since SimpleCacl doesnt deal well with them ...
        lineAfterOperatorResolution = lineAfterOperatorResolution.replace(" ", "");

        BigDecimal finalSum = SimpleCalc.calculateNumericValue(lineAfterOperatorResolution);
        log.debug("Final Sum of (" + line + ") = " + finalSum);
        variableValue.setValue(finalSum.doubleValue());
        variableMap.put(variableValue.getName(), variableValue);

        return convertVariableMap(variableMap);
    }

    /**
     * Replace all the variable found in line by their current value
     * @param line
     * @return
     */
    private String replaceVariableByCurrentValue(String line) {
        log.debug("within replaceVariableByCurrentValue : " + line);
        String newLine = line;
        Matcher matcher = variablePattern.matcher(line);
        while (matcher.find()) {

            log.debug("Full match: " + matcher.group(0));
            VariableValue variable = variableMap.get(matcher.group(0));
            if(variable != null) {
                newLine = newLine.replace(matcher.group(), variable.getValue() + "");
            }
            log.debug("newLine = " + newLine);
        }

        log.debug("processOperatorInLine : line = " + line);
        log.debug("processOperatorInLine : newLine = " + newLine);
        return newLine;
    }

    /**
     * Copy and convert the variableMap to a Map<String, Double> </String,>
     * @param variableMap
     * @return
     */
    private Map<String, Double> convertVariableMap(HashMap<String, VariableValue> variableMap) {
        HashMap<String, Double> result = new HashMap<String, Double>();
        for(String variableName : variableMap.keySet()) {
            VariableValue variableValue = variableMap.get(variableName);
            result.put(variableName, variableValue.getValue());
        }
        return result;
    }


    /**
     * Return a textual representation of the Variables <br/>
     * @return
     */
    public String getVariableStatus() {
        StringBuilder builder = new StringBuilder();
        for(String variableName : variableMap.keySet()) {
            VariableValue variableValue = variableMap.get(variableName);
            builder.append(variableName).append("=").append(variableValue.getValue()).append(" , ");
        }
        return builder.toString();
    }

    @Override
    public String toString() {
        return getVariableStatus();
    }
}

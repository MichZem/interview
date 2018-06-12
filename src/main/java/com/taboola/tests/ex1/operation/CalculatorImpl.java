package com.taboola.tests.ex1.operation;

import com.taboola.tests.aaaaa.SimpleCalc;
import com.taboola.tests.ex1.Calculator;
import com.taboola.tests.ex1.VariableValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by michael on 6/12/18.
 */
public class CalculatorImpl implements Calculator {

    private Logger log = LoggerFactory.getLogger(CalculatorImpl.class);

    private List<Collection<VariableValue>> intermediateResults = new ArrayList<Collection<VariableValue>>();

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


    /**
     * Process equation line and returns the state of each variable till now.
     *
     * @param line of stricted syntax like : k = 2*x + y*y - z++
     * @return Map of <b><i>variable name ==> Double value</i></b>
     */
    public Collection<VariableValue> processEquationLine(String line) {
        String[] splitValues = line.split("=");


        String leftSideOfLine = splitValues[0];
        String rightSideOfLine = splitValues[1];
        String variableName = leftSideOfLine;

        // We have here an equation of type i += 6  or j -= j*2
        if (leftSideOfLine.endsWith("+")) {
            variableName = variableName.substring(0, leftSideOfLine.length() - 1);
            rightSideOfLine = variableName + " +" + rightSideOfLine;
        } else if (leftSideOfLine.endsWith("-")) {
            variableName = variableName.substring(0, leftSideOfLine.length() - 1);
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

        return variableMap.values();

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

    public List<Collection<VariableValue>> getIntermediateResult() {
        return intermediateResults;
    }

    public Collection<VariableValue> getFinalResult() {
        if(!intermediateResults.isEmpty())
            return intermediateResults.get(intermediateResults.size()-1);
        else
            return null;
    }

}
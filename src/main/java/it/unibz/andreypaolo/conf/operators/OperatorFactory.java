package it.unibz.andreypaolo.conf.operators;

import java.util.HashMap;
import java.util.Map;

public class OperatorFactory {
    /**
     * The key is a sign such as = &lt; &gt; etc... the value is a strategy that performs the check
     */
    private final static Map<String, IOperator> operators = initializeOperators();

    private static Map<String, IOperator> initializeOperators() {
        Map<String, IOperator> initOperators = new HashMap<>();
        initOperators.put("=", (compareResult) -> compareResult == 0);
        initOperators.put(">", (compareResult) -> compareResult > 0);
        initOperators.put("<", (compareResult) -> compareResult < 0);
        initOperators.put(">=", (compareResult) -> compareResult >= 0);
        initOperators.put("<=", (compareResult) -> compareResult <= 0);
        initOperators.put("!=", (compareResult) -> compareResult != 0);
        return initOperators;
    }

    public static IOperator get(String operatorStr) {
        return operators.get(operatorStr);
    }
}

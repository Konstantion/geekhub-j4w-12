package edu.geekhub.homework.task3;

/**
 * Enum represents
 * <a href="https://en.wikipedia.org/wiki/Arithmetic#Arithmetic_operations">Arithmetic operations</a>
 * '+', '-', '*' and '/'
 */
public enum ArithmeticOperation {
    ADDITION("+"),
    SUBTRACTION("-"),
    MULTIPLICATION("*"),
    DIVISION("/");

    String operator;

    ArithmeticOperation(String operator) {
        this.operator = operator;
    }

    static boolean isArithmeticOperation(String input) {
        for (ArithmeticOperation arithmeticOperation : ArithmeticOperation.values()) {
            if (arithmeticOperation.operator.equals(input)) {
                return true;
            }
        }
        return false;
    }

    String getOperator() {
        return operator;
    }
}

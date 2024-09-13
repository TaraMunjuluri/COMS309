package main.java.coms309.example.calculator;

public class Calculation {
    private Long id;
    private double operand1;
    private double operand2;
    private String operation; // "add", "subtract", "multiply", "divide"
    private double result;

    public Calculation(Long id, double operand1, double operand2, String operation) {
        this.id = id;
        this.operand1 = operand1;
        this.operand2 = operand2;
        this.operation = operation;
        this.result = performOperation();
    }

    public Long getId() {
        return id;
    }

    public double getOperand1() {
        return operand1;
    }

    public double getOperand2() {
        return operand2;
    }

    public String getOperation() {
        return operation;
    }

    public double getResult() {
        return result;
    }

    private double performOperation() {
        switch (operation) {
            case "add":
                return operand1 + operand2;
            case "subtract":
                return operand1 - operand2;
            case "multiply":
                return operand1 * operand2;
            case "divide":
                if (operand2 != 0) {
                    return operand1 / operand2;
                } else {
                    throw new IllegalArgumentException("Division by zero is not allowed.");
                }
            default:
                throw new IllegalArgumentException("Invalid operation");
        }
    }

    public void setOperand1(double operand1) {
        this.operand1 = operand1;
        this.result = performOperation();
    }

    public void setOperand2(double operand2) {
        this.operand2 = operand2;
        this.result = performOperation();
    }

    public void setOperation(String operation) {
        this.operation = operation;
        this.result = performOperation();
    }
}

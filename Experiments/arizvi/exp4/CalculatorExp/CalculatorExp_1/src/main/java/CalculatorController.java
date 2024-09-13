package main.java.coms309.example.calculator;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/calculations")
public class CalculatorController {

    private List<Calculation> calculations = new ArrayList<>();
    private Long idCounter = 1L;

    // Create a new calculation
    @PostMapping
    public Calculation createCalculation(@RequestParam double operand1,
                                         @RequestParam double operand2,
                                         @RequestParam String operation) {
        Calculation calculation = new Calculation(idCounter++, operand1, operand2, operation);
        calculations.add(calculation);
        return calculation;
    }

    // Get a specific calculation by ID
    @GetMapping("/{id}")
    public Calculation getCalculation(@PathVariable Long id) {
        return calculations.stream()
                .filter(calc -> calc.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("main.java.coms309.example.calculator.Calculation not found"));
    }

    // List all calculations
    @GetMapping
    public List<Calculation> listCalculations() {
        return calculations;
    }

    // Update a calculation by ID
    @PutMapping("/{id}")
    public Calculation updateCalculation(@PathVariable Long id,
                                         @RequestParam double operand1,
                                         @RequestParam double operand2,
                                         @RequestParam String operation) {
        Calculation calculation = calculations.stream()
                .filter(calc -> calc.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("main.java.coms309.example.calculator.Calculation not found"));

        calculation.setOperand1(operand1);
        calculation.setOperand2(operand2);
        calculation.setOperation(operation);
        return calculation;
    }

    // Delete a calculation by ID
    @DeleteMapping("/{id}")
    public void deleteCalculation(@PathVariable Long id) {
        calculations.removeIf(calc -> calc.getId().equals(id));
    }
}

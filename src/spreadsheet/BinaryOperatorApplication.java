package spreadsheet;

import common.api.CellLocation;
import common.api.EvaluationContext;
import common.api.Expression;
import java.util.Set;

public class BinaryOperatorApplication implements Expression {
  private final BinaryOperator operator;
  private final Expression leftExpression;
  private final Expression rightExpression;

  public BinaryOperatorApplication(Expression leftExpression, BinaryOperator operator,
      Expression rightExpression) {
    this.operator = operator;
    this.rightExpression = rightExpression;
    this.leftExpression = leftExpression;
  }

  @Override
  public String toString() {
    return "(" + leftExpression.toString() + " " + operator.toString() + " " + rightExpression
        .toString() + ")";
  }

  @Override
  public double evaluate(EvaluationContext context) {
    return switch (operator.toString()) {
      case "+" -> leftExpression.evaluate(context) + rightExpression.evaluate(context);
      case "-" -> leftExpression.evaluate(context) - rightExpression.evaluate(context);
      case "*" -> leftExpression.evaluate(context) * rightExpression.evaluate(context);
      case "/" -> leftExpression.evaluate(context) / rightExpression.evaluate(context);
      case "^" -> Math.pow(leftExpression.evaluate(context), rightExpression.evaluate(context));
      default -> throw new IllegalStateException("Unexpected value: " + operator);
    };
  }

  @Override
  public void findCellReferences(Set<CellLocation> dependencies) {
    leftExpression.findCellReferences(dependencies);
    rightExpression.findCellReferences(dependencies);
  }
}

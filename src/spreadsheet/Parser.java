package spreadsheet;

import common.api.Expression;
import common.lexer.InvalidTokenException;
import common.lexer.Lexer;
import common.lexer.Token;
import java.util.List;
import java.util.Stack;

public class Parser {

  private static boolean shouldApplyBinaryOperator(
      Token topToken, Stack<BinaryOperator> operatorStack) {
    return operatorStack.size() > 0
        && (operatorStack.peek().getPrecedence() > new BinaryOperator(topToken.kind).getPrecedence()
            || (operatorStack.peek().getPrecedence()
                    == new BinaryOperator(topToken.kind).getPrecedence()
                && new BinaryOperator(topToken.kind).getAssociativity() == BinaryOperator.Assoc.L));
  }

  private static void applyBinaryOperator(
      Stack<Expression> operandStack, Stack<BinaryOperator> operatorStack) {
    Expression rightExpression = operandStack.pop();
    Expression leftExpression = operandStack.pop();
    operandStack.add(
        new BinaryOperatorApplication(leftExpression, operatorStack.pop(), rightExpression));
  }

  /**
   * Parse a string into an Expression.
   *
   * <p>DO NOT CHANGE THE SIGNATURE. The test suite depends on this.
   */
  static Expression parse(String input) throws InvalidSyntaxException {
    Stack<Expression> operandStack = new Stack<>();
    Stack<BinaryOperator> operatorStack = new Stack<>();

    List<Token> tokens;
    try {
      tokens = Lexer.tokenize(input);
    } catch (InvalidTokenException e) {
      throw new InvalidSyntaxException("Cannot parse input " + input);
    }

    while (!tokens.isEmpty()) {
      Token topToken = tokens.get(0);
      if (topToken.kind == Token.Kind.NUMBER || topToken.kind == Token.Kind.CELL_LOCATION) {
        operandStack.add(
            topToken.kind == Token.Kind.NUMBER
                ? new Number(topToken.numberValue)
                : new CellReference(topToken.cellLocationValue));
        tokens.remove(0);
      } else if (shouldApplyBinaryOperator(topToken, operatorStack)) {
        applyBinaryOperator(operandStack, operatorStack);
      } else {
        operatorStack.add(new BinaryOperator(topToken.kind));
        tokens.remove(0);
      }
    }

    while (!operatorStack.isEmpty()) {
      applyBinaryOperator(operandStack, operatorStack);
    }

    if (operandStack.size() != 1) {
      throw new InvalidSyntaxException("Invalid expression " + input);
    }

    return operandStack.firstElement();
  }
}

package spreadsheet;

import common.lexer.Token;

public class BinaryOperator {

  private final Token.Kind operation;

  public enum Assoc {
    R,
    L,
  }

  public BinaryOperator(Token.Kind operation) {
    this.operation = operation;
  }

  public int getPrecedence() {
    return switch (operation) {
      case PLUS, MINUS -> 1;
      case STAR, SLASH -> 2;
      case CARET -> 3;
      default -> throw new IllegalStateException(operation + "is not a binary operator");
    };
  }

  public Assoc getAssociativity() {
    return operation == Token.Kind.CARET ? Assoc.R : Assoc.L;
  }

  @Override
  public String toString() {
    return switch (operation) {
      case PLUS -> "+";
      case MINUS -> "-";
      case SLASH -> "/";
      case STAR -> "*";
      case CARET -> "^";
      default -> throw new IllegalStateException(operation + "is not a binary operator");
    };
  }
}

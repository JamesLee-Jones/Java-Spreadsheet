package common.lexer;

import static common.lexer.Token.Kind.CELL_LOCATION;
import static common.lexer.Token.Kind.NUMBER;

import common.api.CellLocation;
import java.util.Objects;

/**
 * Representation of a token from the input string.
 *
 * <p>If `kind` if NUMBER or CELL_LOCATION, then the represented value may be found respectively in
 * `numberValue` and `cellLocationValue`.
 */
public class Token {
  public final Kind kind;
  public final CellLocation cellLocationValue;
  public final double numberValue;

  Token(Kind kind) {
    this(kind, null, 0);
    assert (kind != NUMBER && kind != CELL_LOCATION);
  }

  Token(double value) {
    this(NUMBER, null, value);
  }

  Token(CellLocation cellLocation) {
    this(CELL_LOCATION, cellLocation, 0);
  }

  private Token(Kind kind, CellLocation cellLocationValue, double numberValue) {
    this.kind = kind;
    this.cellLocationValue = cellLocationValue;
    this.numberValue = numberValue;
  }

  @Override
  public String toString() {
    return switch (kind) {
      case CELL_LOCATION -> "CELL(" + cellLocationValue.toString() + ")";
      case NUMBER -> "NUMBER(" + numberValue + ")";
      default -> kind.name();
    };
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    Token other = (Token) obj;
    if (this.kind != other.kind) {
      return false;
    }
    return switch (this.kind) {
      case NUMBER -> this.numberValue == other.numberValue;
      case CELL_LOCATION -> this.cellLocationValue.equals(other.cellLocationValue);
      default -> true;
    };
  }

  @Override
  public int hashCode() {
    return switch (kind) {
      case NUMBER -> Objects.hash(kind, numberValue);
      case CELL_LOCATION -> Objects.hash(kind, cellLocationValue);
      default -> Objects.hash(kind);
    };
  }

  public enum Kind {
    PLUS,
    MINUS,
    STAR,
    SLASH,
    CARET,
    LPARENTHESIS,
    RPARENTHESIS,
    LANGLE,
    RANGLE,
    EQUALS,
    NUMBER,
    CELL_LOCATION,
  }
}

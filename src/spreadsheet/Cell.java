package spreadsheet;

import common.api.BasicSpreadsheet;
import common.api.CellLocation;
import common.api.Expression;
import java.util.HashSet;
import java.util.Set;

/**
 * A single cell in a spreadsheet, tracking the expression, value, and other parts of cell state.
 */
public class Cell {

  private final CellLocation location;
  private final BasicSpreadsheet spreadsheet;
  private final Set<CellLocation> dependent = new HashSet<>();
  private double value = 0;
  private Expression expression = null;

  /**
   * Constructs a new cell.
   *
   * <p>DO NOT CHANGE THE SIGNATURE. The test suite depends on this.
   *
   * @param spreadsheet The parent spreadsheet,
   * @param location The location of this cell in the spreadsheet.
   */
  Cell(BasicSpreadsheet spreadsheet, CellLocation location) {
    this.spreadsheet = spreadsheet;
    this.location = location;
  }

  /**
   * Gets the cell's last calculated value.
   *
   * <p>DO NOT CHANGE THE SIGNATURE. The test suite depends on this.
   *
   * @return the cell's value.
   */
  public double getValue() {
    return value;
  }

  /**
   * Gets the cell's last stored expression, in string form.
   *
   * <p>DO NOT CHANGE THE SIGNATURE. The test suite depends on this.
   *
   * @return a string that parses to an equivalent expression to that last stored in the cell; if no
   *     expression is stored, we return the empty string.
   */
  public String getExpression() {
    return expression == null ? "" : expression.toString();
  }

  /**
   * Sets the cell's expression from a string.
   *
   * <p>DO NOT CHANGE THE SIGNATURE. The test suite depends on this.
   *
   * @param input The string representing the new cell expression.
   * @throws InvalidSyntaxException if the string cannot be parsed.
   */
  public void setExpression(String input) throws InvalidSyntaxException {
    Set<CellLocation> dependencies = new HashSet<>();

    if (expression != null) {
      expression.findCellReferences(dependencies);
      value = expression.evaluate(spreadsheet);
      for (CellLocation cellLocation : dependencies) {
        if (!spreadsheet.getCellExpression(cellLocation).equals("")) {
          spreadsheet.removeDependency(this.location, cellLocation);
        }
      }
    }

    if (input.isEmpty()) {
      this.expression = null;
    } else {
      this.expression = Parser.parse(input);
      dependencies.clear();
      expression.findCellReferences(dependencies);
      for (CellLocation dependency : dependencies) {
        // Checks if the cell has default value, if it does it creates a cell there to store
        // the dependency
        if (spreadsheet.getCellExpression(dependency).equals("")) {
          spreadsheet.setCellExpression(dependency, "");
        }
        spreadsheet.addDependency(this.location, dependency);
      }
    }
  }

  /**
   * Provides a string representation of a cells value
   *
   * @return a string representing the value, if any, of this cell.
   */
  @Override
  public String toString() {
    return value == 0 ? "" : Double.toString(value);
  }

  /**
   * Adds the given location to this cell's dependents.
   *
   * <p>DO NOT CHANGE THE SIGNATURE. The test suite depends on this.
   *
   * @param location the location to add.
   */
  public void addDependent(CellLocation location) {
    dependent.add(location);
  }

  /**
   * Adds the given location to this cell's dependents.
   *
   * <p>DO NOT CHANGE THE SIGNATURE. The test suite depends on this.
   *
   * @param location the location to add.
   */
  public void removeDependent(CellLocation location) {
    dependent.remove(location);
  }

  /**
   * Adds this cell's expression's references to a set.
   *
   * <p>DO NOT CHANGE THE SIGNATURE. The test suite depends on this.
   *
   * @param target The set that will receive the dependencies for this
   */
  public void findCellReferences(Set<CellLocation> target) {
    if (expression != null) {
      expression.findCellReferences(target);
    }
  }

  /**
   * Recalculates this cell's value based on its expression.
   *
   * <p>DO NOT CHANGE THE SIGNATURE. The test suite depends on this.
   */
  public void recalculate() {
    if (expression == null) {
      value = 0;
    } else {
      value = expression.evaluate(spreadsheet);
    }

    for (CellLocation cellLocation : dependent) {
      spreadsheet.recalculate(cellLocation);
    }
  }
}

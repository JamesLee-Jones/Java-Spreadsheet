package spreadsheet;

import common.api.BasicSpreadsheet;
import common.api.CellLocation;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Spreadsheet implements BasicSpreadsheet {

  private final Map<CellLocation, Cell> cells = new HashMap<>();
  private final CycleDetector cycleDetector = new CycleDetector(this);

  /**
   * Construct an empty spreadsheet.
   *
   * <p>DO NOT CHANGE THE SIGNATURE. The test suite depends on this.
   */
  Spreadsheet() {}

  /**
   * Parse and evaluate an expression, using the spreadsheet as a context.
   *
   * <p>DO NOT CHANGE THE SIGNATURE. The test suite depends on this.
   */
  public double evaluateExpression(String expression) throws InvalidSyntaxException {
    return Parser.parse(expression).evaluate(this);
  }

  @Override
  public double getCellValue(CellLocation location) {
    return cells.containsKey(location) ? cells.get(location).getValue() : 0;
  }

  @Override
  public void setCellExpression(CellLocation location, String input) throws InvalidSyntaxException {
    if (!cells.containsKey(location)) {
      cells.put(location, new Cell(this, location));
    }
    Cell cell = cells.get(location);
    String previousExpression = cell.getExpression();
    cell.setExpression(input);
    if (cycleDetector.hasCycleFrom(location)) {
      cell.setExpression(previousExpression);
    }
    cell.recalculate();
  }

  //
  // end replacing
  //

  @Override
  public String getCellExpression(CellLocation location) {
    return cells.containsKey(location) ? cells.get(location).getExpression() : "";
  }

  @Override
  public String getCellDisplay(CellLocation location) {
    return cells.containsKey(location) ? cells.get(location).toString() : "";
  }

  @Override
  public void addDependency(CellLocation dependent, CellLocation dependency) {
    cells.get(dependency).addDependent(dependent);
  }

  @Override
  public void removeDependency(CellLocation dependent, CellLocation dependency) {
    cells.get(dependency).removeDependent(dependent);
  }

  @Override
  public void recalculate(CellLocation location) {
    cells.get(location).recalculate();
  }

  @Override
  public void findCellReferences(CellLocation subject, Set<CellLocation> target) {
    cells.get(subject).findCellReferences(target);
  }
}

package spreadsheet;

import common.api.BasicSpreadsheet;
import common.api.CellLocation;
import java.util.HashSet;
import java.util.Set;

/** Detects dependency cycles. */
public class CycleDetector {

  private final BasicSpreadsheet spreadsheet;

  /**
   * Constructs a new cycle detector.
   *
   * <p>DO NOT CHANGE THE SIGNATURE. The test suite depends on this.
   *
   * @param spreadsheet The parent spreadsheet, used for resolving cell locations.
   */
  CycleDetector(BasicSpreadsheet spreadsheet) {
    this.spreadsheet = spreadsheet;
  }

  /**
   * Checks for a cycle in the spreadsheet, starting at a particular cell.
   *
   * <p>DO NOT CHANGE THE SIGNATURE. The test suite depends on this.
   *
   * @param start The cell location where cycle detection should start.
   * @return Whether a cycle was detected in the dependency graph starting at the given cell.
   */
  public boolean hasCycleFrom(CellLocation start) {
    Set<CellLocation> dependencies = new HashSet<>();
    spreadsheet.findCellReferences(start, dependencies);

    for (CellLocation dependency : dependencies) {
      Set<CellLocation> seen = new HashSet<>();
      seen.add(start);
      if (hasCycleFromRecursive(dependency, seen)) {
        return true;
      }
    }
    return false;
  }

  private boolean hasCycleFromRecursive(CellLocation start, Set<CellLocation> seen) {
    Set<CellLocation> dependencies = new HashSet<>();
    spreadsheet.findCellReferences(start, dependencies);
    seen.add(start);

    for (CellLocation dependency : dependencies) {
      if (seen.contains(dependency) || hasCycleFromRecursive(dependency, seen)) {
        return true;
      }
      seen.remove(dependency);
    }
    return false;
  }
}

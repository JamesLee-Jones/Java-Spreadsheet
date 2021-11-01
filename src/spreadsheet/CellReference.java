package spreadsheet;

import common.api.CellLocation;
import common.api.EvaluationContext;
import common.api.Expression;
import java.util.Set;

public class CellReference implements Expression {
  private final CellLocation location;

  public CellReference(CellLocation location) {
    this.location = location;
  }

  @Override
  public String toString() {
    return location.toString();
  }

  @Override
  public double evaluate(EvaluationContext context) {
    return context.getCellValue(location);
  }

  @Override
  public void findCellReferences(Set<CellLocation> dependencies) {
    dependencies.add(location);
  }
}

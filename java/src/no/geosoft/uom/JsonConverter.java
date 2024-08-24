package no.geosoft.uom;

import java.io.InputStream;
import java.io.FileInputStream;
import java.util.HashSet;
import java.util.Set;

import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonString;
import javax.json.JsonValue;
import javax.json.Json;
import javax.json.JsonStructure;
import javax.json.JsonWriterFactory;
import javax.json.stream.JsonGenerator;
import javax.json.JsonReader;

/**
 * Class that converts the original Energistics v1.01 standard into the
 * simpler GeoSoft counterpart.
 *
 * @author <a href="mailto:jacob.dreyer@geosoft.no">Jacob Dreyer</a>
 */
public final class JsonConverter
{
  public JsonConverter()
  {
  }

  private static double getValue(String value)
  {
    if (value.equals("PI"))
      return Math.PI;

    if (value.equals("2*PI"))
      return 2 * Math.PI;

    if (value.equals("4*PI"))
      return 4 * Math.PI;

    try {
      return Double.parseDouble(value);
    }
    catch (NumberFormatException exception) {
      exception.printStackTrace();
      return -1.0;
    }
  }

  private static Unit findUnit(Set<Unit> units, String symbol)
  {
    for (Unit unit : units) {
      if (unit.getSymbol().equals(symbol))
        return unit;
    }

    // Not found
    return null;
  }

  public static void main(String[] arguments)
  {
    Set<Unit> units = new HashSet<>();
    Set<Quantity> quantities = new HashSet<>();

    try {
      // Read the Energistics UoM standard
      InputStream stream = new FileInputStream("C:/Users/jacob/dev/osdu-uom/json/Energistics_Unit_of_Measure_Dictionary.json");
      JsonReader reader = Json.createReader(stream);
      JsonObject energisticsStandard = reader.readObject();

      // Parse units
      JsonObject unitSet = energisticsStandard.getJsonObject("UnitSet");
      JsonArray unitObjects = unitSet.getJsonArray("Unit");

      for (JsonObject unitObject : unitObjects.getValuesAs(JsonObject.class)) {
        String name = unitObject.getString("Name");
        String symbol = unitObject.getString("Symbol");
        double a = unitObject.containsKey("A") ? getValue(unitObject.getString("A")) : 1.0;
        double b = unitObject.containsKey("B") ? getValue(unitObject.getString("B")) : 0.0;
        double c = unitObject.containsKey("C") ? getValue(unitObject.getString("C")) : 0.0;
        double d = unitObject.containsKey("D") ? getValue(unitObject.getString("D")) : 1.0;
        Unit unit = new Unit(name, symbol, a, b, c, d);
        units.add(unit);
      }

      // Parse quantities
      JsonObject quantitySet = energisticsStandard.getJsonObject("QuantityClassSet");
      JsonArray quantityObjects = quantitySet.getJsonArray("QuantityClass");

      for (JsonObject quantityObject : quantityObjects.getValuesAs(JsonObject.class)) {
        String name = quantityObject.getString("Name");
        String description = quantityObject.containsKey("Description") ? quantityObject.getString("Description") : null;

        Quantity quantity = new Quantity(name, description);
        quantities.add(quantity);

        String baseUnit = quantityObject.getString("BaseForConversion");
        JsonArray memberUnits = quantityObject.getJsonArray("MemberUnit");
        for (JsonString unitSymbol : memberUnits.getValuesAs(JsonString.class)) {
          Unit unit = findUnit(units, unitSymbol.getString());
          boolean isBaseUnit = unitSymbol.getString().equals(baseUnit);

          quantity.addUnit(unit, isBaseUnit);
        }
      }
    }
    catch (Exception exception) {
      exception.printStackTrace();
    }
  }
}

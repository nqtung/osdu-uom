package no.geosoft.uom;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Collection;
import java.io.IOException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.nio.charset.StandardCharsets;
import java.io.Writer;
import java.io.OutputStreamWriter;

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

public final class JsonWriter
{
  /**
   * Private constructor to prevent client instantiation.
   */
  private JsonWriter()
  {
  }

  /**
   * Add entry of the specified key/value to the given object builder.
   *
   * @param objectBuilder  Object builder to add to. Non-null.
   * @param key            Key of entry to add. Non-null.
   * @param value          Value of key. May be null, in case "null" is added.
   */
  private static void add(JsonObjectBuilder objectBuilder, String key, String value)
  {
    assert objectBuilder != null : "objectBuilder cannot be null";
    assert key != null : "key cannot be null";

    if (value != null)
      objectBuilder.add(key, value);
    else
      objectBuilder.addNull(key);
  }

  /**
   * Add entry of the specified key/value to the given object builder.
   *
   * @param objectBuilder  Object builder to add to. Non-null.
   * @param key            Key of entry to add. Non-null.
   * @param value          Value of key. May be null, in case "null" is added.
   */
  private static void add(JsonObjectBuilder objectBuilder, String key, Double value)
  {
    assert objectBuilder != null : "objectBuilder cannot be null";
    assert key != null : "key cannot be null";

    if (value != null)
      objectBuilder.add(key, value);
    else
      objectBuilder.addNull(key);
  }

  /**
   * Save the specified JSON structure to the given stream.
   *
   * @param stream  Stream to save to. Non-null.
   * @param json  JSON structure to save. Non-null.
   * @throws IllegalArgumentException  If stream of json is null.
   * @throws IOException  If the save operation fails for some reason.
   */
  public static void save(OutputStream stream, JsonStructure json)
    throws IOException
  {
    if (stream == null)
      throw new IllegalArgumentException("stream cannot be null");

    if (json == null)
      throw new IllegalArgumentException("json cannot be null");

    Map<String, Object> config = new HashMap<>();
    config.put(JsonGenerator.PRETTY_PRINTING, true);

    Writer writer = new OutputStreamWriter(stream, StandardCharsets.UTF_8);

    JsonWriterFactory jsonWriterFactory = Json.createWriterFactory(config);
    javax.json.JsonWriter jsonWriter = jsonWriterFactory.createWriter(writer);

    jsonWriter.write(json);
    jsonWriter.close();
  }

  /**
   * Save the specified JSON structure to the given file.
   *
   * @param file  File to save to. Non-null.
   * @param json  JSON structure to save. Non-null.
   * @throws IllegalArgumentException  If file of json is null.
   * @throws IOException  If the save operation fails for some reason.
   */
  public static void save(File file, JsonStructure json)
    throws IOException
  {
    if (file == null)
      throw new IllegalArgumentException("file cannot be null");

    if (json == null)
      throw new IllegalArgumentException("json cannot be null");

    FileOutputStream fileStream = new FileOutputStream(file);
    try {
      JsonWriter.save(fileStream, json);
      fileStream.close();
    }
    catch (IOException exception) {
      throw exception;
    }
    finally {
      fileStream.close();
    }
  }

  /**
   * Return the specified JSON structure as a pretty-printed JSON string.
   *
   * @param json  JSON structure to consider. Non-null.
   * @return      The equivalent pretty-printed JSON string. Never null.
   */
  public static String toString(JsonStructure json)
  {
    if (json == null)
      throw new IllegalArgumentException("json cannot be null");

    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(byteArrayOutputStream);

    try {
      save(printStream, json);
      return new String(byteArrayOutputStream.toByteArray(), StandardCharsets.UTF_8);
    }
    catch (IOException exception) {
      assert false;
      return null;
    }
  }

  /**
   * Return the specified quantity as a JSON object builder.
   *
   * @param quantity  Quantity to consider. Non-null.
   * @return          The equivalent JSON object builder. Never null.
   * @throws IllegalArgumentException  If quantity is null.
   */
  public static JsonObjectBuilder getQuantity(Quantity quantity)
  {
    if (quantity == null)
      throw new IllegalArgumentException("quantity cannot be null");

    JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
    add(objectBuilder, "name", quantity.getName());

    JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
    for (Unit unit : quantity.getUnits())
      arrayBuilder.add(unit.getName());

    objectBuilder.add("units", arrayBuilder);

    return objectBuilder;
  }

  /**
   * Return the specified quantities as a JSON array builder.
   *
   * @param quantities  Quantities to consider. Non-null.
   * @return            The equivalent JSON array builder. Never null.
   * @throws IllegalArgumentException  If quantities is null.
   */
  public static JsonArrayBuilder getQuantities(Collection<Quantity> quantities)
  {
    if (quantities == null)
      throw new IllegalArgumentException("quantities cannot be null");

    JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
    for (Quantity quantity : quantities)
      arrayBuilder.add(getQuantity(quantity));

    return arrayBuilder;
  }

  /**
   * Return the specified unit as a JSON object builder.
   *
   * @param unit  Unit to consider. Non-null.
   * @return      The equivalent JSON object builder. Never null.
   * @throws IllegalArgumentException  If unit is null.
   */
  public static JsonObjectBuilder getUnit(Unit unit)
  {
    if (unit == null)
      throw new IllegalArgumentException("unit cannot be null");

    UnitManager unitManager = UnitManager.getInstance();

    String symbol = unit.getSymbol();

    JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
    add(objectBuilder, "name", unit.getName());
    add(objectBuilder, "symbol", symbol);
    add(objectBuilder, "displaySymbol", unit.getDisplaySymbol());
    add(objectBuilder, "a", unit.getA());
    add(objectBuilder, "b", unit.getB());
    add(objectBuilder, "c", unit.getC());
    add(objectBuilder, "d", unit.getD());

    return objectBuilder;
  }

  /**
   * Return the specified units as a JSON array builder.
   *
   * @param units  Units to consider. Non-null.
   * @return       The equivalent JSON array builder. Never null.
   * @throws IllegalArgumentException  If units is null.
   */
  public static JsonArrayBuilder getUnits(Collection<Unit> units)
  {
    if (units == null)
      throw new IllegalArgumentException("units cannot be null");

    JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
    for (Unit unit : units)
      arrayBuilder.add(getUnit(unit));

    return arrayBuilder;
  }

  private static JsonObjectBuilder getAll(Collection<Unit> units, Collection<Quantity> quantities)
  {
    assert units != null : "units cannot be null";
    assert quantities != null : "quantities cannot be null";

    JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
    objectBuilder.add("quantities", getQuantities(quantities));
    objectBuilder.add("units", getUnits(units));

    return objectBuilder;
  }

  public static void main(String[] arguments)
    throws Exception
  {
    UnitManager unitManager = UnitManager.getInstance();
    JsonStructure json = getAll(unitManager.getUnits(), unitManager.getQuantities()).build();
    File file = new File("C:/Users/jacob/dev/osdu-uom/json/uom.json");
    save(file, json);
  }
}

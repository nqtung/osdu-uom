package no.geosoft.uom;

/**
 * Model a unit, such as "ft", "m", "N", "Hz", "m/s" etc., and
 * how values converts to the base unit of the same quantity.
 * <p>
 * This class is immutable.
 *
 * @author <a href="mailto:jacob.dreyer@geosoft.no">Jacob Dreyer</a>
 */
public final class Unit
{
  /** Name of unit such as "meter". Non-null. */
  private final String name_;

  /** Symbol of unit such as "m". Non-null. */
  private final String symbol_;

  /** Conversion factor a for converting to base unit. */
  private final double a_;

  /** Conversion factor b for converting to base unit. */
  private final double b_;

  /** Conversion factor c for converting to base unit. */
  private final double c_;

  /** Conversion factor d for converting to base unit. */
  private final double d_;

  /** Dislay symbol suitable for user presentation. */
  private final String displaySymbol_;

  /**
   * Create a new unit.
   * <p>
   * The conversion consists of four factors that are applied as
   * follows when <em>value</em> is to be converted to base unit:
   *
   * <pre>
   *    base = (a * value + b) / (c * value + d);
   * </pre>
   *
   * For most conversion only <em>a</em> is needed. In these cases
   * <em>b</em>, <em>c</em> and <em>d</em>
   * should be set to 0.0, 0.0 and 1.0 respectively. For units like
   * temperature a shift (<em>b</em>) is used as well, while c and d
   * is in practice never used.
   *
   * @param name           Name of unit such as "meter". Non-null.
   * @param symbol         Symbol of unit such as "m". Non-null.
   * @param a              Conversion factor a for converting to base unit.
   * @param b              Conversion factor b for converting to base unit.
   * @param c              Conversion factor c for converting to base unit.
   * @param d              Conversion factor d for converting to base unit.
   * @param displaySymbol  Display symbol suitable for user presentation.
   * @throws IllegalArgumentException  If name, symbol or displaySymbol is null.
   */
  public Unit(String name, String symbol, double a, double b, double c, double d, String displaySymbol)
  {
    if (name == null)
      throw new IllegalArgumentException("name cannot be null");

    if (symbol == null)
      throw new IllegalArgumentException("symbol cannot be null");

    if (displaySymbol == null)
      throw new IllegalArgumentException("displaySymbol cannot be null");

    name_ = name;
    symbol_ = symbol;
    a_ = a;
    b_ = b;
    c_ = c;
    d_ = d;
    displaySymbol_ = displaySymbol;
  }

  /**
   * Return name of this unit.
   *
   * @return  Name of this unit. Never null.
   */
  public String getName()
  {
    return name_;
  }

  /**
   * Return symbol of this unit.
   *
   * @return  Symbol of this unit. Never null.
   */
  public String getSymbol()
  {
    return symbol_;
  }

  /**
   * Return conversion factor a for this unit.
   *
   * @return  Conversion factor a for this unit.
   */
  double getA()
  {
    return a_;
  }

  /**
   * Return conversion factor b for this unit.
   *
   * @return  Conversion factor b for this unit.
   */
  double getB()
  {
    return b_;
  }

  /**
   * Return conversion factor c for this unit.
   *
   * @return  Conversion factor c for this unit.
   */
  double getC()
  {
    return c_;
  }

  /**
   * Return conversion factor d for this unit.
   *
   * @return  Conversion factor d for this unit.
   */
  double getD()
  {
    return d_;
  }

  /**
   * Return the display symbol of this unit.
   *
   * @return  The display symbol of this unit. Never null.
   */
  public String getDisplaySymbol()
  {
    return displaySymbol_;
  }

  /**
   * Convert the specified value to base unit in the quantity of this unit.
   *
   * @param value  Value to convert.
   * @return       Value converted to base unit.
   */
  public double toBase(double value)
  {
    double baseValue = (a_ * value + b_) / (c_ * value + d_);
    return baseValue;
  }

  /**
   * Convert the specified value given in base unit to this unit.
   *
   * @param baseValue  Base value to convert.
   * @return           Value converted to this unit.
   */
  public double fromBase(double baseValue)
  {
    double value = (b_ - d_ * baseValue) / (c_ * baseValue - a_);
    return value;
  }

  /** {@inheritDoc} */
  @Override
  public int hashCode()
  {
    return  1 * name_.hashCode() +
            3 * symbol_.hashCode() +
            7 * Double.valueOf(a_).hashCode() +
           11 * Double.valueOf(b_).hashCode() +
           13 * Double.valueOf(c_).hashCode() +
           17 * Double.valueOf(d_).hashCode();
  }

  /** {@inheritDoc} */
  @Override
  public boolean equals(Object object)
  {
    if (object == this)
      return true;

    if (!(object instanceof Unit))
      return false;

    Unit unit = (Unit) object;

    if (!name_.equals(unit.name_))
      return false;

    if (!symbol_.equals(unit.symbol_))
      return false;

    if (a_ != unit.a_)
      return false;

    if (b_ != unit.b_)
      return false;

    if (c_ != unit.c_)
      return false;

    if (d_ != unit.d_)
      return false;

    return true;
  }

  /** {@inheritDoc} */
  @Override
  public String toString()
  {
    return name_ + " [" + symbol_ + "]" + " a=" + a_ + " b=" + b_ + " c=" + c_ + " d=" + d_;
  }
}

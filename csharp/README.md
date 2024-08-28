# GeoSoft.Uom - Units of measurement library for .Net

When dealing with scientific data it is essential to know the units of
measurement in order to _understand_ and present the information correctly.
Likewise, in order to do _computations_ with scientific data it is essential
that software is able to convert data into a common unit framework.

The GeoSoft.Uom library is a convenient, extensible front-end to
[uom.json](/standard/uom.json) which itself is a simplified
view of the
[OSDU/Energistics Unit of Measure](https://energistics.org/energisticsr-consortium-publishes-new-version-its-unit-measure-standard).
It contains more than
250 different quantities with more than
2500 unit definitions.
The API is simple, well documented and easy to use, and the library is trivial
to embed in any scientific software system.

The library is lightweight (&lt; 0.1MB) and self-contained; It embeds the complete
unit database and has no external dependencies.



## Documentation

Full Doxygen documentation is available [here](https://htmlpreview.github.io/?https://raw.githubusercontent.com/geosoft-as/osdu-uom/main/csharp/docs/index.html).



## Examples

The easiest way to get started with juom is to explore the predefined
OSDU/Energistics quantities and units:

```C#
using GeoSoft.Uom;

:

//
// Get the unit manager singleton
//
UnitManager unitManager = UnitManager.GetInstance();

//
// Get all pre-defined quantities and their units
//
IList<Quantity> quantities = unitManager.GetQuantities();
foreach (Quantity quantity in quantities) {
  Console.WriteLine(quantity);
  foreach (Unit unit in quantity.GetUnits())
    Console.WriteLine("  Unit: " + unit.GetSymbol() + " (" + unit.GetName() + ")";
}

:
```


### Unit conversion

Basic unit conversion is done through `UnitManager`
using instances of `Unit` or the unit symbols directly:

```C#
//
// Convert between known units, using unit symbols directly
//
double milesPerHour = 55.0;
double kilometersPerHour = unitManager.Convert("mi/h", "km/h", milesPerHour);

//
// Conversion using Unit instances
//
Unit feet = unitManager.FindUnit("ft");

double lengthFt = 8981.0; // Length of the Golden Gate bridge in feet

Quantity quantity = unitManager.FindQuantity("length");
foreach (Unit unit in quantity.GetUnits()) {
  double length = unitManager.Convert(feet, unit, lengthFt);
  Console.WriteLine("Golden Gate is " + length + " " + unit.GetSymbol());
}
```

Making a user interface units aware includes associating
GUI components with quantities and then provide unit conversions,
either per element or as overall preference settings.

It is essential that the application knows the initial unit of measure
of the values involved. A common advice to reduce complexity and risk of errors
is to keep the entire data model in _base_ units (typically SI or similar)
and convert in GUI only on users request. The associated units will then
be _implied_, effectively making the entire business logic _unitless_.
Conversions to and from base units can be performed directly on the `Unit` instances:

```C#
//
// Capture pressure display unit from GUI or prefernces
//
Unit displayUnit = ...;
String displaySymbol = displayUnit.GetDisplaySymbol();

//
// Populate GUI element
//
double pressure = ...; // From business model, SI implied
pressureText.SetText(displayUnit.FromBase(pressure) + " [" + displaySymbol + "]");

:

//
// Capture user input
//
double value = pressureText.GetValue(); // In user preferred unit
double pressure = displayUnit.ToBase(value); // Converted to business model unit (SI)
```


It may make sense to provide unit conversion even if the quantity of a measure
is unknown. In these cases it is possible to obtain the quantity, but it might
be more convenient to get all convertible units directly:

```C#
//
// Given a unit, find the associated quantity
//
string unitSymbol = "degC"; // Degrees Celsius
Quantity quanitity = unitManager.FindQuantity(unitSymbol);
List<Unit> units = quantity.GetUnits(); // All temperature units

:

//
// Given a unit, find all convertible units
//
string unitSymbol = "degC"; // Degrees Celsius
List<Unit> units = unitManager.FindConvertibleUnits(unitSymbol);
```



## Unit aliases

There is no universal accepted standard or convention for unit symbols, and
to make the module more robust when dealing with units from various sources
it is possible to add unit _aliases_. juom uses the unit symbol defined
by OSDU/Energistics, but have added many aliases for common notations.
In addition, client applications can supply their own:

```C#
unitManager.AddUnitAlias("m/s^2", "m/s2");
unitManager.AddUnitAlias("inch", "in");
unitManager.AddUnitAlias("api", "gAPI");
unitManager.AddUnitAlias("deg", "dega");
:
```

The typical approach would be to read these from a properties file during startup.



## Display symbols

Unit symbols should be regarded as _IDs_, and clients
should never expose these directly in a user interface.
A GUI friendly _display symbol_ may be obtained through
the `Unit.GetDisplaySymbol()` method.

Many symbols will obviously equal their associated display symbol,
but the table below indicates some of the many that doesn't.
The table shows the connection between _unit name_,
_unit symbol_ and _display symbol_:


| Unit name             | Unit symbol | Display symbol   |
|-----------------------|-------------|------------------|
| microseconds per foot | us/ft       | &#181;s/ft       |
| ohm meter             | ohmm        | &#8486;&middot;m |
| cubic centimeters     | cm3         | cm<sup>3</sup>   |
| degrees Celcius       | degC        | &deg;C           |
| meter/second squared  | m/s2        | m/s<sup>2</sup>  |
| etc.                  |             |                  |




## Extensibility

If the predefined set of quantities and units is not sufficient, a client may
easily supply their own:

```java
//
// Define "computer storage" quantity with associated units
//
Quantity q = new Quantity("computer storage");
q.AddUnit(new Unit("byte", "byte", 1.0, 0.0, 0.0, 1.0, "byte"), true);
q.AddUnit(new Unit("kilo byte", "kB", 1.0e3, 0.0, 0.0, 1.0, "kB"), false);
q.AddUnit(new Unit("mega byte", "MB", 1.0e6, 0.0, 0.0, 1.0, "MB"), false);
q.AddUnit(new Unit("giga byte", "GB", 1.0e9, 0.0, 0.0, 1.0, "GB"), false);
:
unitManager.AddQuantity(q);

//
// Test the new units
//
long nBytes = 1230000L;
double nMegaBytes = unitManager.Convert("byte", "MB", nBytes); // 1.23
```



## Building

GeoSoft.Uom can be built from its root folder by

```
$ dotnet build
```

The GeoSoft.Uom delivery will be the `./bin/GeoSoft.Uom.dll` file.




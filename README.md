# OSDU/Energistics Unit of Measure Standard (UoM)

The OSDU UoM standard is a great idea, but its definition is hard to utilize in case an actual
implementation is needed.

The GeoSoft approach contains the minimal part of the standard that is required to give UoM support
to any software system.


## Repository content

The present repository contains the following:

* `xml/UnitDict_2.2.xml`  Version 2.2 of the Energistics Uom standard (2014) kept for reference
* `json/quantities.json`  A list of all quantities (250+) such as _pressure_ or _length_ etc.
* `json/quantities.json`  A list of all units (1200+) such as _m_ or _bar_ etc.



## Quantities and Units

A quantity is defined with a name, an optional description and the list of units that can measure it, like:

```JSON
{
  "name": "length per volume",
  "description": null,
  "units": [
    "per square metre",
    "feet/barrel",
    "feet/cubic foot",
    "feet/US gallon",
    "kilometres/cubic decimetre",
    "kilometres/litre",
    "metres/cubic metre",
    "miles/UK gallon",
    "miles/US gallon"
  ]
}
```

This first unit listed is defined as the _base unit for the quantity.

A unit is defined with a name, a symbol, a display symbol and factors to convert it to the base unit
of the quantity, like:

```JSON
{
  "name": "feet/barrel",
  "symbol": "ft/bbl",
  "displaySymbol": "ft/bbl",
  "a": 1.917134,
  "b": 0.0,
  "c": 0.0,
  "d": 1.0
}
```

Converting a value in a given unit into its corresponding _base_ unit is done by the formula:

```
baseValue = (a * value + b) / (c * value + d)
```


## Display units

Unit symbols should be regarded as _IDs_ suitable for persistent storeag etc., but clients
should never expose these directly in a user interface. For this reason an UTF8 _display symbol_
is provided with every unit, such as:


| Unit name             | Unit symbol | Display symbol   |
|-----------------------|-------------|------------------|
| microseconds per foot | us/ft       | &#181;s/ft       |
| ohm meter             | ohmm        | &#8486;&middot;m |
| cubic centimeters     | cm3         | cm<sup>3</sup>   |
| degrees Celcius       | degC        | &deg;C           |
| meter/second squared  | m/s2        | m/s<sup>2</sup>  |
| etc.                  |             |                  |


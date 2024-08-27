# OSDU/Energistics Unit of Measure Standard (UoM)

The Energistics/OSDU Unit of Measurements (UoM) is an impressive standard,
covering nearly 200 different quantities (_length_, _pressure_, _temperature_, _resistivity_, etc.)
and over 2000 units (_m_, _ft_, _bar_, _Pa_, etc.), complete with conversion factors.
This is a great foundation for any scientific software.

However, the standard has some deficiencies:

* It is not publicly available as it is limited to OSDU members only.
* The definitions are convoluted, making it awkward for practical use.
* It lacks a simple reference implementation or proof of concept (PoC).
* Display units are not supported.

The present solution addresses all these issues.
It includes a single, streamlined JSON file capturing the essentials of the standard,
and which can be applied to any programming platform in just a few lines of code.

Best of all, it includes proper _display units_, allowing the use of symbols like **°C**, **µΩ**, or **m³**
instead of the common but less readable _degC_, _uohm_, or _m3_ etc.



## Repository content

The present repository contains the following:

* `standard/uom.json`   A list of all units and quantities of the Energistics/OSDU UoM standard



## Quantities and Units

A _quantity_ is defined with its _name_, and the list of _units_ that can measure it, like:

```JSON
{
  "name": "length per volume",
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

The first unit listed is defined as the _base unit_ for the quantity.

A _unit_ is defined with its _name_, a _symbol_, a _display symbol_ and the factors to convert
it to the base unit of the quantity, like:

```JSON
{
  "name": "cubic centimetre",
  "symbol": "cm3",
  "displaySymbol": "cm³",
  "a": 0.0000010,
  "b": 0.0,
  "c": 0.0,
  "d": 1.0
}
```

Converting a value in a given unit into its corresponding base unit is done by:

```
baseValue = (a * value + b) / (c * value + d)
```


## Display units

Unit symbols should be regarded as _IDs_ suitable for persistent storage etc., but clients
should never expose these directly in a user interface. For UI purposes a UTF8 _display symbol_
is provided with every unit, such as:


| Unit name             | Unit symbol | Display symbol   |
|-----------------------|-------------|------------------|
| microseconds per foot | us/ft       | &#181;s/ft       |
| ohm meter             | ohmm        | &#8486;&middot;m |
| cubic centimeters     | cm3         | cm<sup>3</sup>   |
| degrees Celcius       | degC        | &deg;C           |
| meter/second squared  | m/s2        | m/s<sup>2</sup>  |



## Access libraries

The repository contains access libraries for UoM in a different programming environments:

* [Java](Java/README.md)

* [Python](Python/README.md)

* [C#](Python/README.md)

* [JavaScript](JavaScript/README.md)






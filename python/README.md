# UoM - Units of measurement library for Python

When dealing with scientific data it is essential to know the units of
measurement in order to understand and present the information correctly.
Likewise, in order to do computations with scientific data it is essential
that software is able to convert data into a common unit framework.

The GeoSoft UoM library is a convenient, extensible front-end to
[OSDU/Energistics Unit of Measure](https://energistics.org/energisticsr-consortium-publishes-new-version-its-unit-measure-standard).
It contains more than
250 different quantities with more than
2500 unit definitions.
The API is simple, well documented and easy to use, and the library is trivial
to embed in any scientific software system.

The library is lightweight and self-contained; It embeds the complete
unit database and has no external dependencies.


## Example Usage

run the example script with:
```python
python example_usage.py
```

You can expect an output like this:
```
Example 1: usage of the UnitManager class
electrical resistivity (The resistance , times the cross-sectional area , divided by the length.)
**************End of Example 1**************

Example 2: usage of the UnitManager class
metre: 10.0m = 10.0m
tenth of foot: 10.00.1 ft = 0.3048m
tenth of US survey foot: 10.00.1 ft[US] = 0.3048006096012192m
tenth of inch: 10.00.1 in = 0.025400000000000002m
tenth of yard: 10.00.1 yd = 0.9143999999999999m
sixteenth of inch: 10.01/16 in = 0.015875m
half of Foot: 10.01/2 ft = 1.524m
thirty-second of inch: 10.01/32 in = 0.0079375m
sixty-fourth of inch: 10.01/64 in = 0.00396875m
ten foot: 10.010 ft = 30.48m
ten inch: 10.010 in = 2.54m
10 kilometre: 10.010 km = 100000.0m
hundred foot: 10.0100 ft = 304.8m
100 kilometre: 10.0100 km = 1000000.0m
thousand foot: 10.01000 ft = 3048.0m
thirty foot: 10.030 ft = 91.44m
thirty metres: 10.030 m = 300.0m
exametre: 10.0Em = 1e+19m
gigametre: 10.0Gm = 10000000000.0m
megametre: 10.0Mm = 10000000.0m
terametre: 10.0Tm = 10000000000000.0m
angstrom: 10.0angstrom = 1e-09m
chain: 10.0chain = 201.168m
British chain [Benoit 1895 A]: 10.0chain[BnA] = 201.16782400000002m
British chain [Benoit 1895 B]: 10.0chain[BnB] = 201.16782494375872m
Clarke chain: 10.0chain[Cla] = 201.166195164m
Indian Chain [1937]: 10.0chain[Ind37] = 201.1669506m
British chain [Sears 1922 truncated]: 10.0chain[SeT] = 201.16755999999998m
British chain [Sears 1922]: 10.0chain[Se] = 201.16765121552632m
US survey chain: 10.0chain[US] = 201.1684023368047m
centimetre: 10.0cm = 0.1m
dekametre: 10.0dam = 100.0m
decimetre: 10.0dm = 1.0m
international fathom: 10.0fathom = 18.288m
femtometre: 10.0fm = 1.0000000000000002e-14m
foot: 10.0ft = 3.048m
British foot [Benoit 1895 A]: 10.0ft[BnA] = 3.047997333333333m
British foot [Benoit 1895 B]: 10.0ft[BnB] = 3.047997347632708m
British foot [1936]: 10.0ft[Br36] = 3.048007491m
British foot [1865]: 10.0ft[Br65] = 3.048008333333333m
Clarke foot: 10.0ft[Cla] = 3.047972654m
Gold Coast foot: 10.0ft[GC] = 3.047997101815088m
indian foot [1937]: 10.0ft[Ind37] = 3.0479841000000003m
indian foot ]1962]: 10.0ft[Ind62] = 3.047996m
indian foot [1975]: 10.0ft[Ind75] = 3.0479950000000002m
indian foot: 10.0ft[Ind] = 3.0479951024814693m
British foot [Sears 1922 truncated]: 10.0ft[SeT] = 3.0479933333333338m
British foot [Sears 1922]: 10.0ft[Se] = 3.0479947153867624m
US survey foot: 10.0ft[US] = 3.048006096012192m
furlong US survey: 10.0fur[US] = 2011.6840233680468m
hectometre: 10.0hm = 1000.0m
inch: 10.0in = 0.254m
US survey inch: 10.0in[US] = 0.254000508001016m
kilometre: 10.0km = 10000.0m
link: 10.0link = 2.01168m
British link [Benoit 1895 A]: 10.0link[BnA] = 2.01167824m
British link [Benoit 1895 B]: 10.0link[BnB] = 2.0116782494375873m
Clarke link: 10.0link[Cla] = 2.01166195164m
British link [Sears 1922 truncated]: 10.0link[SeT] = 2.0116756m
British link [Sears 1922]: 10.0link[Se] = 2.011676512155263m
US survey link: 10.0link[US] = 2.011684023368047m
German legal metre: 10.0m[Ger] = 10.000135965000002m
mile: 10.0mi = 16093.44m
US survey mile: 10.0mi[US] = 16093.472186944375m
United Kingdom nautical mile: 10.0mi[nautUK] = 18530.0m
international nautical mile: 10.0mi[naut] = 18520.0m
mil: 10.0mil = 0.000254m
millimetre: 10.0mm = 0.01m
nanometre: 10.0nm = 1e-08m
picometre: 10.0pm = 1e-11m
rod US Survey: 10.0rod[US] = 50.292100584201165m
micrometre: 10.0um = 9.999999999999999e-06m
yard: 10.0yd = 9.144m
British yard [Benoit 1895 A]: 10.0yd[BnA] = 9.143991999999999m
British yard [Benoit 1895 B]: 10.0yd[BnB] = 9.143992042898123m
Clarke yard: 10.0yd[Cla] = 9.143917962m
Indian yard [1937]: 10.0yd[Ind37] = 9.1439523m
Indian yard [1962]: 10.0yd[Ind62] = 9.143988m
Indian yard [1975]: 10.0yd[Ind75] = 9.143985m
Indian yard: 10.0yd[Ind] = 9.143985307444408m
British yard [Sears 1922 truncated]: 10.0yd[SeT] = 9.14398m
British yard [Sears 1922]: 10.0yd[Se] = 9.143984146160287m
US survey yard: 10.0yd[US] = 9.144018288036577m
**************End of Example 2**************

Example 3: usage of the UnitManager class
55.0 miles per hour is equal to 88.51392 kilometers per hour
**************End of Example 3**************
```

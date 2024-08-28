# UoM .Net #

.Net (C#) version of the Java UoM library.

![UoM I/O library](https://geosoft.no/images/UomBox.250.png)

UoM webpage: [https://geosoft.no/products/uom.html](https://geosoft.no/products/uom.html)


### Setup ###

Capture the UoM .Net code to local disk by:

```
$ git clone https://<token>@github.com/rabbagast/Uom.net
```


### Dependencies ###

UoM .Net has no external dependenies.


### Building UoM .Net ###

Double click the `Uom.Net.sln` file will open Visual Studio.
Select _Build->Build Solution_.

From the command line:

```
$ make
```

We build against _.NET Standard 2.0_ (RMB on Uom.Net in Visual Studio and
select _Properties..._. This will make `Uom.dll` compatible with applications
built for any platform.


### Creating API documentation ###

Doxygen configuration is in `./Doxyfile`. Ducomentation is generated from the root
directory by:

```
$ make doc
$ make brand
$ make analytics
```

The command will populate the `./docs` tree, entry point will be `./docs/index.html`.

Note the `./MainPage.dox` page that becomes part of the documentation.

The _brand_ step will give GeoSoft coloring and embed the GeoSoft logo
while the _analytics_ step will embed Google analytics code for tracking.


### Coding style ###

Namespaces and method style to align with common C# accepted
conventions. But we left in getters and setters methods instead of making
them properties to keep the code closer to the original.

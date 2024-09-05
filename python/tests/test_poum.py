from poum import UnitManager


def test_find_quantity():
    unit_manager = UnitManager.get_instance()
    quantity = unit_manager.find_quantity("length")
    assert quantity.name == "length"
    assert quantity.base_unit.name == "metre"
    assert quantity.base_unit.symbol == "m"


def test_find_unit():
    unit_manager = UnitManager.get_instance()
    unit = unit_manager.find_unit("ohm.m")
    assert unit.name == "ohm metre"
    assert unit.symbol == "ohm.m"


def test_find_quantities_by_unit():
    unit_manager = UnitManager.get_instance()
    unit = unit_manager.find_unit("ohm.m")
    quantities = unit_manager.find_quantities_by_unit(unit)
    assert len(quantities) == 1
    assert quantities[0].name == "electrical resistivity"


def test_find_quantities_by_symbol():
    unit_manager = UnitManager.get_instance()
    quantities = unit_manager.find_quantities_by_symbol("ohm.m")
    assert len(quantities) == 1
    assert quantities[0].name == "electrical resistivity"


def test_convert():
    unit_manager = UnitManager.get_instance()
    miles_per_hour = 55.0
    kilometers_per_our = unit_manager.convert("mi/h", "km/h", miles_per_hour)
    assert kilometers_per_our == 88.51392


def test_find_convertible_units():
    unit_manager = UnitManager.get_instance()
    unit = unit_manager.find_unit("mi/h")
    units = unit_manager.find_convertible_units(unit)
    unit_names = [unit.name for unit in units]
    assert "metre per second" in unit_names


def test_find_convertible_units_by_symbol():
    unit_manager = UnitManager.get_instance()
    unit = unit_manager.find_unit("mi/h")
    units = unit_manager.find_convertible_units(unit)
    unit_symbols = [unit.symbol for unit in units]
    assert "m/s" in unit_symbols


def test_can_convert():
    unit_manager = UnitManager.get_instance()
    unit1 = unit_manager.find_unit("mi/h")
    unit2 = unit_manager.find_unit("m/s")
    assert unit_manager.can_convert(unit1, unit2)
    assert unit_manager.can_convert(unit2, unit1)

    unit3 = unit_manager.find_unit("ohm.m")
    assert not unit_manager.can_convert(unit1, unit3)



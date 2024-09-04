import json
from decimal import Decimal
from threading import Lock
from typing import Optional, List, Sequence


class Unit:
    name_: str
    symbol_: str
    a_: Decimal
    b_: Decimal
    c_: Decimal
    d_: Decimal
    display_symbol_: str

    def __init__(self, name: str, symbol: str, a: Decimal, b: Decimal, c: Decimal, d: Decimal, display_symbol: str):
        self.name_ = name
        self.symbol_ = symbol
        self.a_ = a
        self.b_ = b
        self.c_ = c
        self.d_ = d
        self.display_symbol_ = display_symbol

    @property
    def name(self) -> str:
        return self.name_

    @property
    def symbol(self) -> str:
        return self.symbol_

    @property
    def get_a(self) -> Decimal:
        return self.a_

    @property
    def get_b(self) -> Decimal:
        return self.b_

    @property
    def get_c(self) -> Decimal:
        return self.c_

    @property
    def get_d(self) -> Decimal:
        return self.d_

    @property
    def display_symbol(self) -> str:
        return self.display_symbol_

    def set_a(self, a: Decimal):
        self.a_ = a

    def set_b(self, b: Decimal):
        self.b_ = b

    def set_c(self, c: Decimal):
        self.c_ = c

    def set_d(self, d: Decimal):
        self.d_ = d

    def set_display_symbol(self, display_symbol: str):
        self.display_symbol_ = display_symbol

    def to_base(self, value: Decimal) -> Decimal:
        return ((self.a_ * value) + self.b_) / ((self.c_ * value) + self.d_)

    def from_base(self, value: Decimal) -> Decimal:
        return (self.b_ - (self.d_ * value)) / ((self.c_ * value) - self.a_)

    def __str__(self):
        return f'{self.name_} [{self.symbol_}] a={self.a_}, b={self.b_}, c={self.c_}, d={self.d_}'

    def __repr__(self):
        return f'{self.name_} [{self.symbol_}] a={self.a_}, b={self.b_}, c={self.c_}, d={self.d_}'

    def __eq__(self, other):
        return (self.name_ == other.name_ and self.symbol_ == other.symbol_ and self.a_ == other.a_ and
                self.b_ == other.b_ and self.c_ == other.c_ and self.d_ == other.d_)

    def __hash__(self):
        return int(
                1 * self.name_.__hash__() +
                3 * self.symbol_.__hash__() +
                7 * float(self.a_).__hash__() +
                11 * float(self.b_).__hash__() +
                13 * float(self.c_).__hash__() +
                17 * float(self.d_.__hash__())
        )


class Quantity:
    name_: str
    units_: list[Unit]

    def __init__(self, name: str, units: list = None):
        self.name_ = name
        self.units_ = units or []

    @property
    def name(self) -> str:
        return self.name_

    @property
    def units(self) -> list:
        return self.units_

    def add_unit(self, unit: Unit, is_base_unit: bool = False):
        if is_base_unit:
            self.units_.insert(0, unit)
        else:
            self.units_.append(unit)

    @property
    def base_unit(self) -> Unit:
        return self.units_[0]

    def __str__(self):
        def get_unit_str():
            return '\n'.join([f"{unit.name} ({unit.display_symbol})" for unit in self.units_])
        return f'Quantity Name: {self.name_} \nUnits: \n{get_unit_str()}'


class UnitManager:
    UNITS_FILE = "poum/uom.json"
    UNIT_ALIASES_FILE = "poum/unit_aliases.txt"

    _instance = None
    _lock = Lock()

    def __init__(self):
        self.unit_aliases_ = {}
        self.quantities_ = set()
        self._load_from_json()
        self._load_unit_aliases()

    @classmethod
    def get_instance(cls):
        with cls._lock:
            if cls._instance is None:
                cls._instance = cls()
        return cls._instance

    def add_unit_alias(self, unit_symbol_alias: str, unit_symbol: str):
        if unit_symbol_alias is None:
            raise ValueError("unitSymbolAlias cannot be null")
        if unit_symbol is None:
            raise ValueError("unitSymbol cannot be null")
        self.unit_aliases_[unit_symbol_alias.lower()] = unit_symbol

    def get_quantities(self):
        return self.quantities_

    def add_quantity(self, quantity: Quantity):
        if quantity is None:
            raise ValueError("quantity cannot be null")
        if self.find_quantity(quantity.name) is not None:
            raise ValueError(f"Quantity is already present: {quantity.name}")
        self.quantities_.add(quantity)

    def find_quantity(self, quantity_name: str) -> Quantity | None:
        if quantity_name is None:
            raise ValueError("quantityName cannot be null")
        for quantity in self.quantities_:
            if quantity.name == quantity_name:
                return quantity
        return None

    def get_units(self):
        units = {}
        for quantity in self.quantities_:
            for unit in quantity.units:
                units[unit.name] = unit
        return list(units.values())

    def find_unit(self, unit_symbol: str) -> Optional[Unit]:
        if unit_symbol is None or unit_symbol.strip() == "":
            unit_symbol = "unitless"

        lower_case_symbol = unit_symbol.lower().strip()

        actual_unit_symbol = self.unit_aliases_.get(lower_case_symbol)
        if actual_unit_symbol is not None:
            unit_symbol = actual_unit_symbol

        for quantity in self.quantities_:
            for unit in quantity.units:
                if unit.symbol == unit_symbol:
                    return unit

        for quantity in self.quantities_:
            for unit in quantity.units:
                if unit.symbol.lower() == lower_case_symbol:
                    return unit

        return None

    def find_convertible_units(self, unit: Unit) -> List[Unit]:
        if unit is None:
            raise ValueError("unit cannot be null")
        units = set()
        for quantity in self.quantities_:
            if unit in quantity.units:
                units.update(quantity.units)
        units.discard(unit)
        return list(units)

    def find_convertible_units_by_symbol(self, unit_symbol: str) -> List['Unit']:
        unit = self.find_unit(unit_symbol)
        return self.find_convertible_units(unit) if unit else []

    def find_quantities_by_unit(self, unit: Unit) -> List[Quantity]:
        if unit is None:
            raise ValueError("unit cannot be null")
        quantities = []
        for quantity in self.quantities_:
            if unit in quantity.units:
                quantities.append(quantity)
        euclid_unit = self.find_unit("Euc")
        for quantity in quantities:
            if euclid_unit in quantity.units:
                quantities.append(self.find_quantity("dimensionless"))
                break
        return quantities

    def find_quantities_by_symbol(self, unit_symbol: str) -> List[Quantity]:
        unit = self.find_unit(unit_symbol)
        return self.find_quantities_by_unit(unit) if unit else []

    def find_quantity_by_unit(self, unit: Unit) -> Optional[Quantity]:
        if unit is None:
            raise ValueError("unit cannot be null")
        quantities = self.find_quantities_by_unit(unit)
        if len(quantities) == 0:
            return None
        elif len(quantities) == 1:
            return quantities[0]
        elif "time" in [q.name for q in quantities]:
            return self.find_quantity("time")
        else:
            return quantities[0]

    def can_convert(self, unit1: Unit, unit2: Unit) -> bool:
        if unit1 is None or unit2 is None:
            raise ValueError("unit1 and unit2 cannot be null")
        quantities1 = self.find_quantities_by_unit(unit1)
        quantities2 = self.find_quantities_by_unit(unit2)
        return any(q in quantities2 for q in quantities1)

    def can_convert_by_symbol(self, unit_symbol1: str, unit_symbol2: str) -> bool:
        if unit_symbol1 is None or unit_symbol2 is None:
            return False
        unit1 = self.find_unit(unit_symbol1)
        unit2 = self.find_unit(unit_symbol2)
        return self.can_convert(unit1, unit2) if unit1 and unit2 else False

    def convert(self, from_unit: Unit | str, to_unit: Unit | str, value: Decimal) -> Decimal:
        if isinstance(from_unit, str):
            from_unit = self.find_unit(from_unit)
        if isinstance(to_unit, str):
            to_unit = self.find_unit(to_unit)
        if from_unit is None or to_unit is None:
            raise ValueError("fromUnit and toUnit cannot be null")
        base_value = from_unit.to_base(value)
        return to_unit.from_base(base_value)

    def convert_by_symbol(self, from_unit_symbol: str, to_unit_symbol: str, value: Decimal) -> Decimal:
        if from_unit_symbol is None or to_unit_symbol is None:
            raise ValueError("fromUnitSymbol and toUnitSymbol cannot be null")
        from_unit = self.find_unit(from_unit_symbol)
        to_unit = self.find_unit(to_unit_symbol)
        return self.convert(from_unit, to_unit, value) if from_unit and to_unit else value

    @staticmethod
    def get_display_symbol_by_unit(unit: Unit) -> str:
        return unit.display_symbol if unit else ""

    def find_or_create_quantity(self, quantity_name: str) -> Quantity:
        assert quantity_name is not None, "quantityName cannot be null"
        quantity = self.find_quantity(quantity_name)
        if quantity is None:
            quantity = Quantity(quantity_name)
            self.quantities_.add(quantity)
        return quantity

    def _load_unit_aliases(self):
        try:
            with open(self.UNIT_ALIASES_FILE, 'r') as stream:
                for line in stream.readlines():
                    if line.startswith("#") or "=" not in line:
                        continue
                    key, value = line.strip().split('=')
                    self.unit_aliases_[key.strip()] = value.strip()
        except IOError:
            pass

    @staticmethod
    def _find_unit_by_name(units: Sequence[Unit], name: str) -> Optional[Unit]:
        for unit in units:
            if unit.name == name:
                return unit
        return None

    def _load_from_json(self):
        units = []
        try:
            with open(self.UNITS_FILE, 'r') as stream:
                energistics_standard = json.load(stream)

                unit_objects = energistics_standard.get("units", [])
                for unit_object in unit_objects:
                    name = unit_object["name"]
                    symbol = unit_object["symbol"]
                    a = unit_object["a"]
                    b = unit_object["b"]
                    c = unit_object["c"]
                    d = unit_object["d"]
                    display_symbol = unit_object.get("display_symbol", symbol)
                    unit = Unit(name, symbol, a, b, c, d, display_symbol)
                    units.append(unit)

                quantity_objects = energistics_standard.get("quantities", [])
                for quantity_object in quantity_objects:
                    name = quantity_object["name"]
                    description = quantity_object.get("description", None)
                    quantity = Quantity(name, description)
                    self.quantities_.add(quantity)
                    member_units = quantity_object.get("units", [])
                    for unit_name in member_units:
                        unit = self._find_unit_by_name(units, unit_name)
                        quantity.add_unit(unit, False)

        except Exception as e:
            print(e)

    def __str__(self):
        s = [f"Quantities....: {len(self.quantities_)}\n"]
        n_units = sum(len(quantity.units) for quantity in self.quantities_)
        s.append(f"Units.........: {n_units}\n")
        s.append(f"Unit aliases..: {len(self.unit_aliases_)}")
        return ''.join(s)


__all__ = ['Unit', 'Quantity', 'UnitManager']

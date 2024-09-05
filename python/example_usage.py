from poum import UnitManager

# Example 1:  usage of the UnitManager class
print("Example 1: usage of the UnitManager class")
unit_manager = UnitManager.get_instance()
unit = unit_manager.find_unit("ohm.m")
quantities = unit_manager.find_quantities_by_unit(unit)
for q in quantities:
    print(q)
print("**************End of Example 1**************\n")

# Example 2:  usage of the UnitManager class
print("Example 2: usage of the UnitManager class")
unit_manager = UnitManager.get_instance()
quantity = unit_manager.find_quantity("length")

base_unit = quantity.base_unit

for unit in quantity.units:
    print(f"{unit.name}: 10.0{unit.symbol} = {unit.to_base(10.0)}{quantity.base_unit.symbol}")

print("**************End of Example 2**************\n")


# Example 3:  usage of the UnitManager class
print("Example 3: usage of the UnitManager class")
unit_manager = UnitManager.get_instance()
miles_per_hour = 55.0
kilometers_per_our = unit_manager.convert("mi/h", "km/h", miles_per_hour)
print(f"{miles_per_hour} miles per hour is equal to {kilometers_per_our} kilometers per hour")
print("**************End of Example 3**************\n")








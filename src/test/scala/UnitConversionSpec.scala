import org.scalatest.funsuite.AnyFunSuite

class UnitConversionSpec extends AnyFunSuite
{
    val p1 = new UnitConversion("Air Pressure", 10.0, "Pa");
    val p2 = new UnitConversion("Air pressure", 10.0, "kPa");
    val p3 = new UnitConversion("VPD", 10.0, "mbar");
    val p4 = new UnitConversion("Vpd", 10.0, "cbar");
    val p5 = new UnitConversion("Air Pressure", 10.0, "bar");
    val p6 = new UnitConversion("Air Pressure", 10.0, "hPa");
    val p7 = new UnitConversion("Air Pressure", 10.0, "mmHg");
    val p8 = new UnitConversion("Air Pressure", 10.0, "psi");
    val p9 = new UnitConversion("test", 10.0, "test");  // Test NaN and NoMatch

    test("All sensors with Pressure quantity convert unit and value to SI base unit."){
        assert(p1.getUnitSymbol === "Pa" && p1.getValue === 10.0)
        assert(p2.getUnitSymbol === "Pa" && p2.getValue === 10000.0)
        assert(p3.getUnitSymbol === "Pa" && p3.getValue === 1000.0)
        assert(p4.getUnitSymbol === "Pa" && p4.getValue === 10000.0)
        assert(p5.getUnitSymbol === "Pa" && p5.getValue === 1000000.0)
        assert(p6.getUnitSymbol === "Pa" && p6.getValue === 1000.0)
        assert(p7.getUnitSymbol === "Pa" && p7.getValue === 1333.22387415)
        assert(p8.getUnitSymbol === "Pa" && p8.getValue === 68947.57293168361)
        assert(p9.getUnitSymbol === "test" && p9.getValue.isNaN)
    }

    val e1 = new UnitConversion("Battery", 10.0, "mV")
    val e2 = new UnitConversion("battery", 10.0, "mV")
    val e3 = new UnitConversion("Solar Panel", 10.0, "V")
    val e4 = new UnitConversion("Solar panel", 10.0, "V")

    test("All sensors with ElectricPotential quantity convert unit and value to SI base unit."){
        assert(e1.getUnitSymbol === "V" && e1.getValue === 0.01)
        assert(e2.getUnitSymbol === "V" && e2.getValue === 0.01)
        assert(e3.getUnitSymbol === "V" && e3.getValue === 10.0)
        assert(e4.getUnitSymbol === "V" && e4.getValue === 10.0)
    }

    val pre1 = new UnitConversion("Precipitation", 10.0, "mm")
    val pre2 = new UnitConversion("precipitation", 10.0, "cm")
    val pre3 = new UnitConversion("Precipitation", 10.0, "m")
    val pre4 = new UnitConversion("Precipitation", 10.0, "in")
    val pre5 = new UnitConversion("precipitation", 10.0, "ft")
    val pre6 = new UnitConversion("Precipitation", 10.0, "yd")

    test("All sensors with Precipitation quantity convert unit and value to SI base unit."){
        assert(pre1.getUnitSymbol === "m" && pre1.getValue === 0.01)
        assert(pre2.getUnitSymbol === "m" && pre2.getValue === 0.1)
        assert(pre3.getUnitSymbol === "m" && pre3.getValue === 10.0)
        assert(pre4.getUnitSymbol === "m" && pre4.getValue === 0.254000508)
        assert(pre5.getUnitSymbol === "m" && pre5.getValue === 3.048006096)
        assert(pre6.getUnitSymbol === "m" && pre6.getValue === 9.144018288)
    }

    val temp1 = new UnitConversion("HC Air temperature", 10.0, "°C")
    val temp2 = new UnitConversion("HC air temperature", 10.0, "°F")
    val temp3 = new UnitConversion("DeltaT", 10.0, "°C")
    val temp4 = new UnitConversion("Dew Point", 10.0, "°F")

    test("All sensors with TemperatureScale quantity convert unit and value to SI base unit."){
        assert(temp1.getUnitSymbol === "K" && temp1.getValue === 283.15)
        assert(temp2.getUnitSymbol === "K" && temp2.getValue === 260.92777777777775)
        assert(temp3.getUnitSymbol === "K" && temp3.getValue === 283.15)
        assert(temp4.getUnitSymbol === "K" && temp4.getValue === 260.92777777777775)
    }

    val vel1 = new UnitConversion("U-sonic wind speed", 10.0, "m/s")
    val vel2 = new UnitConversion("U-sonic Wind Speed", 10.0, "km/h")
    val vel3 = new UnitConversion("Wind gust", 10.0, "mph")
    val vel4 = new UnitConversion("Wind Gust", 10.0, "ft/s")
    val vel5 = new UnitConversion("Wind gust", 10.0, "ft/min")
    val vel6 = new UnitConversion("Wind gust", 10.0, "yd/min")
    val vel7 = new UnitConversion("Wind gust", 10.0, "kn")

    test("All sensors with Velocity quantity convert unit and value to SI base unit."){
        assert(vel1.getUnitSymbol === "m/s" && vel1.getValue === 10.0)
        assert(vel2.getUnitSymbol === "m/s" && vel2.getValue === 2.7777777777777777)
        assert(vel3.getUnitSymbol === "m/s" && vel3.getValue === 4.4704089408)
        assert(vel4.getUnitSymbol === "m/s" && vel4.getValue === 3.048006096)
        assert(vel5.getUnitSymbol === "m/s" && vel5.getValue === 0.050800101599999994)
        assert(vel6.getUnitSymbol === "m/s" && vel6.getValue === 0.1524003048)
        assert(vel7.getUnitSymbol === "m/s" && vel7.getValue === 5.144444444444445)
    }

    val time1 = new UnitConversion("Leaf Wetness", 10.0, "sec")
    val time2 = new UnitConversion("Leaf wetness", 10.0, "min")
    val time3 = new UnitConversion("Leaf Wetness", 10.0, "h")

    test("All sensors with Time quantity convert unit and value to SI base unit."){
        assert(time1.getUnitSymbol === "s" && time1.getValue === 10.0)
        assert(time2.getUnitSymbol === "s" && time2.getValue === 600.0)
        assert(time3.getUnitSymbol === "s" && time3.getValue === 36000.0)
    }

    val angle1 = new UnitConversion("U-sonic wind dir", 10.0, "deg")
    val angle2 = new UnitConversion("U-sonic wind dir", 10.0, "rad")

    test("All sensors with Angles quantity convert unit and value to SI base unit."){
        assert(angle1.getUnitSymbol === "rad" && angle1.getValue === 0.17453292519943295)
        assert(angle2.getUnitSymbol === "rad" && angle2.getValue === 10.0)
    }

    val humidity1 = new UnitConversion("HC Relative humidity", 10.0, "ea")
    val humidity2 = new UnitConversion("HC Relative humidity", 20.0, "ea")

    test("All sensors with Humidity quantity convert unit and value to SI base unit."){
        assert(humidity1.getUnitSymbol === "ea" && humidity1.getValue === 10.0)
        assert(humidity2.getUnitSymbol === "ea" && humidity2.getValue === 20.0)
    }

    val rad1 = new UnitConversion("Solar radiation", 10.0, "W/m2")
    val rad2 = new UnitConversion("Solar radiation", 10.0, "J/m2")
    val rad3 = new UnitConversion("Solar radiation", 10.0, "kJ/m2")
    val rad4 = new UnitConversion("Solar radiation", 10.0, "MJ/m2")

    test("All sensors with Solar radiation quantity convert unit and value to SI base unit."){
        assert(rad1.getUnitSymbol === "W/m2" && rad1.getValue === 10.0)
        assert(rad2.getUnitSymbol === "J/m2" && rad2.getValue === 10.0)
        assert(rad3.getUnitSymbol === "kJ/m2" && rad3.getValue === 10.0)
        assert(rad4.getUnitSymbol === "MJ/m2" && rad4.getValue === 10.0)
    }
}
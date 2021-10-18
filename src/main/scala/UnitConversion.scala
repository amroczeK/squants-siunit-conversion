import squants.{Dimension, Dimensionless, UnitOfMeasure}
import squants.electro._
import squants.space._
import squants.motion._
import squants.thermal._
import squants.time._
import squants.radio._
import org.apache.spark.sql.functions._

/**
  * This class aims to provide the ability to convert any incoming sensor data
  * to their SI Base Unit defined below in Quantity.
  * 
  * E.g. Incoming data 10.0 kPa will be converted to 10000 Pa
  *
  * @param sensorName String - Sensor name
  * @param value Double - Sensor data
  * @param unitSymbol String - Unit symbol for data 
  */
final class UnitConversion(var sensorName: String, var value: Double, var unitSymbol: String) {
    private val _PressureSensorNames: List[String] = List("Air Pressure", "VPD")
    private val _ElectricPotentialSensorNames: List[String] = List("Battery", "Solar panel")
    private val _PrecipicationSensorNames: List[String] = List("Precipitation")
    private val _TemperatureSensorNames: List[String] = List("HC Air temperature", "DeltaT", "Dew Point")
    private val _VelocitySensorNames: List[String] = List("Wind gust", "U-sonic wind speed")
    private val _IrradianceSensorNames: List[String] = List("Solar radiation")
    private val _TimeMeasureSensorNames: List[String] = List("Leaf Wetness")
    private val _AngleMeasureSensorNames: List[String] = List("U-sonic wind dir")
    private val _HumiditySensorNames: List[String] = List("HC Relative humidity")

    private val _morePressures: Set[UnitOfMeasure[Pressure]] = Set(KiloPascal,MBar,CBar,HPa)
    private val _moreVelocities: Set[UnitOfMeasure[Velocity]] = Set(FeetPerMin,YardPerMin)
    private val _moreTimes: Set[UnitOfMeasure[Time]] = Set(Sec,Min)
    private val _moreIrradiances: Set[UnitOfMeasure[Irradiance]] = Set(Wm2)
    private val _moreAngles: Set[UnitOfMeasure[Angle]] = Set(Deg)
    
    private val _sensorName: String = sensorName
    private var _unitSymbol: String = unitSymbol
    private val _value: Double = setValue(sensorName, value, unitSymbol)

    def checkSensorName(name: String, sensorNames: List[String]): Boolean = {
      for (s <- sensorNames if s.equalsIgnoreCase(name)) {
        return true
      }
      return false
    }

    def setValue(sensorName: String, value: Double, unitSymbol: String): Double = sensorName match {
        case s if checkSensorName(s, _PressureSensorNames) => {
          var convertedValue: Double = getSi(Pressure, value, unitSymbol)
          if(convertedValue.isNaN()) {
            convertedValue = getSi(Pressure, _morePressures, value, unitSymbol)
          }
          return convertedValue
        }
        case s if checkSensorName(s, _ElectricPotentialSensorNames) => {
          var convertedValue: Double = getSi(ElectricPotential, value, unitSymbol)
          return convertedValue
        }
        case s if checkSensorName(s, _PrecipicationSensorNames) => {
          var convertedValue: Double = getSi(Length, value, unitSymbol)
          return convertedValue
        }
        case s if checkSensorName(s, _TemperatureSensorNames) => {
          var convertedValue: Double = getSi(Temperature, value, unitSymbol)
          return convertedValue
        }
        case s if checkSensorName(s, _VelocitySensorNames) => {
          var convertedValue: Double = getSi(Velocity, value, unitSymbol)
          if(convertedValue.isNaN()) {
            convertedValue = getSi(Velocity, _moreVelocities, value, unitSymbol)
          }
          return convertedValue
        }
        // NOTE: Irradiance/Radiation conversion is not being supported because we need to know
        // the time exposed which differs per sensor and difficult to find out before hand
        // https://electronics.stackexchange.com/questions/538921/how-to-convert-calculate-textkj-textm2-to-textw-textm2
        case s if checkSensorName(s, _IrradianceSensorNames) => {
          // var convertedValue: Double = getSi(Irradiance, value, unitSymbol)
          // if(convertedValue.isNaN()) {
          //   convertedValue = getSi(Irradiance, _moreIrradiances, value, unitSymbol)
          // }
          // return convertedValue
          return value
        }
        case s if checkSensorName(s, _TimeMeasureSensorNames) => {
          var convertedValue: Double = getSi(Time, value, unitSymbol)
          if(convertedValue.isNaN()) {
            convertedValue = getSi(Time, _moreTimes, value, unitSymbol)
          }
          return convertedValue
        }
        case s if checkSensorName(s, _AngleMeasureSensorNames) => {
          var convertedValue: Double = getSi(Angle, value, unitSymbol)
          if(convertedValue.isNaN()) {
            convertedValue = getSi(Angle, _moreAngles, value, unitSymbol)
          }
          return convertedValue
        }
        case s if checkSensorName(s, _HumiditySensorNames) => {
          var convertedValue: Double = getSi(Dimensionless, value, unitSymbol)
          return convertedValue
        }
        case _ => Double.NaN
    }

    def setUnitSymbol(symbol: String) {
      _unitSymbol = symbol;
    }

    def getUnitSymbol: String = _unitSymbol
    def getValue: Double = _value

    def getSi[T <: squants.Quantity[T]] (dimension: Dimension[T], value: Double, unitSymbol: String): Double = {
      for(  u <- dimension.units if(u.symbol == unitSymbol)) {
        setUnitSymbol(u(value).in(dimension.siUnit).unit.symbol)
        return u(value).in(dimension.siUnit).value
      }
      return Double.NaN
    }

    def getSi[T <: squants.Quantity[T]] (
      dimension: Dimension[T],
      moreUom: Set[UnitOfMeasure[T]],
      value: Double,
      unitSymbol: String): Double = {
      for(  u <- dimension.units if(u.symbol == unitSymbol)) {
        setUnitSymbol(u(value).in(dimension.siUnit).unit.symbol)
        return u(value).in(dimension.siUnit).value
      }
      for(  u <- moreUom if(u.symbol == unitSymbol)) {
        setUnitSymbol(u(value).in(dimension.siUnit).unit.symbol)
        return u(value).in(dimension.siUnit).value 
      }
      return value
    }

    // def roundTo2Dp(value: Double): Double = {
    //     BigDecimal(value).setScale(2, BigDecimal.RoundingMode.HALF_UP).toDouble
    // }

    override def toString: String = s"Value: ${_value}, Unit symbol: ${_unitSymbol}"
}

/**
  * These objects are extensions of existing Squants units because the units below
  * don't exist in the library. Add new units here and extend from the base Quantity in Squants.
  */
object KiloPascal extends PressureUnit {
    val symbol = "kPa"
    val conversionFactor = 1000d
}
object MBar extends PressureUnit {
  val symbol = "mbar"
  val conversionFactor = 100d
}
object CBar extends PressureUnit {
  val symbol = "cbar"
  val conversionFactor = 1000d
}
object HPa extends PressureUnit {
  val symbol = "hPa"
  val conversionFactor = 100d
}
object FeetPerMin extends VelocityUnit {
  val symbol = "ft/min"
  val conversionFactor = (Feet.conversionFactor / Meters.conversionFactor) / Time.MinutesPerHour
}
object YardPerMin extends VelocityUnit {
  val symbol = "yd/min"
  val conversionFactor = (Yards.conversionFactor / Meters.conversionFactor) / Time.MinutesPerHour
}
object Sec extends TimeUnit {
  val symbol = "sec"
  val conversionFactor = Seconds.conversionFactor
}
object Min extends TimeUnit {
  val symbol = "min"
  val conversionFactor = Minutes.conversionFactor
}
object Wm2 extends IrradianceUnit {
  val symbol = "W/m2"
  val conversionFactor = 999.0  // Change this
}
object Deg extends AngleUnit {
  val symbol = "deg"
  val conversionFactor = Degrees.conversionFactor
}

// Use these functions on dataframes during pivot for your spark transformer
object UnitConversionFunctions {
    def transformValue = udf((sensorName: String, value: Double, unitSymbol: String) => {
        val sensor = new UnitConversion(sensorName, value, unitSymbol)
        sensor.getValue
    })

    def transformUnit = udf((sensorName: String, value: Double, unitSymbol: String) => {
        val sensor = new UnitConversion(sensorName, value, unitSymbol)
        sensor.getUnitSymbol
    })
}
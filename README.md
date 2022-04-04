# squants-siunit-conversion
Convert incoming data values and units to their base SI unit using Scala and Squants library.

Use the UnitConversionFunctions on the dataframe in your Spark Transformer during the pivot if you want to convert data coming through your data lake from Silver to Gold

## Usage:
```
import UnitConversionFunctions.{transformUnit, transformValue}

// Weather data is Pivoted based on the list of measurement types that are specified. Similar columns that are specified by the map would be renamed before the pivoting
val pivotedDataFrame = dataFrame
.withColumn("Measurement_Type", concat(col("source__Sensor__name"), col("aggregationType__name"))) // Fully qualify the measurement type for the pivot i.e. "Average Wind Speed", "Min Wind Speed" etc.
.withColumn("Measurement_Type", expr(columnRenameExpr))
.withColumn("value",
        when(!col("source__Sensor__name").isNull, 
        transformValue(col("source__Sensor__name"), col("value"), col("unit__symbol")))
    )
.withColumn("unit__symbol",
    when(!col("source__Sensor__name").isNull, 
    transformUnit(col("source__Sensor__name"), col("value"), col("unit__symbol")))
)
.groupBy($"time__Instant__utc", $"source__Sensor__device__name", $"loc__GpsPoint__tz__name", $"loc__GpsPoint__tz__offset")
.pivot($"Measurement_Type",
    measurementTypeList.filter(m => !(newColumnMapping.values.flatten.map(_._1).toList.contains(m))) //List of measurement types minus the columns which have been renamed
    ++ newColumnMapping.values.flatten.map(_._2).toList) // columns with new names assigned to them
.agg(first("value", ignoreNulls = true))
```

## Running Test Suite
```
sbt test
```

```
[info] UnitConversionSpec:
[info] - All sensors with Pressure quantity convert unit and value to SI base unit.
[info] - All sensors with ElectricPotential quantity convert unit and value to SI base unit.
[info] - All sensors with Precipitation quantity convert unit and value to SI base unit.
[info] - All sensors with TemperatureScale quantity convert unit and value to SI base unit.
[info] - All sensors with Velocity quantity convert unit and value to SI base unit.
[info] - All sensors with Time quantity convert unit and value to SI base unit.
[info] - All sensors with Angles quantity convert unit and value to SI base unit.
[info] - All sensors with Humidity quantity convert unit and value to SI base unit.
[info] - All sensors with Solar radiation quantity convert unit and value to SI base unit.
[info] Run completed in 412 milliseconds.
[info] Total number of tests run: 9
[info] Suites: completed 1, aborted 0
[info] Tests: succeeded 9, failed 0, canceled 0, ignored 0, pending 0
[info] All tests passed.
```

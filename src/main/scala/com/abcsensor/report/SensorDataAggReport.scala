package com.abcsensor.report

import com.abcsensor.data.SensorAggregateData


class SensorDataAggReport {

  /**
    *
    * @return
    */
  def showSensorReport(sensorAggregateData:SensorAggregateData) : String = {
    val totalMeasurementsCount = sensorAggregateData.measurementsSuccess+sensorAggregateData.measurementsFailure
    val processedFileCount = sensorAggregateData.processedFileCount
    val measurementsFailure = sensorAggregateData.measurementsFailure

    if (processedFileCount > 0 && totalMeasurementsCount > 0) {
    s"""
       |Num of processed files: $processedFileCount
       |Num of processed measurements: $totalMeasurementsCount
       |Num of failed measurements: $measurementsFailure
       |Sensors with highest avg humidity:
       |sensor-id,min,avg,max
       |${sensorAggregateData.sensorMap.toSeq.sortBy(_._2.avg).reverse.map { case (sensorId, sensorAggregateData) =>
          if (sensorAggregateData.successCnt > 0)
            s"$sensorId,${sensorAggregateData.min},${sensorAggregateData.avg},${sensorAggregateData.max}"
          else
            s"$sensorId,NaN,NaN,NaN"
    }.mkString("\n")
    }""".stripMargin
   } else {
      ""
    }
  }
}

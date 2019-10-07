package com.abcsensor.data

import com.abcsensor.model.Sensor

import scala.collection.mutable

class SensorAggregateData {

    var sensorMap: mutable.SortedMap[String, Sensor] = mutable.SortedMap[String, Sensor]()
    var processedFileCount: Int = 0
    var measurementsSuccess: Int = 0
    var measurementsFailure: Int = 0

    def aggregateData (sensorId:String, humidity:String) = {
      val humidityInt = if (!humidity.isEmpty && !humidity.equalsIgnoreCase("NaN")) humidity.toInt else 0

      /**
        * Case 1 - SensorId exists and Humidity is valid
        */
      if (sensorMap.contains(sensorId) && humidityInt > 0) {
        val temp: Option[Sensor] = sensorMap.get(sensorId.toString)

        temp.get.max = if (temp.get.max <= 0) humidityInt else Math.max(temp.get.max, humidityInt)
        temp.get.min = if (temp.get.min <= 0) humidityInt else Math.min(temp.get.min, humidityInt)

        temp.get.sum += humidityInt
        temp.get.successCnt = if (temp.get.sum <= 0) 0 else (temp.get.successCnt + 1)
        temp.get.avg = if (temp.get.sum <= 0) 0 else (temp.get.sum/(temp.get.successCnt))

        sensorMap.put(sensorId, temp.get)
        measurementsSuccess += 1
      }
      else {
        /**
          * Case 2 - SensorId does not exists and Humidity is valid
          */
        if (!sensorMap.contains(sensorId) && humidityInt > 0) {
          val sensorAggregateData = Sensor(humidityInt, humidityInt, humidityInt, humidityInt, 1)
          sensorMap.put(sensorId, sensorAggregateData)
          measurementsSuccess += 1
        }
        /**
          * Case 3 - SensorId exists but Humidity is invalid
          */
        else if (sensorMap.contains(sensorId) && humidityInt <= 0) {
          measurementsFailure += 1
        }
        else {
          /**
            * Case 4 - SensorId does not exists and Humidity is invalid
            */
          val sensorAggregateData = Sensor(0, 0, 0, 0, 0)
          sensorMap.put(sensorId, sensorAggregateData)
          measurementsFailure += 1
        }
      }
    }
}


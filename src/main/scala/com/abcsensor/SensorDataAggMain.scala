package com.abcsensor

import akka.actor.ActorSystem
import com.abcsensor.data.SensorAggregateData
import com.abcsensor.report.SensorDataAggReport
import com.abcsensor.service.SensorDataService

import scala.concurrent.Await
import scala.concurrent.duration._

object SensorDataAggMain {

  implicit val actorSystem = ActorSystem("SensorMain")

  def main(args: Array[String]): Unit = {

    if (args.length < 1) {
      println("Please enter a path to directory")
      println("USAGE: com.abcsensor.SensorMain -d <Input dir>")
      println("Ex: com.abcsensor.SensorMain -d input")
      System.exit(1)
    }

    val sensorAggregateData = new SensorAggregateData()
    val sensorDataService = new SensorDataService()
    val sensorDataAggReport = new SensorDataAggReport()

    val files = sensorDataService.getFileList(args(1))
    sensorAggregateData.processedFileCount = files.size
    val sources = sensorDataService.processSource(files)
    val result = sensorDataService.processSourceContent(sources, sensorAggregateData)

    Await.ready(result, 30.seconds)

    val showStatistics = sensorDataAggReport.showSensorReport(sensorAggregateData)
    println(showStatistics)
    actorSystem.terminate()
  }
}
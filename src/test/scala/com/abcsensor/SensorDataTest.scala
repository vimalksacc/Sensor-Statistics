package com.abcsensor

import com.abcsensor.data.SensorAggregateData
import com.abcsensor.report.SensorDataAggReport
import com.abcsensor.service.SensorDataService
import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter

import scala.concurrent.Await
import scala.concurrent.duration._

class SensorDataTest extends FunSuite with BeforeAndAfter  {

  var sensorAggregateData:SensorAggregateData = _
  var sensorDataService:SensorDataService = _
  var sensorDataAggReport:SensorDataAggReport = _

  before {
    sensorAggregateData = new SensorAggregateData()
    sensorDataService = new SensorDataService()
    sensorDataAggReport = new SensorDataAggReport()
  }

  test("test_e2eFlow") {
    val files = sensorDataService.getFileList("input")
    sensorAggregateData.processedFileCount = files.size
    val sources = sensorDataService.processSource(files)
    val result = sensorDataService.processSourceContent(sources, sensorAggregateData)
    Await.ready(result, 30.seconds)

    val showStatistics = sensorDataAggReport.showSensorReport(sensorAggregateData)
    //println(showStatistics)
    assert(showStatistics.trim != null)
  }

  test("test_inputPathIsMissing") {
    val files = sensorDataService.getFileList(null)
    assert(files.isEmpty)
  }

  test("test_allDataIsInvalid") {
    val inputDataSeq = Seq("testinput/emptydata.csv")
    sensorAggregateData.processedFileCount = inputDataSeq.size
    val sources = sensorDataService.processSource(inputDataSeq)
    val result = sensorDataService.processSourceContent(sources, sensorAggregateData)
    Await.ready(result, 30.seconds)

    val showStatistics = sensorDataAggReport.showSensorReport(sensorAggregateData)
    assert(showStatistics == null || showStatistics.trim.length == 0)
  }

  test("test_mixeddatasets") {
    val inputDataSeq = Seq("testinput/validdata.csv", "testinput/invaliddata.csv")
    sensorAggregateData.processedFileCount = inputDataSeq.size
    val sources = sensorDataService.processSource(inputDataSeq)
    val result = sensorDataService.processSourceContent(sources, sensorAggregateData)
    Await.ready(result, 30.seconds)

    val showStatistics = sensorDataAggReport.showSensorReport(sensorAggregateData)
    //println(showStatistics)
    assert(showStatistics != null || showStatistics.trim.length > 0)
  }
}
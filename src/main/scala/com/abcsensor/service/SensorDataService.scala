package com.abcsensor.service

import java.nio.file.Paths

import akka.NotUsed
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{FileIO, Framing, Keep, Source}
import akka.util.ByteString
import com.abcsensor.SensorDataAggMain.actorSystem
import com.abcsensor.data.SensorAggregateData

import scala.collection.immutable
import scala.concurrent.ExecutionContext
import scala.util.control.NonFatal
import scala.util.{Failure, Success}

class SensorDataService {

  implicit val materializer = ActorMaterializer()
  implicit def ec:ExecutionContext = actorSystem.dispatcher

  /**
    *
    * @param inputPath
    * @return
    */
  def getFileList(inputPath:String) : List[String] = {
    if (inputPath == null || inputPath.size == 0) {
      println("Input Path is missing...!")
      return immutable.List.empty
    }
    new java.io.File(inputPath).listFiles().map(_.getAbsolutePath).toList
  }

  /**
    * Read Directory Files contents in Chunk
    * @param files
    * @return
    */
  def processSource(files:Seq[String]): Seq[Source[ByteString, NotUsed.type]] = {
    files
      .map(Paths.get(_))
      .map(p =>
        FileIO.fromPath(p)
          .viaMat(Framing.delimiter(ByteString(System.lineSeparator()), 1024, allowTruncation = true))(Keep.left)
          .drop(1)
          .mapMaterializedValue { f =>
            f.onComplete {
              case Success(r) if r.wasSuccessful => /*println(s"Read Success from : $p")*/
              case Success(r) => println(s"Something went wrong when reading $p: ${r.getError}")
              case Failure(NonFatal(e)) => println(s"Something went wrong when reading $p: $e")
            }
            NotUsed
          }
      )
  }

  /**
    * Read, Parse and Process Sensor Data
    * @param sources
    * @return
    */
  def processSourceContent(sources: Seq[Source[ByteString, NotUsed.type]],
                           sensorAggregateData:SensorAggregateData) = {
    Source(sources).flatMapConcat(identity).runForeach(x => {
      val line = x.utf8String
      val arr: Array[String] = line.split(",")
      val sensorId = arr(0).trim
      val humidity = arr(1).trim
      sensorAggregateData.aggregateData(sensorId, humidity)
    })
  }
}

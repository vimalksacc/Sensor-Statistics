package com.abcsensor.model

import java.io.Serializable

case class Sensor(var min: Int, var avg: Int, var max: Int, var sum: Int, var successCnt: Int) extends Serializable

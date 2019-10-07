# Sensor-Statistics

1. To Run Main program
```
    com.abcsensor.SensorDataAggMain -d <input dir path>
    Ex.
    com.abcsensor.SensorDataAggMain -d input
```   
2. To Run Unit Tets
```
    com.abcsensor.SensorDataTest
    Ex.
    test SensorDataTest
```   

####Output-Main Program
```
    Num of processed files: 2
    Num of processed measurements: 7
    Num of failed measurements: 2
    Sensors with highest avg humidity:
    sensor-id,min,avg,max
    s2,78,82,88
    s1,10,54,98
    s3,NaN,NaN,NaN
```
   
####Output-Unit Test
```
   [IJ]sbt:SensorStatisticsTask> test
   Input Path is missing...!
   [info] SensorDataTest:
   [info] - test_e2eFlow
   [info] - test_inputPathIsMissing
   [info] - test_allDataIsInvalid
   [info] - test_mixeddatasets
   [info] Run completed in 1 second, 202 milliseconds.
   [info] Total number of tests run: 4
   [info] Suites: completed 1, aborted 0
   [info] Tests: succeeded 4, failed 0, canceled 0, ignored 0, pending 0
   [info] All tests passed.
```


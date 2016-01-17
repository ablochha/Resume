# team23 Weather Application

## Synopsis
This program displays weather for selected location in three different formats: 

- **Current**: Weather at the time of request

- **Short Term**: Weather forecasts at three hour intervals

- **Long Term**: Weather forecasts at for upcoming days

This program gets its terrestrial data from [OpenWeatherMap](http://openweathermap.org/).

There is also the ability to view Martian weather as seen by the Curiosity rover. This data is retrieved from the [Mars Atmospheric Aggregation System](http://marsweather.ingenology.com). 

The program uses the [GSON](https://code.google.com/p/google-gson/) library for (de)serialization, [Log4j](http://logging.apache.org/log4j/1.2/http://logging.apache.org/log4j/1.2/) for logging, and [JUnit](http://junit.org) for testing.

More information available on the [Project's Wiki](https://github.com/UWO-2212-W2015/team23/wiki).

## Install
Program can be downloaded [here](https://github.com/UWO-2212-W2015/team23/blob/master/23_weather.jar?raw=true). Run with

```
java -jar 23_weather.jar
```

## Build
Using your favourite command line environment navigate to the team23 folder. Program can be built with Maven using:

```
mvn compile
mvn package
```
  
This program depends on:

- Java 7
- [Maven 3.3](https://maven.apache.org/)

In addition, the following Maven dependencies exist:

- [GSON](https://code.google.com/p/google-gson/)
- [Log4j](http://logging.apache.org/log4j/1.2/http://logging.apache.org/log4j/1.2/)
- [JUnit](http://junit.org) 

## Usage Example

For a video tutorial showcasing the features of the app and how to use it, as well as how it was developed, click [here](https://www.youtube.com/watch?v=BbetLj-4md4). 

## Documentaion

JavaDocs are available [here](http://twhelan3.github.io/docs/index.html).

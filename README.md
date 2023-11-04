# opencv-draw-with-face
- This program using `OpenCV` to capture face then draw on webcam frame written by `Java`.

### 1. Requirement
Your machine need to set up:
+ `Java` JDK 17 openjdk amd64
+ `mvn` maven to package program to `JAR`

### 2. Run to capture frame on webcam
Run code by Intellij JDK.

Or package code to `JAR` by command:
```
mvn assembly:assembly -DdescriptorId=jar-with-dependencies
```
Run `JAR` file by command:
```
java -jar target/opencv-1.0-SNAPSHOT-jar-with-dependencies.jar
```
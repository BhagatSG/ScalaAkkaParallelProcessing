# Technical-Assessment - Parallel Processing without Big Data

compute-hash:

simple CLI application that computes the hash of the content of a text file line-by-line and writes it to an output file (provided from command line).

Steps:

1)Install GraalVM on your MAC https://www.graalvm.org/docs/getting-started-with-graalvm/macos/

Note to Remember: Add bin directory to PATH

2)Install native-image command using: gu install native-image

Using sbt-native-packager to create a native-image on SBT

Following configuration added to enable native-image creation with annotation processing defined in advance: 

packageBin in Compile := (packageBin in Compile dependsOn (processAnnotations in Compile)).value 
enablePlugins(GraalVMNativeImagePlugin)

3)Generating a native image by running SBT as follows sbt graalvm-native-image:packageBin

4)A binary will be generated under target/graalvm-native-image with name compute-hash(same as project name)

5)Test using this Command : ./target/graalvm-native-image/compute-hash ./inputMain.txt OutputMain.txt


Hashing-service: 
 Spring Boot Java REST service, which:
  1) Accepts batch requests with a list of strings to hash.
  2) Returns a list of hashes of that strings (in the same order).
  
  

Below 3 projects are POC:

1)cli-poc. 
  Building a CLI application POC

2)akka-poc. 
  Akka Actors POC

3)scalaj-http-poc.
  Scala HTTP Post Request POC

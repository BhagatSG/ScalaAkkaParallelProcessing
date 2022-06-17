1)Install GraalVM on your MAC
https://www.graalvm.org/docs/getting-started-with-graalvm/macos/

Note to Remember: Add bin directory to PATH

2)Install native-image command using:
gu install native-image

Using sbt-native-packager to create a native-image on SBT

Following configuration to enable native-image creation with annotation processing defined in advance:
packageBin in Compile := (packageBin in Compile dependsOn (processAnnotations in Compile)).value
enablePlugins(GraalVMNativeImagePlugin)

3)Generating a native image by running SBT as follows
sbt graalvm-native-image:packageBin

4)A binary will be generated under target/graalvm-native-image with name compute-hash(same as project name)

5)Test using this Command : ./target/graalvm-native-image/compute-hash ./inputMain.txt OutputMain.txt
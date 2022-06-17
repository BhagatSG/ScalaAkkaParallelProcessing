package com.poc.bhagat.utilities

object FileUtility {

    def getFileSizeMegaBytes(file: java.io.File) : Long = {
        file.length() / (1024 * 1024)
    }

    def getFileSizeKiloBytes(file: java.io.File) : Long = {
        file.length() / (1024)
    }

    def writeToOutputFile(lines: Iterator[String], outputFilePath: String) = {
        better.files.File(outputFilePath).createIfNotExists().clear().appendLines(lines.mkString("\n"))
    }
}

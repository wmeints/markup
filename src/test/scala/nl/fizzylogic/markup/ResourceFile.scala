package nl.fizzylogic.markup

import java.io._
import java.nio.charset.Charset
import java.nio.file.{OpenOption, Path, Files}

import scala.io.Source
import collection.JavaConversions._

object ResourceFile {
  def apply(name: String) = new ResourceFile(classOf[ResourceFile].getResourceAsStream(name))
}

class ResourceFile(stream: InputStream) {
  def save(outputFile: File) = {
    val lines = Source.fromInputStream(stream).getLines()
    val output = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile)))

    lines.foreach(line => output.write(line + "\n"))

    output.flush()
    output.close()
  }
}

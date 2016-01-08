package nl.fizzylogic.markup

import java.io.{InputStreamReader, FileInputStream, BufferedReader, File}
import java.nio.file.{Paths, Files}

import org.yaml.snakeyaml.Yaml
import play.twirl.compiler.TwirlCompiler

import scala.io.Source


object Program extends App {
  val parser = new scopt.OptionParser[WebsiteParserOptions]("markup") {
    head("markup","0.0.1")
    opt[String]("input") required() action { (x,c) => c.copy(inputFolder = x) } text("Input folder")
    opt[String]("output") required() action { (x,c) => c.copy(outputFolder = x) } text("Output folder")

    override def showUsageOnError = true
  }

  parser.parse(args, WebsiteParserOptions()) match {
    case Some(options) => WebsiteParser(options).run()
    case None =>
  }
}

case class WebsiteParserOptions(inputFolder: String = "./source", outputFolder: String = "./generated")

case class WebsiteParser(options: WebsiteParserOptions) {
  def run() = {
    // Read all the posts from the input folder
    val articles = for {
      file <- Paths.get(options.inputFolder, "posts").toFile.listFiles
    } yield Article(file)

    //TODO: Compile the templates
    //TODO: Generate the output based on the templates
  }
}

case class GeneratorOptions(siteName: String)

class HtmlGenerator(outputFolder: String) {
  def generate(article: Article, options: GeneratorOptions): Option[String] = {
    None
  }
}



package nl.fizzylogic.markup

import java.io.File

import org.yaml.snakeyaml.Yaml

import scala.io.Source

import collection.JavaConversions._

/**
  * Contains data for a single article in the website
  */
class Article(private val _metadata: Map[String,Any], private val _body: String) {
  /**
    * Metadata for the article
    */
  def metadata: Map[String,Any] = _metadata

  /**
    * The body of the article
    */
  def body: String = _body
}

class MarkdownArticle(metadata: Map[String,Any], body: String) extends Article(metadata,body)
class HtmlArticle(metadata: Map[String,Any], body: String) extends Article(metadata,body)

/**
  * Companion object for the article
  */
object Article {
  val yaml = new Yaml()

  /**
    * Reads the input file parsing it as a markdown article
    * @param input  Input file to read from
    * @return Some article when parsing succeeds otherwise None.
    */
  def apply(input: File): Option[Article] = {
    val data = Source.fromFile(input).getLines().toList

    // Find out about the extension of the file.
    // If no extension was found then the file type is unknown.
    val extension = if(input.getPath.lastIndexOf('.') > -1) {
      input.getPath.substring(input.getPath.lastIndexOf('.'))
    } else {
      ""
    }

    // Keep parsing the file if frontmatter is encountered parse it.
    // Otherwise parse the body of the file and provide an empty set of frontmatter metadata.
    data match {
      case Nil => None
      case _ =>
        data.head match {
          case "---" => parseFrontMatter(data.tail, extension)
          case _ => parseBody(Map[String,Any](), data, extension)
        }
    }
  }

  private def parseFrontMatter(data: List[String], extension: String): Option[Article] = {
    // Keep parsing the file until the frontmatter separator is encountered.
    // When the frontmatter separator is encountered convert the frontmatter content and start parsing the body.
    data.head match {
      case "---" => parseBody(Map[String,Any](), data, extension)
      case _ => parseFrontMatter(new StringBuilder(data.head).append("\n"), data.tail, extension)
    }

  }

  private def parseFrontMatter(buffer: StringBuilder, data: List[String], extension: String): Option[Article] = {
    // Keep parsing the file until the frontmatter separator is encountered.
    // When the frontmatter separator is encountered convert the frontmatter content and start parsing the body.
    data match {
      case Nil => None
      case _ => data.head match {
        case "---" => parseBody(yaml.load(buffer.toString()).asInstanceOf[java.util.LinkedHashMap[String,Any]].toMap[String,Any], data.tail, extension)
        case _ => parseFrontMatter(buffer.append(data.head).append("\n"), data.tail, extension)
      }
    }
  }

  private def parseBody(metadata:Map[String,Any], data: List[String], extension: String): Option[Article] = {
    // Keep parsing the file until the body is list of input lines is empty.
    // When the list of input lines is empty return the final article.
    data match {
      case Nil => createArticle(metadata,"",extension)
      case _ => parseBody(metadata, new StringBuilder(data.head).append("\n"), data.tail, extension)
    }
  }

  private def parseBody(metadata:Map[String,Any], buffer: StringBuilder, data: List[String], extension: String): Option[Article] = {
    // Keep parsing until the list of input lines is empty.
    // When the list of input lines is empty return the final article.
    data match {
      case Nil => createArticle(metadata,buffer.toString,extension)
      case _ => parseBody(metadata, buffer.append(data.head).append("\n"), data.tail, extension)
    }
  }

  private def createArticle(metadata:Map[String,Any], body: String, extension: String): Option[Article] = {
    // Check the extension and create the appropriate article type
    extension.toLowerCase() match {
      case "html" | "htm" => Some(new HtmlArticle(metadata,body))
      case "md" | "markdown" => Some(new HtmlArticle(metadata,body))
      case _ => Some(new Article(metadata,body))
    }
  }
}


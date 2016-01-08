package nl.fizzylogic.markup

import java.nio.file.Files

import org.scalatest.{BeforeAndAfterAll, WordSpec}

class ArticleSpec extends WordSpec with BeforeAndAfterAll {

  val testMarkdownFile = Files.createTempFile("testfile",".md").toFile
  val testHtmlFile = Files.createTempFile("testfile",".html").toFile
  val markdownWithoutFrontMatterFile = Files.createTempFile("markdown_without_frontmatter",".md").toFile
  val markdownWithoutBodyFile = Files.createTempFile("markdown_without_body",".md").toFile

  ResourceFile("/testfile.md").save(testMarkdownFile)
  ResourceFile("/testfile.html").save(testHtmlFile)
  ResourceFile("/article_without_frontmatter.md").save(markdownWithoutFrontMatterFile)
  ResourceFile("/article_without_body.md").save(markdownWithoutBodyFile)

  "An article" should {

    val article: Article = Article(testMarkdownFile).get

    "contain metadata" in {
      article.metadata !== null
      article.metadata("title") === "Sample file"
    }
  }

  "An article without frontmatter" should {
    val article:Article = Article(markdownWithoutFrontMatterFile).get

    "contain empty metadata" in {
      article.metadata !== null
      article.metadata.size === 0
    }

    "contain a body" in {
      article.body !== null
      (article.body.length > 0) === true
    }
  }

  "An article without a body" should {
    val article:Article = Article(markdownWithoutBodyFile).get

    "contain metadata" in {
      article.metadata !== null
      article.metadata.size === 0
    }

    "not contain a body" in {
      article.body === null
    }
  }

  "A markdown article" should {
    val article: Article = Article(testMarkdownFile).get

    "be marked as markdown" in {
      article.isInstanceOf[MarkdownArticle] === true
    }
  }

  "A HTML article" should {
    val article: Article = Article(testHtmlFile).get

    "be marked as html" in {
      article.isInstanceOf[HtmlArticle] === true
    }
  }

  override def afterAll = {
    testMarkdownFile.delete()
    testHtmlFile.delete()
    markdownWithoutBodyFile.delete()
    markdownWithoutFrontMatterFile.delete()
  }
}

package core.utils

import java.net.{URI, URL}
import scala.concurrent.duration._
import scala.collection.JavaConverters._

import akka.util.Timeout
import play.api.Logger

/** Wrapper around play.api.Configuration with extended features **/
class Configuration(val configuration: play.api.Configuration) {
  import Configuration.Getter

  def keys(): Set[String] = configuration.keys

  def list(): Seq[(String, String)] =
    keys().toSeq.map(key => (key, configuration.getOptional[String](key).getOrElse("")))

  def getRawConfig(key: String) =
    Getter[play.api.Configuration](key, configuration.getOptional[play.api.Configuration](key))
  def getConfig(key: String) = getRawConfig(key).map(c => new Configuration(c))

  def getBoolean(key: String) =
    Getter[Boolean](key, configuration.getOptional[Boolean](key))

  def getInt(key: String) =
    Getter[Int](key, configuration.getOptional[Int](key))

  def getLong(key: String) =
    Getter[Long](key, configuration.getOptional[Long](key))

  def getDouble(key: String) =
    Getter[Double](key, configuration.getOptional[Double](key))

  def getString(key: String) =
    Getter[String](key, configuration.getOptional[String](key))

  def getStringSeq(key: String) =
    Getter[Seq[String]](key, {
      if (configuration.underlying.hasPath(key)) {
        Some(configuration.underlying.getStringList(key)).map(_.asScala.toSeq)
      } else None
    })

  def getNotEmptyStringSeq(key: String) = getStringSeq(key).flatMap {
    case Seq() => None
    case seq   => Some(seq)
  }

  def getBytes(key: String) =
    Getter[Long](key, Option(configuration.underlying.getBytes(key)).map(_.toLong))

  def getDuration(key: String): Getter[Duration] =
    getString(key).map(Duration.apply)
  def getDurationInMillis(key: String): Getter[Double] =
    getString(key).map(Duration.apply).map(_.toUnit(MILLISECONDS))
  def getFiniteDuration(key: String): Getter[FiniteDuration] =
    getDurationInMillis(key).map(_.milliseconds)
  def getTimeout(key: String): Getter[Timeout] =
    getFiniteDuration(key).map(Timeout.apply)

  def getURI(key: String): Getter[URI] = getString(key).map(new URI(_))
  def getURL(key: String): Getter[URL] = getString(key).map(new URL(_))
}

object Configuration {
  val logger = Logger("configuration")

  sealed trait Getter[T] { self =>
    def key: String
    lazy val value: Option[T] = {
      val v = try loadValue
      catch {
        case e: Exception =>
          logger.error(s"Configuration error on key $key", e)
          throw e
      }
      logger.debug(s"Configuration key $key loaded: $v")
      v
    }

    protected def loadValue: Option[T]

    def map[U](f: T => U): Getter[U] = new Getter[U] {
      val key = self.key
      def loadValue = self.value.map(f)
    }

    def flatMap[U](f: T => Option[U]): Getter[U] = new Getter[U] {
      val key = self.key
      def loadValue = self.value.flatMap(f)
    }

    def validate(f: T => Boolean): Getter[T] = map { value =>
      if (f(value)) value
      else {
        throw new Exception(s"Validation failed on key $key: $value")
      }
    }

    def optional: Option[T] = value
    def required: T = value match {
      case None =>
        val msg = s"Configuration : can't find $key"
        logger.error(msg)
        throw new NoSuchElementException(msg)
      case Some(v) => v
    }

    def getOrElse[U >: T](o: => U): U = optional.getOrElse(o)
  }

  object Getter {
    def apply[T](confKey: String, f: => Option[T]): Getter[T] = new Getter[T] {
      def key = confKey
      def loadValue = f
    }
  }

  def apply(conf: play.api.Configuration): Configuration =
    new Configuration(conf)
}

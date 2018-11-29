package core.utils

class ConfigurationTest extends test.UnitTest {

  val configStr = """
    str = "my-string"
    boolean = true
    number-int = 1
    number-long = 9223372036854775807
    number-double = 0.5
    time = 2 min

    url = "https://my-example-url.com/ceci"
    seq = ["foo", "bar"]
    bytes = 1 KiB
  """

  val typesafeConfig = com.typesafe.config.ConfigFactory.parseString(configStr)
  val config = core.utils.Configuration(play.api.Configuration(typesafeConfig))

  "Configuration" should {
    "parse basic types" in {
      config.getString("str").required shouldBe "my-string"
      config.getBoolean("boolean").required shouldBe true
      config.getInt("number-int").required shouldBe 1
      config.getLong("number-long").required shouldBe 9223372036854775807L
      config.getDouble("number-double").required shouldBe 0.5
    }

    "parse times" in {
      import scala.concurrent.duration._
      config.getFiniteDuration("time").required shouldBe 2.minutes
    }

    "throw exception when value is required and not present" in {
      val key = "not-present"
      the[NoSuchElementException] thrownBy {
        config.getString(key).required
      } should have message s"Configuration : can't find $key"
    }

    "parse most complex types" in {
      config.getURL("url").required shouldBe new java.net.URL("https://my-example-url.com/ceci")
      config.getNotEmptyStringSeq("seq").required shouldBe Seq("foo", "bar")
      config.getBytes("bytes").required shouldBe 1024L
    }
  }

  val getterSome = core.utils.Configuration.Getter("some-key", Some(1))
  val getterNone = core.utils.Configuration.Getter("some-key", Option.empty[Int])
  "Getters" should {
    "get value when present" in {
      getterSome.required shouldBe 1
      getterNone.optional shouldBe None
    }

    "throw exception when value is not valid" in {
      the[Exception] thrownBy {
        getterSome.validate(x => x < 0).required
      } should have message "Validation failed on key some-key: 1"
    }

    "none values should be preserved" in {
      getterNone.map(_ + 1).optional shouldBe None
      getterNone.flatMap(x => Some(x + 1)).optional shouldBe None
    }
  }
}

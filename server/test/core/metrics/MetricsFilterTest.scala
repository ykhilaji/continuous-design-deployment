package core.metrics

import org.scalatest._
import play.api.libs.typedmap.{TypedEntry, TypedMap}
import play.api.routing.{HandlerDef, Router}
import play.api.test.FakeRequest

class MetricsFilterTest extends test.UnitTest {

  "HttpTraceNameGenerator" should {
    "generate a trace name for routed requests" in {
      val handlerDef = HandlerDef(null, "", "controller", "method", Seq.empty, "GET", "/foo/bar/$id<[^/]+>")
      val request = FakeRequest(method = "GET", path = "/foo/bar/123")
        .withAttrs(TypedMap(TypedEntry(Router.Attrs.HandlerDef, handlerDef)))

      HttpTraceNameGenerator.generateTraceName(request) shouldBe "/foo/bar/:id"
    }

    "set a default name for unknow routes" in {
      val request = FakeRequest(method = "POST", path = "/ceci")

      HttpTraceNameGenerator.generateTraceName(request) shouldBe "not-found"
    }
  }
}

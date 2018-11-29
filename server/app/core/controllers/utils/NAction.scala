package core.controllers.utils

import akka.stream.Materializer
import akka.util.ByteString
import play.api.libs.streams.Accumulator
import play.api.mvc._
import scala.concurrent.Future

/**
 * NAction are a replacement to play.api.mvc.Action.
 * NAction allow to have a check directly on the request headers before applying the body parser
 *
 * Using the NActionBuilder you also have a better way to compose action with `or` and `and` operators
 */
abstract class NAction[A, Tag](
  materializer: Materializer
) extends EssentialAction {
  self =>
  def check(rh: RequestHeader): Future[Either[Result, Tag]]
  def parser: BodyParser[A]
  def apply(request: RequestWithTag[A, Tag]): Future[Result]

  def apply(rh: RequestHeader): Accumulator[ByteString, Result] = {
    val futAcc: Future[Accumulator[ByteString, Result]] = check(rh).map {
      case Left(r) => Accumulator.done[Result](r)
      case Right(tag) =>
        self.parser
          .apply(rh)
          .mapFuture {
            case Left(r) =>
              Future.successful(r)
            case Right(body) =>
              val request = RequestWithTag(Request(rh, body), tag)
              try {
                self.apply(request)
              } catch {
                case e: NotImplementedError => throw new RuntimeException(e)
              }
          }(materializer.executionContext)
    }(materializer.executionContext)
    Accumulator.flatten(futAcc)(materializer)
  }
}

sealed trait CheckResult[+Tag]
object CheckResult {
  case object NotApplicable extends CheckResult[Nothing]
  case class Ok[+Tag](tag: Tag) extends CheckResult[Tag]
  case class Invalid(result: Result) extends CheckResult[Nothing]
}

/**
 * This class wraps a Request and allow to set some tag that is extracted from the request headers
 */
case class RequestWithTag[A, Tag](request: Request[A], tag: Tag) extends WrappedRequest(request)

object NAction {
  def async[A, Tag](checkFunction: RequestHeader => Future[Either[Result, Tag]])(bodyParser: BodyParser[A])(
    block: RequestWithTag[A, Tag]                => Future[Result]
  )(implicit mat: Materializer): NAction[A, Tag] =
    new NAction[A, Tag](mat) {
      def parser = bodyParser
      def check(rh: RequestHeader) = checkFunction(rh)
      def apply(request: RequestWithTag[A, Tag]): Future[Result] =
        block(request)
    }

  def apply[A, Tag](check: RequestHeader => Future[Either[Result, Tag]])(bodyParser: BodyParser[A])(
    block: RequestWithTag[A, Tag]        => Result
  )(implicit mat: Materializer): NAction[A, Tag] =
    async(check)(bodyParser) { req =>
      Future.successful(block(req))
    }
}

abstract class NActionBuilder[+Tag](
  implicit materializer: Materializer
) { self =>
  implicit val ec = materializer.executionContext

  /** Check to execute before parsing the body of the request */
  def partial(rh: RequestHeader): Future[CheckResult[Tag]]

  // Functions to build an NAction from this builder:

  /** Function to instantiate a NAction from the NActionBuilder */
  def async[A, T1 >: Tag](bodyParser: BodyParser[A])(block: RequestWithTag[A, T1] => Future[Result]): NAction[A, T1] =
    new NAction[A, T1](materializer) {
      def parser = bodyParser
      def check(rh: RequestHeader) = self.partial(rh).map {
        case CheckResult.Ok(tag)         => Right(tag)
        case CheckResult.Invalid(result) => Left(result)
        case CheckResult.NotApplicable   => sys.error("Match Error")
      }
      def apply(request: RequestWithTag[A, T1]): Future[Result] = block(request)
    }

  /** Contruct a NAction with a body parser and a block */
  def apply[A, T1 >: Tag](bodyParser: BodyParser[A])(block: RequestWithTag[A, T1] => Result): NAction[A, T1] =
    async[A, T1](bodyParser) { req: RequestWithTag[A, T1] =>
      Future.successful(block(req))
    }

  // Functions to compose the builder:

  /** Execute the other check after the first one succeded */
  def andThenCheck[S](other: (Tag, RequestHeader) => Future[CheckResult[S]]): NActionBuilder[S] =
    new NActionBuilder[S] {
      def partial(rh: RequestHeader) = self.partial(rh).flatMap {
        case CheckResult.Ok(tag)          => other(tag, rh)
        case invalid: CheckResult.Invalid => Future.successful(invalid)
        case CheckResult.NotApplicable =>
          Future.successful(CheckResult.NotApplicable)
      }
    }

  /** Execute the other check if first one was not defined */
  def orElseCheck[T1 >: Tag](other: RequestHeader => Future[CheckResult[T1]]): NActionBuilder[T1] =
    new NActionBuilder[T1] {
      def partial(rh: RequestHeader) = self.partial(rh).flatMap {
        case ok: CheckResult.Ok[_]        => Future.successful(ok)
        case invalid: CheckResult.Invalid => Future.successful(invalid)
        case CheckResult.NotApplicable    => other(rh)
      }
    }

  /** Execute the other builder if first one was not defined */
  def orElse[T1 >: Tag](other: NActionBuilder[T1]): NActionBuilder[T1] =
    new NActionBuilder[T1] {
      def partial(rh: RequestHeader) = self.partial(rh).flatMap {
        case ok: CheckResult.Ok[_]        => Future.successful(ok)
        case invalid: CheckResult.Invalid => Future.successful(invalid)
        case CheckResult.NotApplicable    => other.partial(rh)
      }
    }
}

object NActionBuilder {

  def fromPartial[Tag](
    check: PartialFunction[RequestHeader, Future[Either[Result, Tag]]]
  )(implicit mat: Materializer): NActionBuilder[Tag] =
    new NActionBuilder[Tag] {
      def partial(rh: RequestHeader) =
        if (check.isDefinedAt(rh)) check.apply(rh).map {
          case Right(tag)   => CheckResult.Ok(tag)
          case Left(result) => CheckResult.Invalid(result)
        } else Future.successful(CheckResult.NotApplicable)
    }
}

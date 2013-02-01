package futures

import scala.concurrent.{ExecutionContext,Future,future,Promise}
import scalaz._, Scalaz._

object Demo {
  // demo purposes only
  implicit val ec = ExecutionContext.global
  implicit val F = scalaz.akkaz.future.futureInstancesNonblocking(ec)
  implicit val M = scalaz.akkaz.future.futureMonoid[String]

  type ValidationSNEL[T] = ValidationNEL[String, T]
  
  val app = Applicative[Future].compose[ValidationSNEL]

  def handle(a: Int, b: Int): String = (a+b).toString

  val r1: Future[ValidationSNEL[Int]] = future { "foo".fail.toValidationNEL }
  val r2: Future[ValidationSNEL[Int]] = future { "bar".fail.toValidationNEL }

  val output: Future[ValidationSNEL[String]] = 
    app.apply2(r1,r2)(handle)

  def main(args: Array[String]): Unit = {
    output.onSuccess { 
      case r => println(r)
    }
  }
}
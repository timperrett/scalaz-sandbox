package candy

sealed trait Input
case object Coin extends Input
case object Turn extends Input
case class Machine(locked: Boolean, candies: Int, coins: Int)

import scalaz.syntax.state._
import scalaz.State, State._

object Demo {
  def main(args: Array[String]): Unit = println {
    import Machine._
    use(Coin :: Turn :: Coin :: Turn :: Coin :: Turn :: Nil) eval (default)
  }
}

object Machine {
  val default = Machine(locked = true, candies = 2, coins = 0)

  def use(ops: List[Input]): State[Machine, Int] = 
    for {
      m <- init
      _ <- modify(ops.map(handleInput).reduce(_ andThen _))
      r <- get
    } yield r.coins

  def handleInput(what: Input): Machine => Machine = m => 
    what match {
      case Coin => 
        if(m.locked == true && m.candies > 0) 
          m.copy(locked = false, coins = m.coins+1)
        else m

      case Turn => 
        if(!m.locked) 
          m.copy(candies = m.candies-1, locked = true)
        else m
    }
}

package nightclub

import scalaz._, Scalaz._

object Sobriety extends Enumeration { val Sober, Tipsy, Drunk, Paralytic, Unconscious = Value }
object Gender extends Enumeration { val Male, Female = Value }

case class Person(
  gender: Gender.Value, 
  age: Int, 
  clothes: Set[String], 
  sobriety: Sobriety.Value)

trait Doorman {

  // Person => Validation[String,Person], where the string represents errors
  def checkAge(p: Person): Validation[String, Person] = 
    if (p.age < 18) "Too Young!".failure
    else if (p.age > 40) "Too Old!".failure
    else p.success

  def checkSobriety(p: Person): Validation[String, Person] =
    if (p.gender == Gender.Male && !p.clothes("Tie")) "Smarten Up!".failure
    else if (p.gender == Gender.Female && p.clothes("Trainers")) "Wear high heels".failure
    else p.success

  def checkStyle(p: Person): Validation[String, Person] =
    if (Set(Sobriety.Drunk, Sobriety.Paralytic, Sobriety.Unconscious) contains p.sobriety)
      "Sober Up!".failure
    else p.success
}

trait Nightclub {
  lazy val bouncer = new Doorman {}
  def screenPunter(p: Person): Validation[String, Person]
}

object Clubbers {
  val Dave = Person(Gender.Male, 28, Set("Shoes"), Sobriety.Tipsy)
}

object ClubIce extends Nightclub {
  def screenPunter(p: Person) = 
    for {
      a <- bouncer.checkStyle(p)
    } yield a
}

object PoNaNa extends Nightclub {
  import bouncer._

  def screenPunter(p: Person) = 
    (checkStyle(p).toValidationNEL |@| checkAge(p).toValidationNEL |@| checkSobriety(p).toValidationNEL){
      case (_, _, c) => if (c.gender == Gender.Female) 0D else 7.5D
    }
     
}

object PartyTime {
  import Clubbers._

  def main(args: Array[String]): Unit = {
    println(ClubIce.screenPunter(Dave))
  }
}













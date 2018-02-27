package actors

import logic.Problem._

object Messages {

  abstract class Message

  object Application {

    case object Pause extends Message
    case object Unpause extends Message
    case object Invade extends Message
    case object CreateLegend extends Message
    case object AddGreedy extends Message
    case class UpdateConfiguration(
      populationCount: Int,
      mutationRate: Double,
      freshBloodRate: Double,
      freshBloodFrequency: Int
    ) extends Message

    case object Save
  }

  object Solver{

    case object Work extends Message
    case class Iterated(newBestSolution: Option[(Solution, Long)])
    case class Configuration(
      populationCount: Int,
      mutationRate: Double,
      freshBloodRate: Double,
      freshBloodFrequency: Int
    ) extends Message

    case object GreedyAdded

  }

}

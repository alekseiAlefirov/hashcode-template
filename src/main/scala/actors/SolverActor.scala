package actors

import actors.Messages.Application
import actors.Messages.Application._
import actors.Messages.Solver.{Configuration, GreedyAdded, Work}
import logic.Problem._
import akka.actor._
import logic.evolutional.EvolutionalSolver

class SolverActor(problem: Problem) extends Actor {

  val solver = new EvolutionalSolver(problem, { _ => self ! GreedyAdded })(this.context.dispatcher)
  var bestSolution = problem.emptySolution()
  var highScore = 0L
  context.parent ! Configuration(solver.populationN, solver.copCoef, solver.freshBloodRate, solver.freshBloodFrequency)

  override def receive: Receive = {
    case Work =>
      val updated = solver.iterate()
      if (updated && solver.bestSolution.score > highScore) {
        bestSolution = solver.bestSolution.solution
        highScore = solver.bestSolution.score
        sender ! Messages.Solver.Iterated(Some(bestSolution, solver.bestSolution.score))
      }
      else {
        sender ! Messages.Solver.Iterated(None)
      }

    case Invade => ()//solver.invade()

    case CreateLegend =>
      ()//solver.dropBestCreateLegend()

    case AddGreedy => solver.addGreedy()

    case  (x @ UpdateConfiguration(
      populationCount: Int,
      mutationRate: Double,
      freshBloodRate: Double,
      freshBloodFrequency: Int
    )) =>
      solver.populationN = populationCount
      solver.copCoef = mutationRate
      solver.freshBloodRate = freshBloodRate
      solver.freshBloodFrequency = freshBloodFrequency
      println(s"Configuration updated $x")


    case GreedyAdded => context.parent ! GreedyAdded
  }

}

object SolverActor{

  def props(situation: Problem) = Props(classOf[SolverActor], situation)

}

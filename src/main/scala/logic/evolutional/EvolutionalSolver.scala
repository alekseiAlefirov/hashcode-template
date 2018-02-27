package logic.evolutional

import logic.Problem._
import logic.ScoringFunction

import scala.concurrent.Future
import scala.util.Random
import RandomHelpers._

class EvolutionalSolver(
  problem: Problem,
  greedyAddedCallback: Unit => Unit
)(implicit ec: scala.concurrent.ExecutionContext) {

  import EvolutionalSolver._

  var populationN = 4 //change
  var copCoef = 0.35
  var freshBloodRate = 0.25
  var freshBloodFrequency = 6
  var invasionFrequency = 20
  var invasionSurvivedRate = 0.10

  var bestSolution = ScoredSolution(problem.emptySolution(), 0)
  var previousBest = ScoredSolution(problem.emptySolution(), 0)

  implicit val context = Context(problem)

  var legends = List.empty[ScoredSolution]

  private var lastTimeScoreChanged = 0
  private var lastTimeFreshBlood = 0

  var greedySolution = Option.empty[ScoredSolution]

  greedyFuture()

  var population = populateAndScore(populationN)
  bestSolution = population.maxBy(_.score)

  /**
    *
    * @return true, if best solution got updated
    */
  def iterate(): Boolean = {

    population = EvolutionalSolver.iterate(population, copCoef)
    lastTimeScoreChanged += 1
    var res = false
    val newBest = population.maxBy(_.score)
    if(newBest.score > previousBest.score) {
      println(s"${newBest.score}, boost took iterations: $lastTimeScoreChanged")
      println()
      lastTimeScoreChanged = 0
    }
    previousBest = newBest
    if(newBest.score > bestSolution.score) {
      bestSolution = newBest
      res = true
    }

    //if (lastTimeFreshBlood == freshBloodFrequency) {
    if (lastTimeScoreChanged % freshBloodFrequency == freshBloodFrequency - 1) {
      //println("fresh blood!")
      val freshBloodN = (population.length * freshBloodRate).toInt
      population = (shuffle(population).take(population.length - freshBloodN) ++ populateAndScore(freshBloodN)).toArray
      lastTimeFreshBlood = 0
    }
    lastTimeFreshBlood += 1
    res
  }

  def changePopulationCount(count: Int): Unit = {

    if(count > populationN) {
      population = population ++ populateAndScore(count - populationN)
    } else {
      population = shuffle(population).take(count).toArray
    }

    populationN = count
    println(s"Population count changed: $count")
  }

  def addGreedy() = {
    println("adding GREADY")
    greedySolution.foreach { greedy =>
      population = shuffle(greedy +: shuffle(population).take(populationN - 1)).toArray
    }
  }

  def greedyFuture():Future[Unit] = Future {
    ()
  }

}

object EvolutionalSolver {

  case class ScoredSolution(solution: Solution, score: ScoreType) {

  }

  case class Context(problem: Problem) {
    //helper methods for problem
  }

  def populate(count: Int)(implicit context: Context): Seq[Solution] = {

    val random = new Random()

    (1 to count).par.map( _ => nextSolution(random)(context.problem) ).toList
  }

  def score(solution: Solution)(implicit context: Context): ScoredSolution = {
    import context._
    val score = ScoringFunction.score(problem, solution)
    ScoredSolution(solution, score)
  }

  def populateAndScore(count: Int)(implicit context: Context): Array[ScoredSolution] = {

    import context._
    populate(count).par.map(score).toArray

  }

  def iterate(
    solutions: Array[ScoredSolution],
    copCoef: Double
  )(implicit context: Context): Array[ScoredSolution] = {

    import context._

    val random = new Random()

    def butNot(k: Int, n: Int, not: Set[Int]): Int = {
      if (not contains k) {
        butNot((k + 1) % n, n, not)
      } else {
        k
      }
    }

    solutions.indices.par.map(index => {
      val solution = solutions(index)
      val iA = butNot(random.nextInt(solutions.length), solutions.length, Set(index))
      val iB = butNot(random.nextInt(solutions.length), solutions.length, Set(index, iA))
      val iC = butNot(random.nextInt(solutions.length), solutions.length, Set(index, iA, iB))
      val a = solutions(iA)
      val b = solutions(iB)
      val c = solutions(iC)
      val d = copulate(a.solution, b.solution, c.solution, copCoef)
      val e = crossover(solution.solution, d)
      val eScored = score(e)
      if (eScored.score > solution.score) {
        eScored
      } else {
        solution
      }
    }).toArray
  }

  def copulate(
    a: Solution,
    b: Solution,
    c: Solution,
    copCoef: Double
  )(implicit context: Context): Solution = {
    diff(a, useCoef(diff(b, c), copCoef))
  }

  def crossover(a: Solution, b: Solution)(implicit context: Context): Solution = {
    ???
  }

  def diff(a: Solution, b: Solution)(implicit  context: Context): Solution = {
    ???
  }

  def useCoef(value: Solution, coef: Double)(implicit context: Context): Solution = {
    ???
  }


  def distance(a: Solution, b: Solution)(implicit context: Context): Long = {
    ???
  }

}

package logic.evolutional

import logic.Problem.Problem

import scala.util.Random

object RandomHelpers {

  def nextSolution(random: Random)(implicit problem: Problem) = {
    ???
  }

  def shuffle[A](seq: Seq[A]): Stream[A] = {
    val random = new Random()
    def randomElementAndRest(s: Seq[A]): (A, Seq[A]) = {
      val i = random.nextInt(s.length)
      val (first, value +: rest) = s.splitAt(i)
      (value, first ++ rest)
    }
    def helper(s: Seq[A]): Stream[A] = s match {
      case Seq() => Stream.empty
      case _ =>
        val (value, rest) = randomElementAndRest(s)
        value #:: helper(rest)
    }
    helper(seq)
  }

}

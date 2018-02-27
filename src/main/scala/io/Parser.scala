package io

import logic.Problem._

import scala.io.Source

object Parser {

  def parseFile(filePath: String): Problem = {
    val linesIter = Source.fromFile(filePath).getLines()
    parse(linesIter)
  }

  def parse(linesIter: Iterator[String]) : Problem = {
    //parsing parts of problem
    //
    Problem(/*parts*/)
  }

  /* examples

  def parse3Ints(line: String): (Int, Int, Int) = {
    val regex = """(\d+) (\d+) (\d+)""".r
    line match { case regex(f, s, t) => (f.toInt, s.toInt, t.toInt) }
  }

  def parseIntArray(line: String, n: Int): Array[Int] = {
    val sizeStrs = line.split(" ")
    sizeStrs.map(_.toInt)
  }

  def parseChar2DArray(linesIter: Iterator[String], rowsN: Int, columnsN: Int): Array[Array[Char]] = {
    val res = Array.ofDim[Char](rowsN, columnsN)
    (0 until rowsN).foreach{ rowId =>
      val rowLine = linesIter.next()
      //choose
      val separated = res(rowId) = rowLine.split(" ").map(_.toCharArray.head)
      val notSeparated = res(rowId) = rowLine.toCharArray
      ???
    }
    res
  }

  */


}

package io


import java.io.{File, PrintStream}
import logic.Problem._

object Printer {

  //val sampleToTest = Solution()

  //DO NOT CHANGE
  def printSolutionToFile(solution: Solution, fileName: String): Unit = {
    def file = new File(fileName)
    if(file.exists()) file.delete()
    file.createNewFile()
    printSolution(solution, new PrintStream(file))
  }

  //when implement, create sample (maybe copy from problem definition) and try it (e.g. in REPL)
  def printSolution(solution: Solution, out: PrintStream = Console.out): Unit = {
    import out._  //now `println` prints into file if set, or into regular console otherwise
    ???
  }

}

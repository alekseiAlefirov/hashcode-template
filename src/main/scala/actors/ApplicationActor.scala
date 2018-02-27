package actors

import actors.Messages.Application._
import actors.Messages.Solver.{GreedyAdded, Work}
import io.Parser.parseFile
import actors.Messages.{Application, Solver}
import akka.actor.{Actor, ActorRef, Props}
import gui.SolverStage
import io.Printer.printSolutionToFile
import logic.Problem._


class ApplicationActor(guiStage: SolverStage, args: Array[String]) extends Actor {

  import  ApplicationActor._

  val problemName = getProblemName(args(0))
  var solver = ActorRef.noSender
  var paused = false
  var highscore = 0L
  var bestSolution = dummySolution

  override def preStart() = {

    createOutputFolder(problemName)
    val problem = parseFile(args(0))
    solver = context.actorOf(SolverActor.props(problem))
    solver ! Solver.Work
    initGui()

  }

  override def receive: Receive = {

    case Application.Pause =>
      guiStage.pauseButton.text = "Unpause"
      guiStage.onPauseButtonClicked = _ => self ! Application.Unpause
      paused = true

    case Application.Unpause =>
      guiStage.pauseButton.text = "Pause"
      guiStage.onPauseButtonClicked = _ => self ! Application.Pause
      paused = false
      solver ! Work

    case Invade => solver ! Invade
    case CreateLegend => solver ! CreateLegend

    case Solver.Iterated(updated) => {
      updated.foreach{
        case (newBestSolution, newHighscore) =>
          bestSolution = newBestSolution
          highscore = newHighscore
          guiStage.highscoreLabel.updateHighScore(highscore)
      }
      if(!paused) sender ! Solver.Work
    }

    case Solver.Configuration(
      populationCount: Int,
      mutationRate: Double,
      freshBloodRate: Double,
      freshBloodFrequency: Int
    ) =>
      guiStage.populationCountBox.text_=(populationCount.toString)
      guiStage.mutationBox.text_=(mutationRate.toString)
      guiStage.freshBloodRateBox.text_=(freshBloodRate.toString)
      guiStage.freshBloodFrequencyBox.text_=(freshBloodFrequency.toString)

    case (x @ UpdateConfiguration(_, _, _, _)) => solver ! x

    case AddGreedy => solver ! AddGreedy

    case GreedyAdded => guiStage.addGreedyButton.disable_=(false)

    case Save => {
      save()
      println("Saved")
    }

  }

  def initGui(): Unit = {
    guiStage.onPauseButtonClicked        = _ => self ! Application.Pause
    guiStage.onSaveButtonClicked         = _ => self ! Application.Save
    guiStage.onUpdateButtonClicked =            self ! _
    guiStage.onInvadeButtonClicked =       _ => self ! Application.Invade
    guiStage.onCreateLegendButtonClicked = _ => self ! Application.CreateLegend
    guiStage.onAddGreedyButtonClicked    = _ => self ! Application.AddGreedy
    guiStage.addGreedyButton.disable_=(true)
  }

  def save(): Unit = {
    val fileName = s"./outputs/$problemName/$highscore.out"
    val highscoreFileName = s"./outputs/$problemName/highscore.out"
    printSolutionToFile(bestSolution, fileName)
    printSolutionToFile(bestSolution, highscoreFileName)
  }

}

object ApplicationActor{

  import java.io.File

  def props(guiStage: SolverStage, args: Array[String]) = Props(classOf[ApplicationActor], guiStage, args)

  def getProblemName(filePath: String): String = {
    filePath.substring(filePath.lastIndexOf("/") + 1, filePath.lastIndexOf("."))
  }

  def createOutputFolder(problemName: String): Unit = {
    val folderName = s"./outputs/$problemName"
    val folder = new File(folderName)
    if(!folder.exists()) {
      folder.mkdir()
    }
  }

}

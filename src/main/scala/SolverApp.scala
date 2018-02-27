import akka.actor.ActorSystem
import gui.SolverStage
import actors.ApplicationActor


import scalafx.application.JFXApp
import scalafx.Includes._

object SolverApp extends JFXApp {

  val myStage = new SolverStage(s"Solver: ${parameters.raw(0)}")
  val actorSystem = ActorSystem("solver-system")

  val appActor = actorSystem.actorOf(
    ApplicationActor.props(myStage, parameters.raw.toArray).withDispatcher("scalafx-dispatcher"),
    "app-actor"
  )


  myStage.onCloseRequest = handle {
    actorSystem.terminate()
  }

  stage = myStage

}

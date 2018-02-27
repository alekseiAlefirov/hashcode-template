package gui

import scalafx.Includes.handle
import scalafx.application.JFXApp
import scalafx.scene.Scene
import scalafx.scene.control.{Button, Label, TextArea, TextField}
import scalafx.scene.layout.HBox
import scalafx.scene.layout.VBox
import actors.Messages.Application.UpdateConfiguration


class SolverStage(_title: String) extends JFXApp.PrimaryStage {

  title.value = _title
  width = 600
  height = 450

  var onPauseButtonClicked:        Unit => Unit = { _ => () }
  var onPopulationCountUBClicked:  Int => Unit = { _ => () }
  var onInvadeButtonClicked:       Unit => Unit = {_ => () }
  var onCreateLegendButtonClicked: Unit => Unit = { _ => () }
  var onAddGreedyButtonClicked:    Unit => Unit = { _ => () }
  var onUpdateButtonClicked:       UpdateConfiguration => Unit = { _ => () }
  var onSaveButtonClicked:        Unit => Unit = { _ => () }

  val pauseButton = new Button("Pause"){
    onMouseClicked = handle { onPauseButtonClicked() }
  }

  val saveButton = new Button("Save"){
    onMouseClicked = handle { onSaveButtonClicked() }
  }

  val populationCountBox = new TextField

  val invadeButton = new Button("Invade!"){
    onMouseClicked = handle { onInvadeButtonClicked() }
  }

  val highscoreLabel = new Label("Highscore: ") {
    def updateHighScore(value: Long) = {
      this.text_=(s"Highscore: $value")
    }
  }

  val updateButton = new Button("Update"){
    onMouseClicked = handle {
      onUpdateButtonClicked(
        UpdateConfiguration(
          populationCountBox.text.value.toInt,
          mutationBox.text.value.toDouble,
          freshBloodRateBox.text.value.toDouble,
          freshBloodFrequencyBox.text.value.toInt
        ))
    }
  }

  val populationArea = new HBox{
    children = Seq(
      new Label("Population count:"),
      populationCountBox
    )
  }

  val createLegendButton = new Button("Create legend"){
    onMouseClicked = handle { onCreateLegendButtonClicked() }
  }

  val legendsArea = new HBox{
    children = Seq(
      createLegendButton
    )
  }

  val freshBloodRateBox = new TextField
  val freshBloodFrequencyBox = new TextField
  val freshBloodArea = new VBox{
    children = Seq(
      new HBox(
        new Label("Fresh blood rate:"),
        freshBloodRateBox
      ),
      new HBox(
        new Label("Fresh blood frequency:"),
        freshBloodFrequencyBox
      )
    )
  }

  val mutationBox = new TextField
  val mutationArea = new HBox{
    children = Seq(
      new HBox(
        new Label("Mutation rate:"),
        mutationBox
      )
    )
  }

  val addGreedyButton = new Button("Add GREEDY"){
    onMouseClicked = handle { onAddGreedyButtonClicked() }
  }

  scene = new Scene {
    //fill = LightGreen
    content = new VBox {
      children = Seq (
        highscoreLabel,
        new HBox(){
          children = Seq(pauseButton, saveButton)
        },
        populationArea,
        mutationArea,
        freshBloodArea,
        updateButton,
        legendsArea,
        addGreedyButton,
        invadeButton
      )
    }
  }

}

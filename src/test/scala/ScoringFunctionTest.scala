import logic.Problem._
import org.scalatest._

class ScoringFunctionTest extends FlatSpec with Matchers {

  //behavior of ScoringFunction

  import logic.ScoringFunction._

  it should "score trivial" in {
    //GIVEN
    val situation = Problem()

    val solution =
      Solution()
    // WHEN
    val expectedResult = ???
    //THEN
    score(situation, solution) shouldBe expectedResult
  }

  it should "score example" in {
    //GIVEN
    val situation = Problem()

    val solution =
      Solution()
    // WHEN
    val expectedResult = ???
    //THEN
    score(situation, solution) shouldBe expectedResult
  }

}

package root

object Main {

  // Initial state: all subjects on the left side
  // true - left coast, false - right coast
  class State(val array: Array[Boolean] = Array(true, true, true, true)) {
    require(array.length == 4, "Initial state is invalid")

    private val wolfEatGoat    = wolf == goat && wolf != farmer
    private val goatEatCabbage = goat == cabbage && goat != farmer

    def farmer: Boolean = array(0)

    def wolf: Boolean = array(1)

    def goat: Boolean = array(2)

    def cabbage: Boolean = array(3)

    def isValidStep: Boolean = !wolfEatGoat && !goatEatCabbage

    def isFinish: Boolean = this.array.forall(!_)

    // Farmer moves one passenger to another size.
    def move(idx: Int): State = {
      val newState = this.array.clone()
      newState(0) = !newState(0)
      newState(idx) = !newState(idx)
      new State(newState)
    }

    // Farmer moves back alone.
    def moveBackAlone: State = {
      val newState = this.array.clone()
      newState(0) = !newState(0)
      new State(newState)
    }

    override def toString = s"State(${this.array.map(it ⇒ if (it) "L" else "R").mkString(",")})"

    override def equals(obj: Any): Boolean = obj match {
      case arr: State ⇒ this.array.deep == arr.array.deep
      case _ ⇒ false
    }

  }

  def main(args: Array[String]): Unit = {
    val state = new State()
    findSolutions(state, Nil)
  }

  def findSolutions(state: State, prevStates: Seq[State]) {

    // exit when task is complete
    if (state.isFinish) {
      println(s"Finish state $prevStates")
    }

    val currentCoast = state.farmer

    val potentialPassengers =
      state.array.zipWithIndex.tail
        .filter { case (coast, _) ⇒ coast == currentCoast }
        .map(_._2)

    val allMoves = potentialPassengers
      .flatMap(idx ⇒ Array(state.move(idx))) :+ state.moveBackAlone

    allMoves
      // filter invalid and previous states
      .filter(s ⇒ s.isValidStep && !prevStates.contains(s))
      .foreach(next ⇒ findSolutions(next, prevStates :+ state))

  }

}
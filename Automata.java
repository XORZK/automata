public class Automata {
  private boolean moore = true;
  private State[] states;

  public Automata(int size) {
    this.states = new State[size];
  }

  public Automata(State[] s) {
    this.states = s;
  }

  public State getSuccessive(State s) {
    if (this.states == null || s == null) { return null; }

    State state = null;

    for (int i = 0; i < this.states.length; i++) {
      if (state == null || (this.states[i].getState() > s.getState() && this.states[i].getState() < state.getState())) { state = this.states[i]; }
    }

    return state;
  }

  public State getPrevious(State s) {
    if (this.states == null || s == null) { return null; }

    State state = null;

    for (int i = 0; i < this.states.length; i++) {
      if (state == null || (this.states[i].getState() < s.getState() && this.states[i].getState() > state.getState())) { state = this.states[i]; }
    }

    return state;
  }
};
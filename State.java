public class State {
  private int STATE, COLOR;

  public State() {
    this.STATE = this.COLOR = 0;
  }

  public State(int S) {
    this.STATE = S;
  }

  public State(int S, int C) {
    this.STATE = S;
    this.COLOR = C;
  }

  public State(int S, String C) {
    this.STATE = S;
    this.COLOR = Integer.parseInt(C, 16);
  }

  public static State DEAD() {
    return new State(0, "000000");
  }

  public static State ALIVE() {
    return new State(1, "FFFFFF");
  }

  public int getState() {
    return this.STATE;
  }

  public int getColor() {
    return this.COLOR;
  }

  public String toString() {
    return String.format("%s: 0x%x", this.STATE, this.COLOR);
  }
};
public class Cell {
  private int x, y;
  private State state;

  public Cell() {
    this.x = this.y = 0;
    this.state = State.DEAD();
  }

  public int getState() {
    return (this.state != null ? this.state.getState() : -1);
  }
};
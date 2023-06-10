import java.io.Serializable;

public class Cell implements Serializable {
	private Vec2<Integer> pos;
	private State state, next;

	public Cell() {
		this.pos = new Vec2<Integer>(0,0);
		this.state = State.DEAD();
	}

	public Cell(State s) {
		this.pos = new Vec2<Integer>(0,0);
		this.state = (s != null ? s : State.DEAD());
	}

	public Cell(int px, int py) {
		this.pos = new Vec2<Integer>(px, py);
		this.state = State.DEAD();
	}

	public Cell(int px, int py, State s) {
		this.pos = new Vec2<Integer>(px, py);
		this.state = s;
	}

	public Cell(Vec2<Integer> p, State s) {
		this.pos = p;
		this.state = s;
	}

	public void translate(Vec2<Integer> translation) {
		this.pos = new Vec2<Integer>(this.pos.getX() + translation.getX(), this.pos.getY() + translation.getY());
	}

	public void setX(int x) {
		this.pos.setX(x);
	}

	public int getX() {
		return this.pos.getX();
	}

	public void setY(int y) {
		this.pos.setY(y);
	}

	public void setNext(State n) {
		this.next = n;
	}

	public void setNext() {
		this.state = this.next;
		this.next = null;
	}

	public int getY() {
		return this.pos.getY();
	}

	public void setPos(Vec2<Integer> p) {
		this.pos = p;
	}

	public Vec2<Integer> getPos() {
		return this.pos;
	}

	public void setState(State s) {
		this.state = s;
	}

	public State getState() {
		return this.state;
	}

	public int getValue() {
		return this.state.getValue();
	}

	public int getColor() {
		return this.state.getColor();
	}

	public Cell copy() {
		return new Cell(this.pos, this.state);
	}

	public String toString() {
		return String.format("%s: {%s}", this.pos.toString(), this.state.toString());
	}
};

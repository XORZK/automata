public class Cell {
	private Vec2<Integer> pos;
	private State state;

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

	public int getX() {
		return this.pos.getX();
	}

	public int getY() {
		return this.pos.getY();
	}

	public String toString() {
		return String.format("%s: {%s}", this.pos.toString(), this.state.toString());
	}
};

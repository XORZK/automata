public class State implements Comparable<State> {
	private State successor;
	private int color, value;

	public State() {
		this.color = this.value = 0;
	}

	public State(int v) {
		this.value = v;
	}

	public State(int v, int c) {
		this.value = v;
		this.color = c;
	}

	public State(int v, String c) {
		this.value = v;
		this.color = Integer.parseInt(c, 16);
	}

	public State(int v, int c, State succ) {
		this.value = v;
		this.color = c;
	}

	public void setSuccessor(State s) {
		this.successor = s;
	}

	public State getSuccessor() {
		if (this.successor == null) {
			this.successor = State.DEAD();
		}
		return this.successor;
	}

	public final static State DEAD() {
		return new State(0, "000000");
	}

	public final static State ALIVE() {
		return new State(1, "FFFFFF");
	}

	public void setValue(int v) {
		this.value = v;
	}

	public int getValue() {
		return this.value;
	}

	public int getColor() {
		return this.color;
	}

	public void setColor(int c) {
		this.color = c;
	}

	public void setColor(String c) {
		this.color = Integer.parseInt(c, 16);
	}

	@Override
	public int compareTo(State s) {
		return this.value - s.getValue();
	}

	@Override
	public int hashCode() {
		return Integer.valueOf(this.value).hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) { return true; }
		if (o == null || this.getClass() != o.getClass()) { return false; }
		State s = (State) o;
		return (s.getValue() == this.value);
	}

	@Override
	public String toString() {
		return String.format("%d: 0x%x", this.value, this.color);
	}
};

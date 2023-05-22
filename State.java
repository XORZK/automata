public class State implements Comparable<State> {
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

	public final static State DEAD() {
		return new State(0, "000000");
	}

	public final static State ALIVE() {
		return new State(1, "FFFFFF");
	}

	public int getValue() {
		return this.value;
	}

	public int getColor() {
		return this.color;
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

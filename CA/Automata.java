import java.util.HashMap;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

public class Automata {
	private int radius = 1;
	private boolean moore = true;
	final private State dead = State.DEAD(), alive = State.ALIVE();
	private Transition transition;
	private HashMap<Vec2<Integer>, Cell> activeCells, nextCells;
	private Set<State> states;

	public Automata() {
		this.activeCells = new HashMap<Vec2<Integer>, Cell>();
		this.nextCells = new HashMap<Vec2<Integer>, Cell>();
		this.states = new HashSet<State>();
		this.transition = new Transition();
	}

	public Automata(Transition t) {
		this();
		this.transition = t;
	}

	public void addState(State s) {
		this.states.add(s);
		this.initializeStates();
	}

	public void setStates(State[] s) {
		for (int i = 0; i < s.length; i++) {
			this.states.add(s[i]);
		}
		this.initializeStates();
	}

	public void setStates(Set<State> s) {
		this.states = s;
		this.initializeStates();
	}

	private void initializeState(State state) {
		State succ = null, min = this.states.toArray(new State[this.states.size()])[0];

		for (State s : this.states) {
			min = (min.getValue() > s.getValue() ? s : min);
			if (s.equals(state)) continue;

			if (succ == null) { succ = s; }

			if (s.getValue() > state.getValue()) {
				if (succ.getValue() < state.getValue() || (succ.getValue() > state.getValue() && s.getValue() < succ.getValue())) {
					succ = s;
				}
			}
		}

		succ = ((succ == null) || (succ.getValue() < state.getValue()) ? min : succ);

		System.out.println(String.format("STATE %s: SUCCESSOR %s", state, succ));

		state.setSuccessor(succ);
	}

	private void initializeStates() {
		if (this.states == null) {
			return;
		}

		for (State s : this.states) {
			this.initializeState(s);
		}
	}

	public Transition getTransition() {
		return this.transition;
	}

	public void setTransition(Transition t) {
		this.transition = t;
	}

	public void activateCell(State s, Vec2<Integer> pos) {
		if (this.activeCells.containsKey(pos)) {
			this.activeCells.get(pos).setState(s);
		} else {
			this.activeCells.put(pos, new Cell(pos, s));
		}
	}

	public HashMap<Vec2<Integer>, Cell> getActiveCells() {
		return this.activeCells;
	}

	private Cell[] getNeighbours(Cell c) {
		int idx = 0;
		Cell[] neighbours = new Cell[8];
		for (int dx = -radius; dx <= radius; dx++) {
			for (int dy = -radius; dy <= radius; dy++) {
				if (dx == dy && dx == 0) {
					continue;
				}

				Vec2<Integer> pos = new Vec2<Integer>(c.getX()+dx, c.getY()+dy);

				if (this.activeCells.containsKey(pos)) {
					neighbours[idx] = this.activeCells.get(pos);
					neighbours[idx].setPos(pos);
				} else {
					neighbours[idx] = new Cell(pos, this.dead);
				}
				idx++;
			}
		}

		return neighbours;
	}

	private int countNeighbours(Cell[] nb) {
		int count = 0;
		for (int i = 0; i < nb.length; i++) {
			count += (nb[i].getValue() > 0 ? 1 : 0);
		}
		return count;
	}

	private void updateCell(Cell c, int count) {
		State s = this.transition.getTransition(c.getState(), count);
		c.setState(s);
		if (!s.equals(this.dead)) {
			this.nextCells.put(c.getPos(), c);
		}
	}


	public void tick() {
		System.out.println(this.activeCells);
		for (Vec2<Integer> pos : this.activeCells.keySet()) {
			Cell[] nb = this.getNeighbours(this.activeCells.get(pos));
			for (int k = 0; k < 8; k++) {
				if (nb[k].getValue() > 0) continue;
				int count = this.countNeighbours(this.getNeighbours(nb[k]));
				this.updateCell(nb[k], count);
			}
			this.updateCell(this.activeCells.get(pos), this.countNeighbours(nb));
		}

		this.activeCells = this.nextCells;
		this.nextCells = new HashMap<Vec2<Integer>, Cell>();
	}
};

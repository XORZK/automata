import java.util.HashMap;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

public class Automata implements Serializable {
	private int radius = 1, ticks = 0;
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
		this.setTransition(t);
		this.initializeStates();
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

		state.setSuccessor(succ);
	}

	public State minState() {
		State min = null;
		for (State s : this.states) {
			if (min == null || s.getValue() < min.getValue()) {
				min = s;
			}
		}
		return min;
	}

	public State maxState() {
		State max = null;
		for (State s : this.states) {
			if (max == null || s.getValue() > max.getValue()) {
				max = s;
			}
		}
		return max;
	}

	public Configuration createConfiguration(Vec2<Integer> start, Vec2<Integer> end) {
		Vec2<Integer> topLeft = new Vec2<Integer>(Math.min(start.getX(), end.getX()), Math.max(start.getY(), end.getY())),
					  bottomRight = new Vec2<Integer>(Math.max(start.getX(), end.getX()), Math.min(start.getY(), end.getY()));
		Cell[][] cells = new Cell[Math.abs(topLeft.getY() - bottomRight.getY()) + 1][Math.abs(topLeft.getX() - bottomRight.getX()) + 1];

		for (int x = 0; x <= Math.abs(topLeft.getX() - bottomRight.getX()); x++) {
			for (int y = 0; y <= Math.abs(topLeft.getY() - bottomRight.getY()); y++) {
				Vec2<Integer> pos = new Vec2<Integer>(topLeft.getX() + x, topLeft.getY() - y);
				cells[y][x] = (this.activeCells.containsKey(pos) ? this.activeCells.get(pos).copy() : new Cell(this.minState()));
			}
		}

		Configuration cfg = new Configuration(cells);

		return cfg.crop();
	}

	public void toggleCell(Vec2<Integer> pos) {
		if (this.activeCells.containsKey(pos)) {
			Cell c = this.activeCells.get(pos);
			c.setState(c.getState().getSuccessor());
			if (c.getState().equals(State.DEAD())) {
				this.activeCells.remove(pos);
			}
		} else {
			this.activateCell(this.maxState(), pos);
		}
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
		this.states = (this.transition == null ? null : this.transition.getStates());
	}

	public void activateCell(State s, Vec2<Integer> pos) {
		if (s.equals(this.minState())) {
			this.deleteCell(pos);
			return;
		}
		if (this.activeCells.containsKey(pos)) {
			this.activeCells.get(pos).setState(s);
		} else {
			this.activeCells.put(pos, new Cell(pos, s));
		}
	}

	public ArrayList<Cell> translateBounded(ArrayList<Cell> cells, Vec2<Integer> translation) {
		ArrayList<Cell> translated = new ArrayList<Cell>();
		for (Cell c : cells) {
			Cell copy = c.copy();
			copy.translate(translation);
			translated.add(copy);
		}

		for (int i = 0; i < cells.size(); i++) {
			this.deleteCell(cells.get(i).getPos());
		}

		for (int i = 0; i < translated.size(); i++) {
			Cell c = translated.get(i);
			this.activeCells.put(c.getPos(), c);
		}

		return translated;
	}

	public void deleteCell(Vec2<Integer> pos) {
		if (this.activeCells.containsKey(pos)) {
			this.activeCells.remove(pos);
		}
	}

	public ArrayList<Cell> getBoundedCells(Vec2<Integer> center, int width, int height) {
		return this.getBoundedCells(center, width, width, height, height);
	}

	
	public ArrayList<Cell> getBoundedCells(Vec2<Integer> center, int leftWidth, int rightWidth, int upperHeight, int lowerHeight) {
		ArrayList<Cell> bounded = new ArrayList<Cell>();

		for (Vec2<Integer> v2 : this.activeCells.keySet()) {
			Vec2<Integer> pos = this.activeCells.get(v2).getPos();
			if (pos.getX() >= center.getX() - leftWidth && pos.getX() <= center.getX() + rightWidth &&
					pos.getY() >= center.getY() - lowerHeight && pos.getY() <= center.getY() + upperHeight) {
				bounded.add(this.activeCells.get(v2));
			}
		}
		return bounded;
	}
	
	public HashMap<Vec2<Integer>, Cell> getActiveCells() {
		return this.activeCells;
	}

	public void addConfiguration(Configuration cfg, Vec2<Integer> pos) {
		if (cfg == null) return;
		cfg.setRelativePos(pos);
		ArrayList<Cell> active = cfg.getActiveCells();
		for (Cell c : active) {
			this.activeCells.put(c.getPos(), c.copy());
		}
	}

	public void addConfiguration(Configuration cfg) {
		this.addConfiguration(cfg, cfg.getRelativePos());
	}

	public ArrayList<Vec2<Integer>> getActivePositions() {
		ArrayList<Vec2<Integer>> pos = new ArrayList<Vec2<Integer>>();
		for (Vec2<Integer> p : this.activeCells.keySet()) {
			pos.add(p);
		}
		return pos;
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
				} else {
					neighbours[idx] = new Cell(pos, this.dead);
				}
				idx++;
			}
		}

		return neighbours;
	}

	public Cell getCell(Vec2<Integer> pos) {
		return (this.activeCells.containsKey(pos) ? this.activeCells.get(pos) : new Cell(this.minState()));
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
		if (!s.equals(this.dead) && !this.nextCells.containsKey(c.getPos())) {
			c.setNext(s);
			this.nextCells.put(c.getPos(), c);
		}
	}

	private void updateCell(Cell c) {
		Cell[] neighbours = this.getNeighbours(c);
		int count = this.countNeighbours(neighbours);
		this.updateCell(c, count);
	}

	public void tick() {
		for (Vec2<Integer> pos : this.activeCells.keySet()) {
			Cell c = this.activeCells.get(pos);
			Cell[] nb = this.getNeighbours(c);

			for (int i = 0; i < nb.length; i++) {
				this.updateCell(nb[i]);
			}
			this.updateCell(c);
		}

		this.activeCells = new HashMap<Vec2<Integer>, Cell>(this.nextCells);
		this.nextCells = new HashMap<Vec2<Integer>, Cell>();

		for (Vec2<Integer> pos : this.activeCells.keySet()) {
			this.activeCells.get(pos).setNext();
		}
		this.ticks++;
	}

	public int getTicks() {
		return this.ticks;
	}
};

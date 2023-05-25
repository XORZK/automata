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
		transition = new Transition();
	}

	public void setStates(Set<State> s) {
		this.states = s;
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
		int index = 0;
		Cell[] neighbours = new Cell[8];
		for (int dx = -radius; dx <= radius; dx++) {
			for (int dy = -radius; dy <= radius; dy++) {
				if (dx == dy && dx == 0) {
					continue;
				}

				Vec2<Integer> pos = new Vec2<Integer>(c.getX()+dx, c.getY()+dy);

				if (this.activeCells.containsKey(pos)) {
					neighbours[index] = this.activeCells.get(pos);
					neighbours[index].setPos(pos);
				} else {
					neighbours[index] = new Cell(pos, this.dead);
				}
				index++;
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

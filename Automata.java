import java.util.HashMap;
import java.util.ArrayList;

public class Automata {
	private int radius = 1;
	private boolean moore = true;
	private State dead = State.DEAD(), alive = State.ALIVE();
	private Transition transition;
	private HashMap<Vec2<Integer>, Cell> activeCells, nextCells;

	public Automata() {
		this.activeCells = new HashMap<Vec2<Integer>, Cell>();
		this.nextCells = new HashMap<Vec2<Integer>, Cell>();
		transition = new Transition();
	}

	public Transition getTransition() {
		return this.transition;
	}

	public void setTransition(Transition t) {
		this.transition = t;
	}

	public HashMap<Vec2<Integer>, Cell> getActiveCells() {
		return this.activeCells;
	}

	public Cell[] getNeighbours(Cell c) {
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
					neighbours[index] = new Cell(pos, State.DEAD());
				}
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
		for (int i = 0; i < this.activeCells.size(); i++) {
			Cell[] nb = this.getNeighbours(this.activeCells.get(i));
			for (int k = 0; k < 8; k++) {
				if (nb[k].getValue() > 0) continue;
				int count = this.countNeighbours(this.getNeighbours(nb[k]));
				this.updateCell(nb[k], count);
			}
			this.updateCell(this.activeCells.get(i), this.countNeighbours(nb));
		}

		this.activeCells = this.nextCells;
		this.nextCells = new HashMap<Vec2<Integer>, Cell>();
	}
};

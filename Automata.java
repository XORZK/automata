import java.util.HashMap;
import java.util.ArrayList;

public class Automata {
	private int radius = 1;
	private boolean moore = true;
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

	public ArrayList<Cell> getNeighbours(Cell c) {
		ArrayList<Cell> neighbours = new ArrayList<Cell>();
		for (int dx = -radius; dx <= radius; dx++) {
			for (int dy = -radius; dy <= radius; dy++) {
				if (dx == dy && dx == 0) {
					continue;
				}

				Vec2<Integer> pos = new Vec2<Integer>(c.getX()+dx, c.getY()+dy);

				if (this.activeCells.containsKey(pos)) {
					neighbours.add(this.activeCells.get(pos));
				} else {
					neighbours.add(new Cell(pos, State.DEAD()));
				}
			}
		}

		return neighbours;
	}

	public void tick() {
		for (int i = 0; i < this.activeCells.size(); i++) {
		}
	}
};

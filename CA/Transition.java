import java.util.HashMap;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

public class Transition {
	// cx => (nx, c(x+1))
	private HashMap<State, HashMap<ArrayList<State>, State>> stateTransition;
	private HashMap<State, HashMap<Integer, State>> countTransition;

	public Transition() {
		this.stateTransition = new HashMap<State, HashMap<ArrayList<State>, State>>();
		this.countTransition = new HashMap<State, HashMap<Integer, State>>();
	}

	public Transition(HashMap<State, HashMap<ArrayList<State>, State>> st,
										HashMap<State, HashMap<Integer, State>> ct) {
		this.stateTransition = st;
		this.countTransition = ct;
	}

	public void insert(State sx, State rx, int neighbourCount) {
		HashMap<Integer, State> inner;
		if (!this.countTransition.containsKey(sx)) {
			inner = new HashMap<Integer, State>();
		} else {
			inner = this.countTransition.get(sx);
		}
		inner.put(neighbourCount, rx);
		this.countTransition.put(sx, inner);
	}

	public void insert(State sx, State rx, ArrayList<State> neighbourhood) {
		HashMap<ArrayList<State>, State> inner;

		if (!this.stateTransition.containsKey(sx)) {
			inner = new HashMap<ArrayList<State>, State>();
		} else {
			inner = this.stateTransition.get(sx);
		}

		inner.put(neighbourhood, rx);
		this.stateTransition.put(sx, inner);
	}

	public void insert(State sx, State rx, int... nc) {
		for (int count : nc) {
			this.insert(sx, rx, count);
		}
	}

	public State getTransition(State cx, int neighbourCount) {
		if (this.countTransition.containsKey(cx) &&
				this.countTransition.get(cx).containsKey(neighbourCount)) {
			return this.countTransition.get(cx).get(neighbourCount);
		}

		return cx.getSuccessor();
	}

	public State getTransition(State cx, ArrayList<State> neighbourhood) {
		if (this.stateTransition.containsKey(cx) &&
				this.stateTransition.get(cx).containsKey(neighbourhood)) {
			return this.stateTransition.get(cx).get(neighbourhood);
		}

		return cx.getSuccessor();
	}

	public final static Transition GOL() {
		State a = State.ALIVE(), d = State.DEAD();
		Transition gol = new Transition();
		gol.insert(a, a, 2, 3);
		gol.insert(d, a, 3);

		return gol;
	}

	@Override
	public String toString() {
		String rep = "";

		ArrayList<State> keys = new ArrayList<State>(this.countTransition.keySet());

		for (int i = 0; i < keys.size(); i++) {
			State current = keys.get(i);
			HashMap<Integer, State> inner = this.countTransition.get(current);
			rep += String.format("[%s]: ", current.getValue());

			for (Integer count : inner.keySet()) {
				rep += String.format("(%d: %s)", count, inner.get(count).getValue());
			}

			rep += (i != keys.size() - 1 ? "\n" : "");
		}

		return rep;
	}
};

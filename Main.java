import java.util.ArrayList;

public class Main {
	public static void main(String[] args) {
		State a = State.ALIVE(), d = State.DEAD();
		Automata g = new Automata();

		g.setTransition(Transition.GOL());
		g.activateCell(a, new Vec2<Integer>(0, 0));
		g.activateCell(a, new Vec2<Integer>(0, 1));
		g.activateCell(a, new Vec2<Integer>(1, 1));
		g.tick();
		g.tick();
		g.tick();
		g.tick();
		System.out.println(g.getActiveCells());
	}
};

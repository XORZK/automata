import java.util.ArrayList;

public class Main {
	public static void main(String[] args) {
		Automata g = new Automata();
		g.setTransition(Transition.GOL());
		System.out.println(g.getActiveCells());
	}
};

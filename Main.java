import java.util.ArrayList;

public class Main {
	public static void main(String[] args) {
		Circuit x = new NOTGate();
		Circuit on = new Switch(), off = new Switch();

		x.connect(on.outputPorts()[0], 0);

		((Switch) on).toggle();

		System.out.println(x);
	}
};

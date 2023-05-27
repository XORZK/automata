import java.util.ArrayList;

public class Main {
	public static void main(String[] args) {
		Circuit x = new NOTGate();
		Circuit on = new Switch(), off = new Switch();

		((Switch) on).toggle();

		x.connect(on.outputPorts()[0], 0);

		System.out.println(x);
	}
};

import java.util.ArrayList;

public class Main {
	public static void main(String[] args) {
		MainWindow f = new MainWindow(1000, 1000);

		Circuit x = new NOTGate();
		Circuit on = new Switch(), off = new Switch();

		x.connect(on.outputPorts()[0], 0);

		((Switch) on).toggle();

		System.out.println(x);

		f.exec();
	}
};

import javax.swing.*;
import java.util.ArrayList;

public class MainWindow extends JFrame {
	private final int TICK_RATE = 30;

	public static enum WindowState {
		Menu,
		Automata,
		Rule,
		Circuit
	};

	private String title = "Window";
	private int width, height;
	private JFrame frame;
	private ArrayList<JComponent> components;
	private WindowState state = WindowState.Automata;
	private JPanel automataSim = new AutomataSim();

	public MainWindow(int w, int h) {
		this(w, h, "Window");
	}

	public MainWindow(int w, int h, String t) {
		this.title = t;
		this.width = w;
		this.height = h;

		this.frame = new JFrame(this.title);
		this.frame.setSize(this.width, this.height);
		this.frame.add(this.automataSim);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	public void exec() {
		boolean quit = false;
		while (!quit) {
			if (this.state == WindowState.Automata) {
				this.automataSim.paint(this.frame.getGraphics());
			}

			try { Thread.sleep(1000/TICK_RATE); } catch (Exception e) { System.out.println(e); }
		}
	}
};

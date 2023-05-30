import javax.swing.*;
import java.awt.*;

public class AutomataSim extends JPanel {
	private Automata a = new Automata(Transition.GOL());
	private int cellSize = 100;
	private Camera c = new Camera();

	public AutomataSim() {
		this.setFocusable(true);
		this.requestFocusInWindow();
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2d = (Graphics2D) g;

		g2d.fillRect(0, 0, cellSize, cellSize);

		this.drawGrid(g2d);
	}

	public void drawGrid(Graphics2D g2d) {
		for (int x = 0; x < this.getWidth(); x += cellSize) {
			g2d.drawLine(x, 0, x, this.getHeight());
		}

		for (int y = 0; y < this.getHeight(); y += cellSize) {
			g2d.drawLine(0, y, this.getWidth(), y);
		}
	}

	public void paint(Graphics g) {
		this.paintComponent(g);
	}
};

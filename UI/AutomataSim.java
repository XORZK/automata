import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class AutomataSim extends JPanel {
	private final int INITIAL_CELL_SIZE = 100;
	private Automata automata = new Automata(Transition.GOL());
	private int cellSize = INITIAL_CELL_SIZE, xCount, yCount;
	private Camera camera = new Camera();
	private boolean paused = true;

	public AutomataSim() {
		this.addKeyListener(new KeyController());
		this.addMouseListener(new MouseController());
		this.setFocusable(true);
		this.requestFocusInWindow();

		this.xCount = (this.getWidth() / cellSize);
		this.yCount = (this.getHeight() / cellSize);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2d = (Graphics2D) g;

		this.xCount = (this.getWidth() / this.cellSize) + 1;
		this.yCount = (this.getHeight() / this.cellSize) + 1;

		ArrayList<Cell> cells = automata.getBoundedCells(
				camera.getPos(),
				this.xCount,
				this.yCount
				);

		// Camera is in the center
		Vec2<Integer> center = new Vec2<Integer>((this.xCount / 2) * cellSize, (this.yCount / 2) * cellSize);


		for (int i = 0; i < cells.size(); i++) {
			Cell c = cells.get(i);
			Vec2<Integer> relative = new Vec2<Integer>(center.getX() + (c.getX() - camera.getPos().getX()) * cellSize, center.getY() - (c.getY() - camera.getPos().getY()) * cellSize);
			g.setColor(new Color(
					(c.getColor() << 8) & 0xFF,
					(c.getColor() << 4) & 0xFF,
					(c.getColor()) & 0xFF)
					);
			g.fillRect(relative.getX(), relative.getY(), cellSize, cellSize);
		}

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
		if (!this.paused) {
			this.automata.tick();
		}
		this.paintComponent(g);
	}

	public class KeyController implements KeyListener {
		public void keyPressed(KeyEvent event) {
			int ascii = (event.getKeyCode());

			/** Mappings:
			 * 39: Right
			 * 37: Left
			 * 38: Up
			 * 40: Down
			 * 45: -
			 * 61: =
			 * */
			if (ascii >= 37 && ascii <= 40) {
				int tf = (ascii - (ascii % 2 == 0 ? 39 : 38));
				Vec2<Integer> translate = new Vec2<Integer>(
						(ascii % 2 == 0 ? 0 : tf),
						(ascii % 2 == 0 ? -tf : 0)
						);
				camera.translate(translate);
			}

			switch (ascii) {
				case (45): { camera.zoomTick(true); break; }
				case (61): { camera.zoomTick(false); break; }
				case (32): { paused = (paused ? false : true); break; }
				default: { break; }
			}

			System.out.println(ascii);

			cellSize = (int) (INITIAL_CELL_SIZE * camera.getZoom());
		}

		public void keyReleased(KeyEvent event) {
		}

		public void keyTyped(KeyEvent event) {
		}
	};

	public class MouseController implements MouseListener {
		public void mouseClicked(MouseEvent e) {
			Vec2<Integer> clicked = new Vec2<Integer>(e.getX() / cellSize, yCount - (e.getY() / cellSize)),
										center = new Vec2<Integer>((xCount / 2), (yCount / 2));
			Vec2<Integer> pos = new Vec2<Integer>(clicked.getX() - center.getX() + camera.getX(), clicked.getY() - center.getY() + camera.getY());

			automata.activateCell(State.ALIVE(), pos);
		}
		public void mouseEntered(MouseEvent e) {}
		public void mouseExited(MouseEvent e) {}
		public void mousePressed(MouseEvent e) {}
		public void mouseReleased(MouseEvent e) {}
	};
};

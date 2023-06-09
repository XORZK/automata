import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class AutomataSim extends JPanel implements Serializable {
	private final int BACKGROUND_COLOR = State.DEAD().getColor(), SELECTED_COLOR = 0xBBAAFF, BORDER_COLOR = 0xE5E4E6;
	private final int INITIAL_CELL_SIZE = 100, CFG_SIZE = 150, CFG_PADDING = 25;
	private int cellSize = INITIAL_CELL_SIZE, xCount, yCount;
	private Automata automata = new Automata(Transition.GOL());
	private Camera camera = new Camera();
	private boolean paused = true, ctrl = false, shift = false;
	private Vec2<Integer> selStart = new Vec2<Integer>(0,0), selEnd = new Vec2<Integer>(0,0); 
	private Icon selectedIcon;
	private ArrayList<Icon> cfg = new ArrayList<Icon>();
	private JMenuBar menuBar;
	private JFrame frame;

	public AutomataSim(JFrame frame) {
		super(new BorderLayout());
		this.frame = frame;
		this.initializeMenu();
		this.addKeyListener(new KeyController());
		MouseController mc = new MouseController();
		this.addMouseListener(mc);
		this.addMouseMotionListener(mc);
		this.addMouseWheelListener(new MouseWheelController());
		this.setDoubleBuffered(true);
		this.addIcon(Configuration.GLIDER());

		this.xCount = (this.getWidth() / cellSize);
		this.yCount = (this.getHeight() / cellSize);
	}


	private void initializeMenu() {
		this.menuBar = new JMenuBar();
		JMenu file = new JMenu("File"), edit = new JMenu("Edit");

		JMenuItem open = new JMenuItem("Open"), save = new JMenuItem("Save"), exit = new JMenuItem("Exit"), load = new JMenuItem("Load Configurations");
		JMenuItem clear = new JMenuItem("Clear");

		exit.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { System.exit(0); } });

		load.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String fn = ((MainWindow) frame).getFile();
				if (fn == null) return;
				try (ObjectInput stream = new ObjectInputStream(new FileInputStream(fn))) {
					Object o = stream.readObject();
					if (o instanceof ArrayList<?>) {
						ArrayList<?> list = (ArrayList<?>) o;
						ArrayList<Icon> tmp = new ArrayList<Icon>();
						for (Object value : list) { 
							if (value instanceof Configuration) tmp.add(new Icon((Configuration) value));
						}

						if (tmp.size() >= 1) {
							cfg = tmp;
							configPositions();
						}
					}
				} catch (IOException | ClassNotFoundException err) {
					err.printStackTrace();
				}
			}
		});

		open.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String fn = ((MainWindow) frame).getFile();
				if (fn == null) return;
				try (ObjectInputStream stream = new ObjectInputStream(new FileInputStream(fn))) {
					Object o = stream.readObject();
					if (o instanceof Automata) { automata = (Automata) o; }
				} catch (IOException | ClassNotFoundException err) {
					err.printStackTrace();
				}
			}
		});

		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String fn = ((MainWindow) frame).getFile();

				if (fn != null) {
					try {
						ObjectOutputStream atomOut = new ObjectOutputStream(new FileOutputStream(String.format("%s.automata", fn)));
						atomOut.writeObject(automata);

						ArrayList<Configuration> configurations = new ArrayList<Configuration>();
						for (Icon i : cfg) { configurations.add(i.getConfiguration()); }

						ObjectOutputStream cfgOut = new ObjectOutputStream(new FileOutputStream(String.format("%s.cfg", fn)));
						cfgOut.writeObject(configurations);
						atomOut.close();
						cfgOut.close();
					} catch (IOException err) { err.printStackTrace(); }
				}
			}
		});

		clear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { automata = new Automata(Transition.GOL()); }
		});

		file.add(open);
		file.add(save);
		file.add(load);
		file.add(exit);
		edit.add(clear);
		this.menuBar.add(file);
		this.menuBar.add(edit);
		this.add(menuBar, BorderLayout.NORTH);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
	    ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		if (!this.paused) { this.automata.tick(); }

		this.setBackground(new Color((BACKGROUND_COLOR >> 16) & 0xFF, (BACKGROUND_COLOR >> 8) & 0xFF, BACKGROUND_COLOR & 0xFF));

		this.xCount = (this.getWidth() / this.cellSize) + 1;
		this.yCount = (this.getHeight() / this.cellSize) + 1;

		ArrayList<Cell> cells = automata.getBoundedCells(
				camera.getPos(),
				this.xCount,
				this.yCount
				);

		this.drawCells(cells, g);

		if (this.selStart != null && this.selEnd != null) {
			Vec2<Integer> center = new Vec2<Integer>((this.selStart.getX() + this.selEnd.getX())/2, (this.selStart.getY() + this.selEnd.getY())/2);

			ArrayList<Cell> selected = automata.getBoundedCells(
				center,
				Math.abs(this.selStart.getX() - this.selEnd.getX()) / 2 + 1,
				Math.abs(this.selStart.getY() - this.selEnd.getY()) / 2 + 1
			);

			this.drawCells(selected, SELECTED_COLOR, g);
		}

		this.renderConfigurations(g);
	}

	public void drawCells(ArrayList<Cell> cells, int COLOR, Graphics g) {
		for (Cell c : cells) {				
			Vec2<Double> relative = new Vec2<Double>(((this.xCount/2.0) + (c.getX() - camera.getPos().getX())) * cellSize, 
													   ((this.yCount/2.0) - (c.getY() - camera.getPos().getY())) * cellSize);
			g.setColor(new Color(
				(COLOR >> 16) & 0xFF,
				(COLOR >> 8) & 0xFF,
				(COLOR) & 0xFF
			));
			g.fillRect((int) Math.round(relative.getX()), (int) Math.round(relative.getY()), cellSize, cellSize);
		}
	}

	public void drawCells(ArrayList<Cell> cells, Graphics g) {
		for (Cell c : cells) {				
			Vec2<Double> relative = new Vec2<Double>(((this.xCount/2.0) + (c.getX() - camera.getPos().getX())) * cellSize, 
													   ((this.yCount/2.0) - (c.getY() - camera.getPos().getY())) * cellSize);
			g.setColor(new Color(
				(c.getColor() >> 16) & 0xFF,
				(c.getColor() >> 8) & 0xFF,
				(c.getColor()) & 0xFF
			));
			g.fillRect((int) Math.round(relative.getX()), (int) Math.round(relative.getY()), cellSize, cellSize);
		}
	}


	public void addIcon(Configuration c) {
		Icon newIcon = new Icon(c);
		this.addIcon(newIcon);
	}

	public void configPositions() {
		if (this.cfg.size() >= 1) { 
			Icon i = cfg.get(0);
			i.setPos(new Vec2<Integer>(CFG_PADDING + ((CFG_SIZE - 2 * CFG_PADDING) - i.getDimensions().getX())/2, menuBar.getPreferredSize().height + CFG_PADDING));
		}

		for (int k = 1; k < this.cfg.size(); k++) {
			Icon i = this.cfg.get(k), p = this.cfg.get(k-1);
			int size = CFG_SIZE - 2 * CFG_PADDING;
			i.setPos(new Vec2<Integer>(CFG_PADDING + (size - i.getDimensions().getX())/2, p.getDimensions().getY() + p.getPos().getY() + CFG_PADDING));
		}
	}

	public void addIcon(Icon icon) {
		if (icon != null) this.cfg.add(icon);
		this.configPositions();
	}

	public void renderConfigurations(Graphics g) {
		g.setColor(new Color(
			(BACKGROUND_COLOR >> 16) & 0xFF,
			(BACKGROUND_COLOR >> 8) & 0xFF,
			(BACKGROUND_COLOR) & 0xFF
		));

		g.fillRect(0, 0, CFG_SIZE, getHeight());

		for (int i = 0; i < this.cfg.size(); i++) {
			Icon c = this.cfg.get(i);
			g.drawImage(c.getImage(), c.getPos().getX(), c.getPos().getY(), null);
		}

		g.setColor(new Color(
			(BORDER_COLOR >> 16) & 0xFF,
			(BORDER_COLOR >> 8) & 0xFF,
			(BORDER_COLOR) & 0xFF
		));

		g.drawLine(CFG_SIZE, 0, CFG_SIZE, getHeight());
	}

	public Icon configurationCollision(Vec2<Integer> pos) {
		for (int i = 0; i < this.cfg.size(); i++) {
			Icon c = this.cfg.get(i);
			if (c.intersects(pos)) {
				return c;
			}
		}
		return null;
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

			if (ascii == 8 && selEnd != null && selStart != null) {
			}

			switch (ascii) {
				case (45): { camera.zoomTick(true); break; }
				case (61): { camera.zoomTick(false); break; }
				case (32): { paused = (paused ? false : true); break; }
				case (10): { paused = true; automata.tick(); break; }
				case (16): { shift = true; break; }
				case (17): { ctrl = true; break; }
				case (27): { selEnd = selStart = null; break; }
				case (8): {
					if (selEnd != null && selStart != null) {
						for (int x = Math.min(selEnd.getX(), selStart.getX()); x <= Math.max(selEnd.getX(), selStart.getX()); x++) {
							for (int y = Math.min(selEnd.getY(), selStart.getY()); y <= Math.max(selEnd.getY(), selStart.getY()); y++) {
								Vec2<Integer> pos = new Vec2<Integer>(x,y);
								if (automata.getCell(pos).getValue() > 0) {
									automata.deleteCell(pos);
								}
							}
						}
					}
					selEnd = selStart = null;
					break;
				}
				case (83): {
					if (selEnd != null && selStart != null) {
						Configuration cf = automata.createConfiguration(selStart, selEnd);
						Icon ic = new Icon(cf);
						addIcon(ic);
					}
					break;
				}
				default: { break; }
			}

			cellSize = Math.max(1, (int) (INITIAL_CELL_SIZE * camera.getZoom()));
		}

		public void keyReleased(KeyEvent event) {
			shift = ctrl = false;
		}

		public void keyTyped(KeyEvent event) {
		}
	};

	public class MouseController extends MouseAdapter {
		private Vec2<Integer> computePos(MouseEvent e) {
			Vec2<Integer> clicked = new Vec2<Integer>(e.getX() / cellSize, yCount - (e.getY() / cellSize)),
							center = new Vec2<Integer>(xCount/2, yCount/2);
			Vec2<Integer> pos = new Vec2<Integer>(clicked.getX() - center.getX() + camera.getX(), clicked.getY() - center.getY() + camera.getY());
			return pos;
		}

		private Vec2<Integer> toVec2(MouseEvent e) {
			return new Vec2<Integer>(e.getX(), e.getY());
		}

		public void mouseClicked(MouseEvent e) {
			selEnd = selStart = null;
			Vec2<Integer> pos = toVec2(e), gridPos = this.computePos(e);
			if (pos.getX() > CFG_SIZE + cellSize) {
				if (selectedIcon != null) {
					automata.addConfiguration(selectedIcon.getConfiguration(), gridPos);
					selectedIcon = null;
				} else {
					automata.toggleCell(gridPos);
					if (!shift) { selStart = selEnd = null; }
				}
			} else {
				Icon i = configurationCollision(pos);
				if (i == null) return;
				selectedIcon = i;
			}
		}

		public void mouseEntered(MouseEvent e) {}
		public void mouseExited(MouseEvent e) {}

		public void mousePressed(MouseEvent e) {
			Vec2<Integer> pos = toVec2(e), gridPos = this.computePos(e);
			if (pos.getX() > CFG_SIZE + cellSize) {
				if (shift) { selStart = gridPos; }
			} else {
				Icon i = configurationCollision(pos);
				if (i == null) return;
				selectedIcon = i;
			}

		}

		public void mouseDragged(MouseEvent e) {
			Vec2<Integer> pos = this.toVec2(e);
			if (selectedIcon != null) {
				Graphics g = getGraphics();
				g.drawImage(selectedIcon.getImage(), pos.getX(), pos.getY(), null);
			}
		}

		public void mouseReleased(MouseEvent e) {
			Vec2<Integer> pos = toVec2(e), gridPos = this.computePos(e);
			if (pos.getX() > CFG_SIZE + cellSize) {
				if (selectedIcon != null) {
					automata.addConfiguration(selectedIcon.getConfiguration(), gridPos);
					selectedIcon = null;
				} else {
					if (shift) { selEnd = gridPos; }
				}
			}
		}
	};

	public class MouseWheelController implements MouseWheelListener {
		public void mouseWheelMoved(MouseWheelEvent e) {
			double rotation = e.getWheelRotation();
			if (ctrl) { camera.zoomTick(rotation > 0); }
			else if (!shift && !ctrl) { camera.translate(new Vec2<Integer>(0, (int) (-rotation))); }
			else if (shift) { camera.translate(new Vec2<Integer>((int) rotation, 0)); }
		}
	};
};

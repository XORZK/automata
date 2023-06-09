import java.util.ArrayList;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.awt.Color;

public class Configuration implements Serializable {
	private final int BORDER_COLOR = 0xE5E4E6;
	private String name = "", description = "";
	private Cell[][] configuration;
	private int width, height;
	private Vec2<Integer> relativePos;

	public Configuration() {
		this.width = this.height = 0;
		this.relativePos = new Vec2<Integer>(0,0);
	}

	public Configuration(Cell[][] cfg) {
		this.setConfiguration(cfg);
		this.setRelativePos(new Vec2<Integer>(0, 0));
	}

	public Configuration(Cell[][] cfg, String name) {
		this(cfg);
		this.name = name;
	}

	public Configuration(Cell[][] cfg, String name, String desc) {
		this(cfg, name);
		this.description = desc;
	}

	public void setRelativePos(Vec2<Integer> pos) {
		this.relativePos = pos;
		this.calculatePos();
	}

	public Vec2<Integer> getRelativePos() {
		return this.relativePos;
	}

	private void calculatePos() {
		if (this.configuration == null) {
			return;
		}

		for (int y = 0; y < this.configuration.length; y++) {
			for (int x = 0; x < this.configuration[y].length; x++) {
				this.configuration[y][x].setPos(new Vec2<Integer>(this.relativePos.getX() + x, this.relativePos.getY() - y));
			}
		}
	}

	public void setName(String title) {
		this.name = title;
	}

	public String getName() {
		return this.name;
	}

	public void setDescription(String desc) {
		this.description = desc;
	}

	public String getDescription() {
		return this.description;
	}

	public void setConfiguration(Cell[][] cfg) {
		this.height = cfg.length;

		for (int y = 0; y < cfg.length; y++) { this.width = Math.max(this.width, cfg[y].length); }

		this.configuration = new Cell[this.height][this.width];

		for (int y = 0; y < this.height; y++) {
			for (int x = 0; x < this.width; x++) {
				this.configuration[y][x] = (x < cfg[y].length ? cfg[y][x].copy() : new Cell());
			}
		}

		this.generateImage();
	}

	public ArrayList<Cell> getActiveCells() {
		ArrayList<Cell> cells = new ArrayList<Cell>();
		for (int x = 0; x < this.width; x++) {
			for (int y = 0; y < this.height; y++) {
				if (this.getCell(x, y).getValue() > 0) {
					cells.add(this.getCell(x,y));
				}
			}
		}

		return cells;
	}

	public Configuration crop() {
		ArrayList<Cell> active = this.getActiveCells();
		if (active.size() == 0) return new Configuration();

		Vec2<Integer> min = new Vec2<Integer>(this.width+1, this.height+1),
					  max = new Vec2<Integer>(-1, -1);

		for (int y = 0; y < this.height; y++) {
			for (int x = 0; x < this.width; x++) {
				Cell c = this.getCell(x,y);
				if (c.getValue() > 0) {
					min.setX(Math.min(x, min.getX()));
					min.setY(Math.min(y, min.getY()));
					max.setX(Math.max(x, max.getX()));
					max.setY(Math.max(y, max.getY()));
				}
			}
		}

		Cell[][] cells = new Cell[max.getY() - min.getY() + 1][max.getX() - min.getX() + 1];

		for (int x = 0; x <= max.getX() - min.getX(); x++) {
			for (int y = 0; y <= max.getY() - min.getY(); y++) {
				cells[y][x] = this.getCell(min.getX() + x, min.getY() + y);
			}
		}


		return new Configuration(cells);
	}

	public Cell getCell(int x, int y) {
		return (((x < 0 || x >= this.width) || (y < 0 || y >= this.height)) ? new Cell() : this.configuration[y][x]);
	}

	public Cell getCell(Vec2<Integer> idx) {
		return this.getCell(idx.getX(), idx.getY());
	}

	public int getHeight() {
		return this.height;
	}

	public int getWidth() {
		return this.width;
	}

	public Picture generateImage(double h) {
		int bg = State.DEAD().getColor();
		BufferedImage img = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = img.createGraphics();


		g2d.setColor(
			new Color(
				(bg >> 16) & 0xFF,
				(bg >> 8) & 0xFF,
				(bg) & 0xFF
			)
		);

		g2d.fillRect(0, 0, this.width, this.height);

		for (int x = 0; x < this.width; x++) {
			for (int y = 0; y < this.height; y++) {
				Cell c = this.getCell(x,y);
				if (c.getValue() > 0) {
					g2d.setColor(new Color(
						(c.getColor() >> 16) & 0xFF,
						(c.getColor() >> 8) & 0xFF,
						(c.getColor()) & 0xFF
					));
					g2d.fillRect(x, y, 1, 1);
				}
			}
		} 

		Picture p = new Picture(img);

		p.scale(h/Math.max(this.height, this.width));

		p.drawBorder(BORDER_COLOR);

		return p;
	}

	public Picture generateImage() {
		return this.generateImage(100.0);
	}

	public static Configuration GLIDER() {
		State a = State.ALIVE(), d = State.DEAD();
		Cell[][] glider = {
			{ new Cell(d), new Cell(a), new Cell(d) }, 
			{ new Cell(d), new Cell(d), new Cell(a), }, 
			{ new Cell(a), new Cell(a), new Cell(a) }
		};
		return new Configuration(glider);
	}

	public String toString() {
		String rep = "";
		for (int x = 0; x < this.width; x++) {
			for (int y = 0; y < this.height; y++) {
				rep += String.format("[%d]", this.getCell(x, y).getValue());
			}
			rep += (x != (this.width - 1) ? "\n" : "");
		}
		return rep;
	}
};

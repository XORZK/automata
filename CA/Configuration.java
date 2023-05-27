public class Configuration {
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
				this.configuration[y][x].setPos(new Vec2<Integer>(this.relativePos.getX() + x, this.relativePos.getY() + y));
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
	}

	public Cell getCell(int x, int y) {
		return (((x < 0 || x >= this.width) || (y < 0 || y >= this.height)) ? null : this.configuration[y][x]);
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
};

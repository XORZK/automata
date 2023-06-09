public class Camera implements Movable {
	private Vec2<Integer> pos;
	private double zoom, tick;

	public Camera() {
		this.reset();
	}

	public Camera(Vec2<Integer> p) {
		this.reset();
		this.pos = (p == null ? new Vec2<Integer>(0,0) : p);
	}

	public Camera(Vec2<Integer> p, double z) {
		this(p);
		this.zoom = z;
	}

	public Vec2<Integer> getPos() {
		return this.pos;
	}

	public int getY() {
		return this.pos.getY();
	}

	public int getX() {
		return this.pos.getX();
	}

	public void setTick(double t) {
		this.tick = t;
	}

	public void setZoom(double z) {
		if (z <= 0) return;
		this.zoom = z;
	}

	public double getZoom() {
		return this.zoom;
	}

	public void zoomTick(boolean out) {
		if ((this.zoom * 1/this.tick) == 0 && out) return;
		this.setZoom(this.zoom * (out ? 1/this.tick : this.tick));
	}

	public void translate(Vec2<Integer> translation) {
		this.pos = Vec2.superposition(this.pos, translation);
	}

	public void translate(int shiftx, int shifty) {
		this.pos = Vec2.superposition(this.pos, new Vec2<Integer>(shiftx, shifty));
	}

	public void horizontalShift(int shiftX) {
		this.pos.setX(this.pos.getX() + shiftX);
	}

	public void verticalShift(int shiftY) {
		this.pos.setY(this.pos.getY() + shiftY);
	}

	public void moveTo(Vec2<Integer> p) {
		this.pos = p;
	}

	public void moveTo(int px, int py) {
		this.pos = new Vec2<Integer>(px, py);
	}

	public void reset() {
		this.moveTo(0, 0);
		this.setZoom(1);
		this.setTick(2);
	}

	@Override
	public String toString() {
		return String.format("[ZOOM]: %.2f\nPOS: %s", this.zoom, this.pos);
	}
};

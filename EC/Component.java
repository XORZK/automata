import java.awt.Graphics;

public abstract class Component implements Drawable, Movable {
	private Vec2<Integer> pos;

	public Component() {
		this.pos = new Vec2<Integer>(0,0);
	}

	public Component(Vec2<Integer> p) {
		this.pos = (p == null ? new Vec2<Integer>(0,0) : p);
	}

	public void translate(Vec2<Integer> translation) {
		this.pos = Vec2.superposition(pos, translation);
	}

	public void translate(int shiftX, int shiftY) {
		this.pos = Vec2.superposition(pos, new Vec2<Integer>(shiftX, shiftY));
	}

	public void horizontalShift(int shiftX) {
		this.pos.setX(this.pos.getX() + shiftX);
	}

	public void verticalShift(int shiftY) {
		this.pos.setY(this.pos.getY() + shiftY);
	}

	public void moveTo(Vec2<Integer> pos) {
		this.pos = pos;
	}

	public void moveTo(int px, int py) {
		this.pos = new Vec2<Integer>(px, py);
	}

	abstract public boolean intersects(Vec2<Double> pos);

	abstract public void draw(Graphics g);
};

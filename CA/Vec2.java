public class Vec2<T extends Number & Comparable<T>> implements Comparable<Vec2<T>> {
	private T x, y;

	public Vec2(T v) {
		this.x = this.y = v;
	}

	public Vec2(T px, T py) {
		this.x = px;
		this.y = py;
	}

	public T getX() {
		return this.x;
	}

	public T getY() {
		return this.y;
	}

	public void setX(T px) {
		this.x = px;
	}

	public void setY(T py) {
		this.y = py;
	}

	public double distance(Vec2<T> v2) {
		Vec2<Double> d1 = this.vecDouble(), d2 = v2.vecDouble();
		return Math.sqrt(Math.pow(d1.getX() - d2.getX(), 2) + Math.pow(d1.getY() - d2.getY(), 2));
	}

	public Vec2<Double> vecDouble() {
		return new Vec2<Double>(this.x.doubleValue(), this.y.doubleValue());
	}

	@Override
	public int hashCode() {
		int W0 = 0x3504f333, W1 = 0xf1bbcdcb, M = 741103597;

		int x = this.x.intValue(), y = this.y.intValue();


		x *= W0;
		y *= W1;
		x ^= y;
		x *= M;

		return x;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) { return true; }
		if (o == null || this.getClass() != o.getClass()) { return false; }

		Vec2<?> v2 = (Vec2<?>) o;
		return (v2.getX() == this.x && v2.getY() == this.y);
	}

	@Override
	public int compareTo(Vec2<T> v2) {
		return Math.abs(this.x.compareTo(v2.getX())) +
					 Math.abs(this.y.compareTo(v2.getY()));
	}

	@Override
	public String toString() {
		return String.format("(%s, %s)", this.x.toString(), this.y.toString());
	}

	public static Vec2<Integer> superposition(Vec2<Integer> v1, Vec2<Integer> v2) {
		return new Vec2<Integer>(v1.getX() + v2.getX(), v1.getY() + v2.getY());
	}
};

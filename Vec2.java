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

	@Override
	public int hashCode() {
		int W0 = 0x3504f333, W1 = 0xf1bbcdcb, M = 741103597;

		int x = Integer.parseInt(this.x.toString()),
				y = Integer.parseInt(this.y.toString());

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
};

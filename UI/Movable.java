public interface Movable {
	public void translate(Vec2<Integer> translation);
	public void translate(int shiftX, int shiftY);
	public void horizontalShift(int shiftX);
	public void verticalShift(int shiftY);
	public void moveTo(Vec2<Integer> pos);
	public void moveTo(int px, int py);
};

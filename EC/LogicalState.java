public enum LogicalState {
	OFF(0), ON(1);

	private int value;

	LogicalState(int v) {
		this.value = v;
	}

	public int value() {
		return this.value;
	}
};

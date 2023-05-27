public class Switch extends Circuit {
	public Switch() {
		this.initializePorts(0, 1);
	}

	public Switch(int px, int py) {
		super(px, py);
		this.initializePorts(0, 1);
	}

	public Switch(int px, int py, int w, int h) {
		super(px, py, w, h);
		this.initializePorts(0, 1);
	}

	public void toggle() {
		this.outputs[0].setSignal(new Signal(
				this.outputs[0].getSignal().value() == 1 ?
				LogicalState.OFF : LogicalState.ON));
	}

	public Signal[] compute() {
		Signal[] out = { this.outputs[0].getSignal() };

		return out;
	}
};

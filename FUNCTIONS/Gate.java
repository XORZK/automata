public abstract class Gate extends Circuit {
	public Gate() {
		this.initializePorts(2, 1);
	}

	public Gate(int px, int py) {
		super(px, py);
		this.initializePorts(2, 1);
	}

	public Gate(int px, int py, int w, int h) {
		super(px, py, w, h);
		this.initializePorts(2, 1);
	}

	abstract Signal[] compute();
};

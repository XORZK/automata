public class Buffer extends Circuit {
	public Buffer() {
		this.initializePorts(1, 1);
	}

	public Buffer(int px, int py) {
		super(px, py);
		this.initializePorts(1, 1);
	}

	public Buffer(int px, int py, int w, int h) {
		super(px, py, w, h);
		this.initializePorts(1, 1);
	}

	public Signal[] compute() {
		Signal[] out = {
			this.inputs[0].getSignal()
		};

		this.copy(out);

		return out;
	}
};

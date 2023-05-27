public class NOTGate extends Circuit {
	public NOTGate() {
		this.initializePorts(1, 1);
	}

	public NOTGate(int px, int py) {
		super(px, py);
		this.initializePorts(1, 1);
	}

	public NOTGate(int px, int py, int w, int h) {
		super(px, py, w, h);
		this.initializePorts(1, 1);
	}


	public Signal[] compute() {
		int[] input = this.inputs();

		Signal[] out = {
			new Signal(1 - input[0])
		};

		this.copy(out);

		return out;
	}
};

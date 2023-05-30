public class HalfAdder extends Circuit {
	public HalfAdder() {
		this.initializePorts(2, 2);
	}

	public HalfAdder(int px, int py) {
		super(px, py);
		this.initializePorts(2, 2);
	}

	public HalfAdder(int px, int py, int w, int h) {
		super(px, py, w, h);
		this.initializePorts(2, 2);
	}

	public Signal[] compute() {
		int[] input = this.inputs();

		Signal[] out = {
			new Signal((input[0] + input[1]) % 2),
			new Signal((input[0] + input[1]) / 2)
		};

		this.copy(out);

		return out;
	}
};

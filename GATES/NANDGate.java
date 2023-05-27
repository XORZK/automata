public class NANDGate extends Gate {
	public Signal[] compute() {
		int[] input = this.inputs();

		Signal[] out = {
			new Signal(1 - input[0] * input[1])
		};

		this.copyOutput(out);

		return out;
	}
};

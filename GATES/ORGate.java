public class ORGate extends Gate {
	public Signal[] compute() {
		int[] input = this.inputs();

		Signal[] out = {
			new Signal(input[0] + input[1] - input[0]*input[1])
		};

		this.copyOutput(out);

		return out;
	}
};

public class NORGate extends Gate {
	public Signal[] compute() {
		int[] input = this.inputs();

		Signal[] out = {
			new Signal(1 - (input[0] + input[1] - input[0]*input[1]))
		};

		this.copy(out);

		return out;
	}
};

public class XNORGate extends Gate {
	public Signal[] compute() {
		int[] input = this.inputs();

		Signal[] out = {
			new Signal((input[0] + input[1] + 1) % 2)
		};

		this.copy(out);

		return out;
	}
};

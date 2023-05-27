public class XORGate extends Gate {
	public Signal[] compute() {
		int[] input = this.inputs();

		Signal[] out = {
			new Signal((input[0] + input[1]) % 2)
		};

		this.copyOutput(out);

		return out;
	}
};

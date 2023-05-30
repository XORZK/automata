import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Arrays;

public abstract class Circuit extends Component {
	protected int width = 100, height = 100;
	protected Port[] inputs, outputs;

	public Circuit() {
		this.initializePorts(0, 0);
	}

	public Circuit(int px, int py) {
		super(new Vec2<Integer>(px,py));
		this.initializePorts(0, 0);
	}

	public Circuit(int px, int py, int w, int h) {
		this(px, py);
		this.width = w;
		this.height = h;
	}

	protected void initializePorts(int inputs, int outputs) {
		this.initializeInputs(Math.max(0, inputs));
		this.initializeOutputs(Math.max(0, outputs));
	}

	protected void initializeInputs(int size) {
		this.inputs = new Port[size];

		for (int i = 0; i < size; i++) {
			this.inputs[i] = new Port(this, true);
		}
	}

	protected void initializeOutputs(int size) {
		this.outputs = new Port[size];

		for (int i = 0; i < size; i++) {
			this.outputs[i] = new Port(this);
		}
	}

	protected void copy(Signal[] signals) {
		for (int i = 0; i < Math.min(signals.length, this.outputs.length); i++) {
			this.outputs[i].setSignal(signals[i]);
		}
	}

	public void setWidth(int w) { this.width = w; }

	public int getWidth() { return this.width; }

	public void setHeight(int h) { this.height = h; }

	public int getHeight() { return this.height; }

	public void setDimensions(int w, int h) {
		this.setWidth(w);
		this.setHeight(h);
	}

	public Vec2<Integer> getDimensions() { return new Vec2<Integer>(this.width, this.height); }

	public void connect(Port p, int index) {
		boolean input = p.inputPort();
		if ((!input && (index < 0 || index >= this.inputs.length)) || (input && (index < 0 || index >= this.outputs.length))) {
			return;
		}

		if (input) { this.outputs[index].connect(p); }
		else { this.inputs[index].connect(p); }

		this.recompute();
	}

	public void recompute() {
		this.compute();
		for (int i = 0; i < this.outputs.length; i++) {
			for (Connection c : this.outputs[i].getConnections()) {
				Port dest = c.getDest();
				if (dest.getCircuit() != null) {
					dest.getCircuit().recompute();
				}
			}
		}
	}

	public Port[] inputPorts() {
		return this.inputs;
	}

	public Port[] outputPorts() {
		return this.outputs;
	}

	protected int[] inputs() {
		int[] in = new int[this.inputs.length];

		for (int i = 0; i < this.inputs.length; i++) {
			in[i] = this.inputs[i].getSignal().value();
		}

		return in;
	}

	protected int[] outputs() {
		int[] out = new int[this.outputs.length];

		for (int i = 0; i < this.outputs.length; i++) {
			out[i] = this.outputs[i].getSignal().value();
		}

		return out;
	}

	public void draw(Graphics g) {}

	abstract Signal[] compute();

	@Override
	public String toString() {
		return String.format(
				"INPUTS: %s\nOUTPUTS: %s", Arrays.toString(this.inputs()), Arrays.toString(this.outputs())
				);
	}
};

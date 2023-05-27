import java.awt.Graphics;
import java.util.ArrayList;

public class ElectricalComponent extends Component {
	private int width = 100, height = 100;
	private Port[] inputs, outputs;

	public ElectricalComponent() {
		this.inputs = this.outputs = new Port[0];
	}

	public ElectricalComponent(int px, int py) {
		super(new Vec2<Integer>(px,py));
		this.inputs = this.outputs = new Port[0];
	}

	public ElectricalComponent(int px, int py, int w, int h) {
		this(px, py);
		this.width = w;
		this.height = h;
	}

	public void initializeInputs(int size) {
		this.inputs = new Port[size];

		for (int i = 0; i < size; i++) {
			this.inputs[i] = new Port(this, true);
		}
	}

	public void initializeOutputs(int size) {
		this.outputs = new Port[size];

		for (int i = 0; i < size; i++) {
			this.outputs[i] = new Port(this);
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

	public Vec2<Integer> getDimensions() { return new Vec2<Integer>(this.getWidth(), this.getHeight()); }

	public void connect(Port p, int index) {
		boolean input = p.isInput();
		if ((!input && (index < 0 || index >= this.inputs.length)) || (input && (index < 0 || index >= this.outputs.length))) {
			return;
		}

		if (input) { this.outputs[index].connect(p); }
		else { this.inputs[index].connect(p); }
	}


	public boolean intersects(Vec2<Double> pos) {
		return true;
	}

	public void draw(Graphics g) {
	}
};

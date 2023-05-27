import java.awt.Graphics;

public class Connection extends Component {
	private Port input, output;
	private Signal connection = new Signal();

	public Connection() {
		this.input = this.output = null;
	}

	public Connection(Port in, Port out) {
		this.input = in;
		this.output = out;
	}

	public Port getInput() {
		return this.input;
	}

	public void setInput(Port in) {
		this.input = in;
	}

	public Port getOutput() {
		return this.output;
	}

	public void setOutput(Port out) {
		this.output = out;

		if (this.output != null) {
			this.connection = this.output.getSignal();
		}
	}

	public void connect(Port in, Port out) {
		this.setInput(in);
		this.setOutput(out);
	}

	@Override
	public boolean intersects(Vec2<Double> pos) {
		return true;
	}

	@Override
	public void draw(Graphics g) {
	}
};

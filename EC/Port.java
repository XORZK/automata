import java.awt.Graphics;
import java.util.ArrayList;

public class Port extends Component {
	private boolean input = false;
	private Signal signal = new Signal();
	private Circuit circuit;
	private ArrayList<Connection> connections;

	public Port() {
		this.connections = new ArrayList<Connection>();
	}

	public Port(int px, int py) {
		super(new Vec2<Integer>(px, py));
		this.connections = new ArrayList<Connection>();
	}

	public Port(int px, int py, Circuit c) {
		this(px, py);
		this.circuit = c;
		this.connections = new ArrayList<Connection>();
	}

	public Port(Circuit c) {
		this(0, 0, c);
	}

	public Port(Circuit c, boolean in) {
		this(0, 0, c);
		this.input = in;
	}

	public void setSignal(Signal s) {
		this.signal = s;
	}

	public Signal getSignal() {
		return (this.input && this.connections.size() > 0 ? this.connections.get(0).getSource().getSignal() : this.signal);
	}

	public void connect(Port p) {
		if (p == null) { return; }

		Connection c = (this.input ? new Connection(p, this) : new Connection(this, p));

		if (this.input) {
			if (this.connections.size() != 0) this.connections.clear();
		}

		this.connections.add(c);
	}

	public boolean inputPort() {
		return this.input;
	}

	public void setInput(boolean in) {
		this.input = in;
	}

	public void disconnect(Port p) {
		if (p == null) {
			return;
		}

		for (int i = 0; i < this.connections.size(); i++) {
			if (this.connections.get(i).getDest().equals(p)) {
				this.connections.remove(i);
				break;
			}
		}
	}

	@Override
	public boolean intersects(Vec2<Double> pos) {
		return true;
	}

	@Override
	public void draw(Graphics g) {
	}
};

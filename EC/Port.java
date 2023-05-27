import java.awt.Graphics;
import java.util.ArrayList;

public class Port extends Component {
	private boolean input = false;
	private Signal signal = new Signal();
	private ElectricalComponent component;
	private ArrayList<Connection> connections;

	public Port() {
		this.connections = new ArrayList<Connection>();
	}

	public Port(int px, int py) {
		super(new Vec2<Integer>(px, py));
		this.connections = new ArrayList<Connection>();
	}

	public Port(int px, int py, ElectricalComponent c) {
		this(px, py);
		this.component = c;
		this.connections = new ArrayList<Connection>();
	}

	public Port(ElectricalComponent c) {
		this(0,0,c);
	}

	public Port(ElectricalComponent c, boolean in) {
		this(0,0,c);
		this.input = in;
	}

	public void setSignal(Signal s) {
		this.signal = s;
	}

	public Signal getSignal() {
		return this.signal;
	}

	public void connect(Port p) {
		if (p == null) {
			return;
		}

		Connection c = new Connection(this, p);
		if (this.input && this.connections.size() != 0) {
			this.connections.remove(0);
		}

		this.connections.add(c);
	}

	public boolean isInput() {
		return this.input;
	}

	public void setInput(boolean in) {
		this.input = in;
	}

	public void remove(Port p) {
		if (p == null) {
			return;
		}

		for (int i = 0; i < this.connections.size(); i++) {
			if (this.connections.get(i).getOutput().equals(p)) {
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

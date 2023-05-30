import java.awt.Graphics;

public class Connection extends Component {
	private Port source, dest;
	private Signal signal = new Signal();

	public Connection() {
		this.source = this.dest = null;
	}

	public Connection(Port f, Port t) {
		this.source = f;
		this.dest = t;
	}

	public Port getSource() {
		return this.source;
	}

	public void setSource(Port s) {
		this.source = s;

		if (this.source != null) {
			this.signal = this.source.getSignal();
		}
	}

	public Port getDest() {
		return this.dest;
	}

	public void setDest(Port d) {
		this.dest = d;
	}

	public void connect(Port f, Port t) {
		this.setSource(f);
		this.setDest(t);
	}

	@Override
	public void draw(Graphics g) {
	}
};

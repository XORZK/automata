import java.util.ArrayList;

public class Signal {
	private ArrayList<LogicalState> values;
	private LogicalState currentState;

	public Signal() {
		this.currentState = LogicalState.OFF;
		this.values.add(this.currentState);
	}

	public Signal(LogicalState s) {
		this.currentState = s;
		this.values.add(this.currentState);
	}

	public void setState(LogicalState s) {
		this.currentState = s;
		this.values.add(this.currentState);
	}

	public LogicalState getState() {
		return this.currentState;
	}

	public ArrayList<LogicalState> getSignal() {
		return this.values;
	}

	@Override
	public String toString() {
		return String.format("[%s]", (this.currentState == LogicalState.ON ? "ON" : "OFF"));
	}
};

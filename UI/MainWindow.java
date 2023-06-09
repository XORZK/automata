import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class MainWindow extends JFrame {
	private final int TICK_RATE = 120;

	public static enum WindowState {
		Menu,
		Automata,
		Rule,
		Circuit
	};

	private boolean quit = false;
	private String title = "Window";
	private int width, height;
	private JFrame frame;
	private Thread gameLoop;
	private WindowState state = WindowState.Automata;
	private JPanel automataSim = new AutomataSim(this);

	public MainWindow(int w, int h) {
		this(w, h, "Window");
	}

	public MainWindow(int w, int h, String t) {
		this.title = t;
		this.width = w;
		this.height = h;

		this.automataSim.setFocusable(true);
		this.automataSim.requestFocus();

		this.frame = new JFrame(this.title);
		this.frame.setSize(this.width, this.height);
		this.frame.add(this.automataSim);
		this.frame.setVisible(true);
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.automataSim.setFocusable(true);
		this.automataSim.requestFocusInWindow();

		this.setup();
		this.gameLoop.start();
	}

	public String getFile(String ext) {
		JFileChooser chooser = new JFileChooser();

		if (ext != null && !ext.equals("")) {
			chooser.setAcceptAllFileFilterUsed(false);
			FileNameExtensionFilter filter = new FileNameExtensionFilter(String.format("%s Files", ext), ext);
			chooser.addChoosableFileFilter(filter);
		}

		int status = chooser.showSaveDialog(this);

		if (status == JFileChooser.APPROVE_OPTION) {
			String fn = chooser.getSelectedFile().getPath();
			return fn;
		}
		return null;
	}

	public String getFile() {
		return this.getFile(null);
	}

	public void setup() {
		this.automataSim.setVisible(true);
		this.automataSim.grabFocus();

		this.gameLoop = new Thread(() -> {
			while (!this.quit) {
				this.frame.repaint();
				try {
					Thread.sleep(1000/TICK_RATE);
				} catch (InterruptedException ex) {}
			}
		});
	}
};

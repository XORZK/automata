import java.io.FileReader;

public class Main {
	public static String readContents(String fn) {
		String contents = "";

		try (FileReader reader = new FileReader(fn)) {
			int c;
			while ((c = reader.read()) != -1) {
				contents += (char) c;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return contents;
	}

	public static void main(String[] args) {
		MainWindow f = new MainWindow(1000, 1000);
	} 
};

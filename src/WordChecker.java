import javax.swing.UIManager;

public class WordChecker {

	static boolean ifError = false;

	public static void main(String[] args) {

		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}
		new WordCheckerUI();

		System.out.println("WordChecker v0.2.0 (20180321 BUILD 6)\n");

	}

}

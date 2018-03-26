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

		System.out.println("WordChecker v0.3.0 beta (20180327 BUILD 7)\n");

	}

}

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Frame;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JWindow;

public class SplashWindow extends JWindow {
	private static final long serialVersionUID = 2640056184268485946L;

	public SplashWindow(String filename, int msDelay, Frame f) {
		super(f);

		// use @2x for support retina display on mac
		JLabel l = new JLabel(new ImageIcon(filename));

		setBackground(new Color(0, 0, 0, 0));
		getContentPane().add(l, BorderLayout.CENTER);

		pack();
		setLocationRelativeTo(null);
		setVisible(true);

		try {
			Thread.sleep(msDelay);
		} catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
		}

		setVisible(false);
		dispose();
	}

}
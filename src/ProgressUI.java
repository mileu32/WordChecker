import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;

public class ProgressUI extends JFrame implements PropertyChangeListener {

	private static final long serialVersionUID = -5331586365628729685L;
	private JProgressBar progressBar;
	private static Task task;

	private static ImageIcon icon;
	private JLabel iconLabel;

	class Task extends SwingWorker<Void, Void> {
		// Main task.
		public Void doInBackground() {
			Random random = new Random();
			int progress = 0;
			setProgress(0);
			while (progress < 100) {
				try {
					Thread.sleep(random.nextInt(1000));
				} catch (InterruptedException ignore) {
				}
				progress += random.nextInt(10);
				setProgress(Math.min(progress, 100));
			}
			return null;
		}

		@Override
		public void done() {
			Toolkit.getDefaultToolkit().beep();
			System.exit(0);
		}
	}

	public ProgressUI() {
		super("WordChecker v0.2.0");

		icon = new ImageIcon("data/example.png");
		icon = new ImageIcon(icon.getImage().getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH));

		iconLabel = new JLabel(icon);
		iconLabel.setPreferredSize(new Dimension(50, 50));

		progressBar = new JProgressBar(0, 100);
		progressBar.setValue(0);
		progressBar.setStringPainted(true);

		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout(1, 30, 20));
		panel.add(iconLabel);

		panel.add(progressBar);

		this.add(panel);

		this.pack();
		this.setVisible(true);

		task = new Task();
		task.addPropertyChangeListener(this);
		task.execute();

	}

	public void propertyChange(PropertyChangeEvent evt) {
		if ("progress" == evt.getPropertyName()) {
			int progress = (Integer) evt.getNewValue();
			progressBar.setValue(progress);
		}
	}

}
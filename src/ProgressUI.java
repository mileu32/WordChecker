import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URLEncoder;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class ProgressUI extends JFrame implements PropertyChangeListener {

	private static final long serialVersionUID = -5331586365628729685L;
	private JProgressBar progressBar;
	private Task task;

	private ImageIcon icon;
	private JLabel iconLabel;

	private String targetText, checkedText, errorText;
	private int targetTextLines;

	private boolean ifError = false;

	class Task extends SwingWorker<Void, Void> {
		// Main task.
		public Void doInBackground() {

			try {

				BufferedReader preferences = new BufferedReader(
						new InputStreamReader(new FileInputStream(new File("data/preferences.txt")), "UTF-8"));
				String query = preferences.readLine();
				preferences.close();

				FileInputStream fis = new FileInputStream(new File(targetText));
				InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
				BufferedReader br = new BufferedReader(isr);

				FileOutputStream fos1 = new FileOutputStream(new File(checkedText));
				OutputStreamWriter osw1 = new OutputStreamWriter(fos1, "UTF-8");
				BufferedWriter bw1 = new BufferedWriter(osw1);

				FileOutputStream fos2 = new FileOutputStream(new File(errorText));
				OutputStreamWriter osw2 = new OutputStreamWriter(fos2, "UTF-8");
				BufferedWriter bw2 = new BufferedWriter(osw2);

				// remove UTF-8 byte order mark(BOM)
				br.mark(1);
				if (br.read() != 0xFEFF)
					br.reset();

				int checkedTextLines = 0;

				while (true) {
					setProgress((int) (100 * (float) checkedTextLines / (float) targetTextLines));

					String line = br.readLine();
					checkedTextLines++;
					if (line == null)
						break;

					String target = line.trim();

					//System.out.println(target + "를 검사합니다.");
					int checker = wordChecker(query, target, bw1, bw2);

					if (checker == -1) {
						ifError = true;
						bw2.write(line + System.lineSeparator());
					}
				}

				br.close();
				bw1.close();
				bw2.close();

				if (!ifError) {
					//System.out.println("축하합니다. 어떠한 오류도 발견되지 않았습니다.");
					File errorFile = new File(errorText);
					if (errorFile.exists())
						errorFile.delete();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			return null;
		}

		@Override
		public void done() {
			Toolkit.getDefaultToolkit().beep();
			System.exit(0);
		}
	}

	public ProgressUI(String target, String checked, String error, String profile) {
		super("WordChecker v0.3.0 beta");

		targetText = target;
		checkedText = checked;
		errorText = error;

		targetTextLines = countLines(target);

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

	public static int countLines(String filename) {

		try {
			InputStream is;
			is = new BufferedInputStream(new FileInputStream(filename));

			try {
				byte[] c = new byte[1024];
				int count = 1;
				int readChars = 0;
				boolean empty = true;
				while ((readChars = is.read(c)) != -1) {
					empty = false;
					for (int i = 0; i < readChars; ++i) {
						if (c[i] == '\n') {
							++count;
						}
					}
				}
				return (count == 0 && !empty) ? 1 : count;
			} finally {
				is.close();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}

	public int wordChecker(String query, String targetWord, BufferedWriter ow, BufferedWriter ew) {

		// ow : original writer
		// ew : error writer

		String dicUrl, targetWordUTF8;
		Document document;
		Elements dicWord, dicWordCache, dicDesc;
		try {
			Thread.sleep(100);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		try {
			targetWordUTF8 = URLEncoder.encode(targetWord, "UTF-8");
			dicUrl = query + targetWordUTF8;
			document = Jsoup.connect(dicUrl).timeout(50000).get();

			dicWordCache = document.select("div.entry_wrap");
			int i = -1;
			String cache = "";
			while (!cache.equals("section_card")) {
				i++;
				cache = dicWordCache.select("div:eq(" + i + ")").attr("class");
			}

			dicWordCache = document.select("div:eq(" + i + ") div.word_wrap");
			dicWord = dicWordCache.select("strong.target");
			dicDesc = dicWordCache.select("ul.desc_lst");

			if (!dicWord.text().contains(targetWord)) {
				//System.out.println("오류가 검출되었습니다.\n");
				return -1;
			} else {
				//System.out.println("사전에 등재되어있는 단어입니다.\n");
				ow.write(dicWord.text() + "\t" + dicDesc.text() + System.lineSeparator());
			}

		} catch (Exception e) {
			// e.printStackTrace();
			//System.out.println("오류가 검출되었습니다.\n");
			return -1;
		}

		return 0;

	}

}
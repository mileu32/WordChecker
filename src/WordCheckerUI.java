import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.TitledBorder;

public class WordCheckerUI extends JFrame implements ActionListener, FocusListener {

	private static final long serialVersionUID = 1634599422314429397L;

	private JPanel targetDictsPanel, targetSetPanel;

	private String[] targetDictString = { "example.com", "기타" };
	private JComboBox<String> targetDict;

	private JTextField targetSetText[] = new JTextField[3];

	private JButton targetStartButton;
	private JPanel targetProgressBarPanel;

	private File targetFile, checkedFile, errorFile;

	private JFileChooser fileChooser = new JFileChooser();
	JFrame fileChooseWindow = new JFrame();

	WordCheckerUI() {

		super("WordChecker v0.2.0 beta (20180320 BUILD 5)");

		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		targetFile = new File("검토 대상");
		checkedFile = new File("출력 경로");
		errorFile = new File("에러 출력 경로");

		// 저장할 웹툰 호스팅 업체 설정
		targetDictsPanel = new JPanel();
		targetDict = new JComboBox<String>();

		for (int i = 0; i < targetDictString.length; i++) {
			targetDict.addItem(targetDictString[i]);
		}
		targetDictsPanel.add(targetDict);

		targetDictsPanel.setBorder(new TitledBorder("사용할 사전을 선택해주세요."));
		add(targetDictsPanel, BorderLayout.NORTH);

		targetSetPanel = new JPanel();
		targetSetPanel.setLayout(new GridLayout(3, 1, 5, 5));

		for (int i = 0; i < 3; i++) {
			targetSetText[i] = new JTextField();
			targetSetText[i].setHorizontalAlignment(SwingConstants.CENTER);
			targetSetText[i].setColumns(10);
			targetSetText[i].addFocusListener(this);
		}

		targetSetText[0].setText(targetFile.toString());
		targetSetText[1].setText(checkedFile.toString());
		targetSetText[2].setText(errorFile.toString());

		targetSetPanel.add(targetSetText[0]);
		targetSetPanel.add(targetSetText[1]);
		targetSetPanel.add(targetSetText[2]);

		targetSetPanel.setBorder(new TitledBorder("파일 설정을 검토해주세요."));
		add(targetSetPanel, BorderLayout.CENTER);

		targetProgressBarPanel = new JPanel();

		targetStartButton = new JButton();
		targetStartButton.setText("검토 시작");
		targetStartButton.setEnabled(false);
		targetStartButton.addActionListener(this);

		targetProgressBarPanel.add(targetStartButton);
		add(targetProgressBarPanel, BorderLayout.SOUTH);

		setVisible(true);
		setLocation(100, 100);
		pack();

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		int targetServiece = targetDict.getSelectedIndex();

		if (e.getSource() == targetStartButton) {
			new ProgressUI(targetFile.toString(), checkedFile.toString(), errorFile.toString());

		}
	}

	@Override
	public void focusGained(FocusEvent e) {
		// TODO Auto-generated method stub
		targetSetPanel.requestFocusInWindow();

		if (e.getSource() == targetSetText[0]) {
			// select file
			JFileChooser fileChooser = new JFileChooser();
			int result = fileChooser.showOpenDialog(fileChooseWindow);
			if (result == JFileChooser.APPROVE_OPTION) {
				// 선택한 파일의 경로 반환
				targetFile = fileChooser.getSelectedFile();
				checkedFile = new File(targetFile.getParent() + "\\"
						+ targetFile.getName().substring(0, targetFile.getName().indexOf(".")) + "_checked.txt");
				errorFile = new File(targetFile.getParent() + "\\"
						+ targetFile.getName().substring(0, targetFile.getName().indexOf(".")) + "_error.txt");
				targetSetText[0].setText(targetFile.toString());
				targetSetText[1].setText(checkedFile.toString());
				targetSetText[2].setText(errorFile.toString());

				targetStartButton.setEnabled(true);
			}

		} else if (e.getSource() == targetSetText[1]) {
			JFileChooser fileChooser = new JFileChooser();
			int result = fileChooser.showOpenDialog(fileChooseWindow);
			if (result == JFileChooser.APPROVE_OPTION) {
				// 선택한 파일의 경로 반환
				checkedFile = fileChooser.getSelectedFile();
				targetSetText[1].setText(checkedFile.toString());
			}

		} else if (e.getSource() == targetSetText[2]) {
			JFileChooser fileChooser = new JFileChooser();
			int result = fileChooser.showOpenDialog(fileChooseWindow);
			if (result == JFileChooser.APPROVE_OPTION) {
				// 선택한 파일의 경로 반환
				errorFile = fileChooser.getSelectedFile();
				targetSetText[2].setText(errorFile.toString());
			}

		}

	}

	@Override
	public void focusLost(FocusEvent e) {
	}

}

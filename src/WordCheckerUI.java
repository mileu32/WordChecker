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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.TitledBorder;
import javax.swing.text.JTextComponent;

public class WordCheckerUI extends JFrame implements ActionListener, FocusListener {

	private JPanel downloadDomainsPanel, downloadSetPanel;

	private String[] downloadDomainsString = { "example.com", "기타" };
	private JComboBox<String> downloadDomain;

	private JLabel downloadSetLabel[] = new JLabel[3];
	private JTextField downloadSetText[] = new JTextField[3];

	private JButton downloadStartButton;
	private JPanel downloadProgressBarPanel;

	private JFileChooser fileChooser = new JFileChooser();
	JFrame fileChooseWindow = new JFrame();

	private String[] webtoonData;

	private String accountID, accountPW;

	WordCheckerUI() {

		super("WordChecker v0.2.0 beta (20180318 BUILD 3)");

		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		// 저장할 웹툰 호스팅 업체 설정
		downloadDomainsPanel = new JPanel();
		downloadDomain = new JComboBox<String>();

		for (int i = 0; i < downloadDomainsString.length; i++) {
			downloadDomain.addItem(downloadDomainsString[i]);
		}
		downloadDomainsPanel.add(downloadDomain);

		downloadDomainsPanel.setBorder(new TitledBorder("사용할 사전을 선택해주세요."));
		add(downloadDomainsPanel, BorderLayout.NORTH);

		downloadSetPanel = new JPanel();
		downloadSetPanel.setLayout(new GridLayout(3, 2, 5, 5));

		for (int i = 0; i < 3; i++) {
			downloadSetLabel[i] = new JLabel();
			downloadSetLabel[i].setHorizontalAlignment(SwingConstants.CENTER);
			downloadSetText[i] = new JTextField();
			downloadSetText[i].setHorizontalAlignment(SwingConstants.CENTER);
			downloadSetText[i].setColumns(10);
			downloadSetText[i].addFocusListener(this);
		}

		downloadSetLabel[0].setText("검토 대상");
		downloadSetLabel[1].setText("출력 경로");
		downloadSetLabel[2].setText("에러 출력 경로");

		downloadSetPanel.add(downloadSetLabel[0]);
		downloadSetPanel.add(downloadSetText[0]);
		downloadSetPanel.add(downloadSetLabel[1]);
		downloadSetPanel.add(downloadSetText[1]);
		downloadSetPanel.add(downloadSetLabel[2]);
		downloadSetPanel.add(downloadSetText[2]);

		downloadSetPanel.setBorder(new TitledBorder("파일 설정을 검토해주세요."));
		add(downloadSetPanel, BorderLayout.CENTER);

		downloadProgressBarPanel = new JPanel();

		downloadStartButton = new JButton();
		downloadStartButton.setText("검토 시작");
		downloadStartButton.addActionListener(this);

		downloadProgressBarPanel.add(downloadStartButton);
		add(downloadProgressBarPanel, BorderLayout.SOUTH);

		setVisible(true);
		setLocation(100, 100);
		pack();

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		int targetServiece = downloadDomain.getSelectedIndex();

		if (e.getSource() == downloadStartButton) {

			String webtoonName, titleID, authorName, urlString;
			boolean ifAdult, ifDownloadAll;
			String naverID, naverPW;
			int targetDomain, finalEpisode, downloadStart, downloadEnd;

			webtoonName = downloadSetText[0].getText();
			titleID = downloadSetText[1].getText();
			authorName = webtoonData[2];

			targetDomain = downloadDomain.getSelectedIndex();

			if (webtoonData[3].equals("yes")) {
				ifAdult = true;
				naverID = this.accountID;
				naverPW = this.accountPW;
			} else {
				ifAdult = false;
				naverID = "";
				naverPW = "";
			}

			finalEpisode = Integer.parseInt(webtoonData[4]);
			urlString = webtoonData[5];

			

		}
	}

	@Override
	public void focusGained(FocusEvent e) {
		// TODO Auto-generated method stub
		downloadSetPanel.requestFocusInWindow();
		
		JFileChooser fileChooser = new JFileChooser();

		// 파일오픈 다이얼로그 를 띄움
		int result = fileChooser.showOpenDialog(fileChooseWindow);

		if (result == JFileChooser.APPROVE_OPTION) {
			// 선택한 파일의 경로 반환
			File selectedFile = fileChooser.getSelectedFile();
			((JTextComponent) e.getSource()).setText(selectedFile.toString());

		}
		
	}

	@Override
	public void focusLost(FocusEvent e) {
	}

}

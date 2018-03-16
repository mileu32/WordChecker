import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URLEncoder;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class WordChecker {

	static boolean ifError = false;

	public static void main(String[] args) throws IOException {

		System.out.println("WordChecker v0.1.0 (20180316 build 1)");

		BufferedReader preferences = new BufferedReader(
				new InputStreamReader(new FileInputStream(new File("data/preferences.txt")), "UTF-8"));
		String query = preferences.readLine();
		preferences.close();

		FileInputStream fis = new FileInputStream(new File("raw.txt"));
		InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
		BufferedReader br = new BufferedReader(isr);

		FileOutputStream fos1 = new FileOutputStream(new File("original.txt"));
		OutputStreamWriter osw1 = new OutputStreamWriter(fos1, "UTF-8");
		BufferedWriter bw1 = new BufferedWriter(osw1);

		FileOutputStream fos2 = new FileOutputStream(new File("error.txt"));
		OutputStreamWriter osw2 = new OutputStreamWriter(fos2, "UTF-8");
		BufferedWriter bw2 = new BufferedWriter(osw2);

		// remove UTF-8 byte order mark(BOM)
		br.mark(1);
		if (br.read() != 0xFEFF)
			br.reset();

		while (true) {
			String line = br.readLine();
			if (line == null)
				break;

			String target = line.trim();

			System.out.println(target + "를 검사합니다.");
			int checker = naverDicWordCheck(query, target, bw1, bw2);

			if (checker == -1) {
				ifError = true;
				bw2.write(line + System.lineSeparator());
			}

		}

		if (!ifError) {
			bw2.write("축하합니다. 어떠한 오류도 발견되지 않았습니다.");
		}

		br.close();
		bw1.close();
		bw2.close();

	}

	public static int naverDicWordCheck(String query, String targetWord, BufferedWriter ow, BufferedWriter ew) {

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
				System.out.println("오류가 검출되었습니다.\n");
				return -1;
			} else {
				System.out.println("사전에 등재되어있는 단어입니다.\n");
				ow.write(dicWord.text() + "\t" + dicDesc.text() + System.lineSeparator());
			}

		} catch (Exception e) {
			// e.printStackTrace();
			System.out.println("오류가 검출되었습니다.\n");
			return -1;
		}

		return 0;

	}

}

package task1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Program takes 2 arguments: set of logic characters and some text. Result is frequency analysis of that text.
 * Arguments are taken from freqInput.txt and result is written to freqOutput.txt
 * Both files are located next to src folder
 * 
 * Arguments need to be in this format: first row-logic characters splited by ,
 * 										all other rows can be some text for analysis
 * 
 * @author Hrvoje K.
 */
public class CharFrequency {

	public static void main(String[] args) throws Exception  {
		String executionPath = System.getProperty("user.dir");
		String input = executionPath + "\\freqInput.txt";
		String output = executionPath + "\\freqOutput.txt";
		
		BufferedReader br = Files.newBufferedReader(Path.of(input));
		String str = br.readLine();
		if (str == null) throw new IllegalArgumentException("Please provide logic characters");
		
		String text = "";
		String line = "";
		line = br.readLine();
		if (line == null) throw new IllegalArgumentException("Please provide text for analysis");
		while (line != null) {
			text += line;
			line = br.readLine();
		}
		
		text = text.replaceAll("[^a-zA-Z0-9\s]", "");
		
		int logicSymbolNum = 0;
		String[] chars = str.toLowerCase().split(",");
		for (String s : chars) {
			logicSymbolNum += text.toLowerCase().chars().filter(a -> a == s.charAt(0)).count();			
		}
		
		Set<LetterFrequency> elements = createElements(text, chars, logicSymbolNum);
		
		List<LetterFrequency> tmp = new ArrayList<>(elements);
		tmp.sort(LetterFrequency.BY_RATIO.thenComparing(LetterFrequency.BY_LETTERS));
		
		int withoutSpecial = text.replaceAll("\\s", "").length();
		double tmpNum = (double)logicSymbolNum / withoutSpecial;
		tmpNum = Math.round(tmpNum * 100) / (double)100;		
		
		BufferedWriter bw = Files.newBufferedWriter(Path.of(output));
		for (LetterFrequency lf : tmp) {
			bw.write(lf.toString() + "\n");
			bw.flush();
		}
		bw.write("TOTAL Frequency: " + tmpNum + " (" + logicSymbolNum + "/" + withoutSpecial + ")");
		bw.flush();
	}
	
	public static Set<LetterFrequency> createElements(String text, String[] chars, int logicSymbolNum) {
		String[] words = text.toLowerCase().split(" ");
		
		Set<LetterFrequency> freqSet = new HashSet<>();
		for (String word : words) {
			String letters = "";
			int counter = 0;
			for (String character : chars) {		
				if (word.contains(character)) {
					counter += word.toLowerCase().chars().filter(a -> a == character.charAt(0)).count();
					letters += character + ", ";
				}
			}
			if (counter != 0) {
				letters = letters.substring(0, letters.length() - 2);
				double ratio = (double)counter / logicSymbolNum;
				ratio = Math.round(ratio * 100) / (double)100;
				LetterFrequency lf = new LetterFrequency(letters, counter, ratio, word.length(), logicSymbolNum);
				freqSet.add(lf);
			}
		}
		return freqSet;
	}
}

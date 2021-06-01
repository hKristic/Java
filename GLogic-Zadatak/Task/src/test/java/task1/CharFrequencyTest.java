package task1;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

public class CharFrequencyTest {

	@Test
	public void testCreateElements() {
		String text = "I love to work in global logic";
		String[] chars = new String[] {"l", "o", "g", "i", "c"};
		int logicSymbolNum = 15;
		Set<LetterFrequency> set = CharFrequency.createElements(text, chars, logicSymbolNum);
		Set<LetterFrequency> set2 = new HashSet<>();
		set2.add(new LetterFrequency("i", 1, 0.07, 1, logicSymbolNum));
		set2.add(new LetterFrequency("i", 1, 0.07, 2, logicSymbolNum));
		set2.add(new LetterFrequency("o", 1, 0.07, 2, logicSymbolNum));
		set2.add(new LetterFrequency("o", 1, 0.07, 4, logicSymbolNum));
		set2.add(new LetterFrequency("l, o", 2, 0.13, 4, logicSymbolNum));
		set2.add(new LetterFrequency("l, o, g", 4, 0.27, 6, logicSymbolNum));
		set2.add(new LetterFrequency("l, o, g, i, c", 5, 0.33, 5, logicSymbolNum));
		
		assertEquals(set2, set);
	}
	
	@Test
	public void testCreateElementsEmptySet() {
		String text = "I love to work in global logic";
		String[] chars = new String[] {"x"};
		int logicSymbolNum = 15;
		Set<LetterFrequency> set = CharFrequency.createElements(text, chars, logicSymbolNum);
		Set<LetterFrequency> set2 = new HashSet<>();
		
		assertEquals(set2, set);
	}
}

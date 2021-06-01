package task1;

import java.util.Comparator;

public class LetterFrequency {

	private String letters;
	private int counter;
	private double ratio;
	private int wordLength;
	private int logicSymbolNum;
	
	public LetterFrequency(String letters, int counter, double ratio, int wordLength, int logicSymbolNum) {
		this.letters = letters;
		this.counter = counter;
		this.ratio = ratio;
		this.wordLength = wordLength;
		this.logicSymbolNum = logicSymbolNum;
	}

	public String getLetters() {
		return letters;
	}

	public int getCounter() {
		return counter;
	}
	
	public double getRatio() {
		return ratio;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((letters == null) ? 0 : letters.hashCode());
		result = prime * result + wordLength;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LetterFrequency other = (LetterFrequency) obj;
		if (letters == null) {
			if (other.letters != null)
				return false;
		} else if (!letters.equals(other.letters))
			return false;
		if (wordLength != other.wordLength)
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "{(" + letters + "), " + wordLength + "} = " + ratio + " (" + counter + "/" + logicSymbolNum + ")";
	}
	
	public static final Comparator<LetterFrequency> BY_RATIO = (a, b) -> Double.compare(a.getRatio(), b.getRatio());
	public static final Comparator<LetterFrequency> BY_LETTERS = (a, b) -> a.getLetters().compareTo(b.getLetters());
}

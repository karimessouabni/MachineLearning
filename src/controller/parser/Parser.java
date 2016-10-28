package controller.parser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thomas on 20/09/2016.
 */
public class Parser {
	private ArrayList<String> langs = new ArrayList<>(); //  Array contains all the languages abbreviation 

	// private static final String PATH =
	// "C:\\Users\\Thomas\\School\\Polytech\\Machine Learning\\tp\\workspace\\et5-projet\\train.txt";
	private static final String PATH = "/Users/karim/Downloads/machinelearning16-et5-projet-9d5d53f9619f/train.txt";

	/**
	 * Parse the file
	 */
	public void parse() {
		BufferedReader br = null;

		try {
			String sCurrentLine;
			br = new BufferedReader(new FileReader(PATH));

			while ((sCurrentLine = br.readLine()) != null) {
				parseLine(sCurrentLine);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	/**
	 * parse a line
	 * 
	 * @param line
	 */
	protected void parseLine(String line) {
		// Add the language
		addLang(formatLang(line));

		// Parse the sentence
		parseEssay(formatEssay(line));

		endLine();

	}

	protected void endLine() {
	}

	/**
	 * Filing the array of languages 
	 * @param lng 
	 * 
	 */
	protected void addLang(String lng) {
		if (!langs.contains(lng))
			langs.add(lng);
	}

	/**
	 * format the essay
	 * 
	 * @param line
	 * @return 
	 * @example for line = "(GER,medium) IThe importance and" 
	 * 		return "IThe importance and"
	 */
	private String formatEssay(String line) {
		return line.split("\\) ")[1];
	}

	/**
	 * format the lang
	 * @param line
	 * @return the Language of an essay
	 * @example line = "(GER,medium) IThe importance an…"
	 * 	the returned value is : "GER"
	 */
	private String formatLang(String line) {
		return line.split(",")[0].substring(1);
	}

	/**
	 * Parse the essay, execute the features
	 * 
	 * @param essay
	 */
	protected void parseEssay(String essay) {
		// Word unigram & bigram
		String[] words = essay.split(" ");
		parseWord(words[0]);
		for (int i = 1; i < words.length; i++) {
			// Unigram
			parseWord(words[i]);
			// Bigram
			parseWord(essay.split(" ")[i - 1] + " " + essay.split(" ")[i]);
		}

		// Char uni-bi tri
		/*
		 * parseWord(""+essay.charAt(0)); parseWord(""+essay.charAt(1));
		 * parseWord(""+essay.charAt(2));
		 * 
		 * parseWord(""+essay.charAt(0)+essay.charAt(1));
		 * parseWord(""+essay.charAt(1)+essay.charAt(2));
		 */
		// parseWord(""+essay.charAt(0)+essay.charAt(1)+essay.charAt(2));

		for (int i = 3; i < essay.length(); i++) {
			// Quadri
			// parseWord(""+essay.charAt(i-3)+essay.charAt(i-2)+essay.charAt(i-1)+essay.charAt(i));
			// // -- 29%-34% errors
			// Tri
			// parseWord(""+essay.charAt(i-2)+essay.charAt(i-1)+essay.charAt(i));
			// Bi
			// parseWord(""+essay.charAt(i-1)+essay.charAt(i));
			// Uni
			// parseWord(""+essay.charAt(i));
		}

	}

	/**
	 * parse the word
	 * 
	 * @param word
	 */
	protected void parseWord(String word) {
	}

	public ArrayList<String> getLangs() {
		return langs;
	}
}

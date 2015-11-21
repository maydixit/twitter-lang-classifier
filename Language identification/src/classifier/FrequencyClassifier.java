package classifier;

import edu.stanford.nlp.stats.Counters;
import edu.stanford.nlp.stats.IntCounter;
import util.Language;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class FrequencyClassifier extends Classifier {
	HashMap<Language, ArrayList<String>> mostFrequentWords;
	int numWords = 100;

	// Find most frequent word for every language
	public void train(HashMap<Language, ArrayList<String>> trainingData) {
		mostFrequentWords = new HashMap<Language, ArrayList<String>> ();

		for(Language language : trainingData.keySet()) {
			IntCounter<String> wordCounts = new IntCounter<String>();

			for(String paragraph : trainingData.get(language)) {
				for(String word : paragraph.split(" ")) {
					// Add word to word counts
					wordCounts.incrementCount(word, 1);
				}
			}
			List<String> allWords = Counters.toSortedList(wordCounts);
			if (allWords.size() >= numWords) {
				ArrayList<String> topWords = new ArrayList<String>(allWords.subList(0, numWords));
				mostFrequentWords.put(language, topWords);
			} else {
				mostFrequentWords.put(language, (ArrayList<String>) allWords);	
			}
			//System.out.println("Language: " + language + " max Word: " + maxWord);
		}
	}
	
	public Language classify(String sentence) {
		IntCounter<Language> languageCounts = new IntCounter();
		for (String word: sentence.split(" ")) {
			for (Language lang: mostFrequentWords.keySet()) {
				if (mostFrequentWords.get(lang).contains(word)) {
					languageCounts.incrementCount(lang, 1);
				}
			}
		}
		if (languageCounts.isEmpty()) {
			return Language.UNKNOWN;
		} else {
			Language maxLang = Counters.toSortedList(languageCounts).get(0);
			return maxLang;
		}
	}
}

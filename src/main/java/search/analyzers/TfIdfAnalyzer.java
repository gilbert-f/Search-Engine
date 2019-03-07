package search.analyzers;

import datastructures.concrete.ChainedHashSet;
import datastructures.concrete.DoubleLinkedList;
import datastructures.concrete.KVPair;
import datastructures.concrete.dictionaries.ChainedHashDictionary;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.IList;
import datastructures.interfaces.ISet;

import search.models.Webpage;

import java.net.URI;

/**
 * Project 3
 * Written by : Dennis Muljadi and Gilbert Febrianto
 * 
 * This class is responsible for computing how "relevant" any given document is
 * to a given search query.
 *
 * See the spec for more details.
 */
public class TfIdfAnalyzer {
    // This field must contain the IDF score for every single word in all
    // the documents.
    private IDictionary<String, Double> idfScores;

    // This field must contain the TF-IDF vector for each webpage you were given
    // in the constructor.
    //
    // We will use each webpage's page URI as a unique key.
    private IDictionary<URI, IDictionary<String, Double>> documentTfIdfVectors;

    /** Extra Fields and Constants*/
    // This field must contain the normalized TF-IDF vector for each webpage 
    // you were given in the constructor.
    private IDictionary<URI, Double> normDocumentTfIdfVectors;

    public TfIdfAnalyzer(ISet<Webpage> webpages) {
        this.idfScores = this.computeIdfScores(webpages);
        this.documentTfIdfVectors = this.computeAllDocumentTfIdfVectors(webpages);
        this.normDocumentTfIdfVectors = new ChainedHashDictionary<URI, Double>();
        for (KVPair<URI, IDictionary<String, Double>> pair : documentTfIdfVectors) {
        		URI webpage = pair.getKey();
        		double value = norm(pair.getValue());
        		normDocumentTfIdfVectors.put(webpage, value);
        }
    }

    // Note: this method, strictly speaking, doesn't need to exist. However,
    // we've included it so we can add some unit tests to help verify that your
    // constructor correctly initializes your fields.
    public IDictionary<URI, IDictionary<String, Double>> getDocumentTfIdfVectors() {
        return this.documentTfIdfVectors;
    }

    // Note: these private methods are suggestions or hints on how to structure your
    // code. However, since they're private, you're not obligated to implement exactly
    // these methods: Feel free to change or modify these methods if you want. The
    // important thing is that your 'computeRelevance' method ultimately returns the
    // correct answer in an efficient manner.

    /**
     * This method should return a dictionary mapping every single unique word found
     * in any documents to their IDF score.
     */
    private IDictionary<String, Double> computeIdfScores(ISet<Webpage> pages) {
    		IDictionary<String, Double> wordCount = new ChainedHashDictionary<String, Double>();
    		IDictionary<String, Double> idfDict = new ChainedHashDictionary<String, Double>();
    		
    		for (Webpage webpage : pages) {
    			IList<String> words = webpage.getWords();
    			IList<String> uniqueWords = computeUniqueWords(words);
    			
    			for (String word : uniqueWords) {
    				if (wordCount.containsKey(word)) {
    					double value = wordCount.get(word) + 1.0;
    					wordCount.put(word, value);
    				} else {
    					wordCount.put(word, 1.0);
    				}
    			}
    		}
    		
    		int length = pages.size();
    		for (KVPair<String, Double> pair : wordCount) {
    			String word = pair.getKey();
    			double value = Math.log(length / pair.getValue());
    			idfDict.put(word, value);
    		}
    		return idfDict;
    }

    /**
     * Returns a dictionary mapping every unique word found in the given list
     * to their term frequency (TF) score.
     *
     * We are treating the list of words as if it were a document.
     */
    private IDictionary<String, Double> computeTfScores(IList<String> words) {
		IDictionary<String, Double> tfDict = new ChainedHashDictionary<String, Double>();
		
		double length = words.size();
		for (String word : words) {
			if (tfDict.containsKey(word)) {
				double value = tfDict.get(word) + (1.0 / length);
				tfDict.put(word, value);
			} else {
				double value = (1.0 / length);
				tfDict.put(word, value);
			}
		}
		return tfDict;
    }

    /**
     * See spec for more details on what this method should do.
     */
    private IDictionary<URI, IDictionary<String, Double>> computeAllDocumentTfIdfVectors(ISet<Webpage> pages) {
	    	IDictionary<URI, IDictionary<String, Double>> allRelevanceDict = 
					new ChainedHashDictionary<URI, IDictionary<String, Double>>();
			
		for (Webpage webpage : pages) {
			IList<String> words = webpage.getWords();
			IDictionary<String, Double> relevanceDict = new ChainedHashDictionary<String, Double>();
			IDictionary<String, Double> tfDict = computeTfScores(words);
			
			for (KVPair<String, Double> pair : tfDict) {
				String word = pair.getKey();
				double value = pair.getValue() * idfScores.get(word);
				relevanceDict.put(word, value);
	        }
			URI uri = webpage.getUri();
	        allRelevanceDict.put(uri, relevanceDict);
		}
			
	    return allRelevanceDict;
    }

    /**
     * Returns the cosine similarity between the TF-IDF vector for the given query and the
     * URI's document.
     *
     * Precondition: the given uri must have been one of the uris within the list of
     *               webpages given to the constructor.
     */
    public Double computeRelevance(IList<String> query, URI pageUri) {
    		IDictionary<String, Double> documentVector = documentTfIdfVectors.get(pageUri);
		IDictionary<String, Double> queryVector = computeQueryTfIdfVector(query);
		
		double numerator = 0.0;
		for (KVPair<String, Double> pair : queryVector) {
			String word = pair.getKey();
			double docWordScore;
			if (documentVector.containsKey(word)) {
				docWordScore = documentVector.get(word);
			} else {
				docWordScore = 0.0;
			}
			double queryWordScore = queryVector.get(word);
			numerator += docWordScore * queryWordScore;
		}
		
		double denominator = normDocumentTfIdfVectors.get(pageUri) * norm(queryVector);
		
		if (denominator != 0.0) {
			return numerator / denominator;
		} else {
			return 0.0;
		}
	}
	
	/** Helper Methods */
	private double norm(IDictionary<String, Double> vector) {
		double output = 0.0;
	    for (KVPair<String, Double> pair : vector) {
	        double score = pair.getValue();
	        output += score * score;
	    }
	    return Math.sqrt(output);
	}
	
	private IDictionary<String, Double> computeQueryTfIdfVector(IList<String> query) {
		IDictionary<String, Double> relevanceDict = new ChainedHashDictionary<String, Double>();
		IList<String> uniqueQueryWords = computeUniqueWords(query);
		IDictionary<String, Double> tfDict = computeTfScores(uniqueQueryWords);
		
		for (KVPair<String, Double> pair : tfDict) {
			String word = pair.getKey();
			double value = pair.getValue() * idfScores.get(word);
			relevanceDict.put(word, value);
        }
		return relevanceDict;
	}

	private IList<String> computeUniqueWords(IList<String> list) {
		IList<String> wordsList = new DoubleLinkedList<String>();
		ISet<String> uniqueWords = new ChainedHashSet<String>();
		for (String word : list) {
			uniqueWords.add(word);
		}
		
		for (String word : uniqueWords) {
			wordsList.add(word);
		}
		return wordsList;
	}
}

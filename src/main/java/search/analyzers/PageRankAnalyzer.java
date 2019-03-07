package search.analyzers;

import datastructures.concrete.ChainedHashSet;
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
 * This class is responsible for computing the 'page rank' of all available webpages.
 * If a webpage has many different links to it, it should have a higher page rank.
 * See the spec for more details.
 */
public class PageRankAnalyzer {
    private IDictionary<URI, Double> pageRanks;

    /**
     * Computes a graph representing the internet and computes the page rank of all
     * available webpages.
     *
     * @param webpages  A set of all webpages we have parsed.
     * @param decay     Represents the "decay" factor when computing page rank (see spec).
     * @param epsilon   When the difference in page ranks is less then or equal to this number,
     *                  stop iterating.
     * @param limit     The maximum number of iterations we spend computing page rank. This value
     *                  is meant as a safety valve to prevent us from infinite looping in case our
     *                  page rank never converges.
     */
    public PageRankAnalyzer(ISet<Webpage> webpages, double decay, double epsilon, int limit) {
        // Step 1: Make a graph representing the 'internet'
        IDictionary<URI, ISet<URI>> graph = this.makeGraph(webpages);

        // Step 2: Use this graph to compute the page rank for each webpage
        this.pageRanks = this.makePageRanks(graph, decay, limit, epsilon);
    }

    /**
     * This method converts a set of webpages into an unweighted, directed graph,
     * in adjacency list form.
     *
     * You may assume that each webpage can be uniquely identified by its URI.
     *
     * Note that a webpage may contain links to other webpages that are *not*
     * included within set of webpages you were given. You should omit these
     * links from your graph: we want the final graph we build to be
     * entirely "self-contained".
     */
    private IDictionary<URI, ISet<URI>> makeGraph(ISet<Webpage> webpages) {
		ISet<URI> allowedSetOfWebpages = new ChainedHashSet<URI>();
		IDictionary<URI, ISet<URI>> graph = new ChainedHashDictionary<URI, ISet<URI>>();
		
		for (Webpage page : webpages) {
			URI pageUri = page.getUri();
			allowedSetOfWebpages.add(pageUri);
		}
		
		for (Webpage page : webpages) {
			URI pageUri = page.getUri();
			IList<URI> pageLinks = page.getLinks();
			ISet<URI> allowedSetOfPageLinks = new ChainedHashSet<URI>();
			
			for (URI linkUri : pageLinks) {
				if (allowedSetOfWebpages.contains(linkUri) && !linkUri.equals(pageUri)
						&& !allowedSetOfPageLinks.contains(linkUri)) {
					allowedSetOfPageLinks.add(linkUri);
				}
			}
			graph.put(pageUri, allowedSetOfPageLinks);
		}
		return graph;
    }

    /**
     * Computes the page ranks for all webpages in the graph.
     *
     * Precondition: assumes 'this.graphs' has previously been initialized.
     *
     * @param decay     Represents the "decay" factor when computing page rank (see spec).
     * @param epsilon   When the difference in page ranks is less then or equal to this number,
     *                  stop iterating.
     * @param limit     The maximum number of iterations we spend computing page rank. This value
     *                  is meant as a safety valve to prevent us from infinite looping in case our
     *                  page rank never converges.
     */
    private IDictionary<URI, Double> makePageRanks(IDictionary<URI, ISet<URI>> graph,
                                                   double decay,
                                                   int limit,
                                                   double epsilon) {
    		// Step 1: The initialize step should go here
    		IDictionary<URI, Double> pageRanksDict = new ChainedHashDictionary<URI, Double>();
    		double noOfWebpages = graph.size();
    		double initialPageRank = 1.0 / noOfWebpages;
    		for (KVPair<URI, ISet<URI>> pair : graph) {
    			pageRanksDict.put(pair.getKey(), initialPageRank);
		}
    		
        for (int i = 0; i < limit; i++) {
        		// Step 2: The update step should go here
        		// First, give every web page a new page rank of 0.0.
	        	IDictionary<URI, Double> newPageRanksDict = new ChainedHashDictionary<URI, Double>();
	    		for (KVPair<URI, ISet<URI>> pair : graph) {
	    			newPageRanksDict.put(pair.getKey(), 0.0);
			}
	        
	        // Next, we will take the old page rank for every webpage and equally share it 
	    		// with every web page it links to.
	    		for (KVPair<URI, ISet<URI>> pair : graph) {
	    			URI pageUri = pair.getKey();
	    			ISet<URI> pageLinks = pair.getValue();
	    			double noOfUniqueLinks = (double) pageLinks.size();
	    			
	    			for (URI linkUri : pageLinks) {
	    				double newPageRank = newPageRanksDict.get(linkUri);
	    				double oldPageRank = pageRanksDict.get(pageUri);
	    				double value = newPageRank + decay * 
		            			oldPageRank / noOfUniqueLinks;
		            	newPageRanksDict.put(linkUri, value);
	            }
	    			
	            if (noOfUniqueLinks == 0.0) {
	            		for (KVPair<URI, Double> rankPair : newPageRanksDict) {
	            			double newPageRank = rankPair.getValue();
	            			double oldPageRank = pageRanksDict.get(pageUri);
	            			double value = newPageRank + decay * 
	            					oldPageRank / noOfWebpages;
	            			newPageRanksDict.put(rankPair.getKey(), value);
            			}
            		}
	            
	            // Finally, add (1−d)/N every web page's new page rank.
	            double newPageRank = newPageRanksDict.get(pageUri);
	            double value = newPageRank + (1 - decay) / noOfWebpages;
	            newPageRanksDict.put(pageUri, value);
	        }
	    		
	        // Step 3: the convergence step should go here.
	        // Return early if we've converged.
	    		boolean converged = false;
	    		for (KVPair<URI, Double> rankPair : newPageRanksDict) {
	    			URI pageUri = rankPair.getKey();
	    			double newPageRank = rankPair.getValue();
	    			double oldPageRank = pageRanksDict.get(pageUri);
	    			
	    			if (Math.abs(newPageRank-oldPageRank) < epsilon) {
	    				converged = true;
	    			} else {
	    				pageRanksDict = newPageRanksDict;
	    				converged = false;
	    				break;
	    			}
	    		}
	    		if (converged) {
	    			break;
	    		}
        }
        return pageRanksDict;
    }

    /**
     * Returns the page rank of the given URI.
     *
     * Precondition: the given uri must have been one of the uris within the list of
     *               webpages given to the constructor.
     */
    public double computePageRank(URI pageUri) {
    		return pageRanks.get(pageUri);
    }
}

package ch.heigvd.iict.dmg.labo1;

import ch.heigvd.iict.dmg.labo1.indexer.CACMIndexer;
import ch.heigvd.iict.dmg.labo1.parsers.CACMParser;
import ch.heigvd.iict.dmg.labo1.queries.QueriesPerformer;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.search.similarities.Similarity;

public class Main {

	public static void main(String[] args) throws Exception {

		// 1.1. create an analyzer
		Analyzer analyser = getAnalyzer();

		// TODO student "Tuning the Lucene Score"
		// Similarity similarity = new MySimilarity();
		Similarity similarity = new ClassicSimilarity();
		
		CACMIndexer indexer = new CACMIndexer(analyser, similarity);
		indexer.openIndex();
		CACMParser parser = new CACMParser("documents/cacm.txt", indexer);
		parser.startParsing();
		indexer.finalizeIndex();
		
		QueriesPerformer queriesPerformer = new QueriesPerformer(analyser, similarity);

		// Section "Reading Index"
		readingIndex(queriesPerformer);

		// Section "Searching"
		searching(queriesPerformer);

		queriesPerformer.close();
		
	}

	private static void readingIndex(QueriesPerformer queriesPerformer) throws Exception {
		queriesPerformer.printTopRankingTerms("authors", 10);
		queriesPerformer.printTopRankingTerms("title", 10);
	}

	private static void searching(QueriesPerformer queriesPerformer) {
		// Example
		queriesPerformer.query("compiler program");

		//Publications containing the term "Information Retrieval"
		queriesPerformer.query("Information Retrieval");

		//Publications containing both "Information" and "Retrieval"
		queriesPerformer.query("\"Information\" AND \"Retrieval\"");

		//Publications containing at least the term "Retrieval" and, possibly "Information" but not "Database"
		queriesPerformer.query("+\"Retrieval\" \"Information\" NOT \"Database\"");

		//Publications containing a term starting with "Info"
		queriesPerformer.query("Info*");

		//Publications containing the term "Information" close to "Retrieval" (max distance 5)
		queriesPerformer.query( "\'Information Retrieval\'~5");

	}

	private static Analyzer getAnalyzer() {

		//return new StandardAnalyzer();
		//return new WhitespaceAnalyzer();
		return new EnglishAnalyzer();
		//return new ShingleAnalyzerWrapper();
		//return new ShingleAnalyzerWrapper();
		//return new StopAnalyzer();
	}

}

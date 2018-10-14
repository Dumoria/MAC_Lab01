package ch.heigvd.iict.dmg.labo1.queries;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.misc.HighFreqTerms;
import org.apache.lucene.misc.TermStats;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Arrays;

public class QueriesPerformer {
	
	private Analyzer		analyzer		= null;
	private IndexReader 	indexReader 	= null;
	private IndexSearcher 	indexSearcher 	= null;

	public QueriesPerformer(Analyzer analyzer, Similarity similarity) {
		this.analyzer = analyzer;
		Path path = FileSystems.getDefault().getPath("index");
		Directory dir;
		try {
			dir = FSDirectory.open(path);
			this.indexReader = DirectoryReader.open(dir);
			this.indexSearcher = new IndexSearcher(indexReader);
			if(similarity != null)
				this.indexSearcher.setSimilarity(similarity);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void printTopRankingTerms(String field, int numTerms) throws Exception {
		HighFreqTerms hft = new HighFreqTerms();
		TermStats[] stats = new TermStats[10];

		if (field.equals("authors")) {
			stats = hft.getHighFreqTerms(indexReader, numTerms, field, (a, b) -> Long.compare(a.docFreq, b.docFreq));
		}
		else {
			stats = hft.getHighFreqTerms(indexReader, numTerms, field, (a, b) -> Long.compare(b.totalTermFreq, a.totalTermFreq));
		}



		String[] toDisplay = new String[10];
		for (int i = 0; i < 10; i++) {
			toDisplay[i] = stats[i].termtext.utf8ToString();
		}



	    System.out.println("Top ranking terms for field ["  + field + "] are: " + Arrays.toString(toDisplay));
	}
	
	public void query(String q) {
		System.out.println("Searching for [" + q +"]");

		QueryParser parser = new QueryParser("summary", analyzer);
		Query query = null;
		ScoreDoc[] hits = null;

		try {
			query = parser.parse(q);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		try {
			hits = indexSearcher.search(query, 1000).scoreDocs;
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("Results found: " + hits.length);
		for(ScoreDoc hit: hits){
			Document doc = null;
			try {
				doc = indexSearcher.doc(hit.doc);
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println(doc.get("id") + ": " + doc.get("title") + " (" + hit.score + ")");
		}
	}
	 
	public void close() {
		if(this.indexReader != null)
			try { this.indexReader.close(); } catch(IOException e) { /* BEST EFFORT */ }
	}
	
}

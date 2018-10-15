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
import java.util.LinkedList;


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

		TermStats[] stats;
		String[] toDisplay = new String[numTerms];

		if (field.equals("authors")) {
			stats = HighFreqTerms.getHighFreqTerms(indexReader, numTerms, field, new HighFreqTerms.DocFreqComparator());

		}
		else {
			stats = HighFreqTerms.getHighFreqTerms(indexReader, numTerms, field, new HighFreqTerms.TotalTermFreqComparator());
		}

		for (int i = 0; i < numTerms; i++) {
			toDisplay[i] = stats[i].termtext.utf8ToString();
		}

	    System.out.println("Top ranking terms for field ["  + field + "] are: " + Arrays.toString(toDisplay));

	}
	//----------------Query-------------------

	private ScoreDoc[] performQuery(String q){
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

		return hits;
	}

	public void query(String q) {
		System.out.println("Searching for [" + q +"]");
		ScoreDoc[] hits = performQuery(q);
		displayResults(hits);
	}

	private void displayResults(ScoreDoc[] hits){
		System.out.println("Results found: " + hits.length);
		for(ScoreDoc hit: hits){
			Document doc = null;
			try {
				doc = (Document) indexSearcher.doc(hit.doc);
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println(doc.get("id") + ": " + doc.get("title") + " (" + hit.score + ")");
		}
	}

	public void queryAnd(String q1, String q2){

		ScoreDoc[] hits1 = performQuery(q1);
		ScoreDoc[] hits2 = performQuery(q2);
		LinkedList<ScoreDoc> hitsRes = new LinkedList<>();

		for(ScoreDoc hit1 : hits1){
			for(ScoreDoc hit2 : hits2){
				if(hit2.doc > hit1.doc)		//verif just
					break;
				if(hit1.doc == hit2.doc){
					hitsRes.add(hit1);
					break;
				}
			}
		}

		displayResults((ScoreDoc[]) hitsRes.toArray());
	}

	public void queryTheFirstButNotSecond(String shouldHave, String shouldNotHave){
		ScoreDoc[] hits1 = performQuery(shouldHave);
		ScoreDoc[] hits2 = performQuery(shouldNotHave);
		LinkedList<ScoreDoc> hitsRes = new LinkedList<>();

		for(ScoreDoc hit1 : hits1){
			for(ScoreDoc hit2 : hits2){
				if(hit1.doc == hit2.doc){
					break;
				}
				if(hit2.doc > hit1.doc)		//verif just
					hitsRes.add(hit1);
					break;
			}
		}

		displayResults((ScoreDoc[]) hitsRes.toArray());
	}

	//public void

	public void close() {
		if(this.indexReader != null)
			try { this.indexReader.close(); } catch(IOException e) { /* BEST EFFORT */ }
	}
	
}

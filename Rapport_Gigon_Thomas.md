Gigon Quentin, Thomas Benjamin <div style="text-align:right;">Le 10.10.2018 </div>


# <center> MAC</center>
## <center> Laboratoire n°1</center>

## Introduction
The goal of this laboratory is to get familiar with the <strong>Lucene</strong> library and its java API. This will be done throw experimentation with physical data such as text documents.

## Section D

###### 1)   <strong>Does the command line demo use stopword removal? Explain how you find out the answer.</strong>

<p style="text-align:justify;">Yes, the initial query: “Scientific Business and Applications” was transformed into: “scientific business applications”.
</p>


###### 2)   <strong>Does the command line demo use stopword removal? Explain how you find out the answer.</strong>
<p style="text-align:justify;">No, the query: “automat automation automatic automates” was not transformed.
</p>

###### 3)   <strong>Does the command line demo use stopword removal? Explain how you find out the answer.</strong>
<p style="text-align:justify;">No, every word of the query is put in lowercase.
</p>

###### 4)   <strong>Does it matter whether stemming occurs before or after stopword removal? Consider this as a general question.</strong>
<p style="text-align:justify;">Yes, we must do the stopword removal before the stemming. Because if a stop word is stemmed, e.g. “This” => “thi”, it might not be removed after. There is also a reason of performance, as the stemmer has less words to stemm (?) and he doesn’t have to do an useless job.
</p>

## Section F

### Indexing

######  <strong>What should be added to the code to have access to the term vector in the index? Have a look at the Lucene slides presented in your course (look at different methods of FieldType). Use Luke to check that the term vector is included in the index.</strong>

<p style="text-align:justify;">We need to call the following method after each term’s initialization:

term.setStoreTermVectors(true);

sur Luke
Tu cliques sur summary et tous en bas tu as un TM (si c'est ca ta question)

</p>

######  <strong>Indexing code</strong>

    @Override
	public void onNewDocument(Long id, String authors, String title, String summary) {
		Document doc = new Document();

		FieldType fieldType = new FieldType();
		fieldType.setIndexOptions(IndexOptions.DOCS_AND_FREQS);
		fieldType.setTokenized(true);
		fieldType.setStored(true);
		fieldType.freeze();

		if (id != null)
			doc.add(new StoredField("id", id));

		if (!authors.equals("")) {
			String[] toDisplay = authors.split(";");
			for (String s: toDisplay) {
				if (!s.equals(""))
					doc.add(new StringField("authors", s, Field.Store.YES));
			}
		}

		if (title != null)
			doc.add(new Field("title", title, fieldType));

		if (summary != null)
			doc.add(new TextField("summary", summary, Field.Store.YES));

		try {
			this.indexWriter.addDocument(doc);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


### Using different Analyzers

#### <strong>WhitespaceAnalyzer</strong>

######  <strong>a) The number of indexed documents and indexed terms.</strong>

######  <strong>b) The number of indexed terms in the summary field.</strong>

######  <strong>c) The top 10 frequent terms of the summary field in the index.</strong>

######  <strong>d) The top 10 frequent terms of the summary field in the index.</strong>

######  <strong>e) The required time for indexing.</strong>


#### <strong>EnglishAnalyzer</strong>

######  <strong>a) The number of indexed documents and indexed terms.</strong>

######  <strong>b) The number of indexed terms in the summary field.</strong>

######  <strong>c) The top 10 frequent terms of the summary field in the index.</strong>

######  <strong>d) The top 10 frequent terms of the summary field in the index.</strong>

######  <strong>e) The required time for indexing.</strong>

#### <strong>ShingleAnalyzerWrapper (using shingle size 2)</strong>

######  <strong>a) The number of indexed documents and indexed terms.</strong>

######  <strong>b) The number of indexed terms in the summary field.</strong>

######  <strong>c) The top 10 frequent terms of the summary field in the index.</strong>

######  <strong>d) The top 10 frequent terms of the summary field in the index.</strong>

######  <strong>e) The required time for indexing.</strong>

#### <strong>ShingleAnalyzerWrapper (using shingle size 3)</strong>

######  <strong>a) The number of indexed documents and indexed terms.</strong>

######  <strong>b) The number of indexed terms in the summary field.</strong>

######  <strong>c) The top 10 frequent terms of the summary field in the index.</strong>

######  <strong>d) The top 10 frequent terms of the summary field in the index.</strong>

######  <strong>e) The required time for indexing.</strong>

#### <strong>StopAnalyzer</strong>

######  <strong>a) The number of indexed documents and indexed terms.</strong>

######  <strong>b) The number of indexed terms in the summary field.</strong>

######  <strong>c) The top 10 frequent terms of the summary field in the index.</strong>

######  <strong>d) The top 10 frequent terms of the summary field in the index.</strong>

######  <strong>e) The required time for indexing.</strong>

### Reading Index

###### 1)   <strong>What is the author with the highest number of publications? How many publications does he/she have?</strong>

<p style="text-align:justify;">Thacher Jr., H. C. with.........................................
</p>


###### 2)   <strong>List the top 10 terms in the title field with their frequency.</strong>
<p style="text-align:justify;">Top ranking terms for field [title] are: [algorithm, comput, program, system, gener, method, languag, function, us, data]

The following method goes in the QueriesPerformer.java file:
</p>

    public void printTopRankingTerms(String field, int numTerms) throws Exception {

		TermStats[] stats;
		String[] toDisplay = new String[numTerms];

		if (field.equals("authors"))
			stats = HighFreqTerms.getHighFreqTerms(indexReader, numTerms, field, new HighFreqTerms.DocFreqComparator());
		else
			stats = HighFreqTerms.getHighFreqTerms(indexReader, numTerms, field, new HighFreqTerms.TotalTermFreqComparator());

		for (int i = 0; i < numTerms; i++) {
			toDisplay[i] = stats[i].termtext.utf8ToString();
		}

	    System.out.println("Top ranking terms for field ["  + field + "] are: " + Arrays.toString(toDisplay));
	}

### Searching
<p style="text-align:justify;">Searching for [compiler program]
Results found: 578
3189: An Algebraic Compiler for the FORTRAN Assembly Program (1.0367663)
1459: Requirements for Real-Time Languages (0.9427518)
2652: Reduction of Compilation Costs Through Language Contraction (0.93779767)
1183: A Note on the Use of a Digital Computerfor Doing Tedious Algebra and Programming (0.8799802)
1465: Program Translation Viewed as a General Data Processing Problem (0.82941306)
1988: A Formalism for Translator Interactions (0.82941306)
1647: WATFOR-The University of Waterloo FORTRAN IV Compiler (0.8082413)
1237: Conversion of Decision Tables To Computer Programs (0.7542014)
2944: Shifting Garbage Collection Overhead to Compile Time (0.7542014)
637: A NELIAC-Generated 7090-1401 Compiler (0.74336267)

Searching for [Information Retrieval]
Results found: 188
1457: Data Manipulation and Programming Problemsin Automatic Information Retrieval (1.180699)
891: Everyman's Information Retrieval System (1.1004101)
1935: Randomized Binary Search Technique (1.0225154)
1699: Experimental Evaluation of InformationRetrieval Through a Teletypewriter (0.9628588)
3134: The Use of Normal Multiplication Tablesfor Information Storage and Retrieval (0.9191686)
2307: Dynamic Document Processing (0.913785)
1032: Theoretical Considerations in Information Retrieval Systems (0.8855243)
1681: Easy English,a Language for InformationRetrieval Through a Remote Typewriter Console (0.87130356)
2990: Effective Information Retrieval Using Term Accuracy (0.87130356)
2519: On the Problem of Communicating Complex Information (0.7863115)

Searching for ["Information" AND "Retrieval"]
Results found: 23
1457: Data Manipulation and Programming Problemsin Automatic Information Retrieval (1.180699)
891: Everyman's Information Retrieval System (1.1004101)
1935: Randomized Binary Search Technique (1.0225154)
1699: Experimental Evaluation of InformationRetrieval Through a Teletypewriter (0.9628588)
3134: The Use of Normal Multiplication Tablesfor Information Storage and Retrieval (0.9191686)
2307: Dynamic Document Processing (0.913785)
1032: Theoretical Considerations in Information Retrieval Systems (0.8855243)
1681: Easy English,a Language for InformationRetrieval Through a Remote Typewriter Console (0.87130356)
2990: Effective Information Retrieval Using Term Accuracy (0.87130356)
2519: On the Problem of Communicating Complex Information (0.7863115)

Searching for [+"Retrieval" "Information" NOT "Database"]
Results found: 54
1457: Data Manipulation and Programming Problemsin Automatic Information Retrieval (1.180699)
891: Everyman's Information Retrieval System (1.1004101)
1935: Randomized Binary Search Technique (1.0225154)
1699: Experimental Evaluation of InformationRetrieval Through a Teletypewriter (0.9628588)
3134: The Use of Normal Multiplication Tablesfor Information Storage and Retrieval (0.9191686)
2307: Dynamic Document Processing (0.913785)
1032: Theoretical Considerations in Information Retrieval Systems (0.8855243)
1681: Easy English,a Language for InformationRetrieval Through a Remote Typewriter Console (0.87130356)
2990: Effective Information Retrieval Using Term Accuracy (0.87130356)
2519: On the Problem of Communicating Complex Information (0.7863115)

Searching for [Info*]
Results found: 193
222: Coding Isomorphisms (1.0)
272: A Storage Allocation Scheme for ALGOL 60 (1.0)
396: Automation of Program  Debugging (1.0)
397: A Card Format for Reference Files in Information Processing (1.0)
409: CL-1, An Environment for a Compiler (1.0)
440: Record Linkage (1.0)
483: On the Nonexistence of a Phrase Structure Grammar for ALGOL 60 (1.0)
616: An Information Algebra - Phase I Report-LanguageStructure Group of the CODASYL Development Committee (1.0)
644: A String Language for Symbol Manipulation Based on ALGOL 60 (1.0)
655: COMIT as an IR Language (1.0)

Searching for ['Information Retrieval'~5]
Results found: 159
2160: Canonical Structure in Attribute Based File Organization (0.471206)
2832: Faster Retrieval from Context Trees (Corrigendum) (0.2385921)
2905: Perfect Hashing Functions: A SingleProbe Retrieving Method for Static Sets (0.15906142)
1746: Protection in an Information Processing Utility (0.15630794)
1976: Multi-attribute Retrieval with Combined Indexes (0.13255118)
1516: Automatic Data Compression (0.11844581)
1251: American Standard and IFIP/ICC Vocabularies compared (0.110526405)
1267: Performance of Systems Used for Data TransmissionTransfer Rate of Information Bits -An ASA TutorialStandard (0.110526405)
1332: Subroutine Assembly (0.110526405)
2870: A Lattice Model of Secure Information Flow (0.108125746)

The following method goes in the main.java file:
</p>


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

The following method goes in the QueriesPerformer.java file:

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

      for(int i = 0; i < 10 && i < hits.length; ++i){
        Document doc = null;
        try {
          doc = indexSearcher.doc(hits[i].doc);
        } catch (IOException e) {
          e.printStackTrace();
        }
        System.out.println(doc.get("id") + ": " + doc.get("title") + " (" + hits[i].score + ")");
      }
    }


### Tuning the Lucene Score
<p style="text-align:justify;">
</p>

## Conclusion
<p style="text-align:justify;">
</p>

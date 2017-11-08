package jsf.career.planner;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import org.apache.lucene.analysis.snowball.SnowballAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

/**
 * @author Naren Suri
 *
 */

public class WikiKeyWordsExtractionUsingLucene {

	public static int shortdescsize = 10;
	public static Date dNow = new Date();
	public static Directory dir;
	public static BufferedWriter out;
	public static SimpleDateFormat ft = new SimpleDateFormat("E yyyy.MM.dd 'at' hh:mm:ss a zzz");
	public static IndexSearcher JobInfosearcher;
	public static String prepareddInsertStringLocation = "";
	public static String setUpdateOption;
	public static String SetProcessed;
	public static String selectQuery;
	public static String updateQuery;
	public static PreparedStatement preparedStmtUpdtOptn;
	public static PreparedStatement preparedStmtProcessed;
	public static PreparedStatement preparedStmt;
	public static PreparedStatement preparedStmtForUpdate;
	public static Connection conn;
	public static HashMap<Integer, String> HashToStoreKeywordAsDbRecords;
	public static URL jobindexloc;
	public static int slopmode;
	public static int slopvalue;
	public static int numOfTotalDocsInIndex;

	public void JobDataSearchingForKeyWords() throws IOException, ParseException, ClassNotFoundException, SQLException {
		// ***************************************************************************
		int indexmode = 1; // mode 0 tomcat index ; mode 1 general ; mode 2
							// boosted index
		slopmode = 0; // slop mode 0 = no slop set, mode =1 is slop set,
						// this works only with the phrase query
		slopvalue = 0;
		int modeKeyWordsInQueryBooster = 0; // default 1 boosts keywords while
											// searching. // 0 is no
											// keywordsinQuery boost
		float QryKywrdBostFactr = 5.0F; // the boost factor to be applied to
										// keywords got from the job obtained

		// ***************************************************************************
		String resultspath;
		resultspath = "KeywordExtraction\\LuceneKeyWordsExtracteedResults\\results\\results-indx(" + indexmode
				+ ")slop(" + slopmode + ")-jbkywrdbst(" + modeKeyWordsInQueryBooster + ")-qrKyBst("
				+ (int) QryKywrdBostFactr + ")-" + "-" + System.currentTimeMillis() + ".txt";
		URL resultsloc = null;
		// resultsloc =
		// getClass().getClassLoader().getResource("//KeywordExtraction//LuceneKeyWordsExtracteedResults//results.txt");

		if (resultsloc == null) {
			resultsloc = new URL("file:/D:\\sem2\\ir-project\\" + resultspath);
		}

		out = callfilewriter(resultsloc);
		jobindexloc = null;

		if (indexmode == 0) {

		}

		else if (indexmode == 2) {

			jobindexloc = new URL("file:/D:\\sem2\\ir-project\\lucene\\LuceneCareerAdviseIndexingBoosted\\JobInfo");
			// out.write("\n"
			// + " ==================================== \n\n These results are
			// from Field Boost Index \n ============================ \n\n ");
			//
		} else {
			if (jobindexloc == null) {
				jobindexloc = new URL(
						"file:/D:\\sem2\\ir-project\\lucene\\LuceneCareerAdviseIndexing-For-KeyWikiWordExtractions\\NeoDbJobInfo");
			}

			// out.write("\n"
			// + " ==================================== \n\n These results are
			// from Field General Index \n ============================ \n\n ");

		}

		// System.out.println(getClass().getClassLoader().getResource("logging.properties"));

		Logger log = Logger.getLogger("FromJobDataSearch");
		// out.write("\n" + " " + resultspath);
		// out.append("\n" + ft.format(dNow) + " " + "FromJobDataSearch \n ");
		//
		//
		// ************ System.out.println("Are Pths Working 2 ?");
		JobInfosearcher = IndexSearch(jobindexloc);
		IndexReader reader = IndexReader.open(dir);
		numOfTotalDocsInIndex = reader.numDocs();
	}

	public static void searchForTheKeyword(String SearchigInStemmedData, String OriginalWord)
			throws IOException, ParseException, SQLException {
		// String SearchigInStemmedData = searchWord;
		// out.append("\n" + System.currentTimeMillis() + " " + " \n Your
		// interested Word is " + SearchigInStemmedData);
		// **************** All Indexers created here

		// ========================================================================

		// ###################################

		// QueryParser parser = new MultiFieldQueryParser(Version.LUCENE_30, new
		// String[] { "StemmedData" },
		// new StandardAnalyzer(Version.LUCENE_30));

		QueryParser parser = new QueryParser(Version.LUCENE_30, "StemmedData", new StandardAnalyzer(Version.LUCENE_30));
		/*
		 * QueryParser parser = new MultiFieldQueryParser(Version.LUCENE_30, new
		 * String[] { "StemmedData" }, new
		 * SnowballAnalyzer(Version.LUCENE_30,"English"));
		 */
		// ####################################
		parser.setDefaultOperator(QueryParser.AND_OPERATOR);

		if (slopmode == 1) {
			// out.append("\n\n The query results are in a slop Mode value of "
			// + slopvalue + "\n\n");
			parser.setPhraseSlop(slopvalue);
		}
		// For General Style *******************************

		Query query = parser.parse(SearchigInStemmedData);

		// CombineBoolenQuery combjobField = new CombineBoolenQuery();
		// combjobField.combinequerys(SearchigInStemmedData, 5);

		// out.append("\n \n" + "================================\n" +
		// ft.format(dNow)
		// + " \n The query we got finally for SearchigInStemmedData is : "
		// + query.toString("SearchigInStemmedData") +
		// "\n================================\n\n");
		query.toString();
		long start = System.currentTimeMillis();

		// TopDocs matches = JobInfosearcher.search(query, 2); // Executed the
		// job
		// match query

		TopDocs matches = JobInfosearcher.search(query, numOfTotalDocsInIndex);
		long end = System.currentTimeMillis();

		// System.err.println(query.toString("SearchigInStemmedData"));

		// System.err.println("Found " + matches.totalHits + " document(s) (in "
		// + (end - start)
		// + " milliseconds) that matched query '" + " SearchigInStemmedData : "
		// + SearchigInStemmedData + "':");

		// System.out.println(SearchigInStemmedData);

		// searched Results will be stored in these Array lists objects
		// *******************

		for (ScoreDoc scoreDoc : matches.scoreDocs) {
			Document doc = JobInfosearcher.doc(scoreDoc.doc);
			// out.append("\n" + ft.format(dNow) + " "
			// + " \n \n ========================================== NEW JOB
			// Results Started ==============================================");
			// out.append("\n \n" + ft.format(dNow) + " " + " \n jobId : " +
			// doc.get("autoinccol"));
			// // out.append("\n" + " " + " \n SearchigInStemmedData : " +
			// doc.get("StemmedData"));
			int autocolRecord = Math.round(Float.valueOf(doc.get("autoinccol")));

			pushToHashMap(autocolRecord, OriginalWord);

		}

	}

	public static void pushToHashMap(int autocolRecord, String OriginalWord) throws SQLException {

		// Check if the autocol is already in the HashMap
		if (HashToStoreKeywordAsDbRecords.containsKey(autocolRecord)) {

			String keywords = HashToStoreKeywordAsDbRecords.get(autocolRecord);
			keywords = keywords + "," + OriginalWord;

			HashToStoreKeywordAsDbRecords.put(autocolRecord, keywords);
		} else {

			HashToStoreKeywordAsDbRecords.put(autocolRecord, OriginalWord);

		}
	}

	private static BufferedWriter callfilewriter(URL resultsloc) throws IOException {
		// TODO Auto-generated method stub
		System.out.println(
				" The write location is : " + resultsloc.toString().replaceAll("%20", " ").replaceFirst("file:/", ""));
		File file = new File(resultsloc.toString().replaceAll("%20", " ").replaceFirst("file:/", ""));
		if (!file.exists()) {
			file.createNewFile();
		}
		// Here true is to append the content to file
		FileWriter fw = new FileWriter(file, true);
		// BufferedWriter writer give better performance
		BufferedWriter bw = new BufferedWriter(fw);
		return bw;

	}

	public static IndexSearcher IndexSearch(URL indexDir) throws IOException {
		System.out.println("index is locateed at directory"
				+ indexDir.toString().replaceAll("%20", " ").replaceFirst("file:/", ""));
		dir = FSDirectory.open(new File(indexDir.toString().replaceAll("%20", " ").replaceFirst("file:/", "")));
		IndexSearcher searcher = new IndexSearcher(dir);
		System.out.println("Yes Paths Working.");
		return searcher;
	}

	public static void ProcessEachWikiKeyWordsFIle(String csvFileName, WikiKeyWordsExtractionUsingLucene wikikey)
			throws ParseException, ClassNotFoundException, SQLException {
		String csvFile = csvFileName;
		String line = "";
		String cvsSplitBy = ",";

		try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {

			while ((line = br.readLine()) != null) {

				// use comma as separator
				String[] ProcessedWikiFileTuple = line.split(cvsSplitBy);

				// System.out.println("Stemed Word Being searchedd is = " +
				// ProcessedWikiFileTuple[1]);
				// Check if Hash is greatly filled already!! If So empty it by
				// pushing the data to DB
				if (HashToStoreKeywordAsDbRecords.size() >= 1000) {
					FlushTHeHashToDb();
					HashToStoreKeywordAsDbRecords.clear();
					System.out.println("Processing  Stem : " + ProcessedWikiFileTuple[1]+ "Processing word : " + ProcessedWikiFileTuple[0]);
				}
				searchForTheKeyword(ProcessedWikiFileTuple[1].replaceAll("\"|:|\\|/|<|>|!", " "),
						ProcessedWikiFileTuple[0]);

			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void FlushTHeHashToDb() throws SQLException {

		// Lets create the prepaed statements of the Hash we have and then push
		// all them at once
		String resultantKeywordsTopush = null;
		Iterator HashMapIterator = HashToStoreKeywordAsDbRecords.entrySet().iterator();
		while (HashMapIterator.hasNext()) {
			HashMap.Entry pair = (HashMap.Entry) HashMapIterator.next();

			// get from record from the Database and insert into hash
			preparedStmt.clearParameters();
			preparedStmt.setInt(1, (int) pair.getKey());
			preparedStmt.addBatch();
			ResultSet rs = (ResultSet) preparedStmt.executeQuery();

			preparedStmt.clearBatch();

			while (rs.next()) {

				String existingKeywordsFromDb = rs.getString("keywords");
				if (existingKeywordsFromDb == null) {
					existingKeywordsFromDb = "NA";
				}
				resultantKeywordsTopush = HashToStoreKeywordAsDbRecords.get(pair.getKey()) + ","
						+ existingKeywordsFromDb;

			}

			preparedStmtForUpdate.clearParameters();
			preparedStmtForUpdate.setString(1, resultantKeywordsTopush);
			preparedStmtForUpdate.setInt(2, (int) pair.getKey());
			preparedStmtForUpdate.addBatch();
		}

		// Once you prepared the Batch, now send it to DB

		preparedStmtForUpdate.executeBatch();
		preparedStmtForUpdate.clearBatch();
		conn.commit();
		// System.out.print("SuccessFully Flushed the changes to the DB");

	}

	public static void main(String[] args) throws SQLException, ClassNotFoundException, IOException, ParseException {
		// TODO Auto-generated method stub
		conn = ConnectDB();
		HashToStoreKeywordAsDbRecords = new HashMap<Integer, String>();
		WikiKeyWordsExtractionUsingLucene wikikey = new WikiKeyWordsExtractionUsingLucene();
		wikikey.JobDataSearchingForKeyWords();
		String WikiFilesLocation = "D:\\sem2\\ir-project\\KeywordExtraction\\WikiKeyWords\\stemmer_snowball\\short\\";
		File dir = new File(WikiFilesLocation);
		File[] directoryListing = dir.listFiles();
		if (directoryListing != null) {
			for (File eachfile : directoryListing) {
				// Do something with child
				if (eachfile.isFile()) {
					System.out.println("Processing the File : " + WikiFilesLocation + eachfile.getName());

					ProcessEachWikiKeyWordsFIle(WikiFilesLocation + eachfile.getName(), wikikey);
				}

			}
		} else {
			System.out.println(
					"The path you provided doenst contain data or its not a valid drectory to check for the WIki keywords");
		}
		FlushTHeHashToDb();
		HashToStoreKeywordAsDbRecords.clear();
		JobInfosearcher.close();
		// dir.
		// out.append("\n" + ft.format(dNow) + " " + "\n ===============
		// Finished writing =================");
		// out.close();

	}

	public static Connection ConnectDB() throws ClassNotFoundException, SQLException {
		String myDriver = "org.gjt.mm.mysql.Driver";
		Connection conn;
		String userName = "root";
		String password = "root";
		String myUrl = "jdbc:mysql://localhost:3306/neoacess";

		// MYSQL
		Class.forName(myDriver);
		conn = (Connection) DriverManager.getConnection(myUrl, userName, password);
		conn.setAutoCommit(false);

		setUpdateOption = "SET SQL_SAFE_UPDATES = 0";
		SetProcessed = "UPDATE neoacess.jobdata USE INDEX (jobDataOnAutoCol) SET processed =0";
		selectQuery = "SELECT  keywords FROM neoacess.jobdata USE INDEX (jobDataOnAutoCol) where autoinccol=?";
		updateQuery = "update neoacess.jobdata USE INDEX (jobDataOnAutoCol) set keywords = ? where autoinccol=?";

		preparedStmtUpdtOptn = (PreparedStatement) conn.prepareStatement(setUpdateOption);
		preparedStmtProcessed = (PreparedStatement) conn.prepareStatement(SetProcessed);
		preparedStmtUpdtOptn.execute();
		preparedStmtProcessed.execute();
		preparedStmt = (PreparedStatement) conn.prepareStatement(selectQuery);
		preparedStmtForUpdate = (PreparedStatement) conn.prepareStatement(updateQuery);

		// rs = (ResultSet) preparedStmt.executeQuery();
		return conn;
	}

}

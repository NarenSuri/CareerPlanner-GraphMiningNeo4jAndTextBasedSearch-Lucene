package jsf.career.planner;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

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

public class JobDataSearching extends SearchMoocInfo {

	public List<JobInfoDisplayTemplate> jobsresultlist;
	public List<List<MoocInfoDisplayTemplate>> Superfinalmoocsresultlist;
	public List<MoocInfoDisplayTemplate> concatmoocsresultlist;

	public List<UnivInfoDisplayTemplate> concatunivresultlist;
	public List<UnivInfoDisplayTemplate> univresultlist;
	public static int shortdescsize = 10;

	public static Date dNow = new Date();
	public static SimpleDateFormat ft = new SimpleDateFormat("E yyyy.MM.dd 'at' hh:mm:ss a zzz");
	// public static void main(String[] args) throws IOException, ParseException
	// {
	// TODO Auto-generated method stub

	public void jobTitleSearch(String jobindexlocation, String moocindexlocation, String univindexlocation,
			String searchquery) throws IOException, ParseException {

		// java.util.logging.config.file="D:\keplereclipse\keplerws\NFCInvoicingProject\WebContent\WEB-INF\logging.properties";

		// URL resultsloc = getClass().getClassLoader()
		// .getResource("//projectResultFiles//results-" +
		// System.currentTimeMillis() + ".txt");
		//

		// ***************************************************************************
		int indexmode = 0; // mode 0 tomcat index ; mode 1 general ; mode 2
							// boosted index
		int slopmode = 1; // slop mode 0 = no slop set, mode =1 is slop set,
							// this works only with the phrase query
		int slopvalue = 5;
		int modeKeyWordsInQueryBooster = 1; // default 1 boosts keywords while
											// searching. // 0 is no
											// keywordsinQuery boost
		float QryKywrdBostFactr = 5.0F; // the boost factor to be applied to
										// keywords got from the job obtained

		// ***************************************************************************
		String resultspath;
		resultspath = "projectResultFiles\\results\\results-indx(" + indexmode + ")slop(" + slopmode + ")-jbkywrdbst("
				+ modeKeyWordsInQueryBooster + ")-qrKyBst(" + (int) QryKywrdBostFactr + ")-"
				+ searchquery.replaceAll("\"|:|\\|/|<|>", "-Phrase-") + "-" + System.currentTimeMillis() + ".txt";
		URL resultsloc;
		resultsloc = getClass().getClassLoader().getResource("//projectResultFiles//results.txt");
		if (resultsloc == null) {
			resultsloc = new URL("file:/D:\\sem2\\ir-project\\lucene\\" + resultspath);
		}

		BufferedWriter out = callfilewriter(resultsloc);
		URL jobindexloc = null;
		URL moocindexloc = null;
		URL univindexloc = null;

		if (indexmode == 0) {
			jobindexloc = getClass().getClassLoader().getResource(jobindexlocation);
			moocindexloc = getClass().getClassLoader().getResource(moocindexlocation);
			univindexloc = getClass().getClassLoader().getResource(univindexlocation);
			out.write("\n"
					+ "  ==================================== \n These results are from Field General Index Of Java Resources \n ============================ \n\n ");

		}

		else if (indexmode == 2) {

			jobindexloc = new URL("file:/D:\\sem2\\ir-project\\lucene\\LuceneCareerAdviseIndexingBoosted\\JobInfo");

			moocindexloc = new URL(
					"file:/D:\\sem2\\ir-project\\lucene\\LuceneCareerAdviseIndexingBoosted\\MoocCourseInfo");

			univindexloc = new URL(
					"file:/D:\\sem2\\ir-project\\lucene\\LuceneCareerAdviseIndexingBoosted\\ResidentialCoursesInfo");
			out.write("\n"
					+ "  ==================================== \n\n These results are from Field Boost Index \n ============================ \n\n ");
		} else {
			if (jobindexloc == null) {
				jobindexloc = new URL("file:/D:\\sem2\\ir-project\\lucene\\LuceneCareerAdviseIndexing\\JobInfo");
			}
			if (moocindexloc == null) {
				moocindexloc = new URL(
						"file:/D:\\sem2\\ir-project\\lucene\\LuceneCareerAdviseIndexing\\MoocCourseInfo");
			}
			if (univindexloc == null) {
				univindexloc = new URL(
						"file:/D:\\sem2\\ir-project\\lucene\\LuceneCareerAdviseIndexing\\ResidentialCoursesInfo");
			}
			out.write("\n"
					+ "  ==================================== \n\n These results are from Field General Index \n ============================ \n\n ");

		}

		System.out.println(getClass().getClassLoader().getResource("logging.properties"));

		Logger log = Logger.getLogger("FromJobDataSearch");
		out.write("\n" + "   " + resultspath);
		out.append("\n" + ft.format(dNow) + "   " + "FromJobDataSearch \n ");

		System.out.print("Please enter the Job Title you want to land in to. Also please watch your spellings");

		log.info("Please enter the Job Title you want to land in to. Also please watch your spellings");
		System.out.println("Are Pths Working 2 ?");
		// Scanner scannerjobTitle = new Scanner(System.in);
		// String jobTitle = scannerjobTitle.nextLine();
		String jobTitle = searchquery;
		out.append("\n" + System.currentTimeMillis() + "   " + " \n Your interested jobTitle is " + jobTitle);
		// **************** All Indexers created here
		// *****************************
		IndexSearcher JobInfosearcher = IndexSearch(jobindexloc);
		IndexSearcher MoocInfosearcher = IndexSearch(moocindexloc);
		IndexSearcher UnivInfosearcher = IndexSearch(univindexloc);
		// ========================================================================

		// **************** Query Parsers for all the indexes are created here
		// *********************
		// QueryParser parser = new QueryParser(Version.LUCENE_30, "jobtitle",
		// new StandardAnalyzer(Version.LUCENE_30));
		// Query query = parser.parse(jobTitle);

		QueryParser parser = new MultiFieldQueryParser(Version.LUCENE_30,
				new String[] { "jobtitle", "jobFunctions", "keyWords", "jobDescription" },
				new StandardAnalyzer(Version.LUCENE_30));
		// parser.setDefaultOperator(QueryParser.AND_OPERATOR);

		if (slopmode == 1) {
			out.append("\n\n The query results are in a slop Mode value of " + slopvalue + "\n\n");
			parser.setPhraseSlop(slopvalue);
		}
		// For General Style *******************************

		Query query = parser.parse(jobTitle);

		CombineBoolenQuery combjobField = new CombineBoolenQuery();
		combjobField.combinequerys(jobTitle, 5);

		out.append("\n \n" + "================================\n" + ft.format(dNow)
				+ " \n The query we got finally for jobtitle is : " + query.toString("jobtitle")
				+ "\n================================\n\n");
query.toString();
		QueryParser moocparser = new MultiFieldQueryParser(Version.LUCENE_30,
				new String[] { "CourseDesc", "CourseName", "CourseDetails", "AboutCourse", "CourseSyllabus",
						"CourseFormat", "CourseBackground", "CourseReading", "keywords" },
				new StandardAnalyzer(Version.LUCENE_30));

		QueryParser univparser = new MultiFieldQueryParser(Version.LUCENE_30,
				new String[] { "courseTitle", "courseDescription", "keywords" },
				new StandardAnalyzer(Version.LUCENE_30));

		// ========================================================================================================

		// ****************** Objects of the MoocSearch Class and Univ Search
		// Class are created here ****************

		// Those these objects created i haven't used them, because i used the
		// static methods of their corresponding classes, but in the next
		// versions better to change them to complete object oriented with
		// non-static
		@SuppressWarnings("unused")
		SearchMoocInfo searchmooc = new SearchMoocInfo();
		UnivCourseInfoSearcher searchunivInfo = new UnivCourseInfoSearcher();
		// =========================================================================================================

		// ********************* Start of job matches searching with the
		// queryparser and the indexer created above******************
		// This phase also includes calling the UnivSearcher and the
		// MoocSearcher

		long start = System.currentTimeMillis();

		// TopDocs matches = JobInfosearcher.search(query, 2); // Executed the
		// job
		// match query

		TopDocs matches = JobInfosearcher.search(query, 10);
		long end = System.currentTimeMillis();
		
		Directory dir = FSDirectory.open(new File("D:\\sem2\\ir-project\\lucene\\LuceneCareerAdviseIndexingBoosted\\JobInfo"));
		IndexReader reader = IndexReader.open(dir);
		//IndexReader ireader = DirectoryReader.open(dir);

		// System.err.println(query.toString("jobtitle"));
		System.err.println("Found " + matches.totalHits + " document(s) (in " + (end - start)
				+ " milliseconds) that matched query '" + " jobtitle : " + jobTitle + "':");


		for (ScoreDoc scoreDoc : matches.scoreDocs) {
			Document doc = JobInfosearcher.doc(scoreDoc.doc);


		//	Explanation explanation = JobInfosearcher.explain(query, scoreDoc.doc); // Note Time Consuming

			// out.append("\n"+ ft.format(dNow)+" "+" \n Job Match : " +
			// Integer.toString(matchedjobindexcounter));
			out.append("\n" + ft.format(dNow) + "   "
					+ " \n \n ========================================== NEW JOB Results Started ==============================================");
			out.append("\n \n" + ft.format(dNow) + "   " + " \n jobId  :  " + doc.get("jobId"));
			//out.append("\n" + "   " + " \n The Score  :  " + explanation.getValue());
			out.append("\n" + "   " + " \n jobtitle  :  " + doc.get("jobtitle"));

			out.append("\n" + "   " + " \n jobDescription : " + doc.get("jobDescription"));
			out.append("\n" + "   " + " \n companyName : " + doc.get("companyName"));
			out.append("\n" + "   " + "\n  Keywords  :  " + doc.get("keywords"));

			// out.append("\n"+ ft.format(dNow)+" "+explanation.toString());
			// **************************** Calling the Mooc Search
			// functionality ************************
		}
		JobInfosearcher.close();
		dir.close();

		System.out.print("Superfinalmoocsresultlist");
		System.out.print(Superfinalmoocsresultlist);
		out.append("\n" + ft.format(dNow) + "   " + " \n Iterating over the super final list we created");


		System.out.println("\n =============== Finished writing =================");
		out.append("\n" + ft.format(dNow) + "   " + "\n =============== Finished writing =================");
		out.close();
	}

	private BufferedWriter callfilewriter(URL resultsloc) throws IOException {
		// TODO Auto-generated method stub

		// File file = new File(
		// "D:\\sem2\\ir-project\\lucene\\projectResultFiles\\resultsfile-" +
		// System.currentTimeMillis() + ".txt");

		// .replaceAll(".txt", "-" + System.currentTimeMillis() + ".txt")
		System.out.println(
				" The write location is : " + resultsloc.toString().replaceAll("%20", " ").replaceFirst("file:/", ""));
		File file = new File(resultsloc.toString().replaceAll("%20", " ").replaceFirst("file:/", ""));

		/*
		 * This logic is to create the file if the file is not already present
		 */
		if (!file.exists()) {
			file.createNewFile();
		}

		// Here true is to append the content to file
		FileWriter fw = new FileWriter(file, true);
		// BufferedWriter writer give better performance
		BufferedWriter bw = new BufferedWriter(fw);
		return bw;

	}

	
	public List<List<MoocInfoDisplayTemplate>> getSuperfinalmoocsresultlist() {
		return this.Superfinalmoocsresultlist;
	}

	public List<UnivInfoDisplayTemplate> getconcatuniv() {
		return this.concatunivresultlist;
	}

	public List<MoocInfoDisplayTemplate> getconcatmooc() {
		return this.concatmoocsresultlist;
	}

	public List<JobInfoDisplayTemplate> getjobresult() {
		return this.jobsresultlist;
	}

	static Directory dir;

	public static IndexSearcher IndexSearch(URL indexDir) throws IOException {
		System.out.println("index is locateed at directory"
				+ indexDir.toString().replaceAll("%20", " ").replaceFirst("file:/", ""));
		dir = FSDirectory.open(new File(indexDir.toString().replaceAll("%20", " ").replaceFirst("file:/", "")));
		IndexSearcher searcher = new IndexSearcher(dir);
		System.out.println("Yes Paths Working.");
		return searcher;
	}

}

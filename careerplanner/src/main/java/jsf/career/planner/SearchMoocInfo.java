package jsf.career.planner;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Explanation;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.apache.lucene.analysis.TokenStream;

public class SearchMoocInfo {

	public static List<MoocInfoDisplayTemplate> moocsresultlist;
	public static Date dNow = new Date();
	public static SimpleDateFormat ft = new SimpleDateFormat("E yyyy.MM.dd 'at' hh:mm:ss a zzz");
	public static int shortdescsize = 10;

	public static List<MoocInfoDisplayTemplate> searchMoocInfo(IndexSearcher MoocInfosearcher, QueryParser moocparser,
			int modeKeyWordsInQueryBooster, float qryKywrdBostFactr, String jobDescription, String jobtitleresult,
			String jobkeywordsresult, String userquery, int matchedjobTracker, BufferedWriter out)
			throws ParseException, IOException {

		String jd = stringTokenization(jobDescription, 1.0F);
		String jbTitl = stringTokenization(jobtitleresult, 1.0F);
		String jbkeywrd = "";
		if (modeKeyWordsInQueryBooster == 1) {
			if (jobkeywordsresult == null) {
				jbkeywrd = stringTokenization(jobtitleresult + " " + userquery, qryKywrdBostFactr);
			} else {
				jbkeywrd = stringTokenization(jobkeywordsresult, qryKywrdBostFactr);
			}
		} else {
			if (jobkeywordsresult == null) {
				jbkeywrd = stringTokenization(jobtitleresult + " " + userquery, 1.0F);
			} else {
				jbkeywrd = stringTokenization(jobkeywordsresult, 1.0F);
			}
		}

		String FinalQueryPrepared = jd + " " + jbTitl + " " + jbkeywrd + " " + userquery;
		out.append("\n\n===========================\n" + ft.format(dNow) + "   "
				+ "\n terms used  for Quering Mooc : \n " + FinalQueryPrepared + "\n==================== \n\n");
		Query moocquery = moocparser.parse(FinalQueryPrepared);
		// out.append("\n\n===========================\n" + ft.format(dNow) + "
		// " + "\n Quering Mooc with : \n " + moocquery.toString("jobtitle") +
		// "\n==================== \n\n");
		TopDocs matches = null;
		long start = System.currentTimeMillis();

		try {
			matches = MoocInfosearcher.search(moocquery, 20);
			long end = System.currentTimeMillis();

			// System.err.println("Query Searching For : " +
			// moocquery.toString(Description));
			// out.append("\n"+ ft.format(dNow)+" "+"Query Searching For : " +
			// moocquery.toString(Description));

			System.err.println(
					"Found Mooc Courses" + matches.totalHits + " document(s) (in " + (end - start) + " milliseconds) ");
			// "CourseDesc",
			// "CourseName","CourseDetails","AboutCourse","CourseSyllabus","CourseBackground","CourseReading"
			moocsresultlist = new ArrayList<MoocInfoDisplayTemplate>();
			int NumberOfmatchedMoocIdex = 0;
			for (ScoreDoc scoreDoc : matches.scoreDocs) {
				Document doc = MoocInfosearcher.doc(scoreDoc.doc);
				NumberOfmatchedMoocIdex++;
				// Explanation explanation = MoocInfosearcher.explain(moocquery, scoreDoc.doc);  // Note Time Consuming
				out.append("\n" + "   " + " \n Mooc Match  :  " + Integer.toString(NumberOfmatchedMoocIdex));
				out.append("\n" + "   " + " \n CourseName  :  " + doc.get("CourseName"));
				//out.append("\n" + "   " + " \n The Score  :  " + explanation.getValue());
				out.append("\n" + "   " + " \n keywords  :  " + doc.get("keywords"));
				out.append("\n" + "   " + " \n CourseDesc  :  " + doc.get("CourseDesc"));
				out.append("\n" + "   " + " \n CourseSyllabus  :  " + doc.get("CourseSyllabus"));

				if (doc.get("CourseDesc").length() < 350) {
					shortdescsize = doc.get("CourseDesc").length();
				} else {
					shortdescsize = 350;
				}
				moocsresultlist.add(new MoocInfoDisplayTemplate(matchedjobTracker,
						Integer.toString(NumberOfmatchedMoocIdex), doc.get("CourseName"), doc.get("CourseDesc"),
						doc.get("CourseSyllabus"), new String(doc.get("CourseDesc")).substring(0, shortdescsize)));

				out.append("\n" + ft.format(dNow) + "   " + " \n =============================");

			}
		} catch (Exception e) {
			System.out.println(e);
		}

		return moocsresultlist;
	}

	public static String stringTokenization(String text, float boost) throws IOException {
		Analyzer Textanalyzer = new StandardAnalyzer(Version.LUCENE_30);
		StringReader readerJobDesc = new StringReader(text);
		String Description = "";

		TokenStream tokenTermsJobDesc = null;
		tokenTermsJobDesc = Textanalyzer.tokenStream("field", readerJobDesc);
		TermAttribute term = tokenTermsJobDesc.addAttribute(TermAttribute.class);
		while (tokenTermsJobDesc.incrementToken()) {
			Description = Description + " " + term.term().toString() + "^" + boost;
		}
		Textanalyzer.close();
		return Description;
	}
}

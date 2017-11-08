package jsf.career.planner;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
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
import org.apache.lucene.util.Version;

public class UnivCourseInfoSearcher {

	public static List<UnivInfoDisplayTemplate> univsearchresultlist;
	public static Date dNow = new Date();
	public static SimpleDateFormat ft = new SimpleDateFormat("E yyyy.MM.dd 'at' hh:mm:ss a zzz");
	public static int shortdescsize = 10;
	
	public static List<UnivInfoDisplayTemplate> searchUnivCoursInfo(IndexSearcher UnivInfosearcher,
			QueryParser univparser, int modeKeyWordsInQueryBooster, float qryKywrdBostFactr, String jobDescription,
			String jobtitleresult, String jobkeywordsresult, String userquery, int matchedjobTracker,
			BufferedWriter out) throws ParseException, IOException {

		String jd = stringTokenization(jobDescription, 1.0F);
		String jbTitl = stringTokenization(jobtitleresult, 1.0F);
		String jbkeywrd = "";
		if (modeKeyWordsInQueryBooster == 1) {
			if (jobkeywordsresult == null) {
				jbkeywrd = stringTokenization(jobtitleresult + " " + userquery, qryKywrdBostFactr);
			} else {
				jbkeywrd = stringTokenization(jobkeywordsresult, qryKywrdBostFactr);
			}
		}

		String FinalQueryPrepared = jd + " " + jbTitl + " " + jbkeywrd + " " + userquery;

		// processed tokens => description
		System.out.println(FinalQueryPrepared);
		// creating ground for the UnivSearch
		Query univquery = univparser.parse(FinalQueryPrepared);
		// out.append("\n\n===========================\n" + ft.format(dNow) + "
		// "+ "\n terms used for Quering Univ : \n " + FinalQueryPrepared +
		// "\n==================== \n\n");

		long start = System.currentTimeMillis();
		try{
		TopDocs matches = UnivInfosearcher.search(univquery, 20);
		long end = System.currentTimeMillis();

		System.err.println(
				"Found IU Courses " + matches.totalHits + " document(s) (in " + (end - start) + " milliseconds)");

		// "CourseName","CourseDetails","AboutCourse","CourseSyllabus",

		univsearchresultlist = new ArrayList<UnivInfoDisplayTemplate>();

		int NumberOfmatchedunivIdex = 0;
		for (ScoreDoc scoreDoc : matches.scoreDocs) {
			Document doc = UnivInfosearcher.doc(scoreDoc.doc);
			NumberOfmatchedunivIdex++;

		//	Explanation explanation = UnivInfosearcher.explain(univquery, scoreDoc.doc);  // Note Time Consuming
			// out.append("\n"+ ft.format(dNow)+" "+explanation.toString());

			out.append("\n \n" + ft.format(dNow) + "   " + " \n Univ Match  :  "
					+ Integer.toString(NumberOfmatchedunivIdex));

			out.append("\n" + "   " + " \n UniversityName  :  " + doc.get("UniversityName"));
			//out.append("\n" + "   " + " \n The Score  :  " + explanation.getValue());
			out.append("\n" + "   " + " \n courseId  :  " + doc.get("courseId"));
			out.append("\n" + "   " + " \n courseTitle  :  " + doc.get("courseTitle"));
			out.append("\n" + "   " + " \n Keywords  :  " + doc.get("keywords"));
			out.append("\n" + "   " + " \n courseDesc  :  " + doc.get("courseDescription"));
			
			if(doc.get("courseDescription").length()<350){shortdescsize=doc.get("courseDescription").length();}
			else{shortdescsize=350;}
			
			univsearchresultlist.add(new UnivInfoDisplayTemplate(matchedjobTracker,
					Integer.toString(NumberOfmatchedunivIdex), doc.get("UniversityName"), doc.get("department"),
					doc.get("courseId"), doc.get("courseDescription"), doc.get("courseTitle"), doc.get("creditHours"),new String(doc.get("courseDescription")).substring(0,shortdescsize )));

			out.append("\n" + ft.format(dNow) + "   " + " \n =============================");

		}}
	catch(Exception e){}

		return univsearchresultlist;
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

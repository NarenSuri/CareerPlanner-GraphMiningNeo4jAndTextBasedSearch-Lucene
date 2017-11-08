package jsf.career.planner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.lucene.queryParser.ParseException;

import jsf.career.planner.*;

public class StarterSearch extends JobDataSearching {

	public static void main(String[] args) throws IOException, ParseException {
		// TODO Auto-generated method stub
		search("data science");
		//search("\"finance modeling\"~5");
	}

	public static List<JobInfoDisplayTemplate> finaljobsresultlist;
	public static List<List<MoocInfoDisplayTemplate>> finalmoocresult;

	public static void search(String query) throws IOException, ParseException {
		
		
		JobDataSearching searchcareeradvise = new JobDataSearching();

		// Locations of all the indexers located on the system

		// searchcareeradvise.jobTitleSearch("\\LuceneCareerAdviseIndexing\\JobInfo",
		// "\\LuceneCareerAdviseIndexing\\MoocCourseInfo",
		// "\\LuceneCareerAdviseIndexing\\ResidentialCoursesInfo", query);

		searchcareeradvise.jobTitleSearch("\\LuceneCareerAdviseIndexing\\JobInfo",
				"\\LuceneCareerAdviseIndexing\\MoocCourseInfo", "\\LuceneCareerAdviseIndexing\\ResidentialCoursesInfo",
				query);

		finaljobsresultlist = searchcareeradvise.getjobresult();
		// finalmoocresult = searchcareeradvise.getSuperfinalmoocsresultlist();

		System.out.println("printing from the search method of bean : Testing if all the objects working well");

	}

}

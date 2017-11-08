package jsf.career.planner;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.apache.lucene.queryParser.ParseException;

import jsf.career.planner.*;

@ManagedBean
@SessionScoped

public class PanelManagedBean implements Serializable {

	private static final long serialVersionUID = 5379157825097697378L;
	private String query;
	public List<JobInfoDisplayTemplate> finaljobsresultlist;
	public List<List<MoocInfoDisplayTemplate>> finalmoocresult;
	public List<MoocInfoDisplayTemplate> finalconcatmooc;
	public List<UnivInfoDisplayTemplate> finalconcatuniv;
	private JobInfoDisplayTemplate selectedjob;
	private JobInfoDisplayTemplate selectedjobforMoocUniv;
	private MoocInfoDisplayTemplate selectedMooc;
	private UnivInfoDisplayTemplate selectedUniv;
	private Connection conn;
	private Statement stmt;
	private boolean slopmode;
	private int coursesAllowed = 0;
	private int maxcoursesAllowed = 2;

	private List<String> jobFieldsToSearchOn = new ArrayList<String>();
	private List<String> selectedjobFieldsToSearchOn = new ArrayList<String>();

	private List<String> moocFieldsToSearchOn = new ArrayList<String>();
	private List<String> selectedmoocFieldsToSearchOn = new ArrayList<String>();

	private List<String> univFieldsToSearchOn = new ArrayList<String>();
	private List<String> selectedunivFieldsToSearchOn = new ArrayList<String>();
	private Integer progress;
	private Integer cancel;

	@PostConstruct
	public void init() {
		this.jobFieldsToSearchOn = new ArrayList<String>();
		this.jobFieldsToSearchOn.add("jobtitle");
		this.jobFieldsToSearchOn.add("jobFunctions");
		this.jobFieldsToSearchOn.add("keyWords");
		this.jobFieldsToSearchOn.add("jobDescription");

		this.moocFieldsToSearchOn = new ArrayList<String>();
		this.moocFieldsToSearchOn.add("CourseDesc");
		this.moocFieldsToSearchOn.add("CourseName");
		this.moocFieldsToSearchOn.add("CourseDetails");
		this.moocFieldsToSearchOn.add("AboutCourse");
		this.moocFieldsToSearchOn.add("CourseSyllabus");
		this.moocFieldsToSearchOn.add("CourseFormat");
		this.moocFieldsToSearchOn.add("CourseBackground");
		this.moocFieldsToSearchOn.add("CourseReading");
		this.moocFieldsToSearchOn.add("keywords");

		this.univFieldsToSearchOn.add("courseTitle");
		this.univFieldsToSearchOn.add("courseDescription");
		this.univFieldsToSearchOn.add("keywords");

	}

	public Integer getProgress() {
		if (progress == null) {
			progress = 0;
		} else if (progress < 75) {
			progress = progress + 10;
		}
		return progress;
	}

	public void setProgress(Integer progress) {
		this.progress = progress;
	}

	public void onComplete() {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Progress Completed"));
	}

	public List<String> getJobFieldsToSearchOn() {
		return jobFieldsToSearchOn;
	}

	public void setJobFieldsToSearchOn(List<String> jobFieldsToSearchOn) {
		this.jobFieldsToSearchOn = jobFieldsToSearchOn;
	}

	public List<String> getSelectedjobFieldsToSearchOn() {
		return selectedjobFieldsToSearchOn;
	}

	public void setSelectedjobFieldsToSearchOn(List<String> selectedjobFieldsToSearchOn) {
		this.selectedjobFieldsToSearchOn = selectedjobFieldsToSearchOn;
	}

	LuceneSearchUserFeedback relvFeedback = new LuceneSearchUserFeedback();

	public String submitRelevancy(String indexStr) throws SQLException {
		Integer index = Integer.parseInt(indexStr);
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage("You've submitted " + finaljobsresultlist.get(index - 1).getSelectedValue()));
		System.out.println("Value Select:" + ":" + finaljobsresultlist.get(index - 1).getSelectedValue());
		System.out.println("Query is:" + this.query);
		System.out.println("Selected JobId is:" + finaljobsresultlist.get(index - 1).jobId);
		System.out.println("Matched Job Index :" + finaljobsresultlist.get(index - 1).matchedjobindex);

		this.relvFeedback.insertResultMatchRelevancyFeedBak(this.query, finaljobsresultlist.get(index - 1).jobId,
				finaljobsresultlist.get(index - 1).getSelectedValue(),
				 Integer.toString(finaljobsresultlist.get(index - 1).matchedjobindex), this.stmt, "jobRelevancy");

		return "";
	}

	public String submitWillTakeItjobValue(String indexStr) throws SQLException {
		Integer index = Integer.parseInt(indexStr);
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage("You've submitted " + finaljobsresultlist.get(index - 1).getWillUtakejob()));

		System.out.println("Value Select:" + ":" + finaljobsresultlist.get(index - 1).getWillUtakejob());
		System.out.println("Query is:" + this.query);
		System.out.println("Selected JobId is:" + finaljobsresultlist.get(index - 1).jobId);
		System.out.println("Matched Job Index :" + finaljobsresultlist.get(index - 1).matchedjobindex);

		this.relvFeedback.insertResultWillYouTakeFeedBak(this.query, finaljobsresultlist.get(index - 1).jobId,
				finaljobsresultlist.get(index - 1).getWillUtakejob(),
				 Integer.toString(finaljobsresultlist.get(index - 1).matchedjobindex), this.stmt, "JobWillYouTake");

		return "";
	}

	public String submitMoocRelevancy(String indexStr) throws SQLException {

		Integer index = Integer.parseInt(indexStr);
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage("You've submitted " + finalconcatmooc.get(index - 1).getSelectedValue()));
		System.out.println("Value Select:" + ":" + finalconcatmooc.get(index - 1).getSelectedValue());
		System.out.println("Query is:" + this.query);
		System.out.println("Selected Mooc ID  is:" + finalconcatmooc.get(index - 1).coursmatchedforjob + "-"
				+ finalconcatmooc.get(index - 1).matchedindex);
		System.out.println("Matched Mooc Course :" + finalconcatmooc.get(index - 1).courseTitle);

		this.relvFeedback.insertResultMatchRelevancyFeedBak(this.query, finalconcatmooc.get(index - 1).courseTitle,
				finalconcatmooc.get(index - 1).getSelectedValue(), finalconcatmooc.get(index - 1).coursmatchedforjob
						+ "-" + finalconcatmooc.get(index - 1).matchedindex,
				this.stmt, "moocRelevancy");
		return "";

	}

	public String submitWillTakeItMoocValue(String indexStr) throws SQLException {

		Integer index = Integer.parseInt(indexStr);
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage("You've submitted " + finalconcatmooc.get(index - 1).getWillUtakejob()));

		System.out.println("Value Select:" + ":" + finalconcatmooc.get(index - 1).getWillUtakejob());
		System.out.println("Query is:" + this.query);
		System.out.println("Selected Mooc ID  is:" + finalconcatmooc.get(index - 1).coursmatchedforjob + "-"
				+ finalconcatmooc.get(index - 1).matchedindex);
		System.out.println("Matched Mooc Course :" + finalconcatmooc.get(index - 1).courseTitle);

		this.relvFeedback.insertResultWillYouTakeFeedBak(this.query, finalconcatmooc.get(index - 1).courseTitle,
				finalconcatmooc.get(index - 1).getWillUtakejob(), finalconcatmooc.get(index - 1).coursmatchedforjob + "-"
						+ finalconcatmooc.get(index - 1).matchedindex,
				this.stmt, "MoocWillYouTake");

		return "";
	}

	public String submitWillTakeItUnivValue(String indexStr) throws SQLException {

		Integer index = Integer.parseInt(indexStr);
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage("You've submitted " + finalconcatuniv.get(index - 1).getWillUtakejob()));

		System.out.println("Value Select Univ:" + ":" + finalconcatuniv.get(index - 1).getWillUtakejob());
		System.out.println("Query is:" + this.query);

		System.out.println("Selected Univ ID  is:" + finalconcatuniv.get(index - 1).coursmatchedforjob + "-"
				+ finalconcatuniv.get(index - 1).matchedindex);
		System.out.println("Matched Univ Course :" + finalconcatuniv.get(index - 1).courseId);

		this.relvFeedback.insertResultWillYouTakeFeedBak(this.query, finalconcatuniv.get(index - 1).courseId,
				finalconcatuniv.get(index - 1).getWillUtakejob(), finalconcatuniv.get(index - 1).coursmatchedforjob
						+ "-" + finalconcatuniv.get(index - 1).matchedindex,
				this.stmt, "UnivWillYouTake");

		return "";
	}

	public String submitUnivRelevancy(String indexStr) throws SQLException {

		Integer index = Integer.parseInt(indexStr);
		this.coursesAllowed = this.coursesAllowed+1;
		if(this.maxcoursesAllowed>=this.coursesAllowed){
		
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage("You've submitted " + finalconcatuniv.get(index - 1).getSelectedValue()));
		System.out.println("Value Select Univ:" + ":" + finalconcatuniv.get(index - 1).getSelectedValue());
		System.out.println("Query is:" + this.query);

		System.out.println("Selected Univ ID  is:" + finalconcatuniv.get(index - 1).coursmatchedforjob + "-"
				+ finalconcatuniv.get(index - 1).matchedindex);
		System.out.println("Matched Univ Course :" + finalconcatuniv.get(index - 1).courseTitle + " : "
				+ finalconcatuniv.get(index - 1).courseId);

		this.relvFeedback.insertResultMatchRelevancyFeedBak(this.query,
				finalconcatuniv.get(index - 1).courseId, finalconcatuniv.get(index - 1).getSelectedValue(),
				finalconcatuniv.get(index - 1).coursmatchedforjob + "-"
						+ finalconcatuniv.get(index - 1).matchedindex,
				this.stmt, "univRelevancy");}
		else{FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage("You've already taken max courses"));
		}
		return "";

	}

	public void setfinalconcatmooc(List<MoocInfoDisplayTemplate> concatmoocsresultlist2) {
		this.finalconcatmooc = concatmoocsresultlist2;
	}

	public void setfinalconcatuniv(List<UnivInfoDisplayTemplate> concatunivresultlist2) {
		// TODO Auto-generated method stub
		this.finalconcatuniv = concatunivresultlist2;
	}

	public List<UnivInfoDisplayTemplate> getfinalconcatuniv() {
		return this.finalconcatuniv;
	}

	public List<JobInfoDisplayTemplate> getfinaljobsresultlist() {
		// System.out.println("Checking the access of parameter NotAtAll " +
		// this.finaljobsresultlist.get(0).relevancy.notAtAll);
		return this.finaljobsresultlist;
	}

	public List<List<MoocInfoDisplayTemplate>> getfinalmoocrresult() {
		return this.finalmoocresult;
	}

	public List<MoocInfoDisplayTemplate> getfinalconcatmooc() {
		return this.finalconcatmooc;
	}

	public JobInfoDisplayTemplate getselectedjob() {
		return this.selectedjob;
	}

	public void setselectedjob(JobInfoDisplayTemplate selectedjob) {
		this.selectedjob = selectedjob;
		System.out.println(selectedjob);
		System.out.println(selectedjob.jobtitle);

	}

	public MoocInfoDisplayTemplate getselectedMooc() {
		return this.selectedMooc;
	}

	public void setselectedMooc(MoocInfoDisplayTemplate selectedMoo) {
		this.selectedMooc = selectedMoo;
		System.out.println(selectedMooc);
		System.out.println(selectedMooc.courseTitle);

	}

	public UnivInfoDisplayTemplate getselectedUniv() {
		return this.selectedUniv;
	}

	public void setselectedUniv(UnivInfoDisplayTemplate selectedUniv) {
		this.selectedUniv = selectedUniv;
		System.out.println(selectedUniv);
		System.out.println(selectedUniv.courseTitle);

	}

	public String getquery() {
		return query;
	}

	public void setquery(String query) {
		this.query = query;
	}

	public String search() throws IOException, ParseException, ClassNotFoundException, SQLException {
		JobDataSearching searchcareeradvise = new JobDataSearching();
		this.progress = 10;
		// Locations of all the indexers located on the system
		// searchcareeradvise.jobTitleSearch("D:\\sem2\\ir-project\\lucene\\LuceneCareerAdviseIndexing\\JobInfo",
		// "D:\\sem2\\ir-project\\lucene\\LuceneCareerAdviseIndexing\\MoocCourseInfo",
		// "D:\\sem2\\ir-project\\lucene\\LuceneCareerAdviseIndexing\\ResidentialCoursesInfo",
		// query);

		System.out.println("Slop mode selected :" + slopmode);
		String selectedJobFieldsList = "";
		for (String s : this.selectedjobFieldsToSearchOn) {
			selectedJobFieldsList = selectedJobFieldsList + s + ",";
		}
		System.out.println("Selected Job fields: " + selectedJobFieldsList);

		for (String s : this.selectedunivFieldsToSearchOn) {
			selectedJobFieldsList = selectedJobFieldsList + s + ",";
		}
		System.out.println("Selected Job fields: " + selectedJobFieldsList);

		for (String s : this.selectedmoocFieldsToSearchOn) {
			selectedJobFieldsList = selectedJobFieldsList + s + ",";
		}
		System.out.println("Selected Job fields: " + selectedJobFieldsList);

		// ==================== Important Stuff Starts
		// Here==============================

		searchcareeradvise.jobTitleSearch("\\LuceneCareerAdviseIndexing\\JobInfo",
				"\\LuceneCareerAdviseIndexing\\MoocCourseInfo", "\\LuceneCareerAdviseIndexing\\ResidentialCoursesInfo",
				query);
		this.progress = 30;
		finaljobsresultlist = searchcareeradvise.getjobresult();
		this.progress = 40;
		// finalmoocresult = searchcareeradvise.getSuperfinalmoocsresultlist();
		finalconcatmooc = searchcareeradvise.getconcatmooc();
		this.progress = 80;
		finalconcatuniv = searchcareeradvise.getconcatuniv();
		this.progress = 85;
		databaseConnection();
		this.progress = 90;
		System.out.println("printing from the search method of bean : Testing if all the objects working well");

		System.out.println(
				"printing from the search method of bean univ : " + finalconcatuniv.get(0).courseTitle);
		System.out.println("printing from the search method of bean mooc : " + finalconcatmooc.get(0).courseTitle);
		System.out.println("printing from the search method of bean job : " + finaljobsresultlist.get(0).jobId);
		this.progress = 100;

		// return "search?faces-redirect=true";
		return "jobresult?faces-redirect=true";
	}

	public void databaseConnection() throws ClassNotFoundException, SQLException {
		String myDriver = "org.gjt.mm.mysql.Driver";
		// Setting the configuration values
		String userName = "root";
		String password = "root";
		String myUrl = "jdbc:mysql://localhost:3306/LuceneSearchMatchFeedback";
		// MYSQL
		Class.forName(myDriver);
		this.setconn((Connection) DriverManager.getConnection(myUrl, userName, password));
		this.setstmt(this.conn.createStatement());
	}

	public Connection getconn() {
		return conn;
	}

	public void setconn(Connection conn) {
		this.conn = conn;
	}

	public Statement getstmt() {
		return stmt;
	}

	public void setstmt(Statement stmt) {
		this.stmt = stmt;
	}

	public boolean isSlopmode() {
		return slopmode;
	}

	public void setSlopmode(boolean slopmode) {
		this.slopmode = slopmode;
	}

	public List<String> getMoocFieldsToSearchOn() {
		return moocFieldsToSearchOn;
	}

	public void setMoocFieldsToSearchOn(List<String> moocFieldsToSearchOn) {
		this.moocFieldsToSearchOn = moocFieldsToSearchOn;
	}

	public List<String> getSelectedmoocFieldsToSearchOn() {
		return selectedmoocFieldsToSearchOn;
	}

	public void setSelectedmoocFieldsToSearchOn(List<String> selectedmoocFieldsToSearchOn) {
		this.selectedmoocFieldsToSearchOn = selectedmoocFieldsToSearchOn;
	}

	public List<String> getUnivFieldsToSearchOn() {
		return univFieldsToSearchOn;
	}

	public void setUnivFieldsToSearchOn(List<String> univFieldsToSearchOn) {
		this.univFieldsToSearchOn = univFieldsToSearchOn;
	}

	public List<String> getSelectedunivFieldsToSearchOn() {
		return selectedunivFieldsToSearchOn;
	}

	public void setSelectedunivFieldsToSearchOn(List<String> selectedunivFieldsToSearchOn) {
		this.selectedunivFieldsToSearchOn = selectedunivFieldsToSearchOn;
	}

	public Integer getCancel() {
		return cancel;
	}

	public void setCancel(Integer cancel) {
		progress = 0;
		this.cancel = cancel;
	}
	
	
	public String selectedjobforMoocUniv(JobInfoDisplayTemplate selectedjob) {
		this.selectedjob = selectedjob;
		System.out.println(selectedjob);
		System.out.println(selectedjob.jobtitle);
		return "moocunivresult?faces-redirect=true";

	}

	public JobInfoDisplayTemplate getSelectedjobforMoocUniv() {
		return selectedjobforMoocUniv;
	}

	public void setSelectedjobforMoocUniv(JobInfoDisplayTemplate selectedjobforMoocUniv) {
		this.selectedjobforMoocUniv = selectedjobforMoocUniv;
		System.out.println("Coming From the Selected Job For Mooc Univ");
		System.out.println(selectedjob);
		System.out.println(selectedjob.jobtitle);		
	}

	public int getCoursesAllowed() {
		return coursesAllowed;
	}

	public void setCoursesAllowed(int coursesAllowed) {
		this.coursesAllowed = coursesAllowed;
	}
	

}

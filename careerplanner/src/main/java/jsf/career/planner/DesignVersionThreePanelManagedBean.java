package jsf.career.planner;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.apache.lucene.queryParser.ParseException;

@ManagedBean
@SessionScoped

public class DesignVersionThreePanelManagedBean implements Serializable {

	private static final long serialVersionUID = 5379157825097697378L;
	private String query;
	public List<JobInfoDisplayTemplate> finaljobsresultlist;
	public List<List<MoocInfoDisplayTemplate>> finalmoocresult;
	public List<MoocInfoDisplayTemplate> finalconcatmooc;
	public List<UnivInfoDisplayTemplate> finalconcatuniv;
	private JobInfoDisplayTemplate selectedjob;
	private JobInfoDisplayTemplate selectedjobforMoocUniv;
	private ParentOfAllinfoDisplayTemplates selectedMoocUnivParentTemplate;
	private MoocInfoDisplayTemplate selectedMooc;
	private UnivInfoDisplayTemplate selectedUniv;
	private Connection conn;
	private Statement stmt;
	private boolean slopmode;
	private int coursesAllowed = 0;
	private int maxAllowedCoursesToChooseDefaultVal = 0;

	public int getMaxAllowedCoursesToChooseDefaultVal() {
		return maxAllowedCoursesToChooseDefaultVal;
	}

	public void setMaxAllowedCoursesToChooseDefaultVal(int maxAllowedCoursesToChooseDefaultVal) {
		this.maxAllowedCoursesToChooseDefaultVal = maxAllowedCoursesToChooseDefaultVal;
	}

	private int maxAllowedCoursesToChoose = 6; // effective now
	private int maxcoursesAllowed = 2; // dont use this one

	private List<String> jobFieldsToSearchOn = new ArrayList<String>();
	private List<String> selectedjobFieldsToSearchOn = new ArrayList<String>();

	private List<String> moocFieldsToSearchOn = new ArrayList<String>();
	private List<String> selectedmoocFieldsToSearchOn = new ArrayList<String>();

	private List<String> univFieldsToSearchOn = new ArrayList<String>();
	private List<String> selectedunivFieldsToSearchOn = new ArrayList<String>();
	private Integer progress;
	private Integer cancel;

	public LinkedHashMap<JobInfoDisplayTemplate, List<ParentOfAllinfoDisplayTemplates>> lhm = new LinkedHashMap<JobInfoDisplayTemplate, List<ParentOfAllinfoDisplayTemplates>>();

	public LinkedHashMap<JobInfoDisplayTemplate, List<MoocInfoDisplayTemplate>> lhmMooc = new LinkedHashMap<JobInfoDisplayTemplate, List<MoocInfoDisplayTemplate>>();
	public LinkedHashMap<JobInfoDisplayTemplate, List<UnivInfoDisplayTemplate>> lhmUniv = new LinkedHashMap<JobInfoDisplayTemplate, List<UnivInfoDisplayTemplate>>();

	public List<MoocInfoDisplayTemplate> MoocPg1Display = new ArrayList<MoocInfoDisplayTemplate>();
	public List<UnivInfoDisplayTemplate> UnivPg2Display = new ArrayList<UnivInfoDisplayTemplate>();

	private List<ParentOfAllinfoDisplayTemplates> rndmSndwichResltsofMoocUniv;
	private List<MoocInfoDisplayTemplate> page1MoocResults;
	private List<UnivInfoDisplayTemplate> page2UnivResults;

	private List<ParentOfAllinfoDisplayTemplates> selectAmongRelevantCourses;

	private List<ParentOfAllinfoDisplayTemplates> selectAmongRelevantCoursesNotAtAll;
	private List<ParentOfAllinfoDisplayTemplates> selectAmongRelevantCoursesJustFewWords;
	private List<ParentOfAllinfoDisplayTemplates> selectAmongRelevantCoursesModeratelyRelev;
	private List<ParentOfAllinfoDisplayTemplates> selectAmongRelevantCoursesMostlyRelev;
	private List<ParentOfAllinfoDisplayTemplates> selectAmongRelevantCoursesVeryRelev;

	private List<ParentOfAllinfoDisplayTemplates> finalResults;


	private int totalMoocChoosen;
	private int totalUnivChoosen;

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

	public void takeToPage2Results() throws IOException {
		ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
		context.redirect(context.getRequestContextPath() + "/designv4moocunivresultpage2.jsf");
	}

	public void finalResults() throws IOException {

		finalResults = new ArrayList<ParentOfAllinfoDisplayTemplates>();
		
		for (ParentOfAllinfoDisplayTemplates selectedCourses : selectAmongRelevantCourses) {

			if (selectedCourses instanceof UnivInfoDisplayTemplate) {
				if (((UnivInfoDisplayTemplate) selectedCourses).getWillUtakejob().equals("Yes")) {
					finalResults.add(selectedCourses);
				}

			}

			else {
				if (((MoocInfoDisplayTemplate) selectedCourses).getWillUtakejob().equals("Yes")) {
					finalResults.add(selectedCourses);
				}
			}

		}

		ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
		context.redirect(context.getRequestContextPath() + "/designv3FinalResuts.jsf");
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public List<ParentOfAllinfoDisplayTemplates> getFinalResults() {
		return finalResults;
	}

	public void setFinalResults(List<ParentOfAllinfoDisplayTemplates> finalResults) {
		this.finalResults = finalResults;
	}

	public void gotohomepage() throws IOException {
		ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
		context.redirect(context.getRequestContextPath() + "/designv3main.jsf");
	}

	public void loadchooseAmongRelevantCourses() throws IOException {
		System.out.println("Im in choosing relevant course option page");

		selectAmongRelevantCourses = new ArrayList<ParentOfAllinfoDisplayTemplates>();
		selectAmongRelevantCoursesNotAtAll = new ArrayList<ParentOfAllinfoDisplayTemplates>();
		selectAmongRelevantCoursesJustFewWords = new ArrayList<ParentOfAllinfoDisplayTemplates>();
		selectAmongRelevantCoursesModeratelyRelev = new ArrayList<ParentOfAllinfoDisplayTemplates>();
		selectAmongRelevantCoursesMostlyRelev = new ArrayList<ParentOfAllinfoDisplayTemplates>();
		selectAmongRelevantCoursesVeryRelev = new ArrayList<ParentOfAllinfoDisplayTemplates>();

		for (UnivInfoDisplayTemplate isUnivRelevancyRated : finalconcatuniv) {
			if (isUnivRelevancyRated.getSelectedValue().equals("none")
					| isUnivRelevancyRated.getSelectedValue().equals("Is This Relevant?")) {
			} else {
				if (isUnivRelevancyRated.getSelectedValue().equals("Notatall-1")) {
					selectAmongRelevantCoursesNotAtAll.add((ParentOfAllinfoDisplayTemplates) isUnivRelevancyRated);
				} else if (isUnivRelevancyRated.getSelectedValue().equals("JustFewWordsMatched-2")) {
					selectAmongRelevantCoursesJustFewWords.add((ParentOfAllinfoDisplayTemplates) isUnivRelevancyRated);
				} else if (isUnivRelevancyRated.getSelectedValue().equals("ModeratelyRelevant-3")) {
					selectAmongRelevantCoursesModeratelyRelev
							.add((ParentOfAllinfoDisplayTemplates) isUnivRelevancyRated);
				} else if (isUnivRelevancyRated.getSelectedValue().equals("MostlyRelevant-4")) {
					selectAmongRelevantCoursesMostlyRelev.add((ParentOfAllinfoDisplayTemplates) isUnivRelevancyRated);
				} else if (isUnivRelevancyRated.getSelectedValue().equals("VeryRelevant-5")) {
					selectAmongRelevantCoursesVeryRelev.add((ParentOfAllinfoDisplayTemplates) isUnivRelevancyRated);
				} else {
				}
				selectAmongRelevantCourses.add((ParentOfAllinfoDisplayTemplates) isUnivRelevancyRated);
			}
		}

		for (MoocInfoDisplayTemplate isMoocRelevancyRated : finalconcatmooc) {
			if (isMoocRelevancyRated.getSelectedValue().equals("none")
					| isMoocRelevancyRated.getSelectedValue().equals("Is This Relevant?")) {
			} else {

				if (isMoocRelevancyRated.getSelectedValue().equals("Notatall-1")) {
					selectAmongRelevantCoursesNotAtAll.add((ParentOfAllinfoDisplayTemplates) isMoocRelevancyRated);
				} else if (isMoocRelevancyRated.getSelectedValue().equals("JustFewWordsMatched-2")) {
					selectAmongRelevantCoursesJustFewWords.add((ParentOfAllinfoDisplayTemplates) isMoocRelevancyRated);
				} else if (isMoocRelevancyRated.getSelectedValue().equals("ModeratelyRelevant-3")) {
					selectAmongRelevantCoursesModeratelyRelev
							.add((ParentOfAllinfoDisplayTemplates) isMoocRelevancyRated);
				} else if (isMoocRelevancyRated.getSelectedValue().equals("MostlyRelevant-4")) {
					selectAmongRelevantCoursesMostlyRelev.add((ParentOfAllinfoDisplayTemplates) isMoocRelevancyRated);
				} else if (isMoocRelevancyRated.getSelectedValue().equals("VeryRelevant-5")) {
					selectAmongRelevantCoursesVeryRelev.add((ParentOfAllinfoDisplayTemplates) isMoocRelevancyRated);
				} else {
				}
				selectAmongRelevantCourses.add((ParentOfAllinfoDisplayTemplates) isMoocRelevancyRated);

			}
		}

		System.out.println("The moocs and Univs seleccted to choose among are:");
		System.out.println(selectAmongRelevantCourses);

		ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
		context.redirect(context.getRequestContextPath() + "/designv3chooseAmongRelevantCourses.jsf");
	}

	public void loadchoosenFinalCourses() throws IOException {
		System.out.println("Im in Displaying Final Results");

		ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
		context.redirect(context.getRequestContextPath() + "/designv3FinalResults.jsf");
	}

	// ======================================================================================================================
	public String submitSelectedAmongRelCourses(String indexStr, int coursmatchedforjob, String moocOrUnivType)
			throws SQLException, IOException {
		System.out.println("Entereed to process the selections of yes or no.. will u take it");
		Integer index = Integer.parseInt(indexStr);
		maxAllowedCoursesToChooseDefaultVal = 0;
		totalUnivChoosen = 0;
		totalMoocChoosen = 0;

		for (ParentOfAllinfoDisplayTemplates selectedCourses : selectAmongRelevantCourses) {

			if (selectedCourses instanceof MoocInfoDisplayTemplate) {

				if (((MoocInfoDisplayTemplate) selectedCourses).getWillUtakejob().equals("Yes")) {
					// ((MoocInfoDisplayTemplate)
					// selectedCourses).setisVisible(false);
					maxAllowedCoursesToChooseDefaultVal++; // This line should
															// be optimized, it
															// calculates for
															// every selection
															// change in the UI
					totalMoocChoosen++;

					System.out.print("");
				}
			}

			else {
				if (((UnivInfoDisplayTemplate) selectedCourses).getWillUtakejob().equals("Yes")) {

					// ((UnivInfoDisplayTemplate)
					// selectedCourses).setisVisible(false);
					maxAllowedCoursesToChooseDefaultVal++; // This line should
															// be optimized, it
															// calculates for
															// every selection
															// change in the UI
					totalUnivChoosen++;

					System.out.print("");
				}
			}

		}

		if (moocOrUnivType.equals("Mooc")) {

			MoocInfoDisplayTemplate displygrowlmooc = null;

			for (MoocInfoDisplayTemplate tempMoocObj : finalconcatmooc) {
				if (tempMoocObj.getMatchedindex() == index
						&& tempMoocObj.getCoursmatchedforjob() == coursmatchedforjob) {
					displygrowlmooc = tempMoocObj;
				}
			}

			if (maxAllowedCoursesToChooseDefaultVal <= maxAllowedCoursesToChoose) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage("You've submitted " + displygrowlmooc.getWillUtakejob()));

				System.out.println("Value Select:" + ":" + displygrowlmooc.getWillUtakejob());
				System.out.println("Query is:" + this.query);
				System.out.println("Selected Mooc ID  is:" + displygrowlmooc.coursmatchedforjob + "-"
						+ displygrowlmooc.matchedindex);
				System.out.println("Matched Mooc Course :" + displygrowlmooc.courseTitle);

				this.relvFeedback.insertResultWillYouTakeFeedBak(this.query, displygrowlmooc.courseTitle,
						displygrowlmooc.getWillUtakejob(),
						displygrowlmooc.coursmatchedforjob + "-" + displygrowlmooc.matchedindex, this.stmt,
						"MoocWillYouTake");
			} else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("You are supposed to Choose only "
						+ maxAllowedCoursesToChoose + " Please reset some to continue"));
			}
		} else {

			UnivInfoDisplayTemplate displygrowluniv = null;

			for (UnivInfoDisplayTemplate tempUnivObj : finalconcatuniv) {
				if (tempUnivObj.getMatchedindex() == index
						&& tempUnivObj.getCoursmatchedforjob() == coursmatchedforjob) {
					displygrowluniv = tempUnivObj;
				}
			}

			if (maxAllowedCoursesToChooseDefaultVal <= maxAllowedCoursesToChoose) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage("You've submitted " + displygrowluniv.getWillUtakejob()));

				System.out.println("Value Select Univ:" + ":" + displygrowluniv.getWillUtakejob());
				System.out.println("Query is:" + this.query);

				System.out.println("Selected Univ ID  is:" + displygrowluniv.coursmatchedforjob + "-"
						+ displygrowluniv.matchedindex);
				System.out.println("Matched Univ Course :" + displygrowluniv.courseId);

				this.relvFeedback.insertResultWillYouTakeFeedBak(this.query, displygrowluniv.courseId,
						displygrowluniv.getWillUtakejob(),
						displygrowluniv.coursmatchedforjob + "-" + displygrowluniv.matchedindex, this.stmt,
						"UnivWillYouTake");

			} else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("You are supposed to Choose only "
						+ maxAllowedCoursesToChoose + " Please reset some to continue"));
			}
		}

		if (maxAllowedCoursesToChooseDefaultVal == maxAllowedCoursesToChoose) {

			for (ParentOfAllinfoDisplayTemplates selectedCourses : selectAmongRelevantCourses) {

				if (selectedCourses instanceof MoocInfoDisplayTemplate) {

					if (((MoocInfoDisplayTemplate) selectedCourses).getWillUtakejob().equals("Yes")) {
						((MoocInfoDisplayTemplate) selectedCourses).setisVisible(true);
						System.out.print("");
					} else {
						((MoocInfoDisplayTemplate) selectedCourses).setisVisible(false);
						System.out.print("");
					}
				}

				else {
					if (((UnivInfoDisplayTemplate) selectedCourses).getWillUtakejob().equals("Yes")) {
						((UnivInfoDisplayTemplate) selectedCourses).setisVisible(true);
						System.out.print("");
					} else {
						((UnivInfoDisplayTemplate) selectedCourses).setisVisible(false);
						System.out.print("");
					}
				}

			}
		} else {

			for (ParentOfAllinfoDisplayTemplates selectedCourses : selectAmongRelevantCourses) {

				if (selectedCourses instanceof MoocInfoDisplayTemplate) {

					((MoocInfoDisplayTemplate) selectedCourses).setisVisible(true);

				}

				else {
					((UnivInfoDisplayTemplate) selectedCourses).setisVisible(true);

				}

			}

		}

		ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
		context.redirect(context.getRequestContextPath() + "/designv3chooseAmongRelevantCourses.jsf");

		return "";
	}

	public List<ParentOfAllinfoDisplayTemplates> getSelectAmongRelevantCourses() {
		return selectAmongRelevantCourses;
	}

	public void setSelectAmongRelevantCourses(List<ParentOfAllinfoDisplayTemplates> selectAmongRelevantCourses) {
		this.selectAmongRelevantCourses = selectAmongRelevantCourses;
	}

	public Integer getProgress() {
		if (progress == null) {
			progress = 0;
		}
		// else if (progress < 80) {
		// progress = progress + 10;
		// }
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
				finalconcatmooc.get(index - 1).getSelectedValue(),
				finalconcatmooc.get(index - 1).coursmatchedforjob + "-" + finalconcatmooc.get(index - 1).matchedindex,
				this.stmt, "moocRelevancy");
		return "";

	}

	public String submitMoocUnivRelevancy(String indexStr, int coursmatchedforjob, String moocOrUnivType)
			throws SQLException {

		Integer index = Integer.parseInt(indexStr);
		// if(rndmSndwichResltsofMoocUniv.get(index - 1) instanceof
		// MoocInfoDisplayTemplate){
		// FacesContext.getCurrentInstance().addMessage(null,
		// new FacesMessage("You've submitted " + ( (MoocInfoDisplayTemplate)
		// rndmSndwichResltsofMoocUniv.get(index - 1)).getSelectedValue()));
		// System.out.println("Value Select:" + ":" + ((MoocInfoDisplayTemplate)
		// rndmSndwichResltsofMoocUniv.get(index - 1)).getSelectedValue());
		// System.out.println("Query is:" + this.query);
		//
		//
		// System.out.println("Selected ID is:" +
		// ((MoocInfoDisplayTemplate)rndmSndwichResltsofMoocUniv.get(index -
		// 1)).coursmatchedforjob + "-"
		// + ((MoocInfoDisplayTemplate)rndmSndwichResltsofMoocUniv.get(index -
		// 1)).matchedindex);
		// System.out.println("Matched Mooc Course :" +
		// ((MoocInfoDisplayTemplate)rndmSndwichResltsofMoocUniv.get(index -
		// 1)).courseTitle);
		//
		// this.relvFeedback.insertResultMatchRelevancyFeedBak(this.query,
		// ((MoocInfoDisplayTemplate)rndmSndwichResltsofMoocUniv.get(index -
		// 1)).courseTitle,
		// ((MoocInfoDisplayTemplate)rndmSndwichResltsofMoocUniv.get(index -
		// 1)).getSelectedValue(),
		// ((MoocInfoDisplayTemplate)rndmSndwichResltsofMoocUniv.get(index -
		// 1)).coursmatchedforjob
		// + "-" +
		// ((MoocInfoDisplayTemplate)rndmSndwichResltsofMoocUniv.get(index -
		// 1)).matchedindex,
		// this.stmt, "moocRelevancy");
		// }

		if (moocOrUnivType.equals("Mooc")) {
			MoocInfoDisplayTemplate displygrowlmooc = null;

			for (MoocInfoDisplayTemplate tempMoocObj : finalconcatmooc) {
				if (tempMoocObj.getMatchedindex() == index
						&& tempMoocObj.getCoursmatchedforjob() == coursmatchedforjob) {
					displygrowlmooc = tempMoocObj;
				}
			}

			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage("You've submitted " + displygrowlmooc.getSelectedValue()));
			System.out.println("=========================================================");
			System.out.println("Value Select:" + ":" + displygrowlmooc.getSelectedValue());
			System.out.println("Query is:" + this.query);
			System.out.println(
					"Selected Mooc ID  is:" + displygrowlmooc.coursmatchedforjob + "-" + displygrowlmooc.matchedindex);
			System.out.println("Matched Mooc Course :" + displygrowlmooc.courseTitle);

			this.relvFeedback.insertResultMatchRelevancyFeedBak(this.query, displygrowlmooc.courseTitle,
					displygrowlmooc.getSelectedValue(),
					displygrowlmooc.coursmatchedforjob + "-" + displygrowlmooc.matchedindex, this.stmt,
					"moocRelevancy");

		} else {

			UnivInfoDisplayTemplate displygrowluniv = null;

			for (UnivInfoDisplayTemplate tempUnivObj : finalconcatuniv) {
				if (tempUnivObj.getMatchedindex() == index
						&& tempUnivObj.getCoursmatchedforjob() == coursmatchedforjob) {
					displygrowluniv = tempUnivObj;
				}
			}

			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage("You've submitted " + displygrowluniv.getSelectedValue()));
			System.out.println("=========================================================");
			System.out.println("Value Select Univ:" + ":" + displygrowluniv.getSelectedValue());
			System.out.println("Query is:" + this.query);

			System.out.println(
					"Selected Univ ID  is:" + displygrowluniv.coursmatchedforjob + "-" + displygrowluniv.matchedindex);
			System.out
					.println("Matched Univ Course :" + displygrowluniv.courseTitle + " : " + displygrowluniv.courseId);

			this.relvFeedback.insertResultMatchRelevancyFeedBak(this.query, displygrowluniv.courseId,
					displygrowluniv.getSelectedValue(),
					displygrowluniv.coursmatchedforjob + "-" + displygrowluniv.matchedindex, this.stmt,
					"univRelevancy");

		}

		return "";

	}

	public int getMaxcoursesAllowed() {
		return maxcoursesAllowed;
	}

	public void setMaxcoursesAllowed(int maxcoursesAllowed) {
		this.maxcoursesAllowed = maxcoursesAllowed;
	}

	public LinkedHashMap<JobInfoDisplayTemplate, List<ParentOfAllinfoDisplayTemplates>> getLhm() {
		return lhm;
	}

	public void setLhm(LinkedHashMap<JobInfoDisplayTemplate, List<ParentOfAllinfoDisplayTemplates>> lhm) {
		this.lhm = lhm;
	}

	public List<ParentOfAllinfoDisplayTemplates> getRndmSndwichResltsofMoocUniv() {
		return rndmSndwichResltsofMoocUniv;
	}

	public void setRndmSndwichResltsofMoocUniv(List<ParentOfAllinfoDisplayTemplates> rndmSndwichResltsofMoocUniv) {
		this.rndmSndwichResltsofMoocUniv = rndmSndwichResltsofMoocUniv;
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
				finalconcatmooc.get(index - 1).getWillUtakejob(),
				finalconcatmooc.get(index - 1).coursmatchedforjob + "-" + finalconcatmooc.get(index - 1).matchedindex,
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
				finalconcatuniv.get(index - 1).getWillUtakejob(),
				finalconcatuniv.get(index - 1).coursmatchedforjob + "-" + finalconcatuniv.get(index - 1).matchedindex,
				this.stmt, "UnivWillYouTake");

		return "";
	}

	public String submitUnivRelevancy(String indexStr) throws SQLException {

		Integer index = Integer.parseInt(indexStr);
		this.coursesAllowed = this.coursesAllowed + 1;
		if (this.maxcoursesAllowed >= this.coursesAllowed) {

			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage("You've submitted " + finalconcatuniv.get(index - 1).getSelectedValue()));
			System.out.println("Value Select Univ:" + ":" + finalconcatuniv.get(index - 1).getSelectedValue());
			System.out.println("Query is:" + this.query);

			System.out.println("Selected Univ ID  is:" + finalconcatuniv.get(index - 1).coursmatchedforjob + "-"
					+ finalconcatuniv.get(index - 1).matchedindex);
			System.out.println("Matched Univ Course :" + finalconcatuniv.get(index - 1).courseTitle + " : "
					+ finalconcatuniv.get(index - 1).courseId);

			this.relvFeedback.insertResultMatchRelevancyFeedBak(this.query, finalconcatuniv.get(index - 1).courseId,
					finalconcatuniv.get(index - 1).getSelectedValue(), finalconcatuniv.get(index - 1).coursmatchedforjob
							+ "-" + finalconcatuniv.get(index - 1).matchedindex,
					this.stmt, "univRelevancy");
		} else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("You've already taken max courses"));
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

	public String search()
			throws IOException, ParseException, ClassNotFoundException, SQLException, InterruptedException {
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
		this.progress = 20;
		searchcareeradvise.jobTitleSearch("\\LuceneCareerAdviseIndexing\\JobInfo",
				"\\LuceneCareerAdviseIndexing\\MoocCourseInfo", "\\LuceneCareerAdviseIndexing\\ResidentialCoursesInfo",
				query);
		this.progress = 30;
		finaljobsresultlist = searchcareeradvise.getjobresult();
		this.progress = 40;
		// finalmoocresult = searchcareeradvise.getSuperfinalmoocsresultlist();
		finalconcatmooc = searchcareeradvise.getconcatmooc();
		this.progress = 60;
		finalconcatuniv = searchcareeradvise.getconcatuniv();
		this.progress = 75;
		databaseConnection();
		this.progress = 85;
		System.out.println("printing from the search method of bean : Testing if all the objects working well");

		System.out.println("printing from the search method of bean univ : " + finalconcatuniv.get(0).courseTitle);
		System.out.println("printing from the search method of bean mooc : " + finalconcatmooc.get(0).courseTitle);
		System.out.println("printing from the search method of bean job : " + finaljobsresultlist.get(0).jobId);
		this.progress = 90;
		System.out.println("Creating the Hash Map of the results");
		createLinkedHasMapOfJobsWithUnivAndMooc();
		this.progress = 100;
		Thread.currentThread();
		Thread.currentThread().sleep(1000);
		this.progress = 0;
		// return "search?faces-redirect=true";
		return "designv3jobresults?faces-redirect=true";

	}

	private void createLinkedHasMapOfJobsWithUnivAndMooc() {
		// TODO Auto-generated method stub

		for (JobInfoDisplayTemplate jobResultObject : finaljobsresultlist) {
			List<ParentOfAllinfoDisplayTemplates> MoocUnivOrdByPgRnkRand = new ArrayList<ParentOfAllinfoDisplayTemplates>();

			GenQueue<ParentOfAllinfoDisplayTemplates> sandwidtchMoocQueue = new GenQueue<ParentOfAllinfoDisplayTemplates>();
			GenQueue<ParentOfAllinfoDisplayTemplates> sandwidtchUnivQueue = new GenQueue<ParentOfAllinfoDisplayTemplates>();

			MoocPg1Display = new ArrayList<MoocInfoDisplayTemplate>();
			UnivPg2Display = new ArrayList<UnivInfoDisplayTemplate>();

			Random randomno = new Random();
			int sandwidtchMoocBy;
			int sandwidtchUnivBy;

			// sandwidtchMoocBy=
			// randomno.nextInt(Math.round(finalconcatmooc.size() /
			// finaljobsresultlist.size()));
			// sandwidtchUnivBy=
			// randomno.nextInt(Math.round(finalconcatuniv.size() /
			// finaljobsresultlist.size()));

			for (MoocInfoDisplayTemplate moocResultsObject : finalconcatmooc) {

				if (moocResultsObject.coursmatchedforjob == jobResultObject.matchedjobindex) {
					sandwidtchMoocQueue.enqueue((ParentOfAllinfoDisplayTemplates) moocResultsObject);
					MoocPg1Display.add(moocResultsObject);
					System.out.println("Mooc Pge 1");
				}
			}
			for (UnivInfoDisplayTemplate univResultsObject : finalconcatuniv) {
				if (univResultsObject.coursmatchedforjob == jobResultObject.matchedjobindex) {
					sandwidtchUnivQueue.enqueue((ParentOfAllinfoDisplayTemplates) univResultsObject);
					UnivPg2Display.add(univResultsObject);
					System.out.println("Univ Pge 2");
				}
			}

			// Prepring the Final Sandwidtch List of Mooc and Univ.
			while (sandwidtchMoocQueue.hasItems() | sandwidtchUnivQueue.hasItems()) {

				sandwidtchMoocBy = randomno.nextInt(Math.round(finalconcatmooc.size() / finaljobsresultlist.size()));
				sandwidtchUnivBy = randomno.nextInt(Math.round(finalconcatuniv.size() / finaljobsresultlist.size()));

				for (int i = 0; i < sandwidtchMoocBy; i++) {
					if (sandwidtchMoocQueue.hasItems()) {
						MoocUnivOrdByPgRnkRand.add((ParentOfAllinfoDisplayTemplates) (sandwidtchMoocQueue.dequeue()));
						System.out.println("Mooc");
					}
				}
				for (int j = 0; j < sandwidtchUnivBy; j++) {
					if (sandwidtchUnivQueue.hasItems()) {
						MoocUnivOrdByPgRnkRand.add((ParentOfAllinfoDisplayTemplates) sandwidtchUnivQueue.dequeue());
						System.out.println("Univ");
					}
				}
			}

			lhm.put(jobResultObject, MoocUnivOrdByPgRnkRand);
			lhmMooc.put(jobResultObject, MoocPg1Display);
			lhmUniv.put(jobResultObject, UnivPg2Display);
		}

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
		rndmSndwichResltsofMoocUniv = lhm.get(selectedjob);
		page1MoocResults = lhmMooc.get(selectedjob);
		page2UnivResults = lhmUniv.get(selectedjob);
		System.out
				.println("Results Captured For Sandwidtch Display and Page 1 and Pge 2 display in to resective lists");
		return "designv4moocunivresult?faces-redirect=true";

	}

	public void selectedjobforMoocUnivButton(JobInfoDisplayTemplate selectedjob) throws IOException {
		this.selectedjob = selectedjob;
		System.out.println(selectedjob);
		System.out.println(selectedjob.jobtitle);
		rndmSndwichResltsofMoocUniv = lhm.get(selectedjob);
		page1MoocResults = lhmMooc.get(selectedjob);
		page2UnivResults = lhmUniv.get(selectedjob);
		System.out
				.println("Results Captured For Sandwidtch Display and Page 1 and Pge 2 display in to resective lists");
		ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
		context.redirect(context.getRequestContextPath() + "/designv4moocunivresult.jsf");

	}

	public JobInfoDisplayTemplate getSelectedjobforMoocUniv() {
		return selectedjobforMoocUniv;
	}

	public void setSelectedjobforMoocUniv(JobInfoDisplayTemplate selectedjobforMoocUniv) {
		this.selectedjobforMoocUniv = selectedjobforMoocUniv;
		System.out.println("Coming From the Selected Job For Mooc Univ");
		System.out.println(selectedjob);
		System.out.println(selectedjob.jobtitle);
		rndmSndwichResltsofMoocUniv = lhm.get(selectedjobforMoocUniv);
		System.out.println("Results Captured For Sandwidtch Display in to a list");
	}

	public int getCoursesAllowed() {
		return coursesAllowed;
	}

	public void setCoursesAllowed(int coursesAllowed) {
		this.coursesAllowed = coursesAllowed;
	}

	public ParentOfAllinfoDisplayTemplates getSelectedMoocUnivParentTemplate() {
		return selectedMoocUnivParentTemplate;
	}

	public void setSelectedMoocUnivParentTemplate(ParentOfAllinfoDisplayTemplates selectedMoocUnivParentTemplate) {
		this.selectedMoocUnivParentTemplate = selectedMoocUnivParentTemplate;
	}

	public JobInfoDisplayTemplate getSelectedjob() {
		return selectedjob;
	}

	public void setSelectedjob(JobInfoDisplayTemplate selectedjob) {
		this.selectedjob = selectedjob;
	}

	public LinkedHashMap<JobInfoDisplayTemplate, List<MoocInfoDisplayTemplate>> getLhmMooc() {
		return lhmMooc;
	}

	public void setLhmMooc(LinkedHashMap<JobInfoDisplayTemplate, List<MoocInfoDisplayTemplate>> lhmMooc) {
		this.lhmMooc = lhmMooc;
	}

	public LinkedHashMap<JobInfoDisplayTemplate, List<UnivInfoDisplayTemplate>> getLhmUniv() {
		return lhmUniv;
	}

	public void setLhmUniv(LinkedHashMap<JobInfoDisplayTemplate, List<UnivInfoDisplayTemplate>> lhmUniv) {
		this.lhmUniv = lhmUniv;
	}

	public List<MoocInfoDisplayTemplate> getMoocPg1Display() {
		return MoocPg1Display;
	}

	public void setMoocPg1Display(List<MoocInfoDisplayTemplate> moocPg1Display) {
		MoocPg1Display = moocPg1Display;
	}

	public List<UnivInfoDisplayTemplate> getUnivPg2Display() {
		return UnivPg2Display;
	}

	public void setUnivPg2Display(List<UnivInfoDisplayTemplate> univPg2Display) {
		UnivPg2Display = univPg2Display;
	}

	public List<MoocInfoDisplayTemplate> getPage1MoocResults() {
		return page1MoocResults;
	}

	public void setPage1MoocResults(List<MoocInfoDisplayTemplate> page1MoocResults) {
		this.page1MoocResults = page1MoocResults;
	}

	public List<UnivInfoDisplayTemplate> getPage2UnivResults() {
		return page2UnivResults;
	}

	public void setPage2UnivResults(List<UnivInfoDisplayTemplate> page2UnivResults) {
		this.page2UnivResults = page2UnivResults;
	}

	public List<JobInfoDisplayTemplate> getFinaljobsresultlist() {
		return finaljobsresultlist;
	}

	public void setFinaljobsresultlist(List<JobInfoDisplayTemplate> finaljobsresultlist) {
		this.finaljobsresultlist = finaljobsresultlist;
	}

	public List<List<MoocInfoDisplayTemplate>> getFinalmoocresult() {
		return finalmoocresult;
	}

	public void setFinalmoocresult(List<List<MoocInfoDisplayTemplate>> finalmoocresult) {
		this.finalmoocresult = finalmoocresult;
	}

	public List<MoocInfoDisplayTemplate> getFinalconcatmooc() {
		return finalconcatmooc;
	}

	public void setFinalconcatmooc(List<MoocInfoDisplayTemplate> finalconcatmooc) {
		this.finalconcatmooc = finalconcatmooc;
	}

	public List<UnivInfoDisplayTemplate> getFinalconcatuniv() {
		return finalconcatuniv;
	}

	public void setFinalconcatuniv(List<UnivInfoDisplayTemplate> finalconcatuniv) {
		this.finalconcatuniv = finalconcatuniv;
	}

	public MoocInfoDisplayTemplate getSelectedMooc() {
		return selectedMooc;
	}

	public void setSelectedMooc(MoocInfoDisplayTemplate selectedMooc) {
		this.selectedMooc = selectedMooc;
	}

	public UnivInfoDisplayTemplate getSelectedUniv() {
		return selectedUniv;
	}

	public void setSelectedUniv(UnivInfoDisplayTemplate selectedUniv) {
		this.selectedUniv = selectedUniv;
	}

	public Connection getConn() {
		return conn;
	}

	public void setConn(Connection conn) {
		this.conn = conn;
	}

	public Statement getStmt() {
		return stmt;
	}

	public void setStmt(Statement stmt) {
		this.stmt = stmt;
	}

	public List<ParentOfAllinfoDisplayTemplates> getSelectAmongRelevantCoursesNotAtAll() {
		return selectAmongRelevantCoursesNotAtAll;
	}

	public void setSelectAmongRelevantCoursesNotAtAll(
			List<ParentOfAllinfoDisplayTemplates> selectAmongRelevantCoursesNotAtAll) {
		this.selectAmongRelevantCoursesNotAtAll = selectAmongRelevantCoursesNotAtAll;
	}

	public List<ParentOfAllinfoDisplayTemplates> getSelectAmongRelevantCoursesJustFewWords() {
		return selectAmongRelevantCoursesJustFewWords;
	}

	public void setSelectAmongRelevantCoursesJustFewWords(
			List<ParentOfAllinfoDisplayTemplates> selectAmongRelevantCoursesJustFewWords) {
		this.selectAmongRelevantCoursesJustFewWords = selectAmongRelevantCoursesJustFewWords;
	}

	public List<ParentOfAllinfoDisplayTemplates> getSelectAmongRelevantCoursesModeratelyRelev() {
		return selectAmongRelevantCoursesModeratelyRelev;
	}

	public void setSelectAmongRelevantCoursesModeratelyRelev(
			List<ParentOfAllinfoDisplayTemplates> selectAmongRelevantCoursesModeratelyRelev) {
		this.selectAmongRelevantCoursesModeratelyRelev = selectAmongRelevantCoursesModeratelyRelev;
	}

	public List<ParentOfAllinfoDisplayTemplates> getSelectAmongRelevantCoursesMostlyRelev() {
		return selectAmongRelevantCoursesMostlyRelev;
	}

	public void setSelectAmongRelevantCoursesMostlyRelev(
			List<ParentOfAllinfoDisplayTemplates> selectAmongRelevantCoursesMostlyRelev) {
		this.selectAmongRelevantCoursesMostlyRelev = selectAmongRelevantCoursesMostlyRelev;
	}

	public List<ParentOfAllinfoDisplayTemplates> getSelectAmongRelevantCoursesVeryRelev() {
		return selectAmongRelevantCoursesVeryRelev;
	}

	public void setSelectAmongRelevantCoursesVeryRelev(
			List<ParentOfAllinfoDisplayTemplates> selectAmongRelevantCoursesVeryRelev) {
		this.selectAmongRelevantCoursesVeryRelev = selectAmongRelevantCoursesVeryRelev;
	}

	public int getMaxAllowedCoursesToChoose() {
		return maxAllowedCoursesToChoose;
	}

	public void setMaxAllowedCoursesToChoose(int maxAllowedCoursesToChoose) {
		this.maxAllowedCoursesToChoose = maxAllowedCoursesToChoose;
	}

	public int getTotalMoocChoosen() {
		return totalMoocChoosen;
	}

	public void setTotalMoocChoosen(int totalMoocChoosen) {
		this.totalMoocChoosen = totalMoocChoosen;
	}

	public int getTotalUnivChoosen() {
		return totalUnivChoosen;
	}

	public void setTotalUnivChoosen(int totalUnivChoosen) {
		this.totalUnivChoosen = totalUnivChoosen;
	}

	public LuceneSearchUserFeedback getRelvFeedback() {
		return relvFeedback;
	}

	public void setRelvFeedback(LuceneSearchUserFeedback relvFeedback) {
		this.relvFeedback = relvFeedback;
	}

}

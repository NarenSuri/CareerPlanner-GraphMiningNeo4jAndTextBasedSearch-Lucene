package jsf.career.planner;

import java.util.ArrayList;
import java.util.List;

public class MoocInfoDisplayTemplate extends ParentOfAllinfoDisplayTemplates {

	public int coursmatchedforjob;
	public int matchedindex;
	public String courseTitle;
	public String courseDescription;
	public String courseSyllabus;
	public String courseDescriptionShort;
	public int UiOrder = 0;
	public String objType = "Mooc";
	public boolean isVisible = true;

	public boolean getisVisible() {
		return isVisible;
	}

	public void setisVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}

	public void setObjType(String objType) {
		this.objType = objType;
	}

	public List<String> relevancymatch;
	public List<String> willyoutakeit;

	private String selectedValue="none";
	private String willUtakejob="none";

	public MoocInfoDisplayTemplate(int moocmatchedforjo, String matchedMoocinde, String CourseNam, String CourseDes,
			String CourseSyllabu, String courseDescriptionShort) {
		this.coursmatchedforjob = moocmatchedforjo;
		this.matchedindex = Integer.parseInt(matchedMoocinde);

		this.courseTitle = CourseNam;
		this.courseDescription = CourseDes;
		this.courseSyllabus = CourseSyllabu;
		this.courseDescriptionShort = courseDescriptionShort;
		this.objType = "Mooc";
		this.selectedValue="none";
		this.UiOrder = 0;
		this.isVisible = true;

		this.relevancymatch = new ArrayList<String>();
		this.relevancymatch.add("Is This Relevant?");
		this.relevancymatch.add("Notatall-1");
		this.relevancymatch.add("JustFewWordsMatched-2");
		this.relevancymatch.add("ModeratelyRelevant-3");
		this.relevancymatch.add("MostlyRelevant-4");
		this.relevancymatch.add("VeryRelevant-5");

		this.willyoutakeit = new ArrayList<String>();
		this.willyoutakeit.add("Will You Take this Course?");
		this.willyoutakeit.add("No");
		this.willyoutakeit.add("Yes");

	}

	public int getcoursmatchedforjob() {
		return coursmatchedforjob;
	}

	public void setcoursmatchedforjob(int moocmatchedforjo) {
		this.coursmatchedforjob = moocmatchedforjo;
	}

	public int getmatchedindex() {
		return matchedindex;
	}

	public void setmatchedindex(int matchedMoocinde) {
		this.matchedindex = matchedMoocinde;
	}

	public String getcourseTitle() {
		return courseTitle;
	}

	public void setcourseTitle(String CourseNam) {
		this.courseTitle = CourseNam;
	}

	public String getcourseDescription() {
		return courseDescription;
	}

	public void setcourseDescription(String CourseDes) {
		this.courseDescription = CourseDes;
	}

	public String getcourseSyllabus() {
		return courseSyllabus;
	}

	public void setcourseSyllabus(String CourseSyllabu) {
		this.courseSyllabus = CourseSyllabu;
	}

	public String getSelectedValue() {
		return selectedValue;
	}

	public void setSelectedValue(String selectedValue) {
		this.selectedValue = selectedValue;
	}

	public String getWillUtakejob() {
		return willUtakejob;
	}

	public void setWillUtakejob(String willUtakejob) {
		this.willUtakejob = willUtakejob;
	}

	public List<String> getRelevancymatch() {
		return relevancymatch;
	}

	public void setRelevancymatch(List<String> relevancymatch) {
		this.relevancymatch = relevancymatch;
	}

	public List<String> getWillyoutakeit() {
		return willyoutakeit;
	}

	public void setWillyoutakeit(List<String> willyoutakeit) {
		this.willyoutakeit = willyoutakeit;
	}

	public int getUiOrder() {
		return UiOrder;
	}

	public void setUiOrder(int uiOrder) {
		UiOrder = uiOrder;
	}

	public String getObjType() {
		return objType;
	}

	public void setObjType() {
		this.objType = "Mooc";
	}

	public int getCoursmatchedforjob() {
		return coursmatchedforjob;
	}

	public void setCoursmatchedforjob(int coursmatchedforjob) {
		this.coursmatchedforjob = coursmatchedforjob;
	}

	public int getMatchedindex() {
		return matchedindex;
	}

	public void setMatchedindex(int matchedindex) {
		this.matchedindex = matchedindex;
	}

	public String getCourseTitle() {
		return courseTitle;
	}

	public void setCourseTitle(String courseTitle) {
		this.courseTitle = courseTitle;
	}

	public String getCourseDescription() {
		return courseDescription;
	}

	public void setCourseDescription(String courseDescription) {
		this.courseDescription = courseDescription;
	}

	public String getCourseSyllabus() {
		return courseSyllabus;
	}

	public void setCourseSyllabus(String courseSyllabus) {
		this.courseSyllabus = courseSyllabus;
	}

	public String getCourseDescriptionShort() {
		return courseDescriptionShort;
	}

	public void setCourseDescriptionShort(String courseDescriptionShort) {
		this.courseDescriptionShort = courseDescriptionShort;
	}

}

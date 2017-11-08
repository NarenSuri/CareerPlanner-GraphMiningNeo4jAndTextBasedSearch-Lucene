package jsf.career.planner;

import java.util.ArrayList;
import java.util.List;

public class UnivInfoDisplayTemplate extends ParentOfAllinfoDisplayTemplates{

	public int coursmatchedforjob;
	public int matchedindex;
	
	public String universityName;
	public String department;
	public String courseId;
	public String courseDescription;
	public String courseTitle;
	public String creditHours;
	public String courseDescriptionShort;
	public int UiOrder=0;
	public String objType="Univ";
	public boolean isVisible = true;
	


	public List<String> relevancymatch;
	public List<String> willyoutakeit;
	
	private String selectedValue="none";
	private String willUtakejob="none";
	

	public UnivInfoDisplayTemplate(int coursmatchedforjob, String matchedindex, String universityName, String department,
			String courseId,String courseDescription , String courseTitle, String creditHours,String courseDescriptionShort) {
		
		this.coursmatchedforjob = coursmatchedforjob;
		this.matchedindex = Integer.parseInt(matchedindex);		
		this.universityName = universityName;
		this.department = department;
		this.courseId = courseId;
		this.objType="Univ";
		this.selectedValue="none";
		this.UiOrder=0;
		this.isVisible = true;
		
		this.courseTitle = courseTitle;
		this.creditHours = creditHours;
		this.courseDescription = courseDescription;
		this.courseDescriptionShort = courseDescriptionShort;
		
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

	public String getcourseTitle() {
		return courseTitle;
	}

	public void setcourseTitle(String courseTitle) {
		this.courseTitle = courseTitle;
	}

	public String getcreditHours() {
		return creditHours;
	}

	public void setcreditHours(String creditHours) {
		this.creditHours = creditHours;
	}

	public String getcourseDescription() {
		return courseDescription;
	}

	public void setcourseDescription(String courseDescription) {
		this.courseDescription = courseDescription;
	}

	public int getcoursmatchedforjob() {
		return coursmatchedforjob;
	}

	public void setcoursmatchedforjob(int coursmatchedforjob) {
		this.coursmatchedforjob = coursmatchedforjob;
	}

	public int getmatchedindex() {
		return matchedindex;
	}

	public void setmatchedindex(int matchedindex) {
		this.matchedindex = matchedindex;
	}

	public String getuniversityName() {
		return universityName;
	}

	public void setuniversityName(String universityName) {
		this.universityName = universityName;
	}

	public String getdepartment() {
		return department;
	}

	public void setdepartment(String department) {
		this.department = department;
	}

	public String getcourseId() {
		return courseId;
	}

	public void setcourseId(String courseId) {
		this.courseId = courseId;
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
	public String getObjType() {
		return objType;
	}

	public void setObjType() {
		this.objType="Univ";
	}

	public int getUiOrder() {
		return UiOrder;
	}

	public void setUiOrder(int uiOrder) {
		UiOrder = uiOrder;
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

	public String getUniversityName() {
		return universityName;
	}

	public void setUniversityName(String universityName) {
		this.universityName = universityName;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getCourseId() {
		return courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	public String getCourseDescription() {
		return courseDescription;
	}

	public void setCourseDescription(String courseDescription) {
		this.courseDescription = courseDescription;
	}

	public String getCourseTitle() {
		return courseTitle;
	}

	public void setCourseTitle(String courseTitle) {
		this.courseTitle = courseTitle;
	}

	public String getCreditHours() {
		return creditHours;
	}

	public void setCreditHours(String creditHours) {
		this.creditHours = creditHours;
	}

	public String getCourseDescriptionShort() {
		return courseDescriptionShort;
	}

	public void setCourseDescriptionShort(String courseDescriptionShort) {
		this.courseDescriptionShort = courseDescriptionShort;
	}

	

	public boolean getisVisible() {
		return isVisible;
	}

	public void setisVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}

	public void setObjType(String objType) {
		this.objType = objType;
	}
}

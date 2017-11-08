package jsf.career.planner;

import java.util.ArrayList;
import java.util.List;

public class JobInfoDisplayTemplate extends ParentOfAllinfoDisplayTemplates {

	public int matchedjobindex;
	public String jobId;
	public String jobtitle;
	public String jobDescription;
	public String jobDescriptionShort;
	public String readMore;
	public String objType = "Univ";
	public String companyName;

	// public SelectRelevancy relevancy;
	public List<String> relevancymatch;
	public List<String> willyoutakeit;

	private String selectedValue = "none";
	private String willUtakejob;

	public String getJobDescriptionShort() {
		return jobDescriptionShort;
	}

	public void setJobDescriptionShort(String jobDescriptionShort) {
		this.jobDescriptionShort = jobDescriptionShort;
	}

	public int getMatchedjobindex() {
		return matchedjobindex;
	}

	public void setMatchedjobindex(int matchedjobindex) {
		this.matchedjobindex = matchedjobindex;
	}

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	public String getJobtitle() {
		return jobtitle;
	}

	public void setJobtitle(String jobtitle) {
		this.jobtitle = jobtitle;
	}

	public String getJobDescription() {
		return jobDescription;
	}

	public void setJobDescription(String jobDescription) {
		this.jobDescription = jobDescription;
	}

	public String getSelectedValue() {
		return selectedValue;
	}

	public void setSelectedValue(String selectedValue) {
		this.selectedValue = selectedValue;
	}

	public JobInfoDisplayTemplate(String matchedjobindex, String jobId, String jobtitle, String jobDescription,
			String jobDescriptionShort, String readMore,String companyName) {
		this.matchedjobindex = Integer.parseInt(matchedjobindex);

		this.jobId = jobId;
		this.jobtitle = jobtitle;
		this.jobDescription = jobDescription;
		this.jobDescriptionShort = jobDescriptionShort;
		this.readMore = readMore;
		this.objType = "Univ";
		this.selectedValue = "none";
		this.companyName = companyName;
		this.relevancymatch = new ArrayList<String>();
		this.relevancymatch.add("Is This Relevant?");
		this.relevancymatch.add("Notatall-1");
		this.relevancymatch.add("JustFewWordsMatched-2");
		this.relevancymatch.add("ModeratelyRelevant-3");
		this.relevancymatch.add("MostlyRelevant-4");
		this.relevancymatch.add("VeryRelevant-5");

		this.willyoutakeit = new ArrayList<String>();
		this.willyoutakeit.add("Will You Take this Job?");
		this.willyoutakeit.add("No");
		this.willyoutakeit.add("Yes");
	}

	public List<String> getWillyoutakeit() {
		return willyoutakeit;
	}

	public void setWillyoutakeit(List<String> willyoutakeit) {
		this.willyoutakeit = willyoutakeit;
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

	// public SelectRelevancy getrelevancy() {
	// return relevancy;
	// }
	//
	// public void setrelevancy(SelectRelevancy relevancy) {
	// this.relevancy = new SelectRelevancy();
	// }

	public int getmatchedjobindex() {
		return matchedjobindex;
	}

	public void setmatchedjobindex(int matchedjobindex) {
		this.matchedjobindex = matchedjobindex;
	}

	public String getjobId() {
		return jobId;
	}

	public void setjobId(String jobId) {
		this.jobId = jobId;
	}

	public String getjobtitle() {
		return jobtitle;
	}

	public void setjobtitle(String jobtitle) {
		this.jobtitle = jobtitle;
	}

	public String getjobDescription() {
		return jobDescription;
	}

	public void setjobDescription(String jobDescription) {
		this.jobDescription = jobDescription;
	}

	public String getwillUtakejob() {
		return willUtakejob;
	}

	public void setwillUtakejob(String willUtakejob) {
		this.willUtakejob = willUtakejob;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public void setObjType(String objType) {
		this.objType = objType;
	}

	public String getObjType() {
		return objType;
	}

	public void setObjType() {
		this.objType = "Univ";
		;
	}

	public String getReadMore() {
		return readMore;
	}

	public void setReadMore(String readMore) {
		this.readMore = readMore;
	}

}

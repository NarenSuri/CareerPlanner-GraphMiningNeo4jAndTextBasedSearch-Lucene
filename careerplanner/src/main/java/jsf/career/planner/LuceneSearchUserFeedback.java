package jsf.career.planner;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class LuceneSearchUserFeedback {

	public void insertResultMatchRelevancyFeedBak(String query, String IdName, String selectedValue,
			String matchedindex, Statement stmt, String task) throws SQLException {
		// TODO Auto-generated method stub
		String sqlquery = "";
		int selectedfeedbackVal = 0;
		String PreparedStatement;

		if (selectedValue.equals("Notatall-1")) {
			selectedfeedbackVal = 1;
		} else if (selectedValue.equals("JustFewWordsMatched-2")) {
			selectedfeedbackVal = 2;
		} else if (selectedValue.equals("ModeratelyRelevant-3")) {
			selectedfeedbackVal = 3;
		} else if (selectedValue.equals("MostlyRelevant-4")) {
			selectedfeedbackVal = 4;
		} else if (selectedValue.equals("VeryRelevant-5")) {
			selectedfeedbackVal = 5;
		} else {
			selectedfeedbackVal = 0;
		}

		if (task.equals("jobRelevancy")) {

			PreparedStatement = "\"" + query + "\"," + "\"" + IdName + "\"," + "\"" + selectedfeedbackVal + "\"," + "\""
					+ matchedindex + "\",now()";

			sqlquery = "INSERT INTO JobRelevancyFeedBack(userquery,jobId,feedBack,matchedjobindex,feedBackGivenAt)  VALUES ( "
					+ PreparedStatement + " )";
			System.out.println("Query about to execute is : " + PreparedStatement);
			stmt.executeUpdate(sqlquery);
		}

		else if (task.equals("moocRelevancy")) {
			String moocCourseName = IdName;
			String mocindexmatched = matchedindex;
			PreparedStatement = "\"" + query + "\"," + "\"" + moocCourseName + "\"," + "\"" + selectedfeedbackVal
					+ "\"," + "\"" + mocindexmatched + "\",now()";

			sqlquery = "INSERT INTO MoocRelevancyFeedBack(userquery,MoocCourseName,feedBack,MoocJobMatchIndex,feedBackGivenAt)  VALUES ( "
					+ PreparedStatement + " )";
			System.out.println("Query about to execute is : " + PreparedStatement);
			stmt.executeUpdate(sqlquery);
		}
		
		else if (task.equals("univRelevancy")) {
			String univCourseName = IdName;
			String univindexmatched = matchedindex;
			PreparedStatement = "\"" + query + "\"," + "\"" + univCourseName + "\"," + "\"" + selectedfeedbackVal
					+ "\"," + "\"" + univindexmatched + "\",now()";

			sqlquery = "INSERT INTO UnivRelevancyFeedBack(userquery,UnivCourseId,feedBack,UnivJobMatchIndex,feedBackGivenAt)  VALUES ( "
					+ PreparedStatement + " )";
			System.out.println("Query about to execute is : " + PreparedStatement);
			stmt.executeUpdate(sqlquery);
		}
	}

	public void insertResultWillYouTakeFeedBak(String query, String IdName, String willUtakejob, String matchedindex,
			Statement stmt,String task) throws SQLException {
		// TODO Auto-generated method stub

		String sqlquery = "";
		int selectedfeedbackVal = 0;
		String PreparedStatement;

		if (willUtakejob.equals("No")) {
			selectedfeedbackVal = 0;
		} else if (willUtakejob.equals("Yes")) {
			selectedfeedbackVal = 1;
		} else {
			selectedfeedbackVal = 0;
		}

		if(task.equals("JobWillYouTake")){
		
		PreparedStatement = "\"" + query + "\"," + "\"" + IdName + "\"," + "\"" + selectedfeedbackVal + "\"," + "\""
				+ matchedindex + "\",now()";

		sqlquery = "INSERT INTO JobWillYouTakeItFeedBack(userquery,jobId,feedBack,matchedjobindex,feedBackGivenAt)  VALUES ( "
				+ PreparedStatement + " )";
		System.out.println("Query about to execute is : " + PreparedStatement);
		stmt.executeUpdate(sqlquery);

	}
		else if(task.equals("MoocWillYouTake")){
			
			PreparedStatement = "\"" + query + "\"," + "\"" + IdName + "\"," + "\"" + selectedfeedbackVal + "\"," + "\""
					+ matchedindex + "\",now()";

			sqlquery = "INSERT INTO moocwillyoutakeitfeedback(userquery,MoocCourseName,feedBack,MoocJobMatchIndex,feedBackGivenAt)  VALUES ( "
					+ PreparedStatement + " )";
			System.out.println("Query about to execute is : " + PreparedStatement);
			stmt.executeUpdate(sqlquery);
		}	
		
		else if(task.equals("UnivWillYouTake")){
			String univCourseId = IdName;
			String univindexmatched = matchedindex;
			
			PreparedStatement = "\"" + query + "\"," + "\"" + univCourseId + "\"," + "\"" + selectedfeedbackVal + "\"," + "\""
					+ univindexmatched + "\",now()";

			sqlquery = "INSERT INTO Univwillyoutakeitfeedback(userquery,UnivCourseId,feedBack,UnivJobMatchIndex,feedBackGivenAt)  VALUES ( "
					+ PreparedStatement + " )";
			System.out.println("Query about to execute is : " + PreparedStatement);
			stmt.executeUpdate(sqlquery);
		}
	}

}

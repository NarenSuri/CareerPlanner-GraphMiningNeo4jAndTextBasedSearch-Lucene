package jsf.career.planner;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.NumericField;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.util.Version;
import org.apache.lucene.analysis.PorterStemFilter;

import luceneCareerAdvising.CollectShouts_Tracks;

// THis code is same as the 


public class IndexDataForkeyWordExtraction {

	private static IndexWriter writer;

	public static IndexWriter Indexer(String indexDir)
			throws CorruptIndexException, LockObtainFailedException, IOException {
		Directory dir = FSDirectory.open(new File(indexDir));
		return writer = new IndexWriter(dir, new StandardAnalyzer(Version.LUCENE_30), true,
				IndexWriter.MaxFieldLength.UNLIMITED);
	}

	public static void close() throws IOException {
		writer.close();
	}

	public static void main(String[] args) throws SQLException, ClassNotFoundException {
		try {
			String indexDir = "D:\\sem2\\ir-project\\lucene\\LuceneCareerAdviseIndexing-For-KeyWikiWordExtractions\\NeoDbJobInfo\\";
			IndexingData(indexDir);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private static void IndexingData(String indexDir) throws SQLException, IOException, ClassNotFoundException {
		// TODO Auto-generated method stub

		String myDriver = "org.gjt.mm.mysql.Driver";
		Connection conn;
		// Setting the configuration values

		String userName = "root";
		String password = "root";
		String myUrl = "jdbc:mysql://localhost:3306/NeoAcess";

		// MYSQL
		Class.forName(myDriver);
		conn = (Connection) DriverManager.getConnection(myUrl, userName, password);
		conn.setAutoCommit(false);

		String setUpdateOption = "SET SQL_SAFE_UPDATES = 0";
		String SetProcessed = "UPDATE jobdata SET processed = 0";
		String selectQueryJob = "select * from jobdata where processed=0";
		String updateQueryJob = "update jobdata set processed = 1 where autoinccol=?";

		ResultSet rs;
		int c;
		PreparedStatement preparedStmtUpdtOptn = (PreparedStatement) conn.prepareStatement(setUpdateOption);
		PreparedStatement preparedStmtProcessed = (PreparedStatement) conn.prepareStatement(SetProcessed);
		preparedStmtUpdtOptn.execute();
		preparedStmtProcessed.execute();

		PreparedStatement preparedStmt = (PreparedStatement) conn.prepareStatement(selectQueryJob);
		PreparedStatement preparedStmtUpdate = (PreparedStatement) conn.prepareStatement(updateQueryJob);

		rs = (ResultSet) preparedStmt.executeQuery();
		int batch = 0;
		int autoinccol;
		double jobId = 0;
		String jobtitle;
		String jobFunctions;
		String StemmedData;

		try {
			System.out.println("Creating Index Writer");
			c = 0;
			IndexWriter writer = Indexer(indexDir);

			while (rs.next()) {
				autoinccol = rs.getInt("autoinccol");
				jobId = rs.getDouble("jobId");
				jobtitle = rs.getString("jobtitle");				
				jobFunctions = rs.getString("jobFunctions");				
				StemmedData = rs.getString("StemmedData");


				Document doc = new Document();
				
				doc.add(new NumericField("autoinccol", Field.Store.YES, true).setDoubleValue(autoinccol));
				doc.add(new NumericField("jobId", Field.Store.YES, true).setDoubleValue(jobId));
				doc.add(new Field("jobtitle", jobtitle, Field.Store.YES, Field.Index.ANALYZED,Field.TermVector.YES));				
				doc.add(new Field("jobFunctions", jobFunctions, Field.Store.YES, Field.Index.ANALYZED,Field.TermVector.YES));				
				doc.add(new Field("StemmedData", StemmedData, Field.Store.YES, Field.Index.ANALYZED,Field.TermVector.YES));
	

				writer.addDocument(doc);
				writer.commit();
				preparedStmtUpdate.clearParameters();
				preparedStmtUpdate.setInt(1, autoinccol);
				preparedStmtUpdate.addBatch();
				c++;
				if (c == 1000) {
					c = 0;
					//preparedStmtUpdate.executeBatch();
					preparedStmtUpdate.clearBatch();
					conn.commit();
					batch++;
					System.out.println("Did a batch of query's execution  " + batch);
				}

			}

			//preparedStmtUpdate.executeBatch();
			preparedStmtUpdate.clearBatch();

			preparedStmt.close();
			preparedStmtUpdate.close();
			batch++;
			System.out.println("Did a Final batch of query's execution  " + batch);

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			conn.close();
			close();
		}

	}

	public static Logger prepareLogger(String log_file_path) throws IOException {
		Logger log;
		log = Logger.getLogger(CollectShouts_Tracks.class.getName());
		java.util.Date date = new java.util.Date();
		SimpleFormatter formatterTxt = new SimpleFormatter();

		log.setLevel(Level.FINEST);
		FileHandler logFile = new FileHandler(log_file_path + "log_"
				+ new Timestamp(date.getTime()).toString().replaceAll(":", "_").replaceAll("\\.", "_") + ".log");
		logFile.setFormatter(formatterTxt);
		log.addHandler(logFile);
		log.finest("********** LOG FILE : Creating Nodes ****************");
		return log;
	}

}



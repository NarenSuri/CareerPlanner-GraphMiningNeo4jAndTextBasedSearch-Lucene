package jsf.career.planner;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.TermQuery;

public class CombineBoolenQuery {

	public BooleanQuery combinequerys(String jobTerm,int slop){
		
		PhraseQuery phrasquer = new PhraseQuery();
		phrasquer.setSlop(slop);
		
	TermQuery searchingjobtitle =
			new TermQuery(new Term("jobtitle", "search"));
	
	TermQuery searchingjobFunctions =
			new TermQuery(new Term("jobFunctions", "search"));
	
	TermQuery searchingkeyWords =
			new TermQuery(new Term("keyWords", "search"));
	
	TermQuery searchingjobDescription =
			new TermQuery(new Term("jobDescription", "search"));

			
			BooleanQuery searchingAllJobFields = new BooleanQuery();
			//searchingAllJobFields.add(query, occur);
			searchingAllJobFields.add(searchingjobtitle, BooleanClause.Occur.MUST);
			searchingAllJobFields.add(searchingjobFunctions, BooleanClause.Occur.MUST);
			searchingAllJobFields.add(searchingkeyWords, BooleanClause.Occur.MUST);
			searchingAllJobFields.add(searchingjobDescription, BooleanClause.Occur.MUST);
			
			return searchingAllJobFields;
	}
}

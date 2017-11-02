/*
*
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
* This file is part of Entando Enterprise Edition software.
* You can redistribute it and/or modify it
* under the terms of the Entando's EULA
* 
* See the file License for the specific language governing permissions   
* and limitations under the License
* 
* 
* 
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/
package com.agiletec.plugins.jpforum.aps.system.services.searchengine;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.plugins.jpforum.aps.system.services.section.Section;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.SimpleFSDirectory;

public class ForumSearcherDAO implements IForumSearcherDAO {

	public void init(File dir) {
		this._indexDir = dir;
	}

	@Override
	public List<Integer> searchPost(String word, Set<String> userGroups) throws ApsSystemException {
		List<Integer> postCodes = new ArrayList<Integer>();
		IndexSearcher searcher = null;
		try {
			searcher = this.getSearcher();
    		QueryParser parser = new QueryParser(IForumIndexerDAO.POST_TEXT_FIELD_NAME, this.getAnalyzer());
    		String queryString = this.createQueryString(word, userGroups);
        	Query query = parser.parse(queryString);
        	TopDocs topDocs = searcher.search(query, null, 1000);
        	//Hits hits = searcher.search(query);
    		ScoreDoc[] scoreDoc = topDocs.scoreDocs;
    		if (scoreDoc.length > 0) {
				for (int index=0; index<scoreDoc.length; index++) {
					ScoreDoc sDoc = scoreDoc[index];
    				Document doc = searcher.doc(sDoc.doc);
					String code = doc.get(IForumIndexerDAO.POST_CODE_FIELD_NAME);
					postCodes.add(new Integer(code));
				}
			}
		} catch (IOException e) {
			throw new ApsSystemException("Errore in estrazione " +
					"documento in base ad indice", e);
		} catch (ParseException e) {
			throw new ApsSystemException("Errore in estrazione " +
					"documento in base ad indice", e);		
		} finally {
			this.releaseSearcher(searcher);
		}
		return postCodes;
	}

	private String createQueryString(String word, Set<String> userGroups) {
		String queryString = "(" + IForumIndexerDAO.POST_TEXT_FIELD_NAME + ":'" + word + "' OR " + IForumIndexerDAO.POST_TITLE_FIELD_NAME + ":'" + word + "') ";
		if (userGroups == null) {
			userGroups = new HashSet<String>();
		}
		if (!userGroups.contains(Group.ADMINS_GROUP_NAME)) {
			if (!userGroups.contains(Group.FREE_GROUP_NAME)) {
				userGroups.add(Group.FREE_GROUP_NAME);
			}
			queryString += " AND (";
			boolean isFirstGroup = true;
			Iterator<String> iterGroups = userGroups.iterator();
			while (iterGroups.hasNext()) {
				String group =  iterGroups.next();
				if (!isFirstGroup) queryString += " OR ";
				queryString += IForumIndexerDAO.POST_SECTION_GROUPNAME_FIELD_NAME + ":" + group;
				isFirstGroup = false;
			}
			queryString+= " OR " + IForumIndexerDAO.POST_SECTION_UNAUTHBEHAVIOUR_FIELD_NAME + ":" + new Integer(Section.UNAUTH_BEHAVIOUR_READONLY).toString();
			queryString += ")";
		}
		return queryString;
	}
	
	private IndexSearcher getSearcher() throws IOException {
		FSDirectory directory = new SimpleFSDirectory(_indexDir);
		IndexReader reader = DirectoryReader.open(directory);
		IndexSearcher searcher = new IndexSearcher(reader);
		return searcher;
	}
	
	private void releaseSearcher(IndexSearcher searcher) throws ApsSystemException {
		try {
			if (searcher != null) {
				searcher.getIndexReader().close();
			}
		} catch (IOException e) {
			throw new ApsSystemException("Error closing searcher", e);
		}
	}
	
	private Analyzer getAnalyzer() {
		return new StandardAnalyzer();
	}
	
	@Override
	public void close() {
		// Nothing to do
	}

	private File _indexDir;
}

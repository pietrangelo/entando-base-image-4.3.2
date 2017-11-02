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
import com.agiletec.aps.util.HtmlHandler;
import com.agiletec.plugins.jpforum.aps.system.services.markup.IMarkupParser;
import com.agiletec.plugins.jpforum.aps.system.services.section.ISectionManager;
import com.agiletec.plugins.jpforum.aps.system.services.section.Section;
import com.agiletec.plugins.jpforum.aps.system.services.thread.IThreadManager;
import com.agiletec.plugins.jpforum.aps.system.services.thread.Thread;
import com.agiletec.plugins.jpforum.aps.system.services.thread.Post;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.SimpleFSLockFactory;
import org.apache.lucene.util.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ForumIndexerDAO implements IForumIndexerDAO {

	private static final Logger _logger =  LoggerFactory.getLogger(ForumIndexerDAO.class);

	/**
	 * Inizializzazione dell'indicizzatore.
	 * @param dir La cartella locale contenitore dei dati persistenti.
	 * @param newIndex true se è una nuova indicizzazione (ed in tal caso 
	 * cancella tutte le precedenti indicizzazioni), false in caso contrario.
	 * @throws ApsSystemException
	 */
	@Override
	public void init(File dir, boolean newIndex) throws ApsSystemException {
		try {
			this._dir = FSDirectory.open(dir);
			this._dir.setLockFactory(new SimpleFSLockFactory(dir));
		} catch (Throwable t) {
			_logger.error("Error creating directory", t);
			throw new ApsSystemException("Error creating directory", t);
		}
		_logger.debug("Indexer: search engine index ok.");
	}
	
	@Override
	public void add(Post post) throws ApsSystemException {
		try {
            Document document = this.createDocument(post);
            this.add(document);
        } catch (ApsSystemException e) {
        	_logger.error("Errore in aggiunta di un post", e);
            throw e;
        }
	}
	
	/**
	 * Aggiunge un documento nel db del motore di ricerca.
     * @param document Il documento da aggiungere.
	 * @throws ApsSystemException In caso di errori in accesso al db.
	 */
    private synchronized void add(Document document) throws ApsSystemException {
        IndexWriter writer = null;
		try {
			writer = new IndexWriter(this._dir, this.getIndexWriterConfig());
            writer.addDocument(document);
        } catch (Throwable t) {
        	_logger.error("Errore saving document", t);
        	throw new ApsSystemException("Error saving document", t);
        } finally {
			if (null != writer) {
				try {
					writer.close();
				} catch (IOException ex) {
					_logger.error("Error closing IndexWriter", ex);
				}
			}
		}
    }
    
    /**
     * Crea un oggetto Document pronto per l'indicizzazione da un oggetto Content.
     * @param entity Il contenuto dal quale ricavare il Document.
     * @return L'oggetto Document ricavato dal contenuto.
     * @throws ApsSystemException
     */
    private Document createDocument(Post post) throws ApsSystemException {
		Document document = new Document();
		String html = this.getMarkupParser().XMLtoHTML(post.getText());
		HtmlHandler handler = new HtmlHandler();
		String parsedText =  handler.getParsedText(html);
		String code = new Integer(post.getCode()).toString();
		Thread thread = this.getThreadManager().getThread(post.getThreadId());
		Section section = this.getSectionManager().getSection(thread.getSectionId());
		try {
			document.add(new Field(POST_CODE_FIELD_NAME, code, Field.Store.YES, Field.Index.ANALYZED));
			document.add(new Field(POST_TITLE_FIELD_NAME, post.getTitle(), Field.Store.YES, Field.Index.ANALYZED));
			document.add(new Field(POST_TEXT_FIELD_NAME, parsedText, Field.Store.YES, Field.Index.ANALYZED));
			Set<String> groups = section.getGroups();
			if (null != groups) {
				Iterator<String>  it = groups.iterator();
				while (it.hasNext()) {
					document.add(new Field(POST_SECTION_GROUPNAME_FIELD_NAME, it.next(), Field.Store.YES, Field.Index.ANALYZED));
				}
			}
			document.add(new Field(POST_SECTION_UNAUTHBEHAVIOUR_FIELD_NAME, new Integer(section.getUnauthBahaviour()).toString(), Field.Store.YES, Field.Index.ANALYZED));

		} catch (Throwable t) {
			throw new ApsSystemException("errore in createDocument", t);
		}
		return document;
    }
    
    /**
     * Cancella un documento in base alla chiave (di nome "id") 
     * mediante il quale è stato indicizzato.
     * Nel caso della cancellazione di un contenuto il nome del campo 
     * da utilizzare sarà "id" mentre il valore sarà l'identificativo del contenuto.
     * @param name Il nome del campo Field da utilizzare per recupero del documento.
     * @param value La chiave mediante il quale 
     * è stato indicizzato il documento.
     * @throws ApsSystemException
     */
    @Override
	public synchronized void delete(String name, int value) throws ApsSystemException {
        try {
            IndexWriter writer = new IndexWriter(this._dir, this.getIndexWriterConfig());
            writer.deleteDocuments(new Term(name, new Integer(value).toString()));
            writer.close();
        } catch (IOException e) {
			_logger.error("Error deleting document", e);
            throw new ApsSystemException("Error deleting document", e);
        }
    }
    
    @Override
	public void close() {
    	// nothing to do
    }
    
    private Analyzer getAnalyzer() {
    	return new StandardAnalyzer();
    }
	
	private IndexWriterConfig getIndexWriterConfig() {
		return new IndexWriterConfig(Version.LUCENE_4_10_4, this.getAnalyzer());
	}
    
	public void setMarkupParser(IMarkupParser markupParser) {
		this._markupParser = markupParser;
	}
	protected IMarkupParser getMarkupParser() {
		return _markupParser;
	}

	public void setThreadManager(IThreadManager threadManager) {
		this._threadManager = threadManager;
	}
	protected IThreadManager getThreadManager() {
		return _threadManager;
	}

	public void setSectionManager(ISectionManager sectionManager) {
		this._sectionManager = sectionManager;
	}
	protected ISectionManager getSectionManager() {
		return _sectionManager;
	}

	private Directory _dir;
	private IMarkupParser _markupParser;
	private IThreadManager _threadManager;
	private ISectionManager _sectionManager;
}

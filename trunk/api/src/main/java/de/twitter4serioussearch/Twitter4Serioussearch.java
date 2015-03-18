package de.twitter4serioussearch;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;

import twitter4j.TwitterStream;
import de.twitter4serioussearch.common.FieldNames;
import de.twitter4serioussearch.common.Util;
import de.twitter4serioussearch.search.Searcher;

public class Twitter4Serioussearch {

	private IndexWriter iwriter;
	private QueryHolder queryHolder;
	private TwitterStream twitterStream;
	private Directory currentDirectory;
	private TweetHolder tweetHolder;
	private IdGenerator idGenerator;
	private Searcher searcher;
	private static Logger log = LogManager.getLogger();
	
	/**
	 * Registriert einen {@link de.twitter4serioussearch.TweetListener
	 * TweetListener} für die Kombination aus Query und Session
	 *
	 * @param query
	 *            Vom User gesuchter String
	 * @param sessionId
	 *            eindeutiger Session Identifier (Hintergrund: Die gleiche Query
	 *            kann von mehreren Usern registriert werden)
	 * @param actionListener
	 *            {@link de.twitter4serioussearch.TweetListener TweetListener}
	 *            der invoked wird, sobald ein zum query passender Tweet
	 *            empfangen wurde
	 */
	public void registerQuery(String query, String sessionId, TweetListener actionListener) {
		
		query = StringUtils.join(Tokenizer.getTokensForString(query), " ");
		queryHolder.registerQuery(query, sessionId, actionListener);
		if(log.isTraceEnabled()) {
			log.trace("Registered Query : " + query + " (untokenized) on Session " + sessionId);
		}
		List<Document> documents = searcher.searchForTweets(query);
		for (Document document : Util.safe(documents)) {
			actionListener.handleNewTweet(tweetHolder.getTweets().get(
					Integer.parseInt(document.get(FieldNames.ID.getField())))); 
		}
	}

	/**
	 * Deregistriert ein Query für die gegebene Session
	 *
	 * @param query
	 *            zu unregistrierendes Query
	 * @param sessionId
	 *            eindeutiger Session Identifier (Hintergrund: Die gleiche Query
	 *            kann von mehreren Usern registriert werden)
	 */
	public void unregisterQuery(String query, String sessionId) {
		query = StringUtils.join(Tokenizer.getTokensForString(query), " ");
		queryHolder.unregisterQuery(query, sessionId);
	}

	/**
	 * Deregistriert alle querys für eine gegebene session
	 *
	 * @param sessionId
	 *            eindeutiger Session Identifier
	 */
	public void unregisterSession(String sessionId) {
		queryHolder.unregisterSession(sessionId);
	}

	TwitterStream getTwitterStream() {
		return twitterStream;
	}

	void setTwitterStream(TwitterStream twitterStream) {
		this.twitterStream = twitterStream;
	}

	@Override
	protected void finalize() throws Throwable {
		twitterStream.clearListeners();
		twitterStream.cleanUp();
		twitterStream.shutdown();
		if(log.isInfoEnabled()) {
			log.info("Cleanup invoked: listeners are cleared, stream is cleaned up and shut down.");
		}
		iwriter.close();
		super.finalize();
	}

	public IndexWriter getIndexWriter() {
		return iwriter;
	}

	public void setIndexWriter(IndexWriter iwriter) {
		this.iwriter = iwriter;
	}

	public QueryHolder getKeywordHolder() {
		return queryHolder;
	}

	public void setKeywordHolder(QueryHolder keywordHolder) {
		this.queryHolder = keywordHolder;
	}

	public Directory getCurrentDirectory() {
		return currentDirectory;
	}

	public void setCurrentDirectory(Directory currentDirectory) {
		this.currentDirectory = currentDirectory;
	}

	public TweetHolder getTweetHolder() {
		return tweetHolder;
	}

	public void setTweetHolder(TweetHolder tweetHolder) {
		this.tweetHolder = tweetHolder;
	}

	public IdGenerator getIdGenerator() {
		return idGenerator;
	}

	public void setIdGenerator(IdGenerator idGenerator) {
		this.idGenerator = idGenerator;
	}

	public Searcher getSearcher() {
		return searcher;
	}

	public void setSearcher(Searcher searcher) {
		this.searcher = searcher;
	}
}

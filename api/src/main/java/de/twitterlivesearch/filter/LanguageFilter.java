/*
 * Copyright 2015 Tobias Larscheid, Jan Schmitz-Hermes, Felix Nordhusen, Florian Scheil
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.twitterlivesearch.filter;

import twitter4j.Status;

/**
 * an example implementation of a filter. <br />
 * This filter only allows tweets in German or English.
 * @author schmitzhermes
 *
 */
public class LanguageFilter implements TweetFilter {

	@Override
	public boolean tweetMatches(Status tweet) {
		if(tweet.getLang() == "DE" || tweet.getLang() == "EN") {
			return true;
		}
		return false;
	}

}

package com.cosm.client.http.api;

import java.util.Collection;
import java.util.Map;

import com.cosm.client.http.exception.HttpException;
import com.cosm.client.http.util.exception.ParseToObjectException;
import com.cosm.client.model.Feed;

public interface FeedResource
{

	/**
	 * @param toCreate
	 *            the feed to be created over the API
	 * @return the feed created, on successful operation
	 * @throws HttpException
	 *             if failed to create feed over the API
	 * @throws ParseToObjectException
	 *             if failed to parse the returned json to feed
	 */
	public Feed create(Feed toCreate) throws HttpException;

	/**
	 * @param feedId
	 *            the id of the feed to be retrieved
	 * @return a feed object parsed from the json returned from the API
	 * @throws HttpException
	 *             if failed to get feed over the API
	 * @throws ParseToObjectException
	 *             if failed to parse the returned json to feed
	 */
	public Feed get(int feedId) throws HttpException, ParseToObjectException;

	/**
	 * 
	 * @param feedId
	 *            the id of the feed to be retrieved
	 * @param isShowUser
	 * @param dataStreamIds
	 * @return a collection of feed objects matching the params, parsed from the
	 *         json returned from the API
	 * @throws HttpException
	 *             if failed to get feed over the API
	 * @throws ParseToObjectException
	 *             if failed to parse the returned json to feed
	 */
	public Collection<Feed> get(int feedId, String isShowUser, String... dataStreamIds) throws HttpException,
			ParseToObjectException;

	// TODO can this location based params be also in filterParam?
	/**
	 * 
	 * @param feedId
	 * @param latitude
	 * @param longitude
	 * @param distance
	 * @param distanceUnits
	 * @return a collection of feed objects matching the params, parsed from the
	 *         json returned from the API
	 * @throws HttpException
	 *             if failed to get feed over the API
	 * @throws ParseToObjectException
	 *             if failed to parse the returned json to feed
	 */
	public Collection<Feed> getByLocation(int feedId, String latitude, String longitude, Double distance, String distanceUnits)
			throws HttpException, ParseToObjectException;

	/**
	 * @param filterParam
	 * @return a collection of feed objects matching the parameters given,
	 *         parsed from the json returned from the API
	 * @throws HttpException
	 *             if failed to get feed over the API
	 * @throws ParseToObjectException
	 *             if failed to parse the returned json to feed
	 */
	public Collection<Feed> get(Map filterParam) throws HttpException, ParseToObjectException;

	/**
	 * @param feedId
	 * @param startAt
	 * @param endAt
	 * @param samplingInterval
	 * @return a feed with history on datastreams and datapoints that matches
	 *         the given parameters
	 * @throws HttpException
	 *             if failed to get feed over the API
	 * @throws ParseToObjectException
	 *             if failed to parse the returned json to feed
	 */
	public Feed getHistory(int feedId, String startAt, String endAt, int samplingInterval) throws HttpException,
			ParseToObjectException;

	/**
	 * @param toUpdate
	 *            the feed to be updated over the API
	 * @return the feed that was passed in, on successful operation
	 * @throws HttpException
	 *             if failed to get feed over the API
	 */
	public Feed update(Feed toUpdate) throws HttpException;

	public void delete(int feedId) throws HttpException;

}
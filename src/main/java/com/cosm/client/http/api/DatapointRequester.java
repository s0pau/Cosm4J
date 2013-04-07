package com.cosm.client.http.api;

import java.util.Collection;

import com.cosm.client.http.exception.HttpException;
import com.cosm.client.http.impl.DatapointRequesterImpl;
import com.cosm.client.http.util.exception.ParseToObjectException;
import com.cosm.client.model.Datapoint;

public interface DatapointRequester
{
	/**
	 * @param feedId
	 *            indirect parent of the datapoint
	 * @param dataStreamId
	 *            parent of the datapoint
	 * @param toCreate
	 *            datapoint to be created over the API
	 * @return the datapoint with created fields populated, on successful
	 *         operation
	 * @throws HttpException
	 *             if failed to create datapoint over the API
	 */
	public Datapoint create(int feedId, String dataStreamId, Datapoint toCreate) throws HttpException;

	/**
	 * @param feedId
	 *            indirect parent of the datapoint
	 * @param dataStreamId
	 *            parent of the datapoint
	 * @param toCreate
	 *            one or many datapoints to be created over the API
	 * @return a collection of datapoints with created fields populated, on
	 *         successful operation
	 * @throws HttpException
	 *             if failed to create datapoint over the API
	 * @see DatapointRequesterImpl#create(int, String, Datapoint) for creating
	 *      and returning one datapoint
	 */
	public Collection<Datapoint> create(int feedId, String dataStreamId, Datapoint... toCreate) throws HttpException;

	/**
	 * @param feedId
	 *            indirect parent of the datapoint
	 * @param dataStreamId
	 *            parent of the datapoint
	 * @param datapointAt
	 *            the id of the datapoint to be retrieved
	 * @return a datapoint object parsed from the json returned from the API
	 * @throws HttpException
	 *             if failed to get datapoint over the API
	 * @throws ParseToObjectException
	 *             if failed to parse the returned json to datapoint
	 */
	public Datapoint get(int feedId, String dataStreamId, String datapointAt) throws HttpException, ParseToObjectException;

	/**
	 * @param feedId
	 *            indirect parent of the datapoint
	 * @param dataStreamId
	 *            parent of the datapoint
	 * @param startAt
	 * @param endAt
	 * @param samplingInterval
	 * @return a collection of datapoint objects matching the params, parsed
	 *         from the json returned from the API
	 * @throws HttpException
	 *             if failed to get datapoint over the API
	 * @throws ParseToObjectException
	 *             if failed to parse the returned json to datapoint
	 */
	public Collection<Datapoint> get(int feedId, String dataStreamId, String startAt, String endAt, int samplingInterval)
			throws HttpException, ParseToObjectException;

	/**
	 * @param feedId
	 *            indirect parent of the datapoint
	 * @param dataStreamId
	 *            parent of the datapoint
	 * @param toUpdate
	 *            datapoint to be updated over the API
	 * @return the datapoint that was passed in, on successful operation
	 * @throws HttpException
	 *             if failed to create datapoint over the API
	 */
	public Datapoint update(int feedId, String dataStreamId, Datapoint toUpdate) throws HttpException;

	/**
	 * @param feedId
	 *            the id of the parent of the datastream that holds the
	 *            datapoint
	 * @param dataStreamId
	 *            the id of the parent of the datapoint
	 * @param datapointAt
	 *            the id of the datapoint for the given datastreamId and feedId
	 *            to be deleted over the API
	 * @throws HttpException
	 *             if failed to delete the datapoint over the API
	 */
	public void delete(int feedId, String dataStreamId, String datapointAt) throws HttpException;

	/**
	 * @param feedId
	 *            the id of the parent of the datastream that holds the
	 *            datapoints
	 * @param dataStreamId
	 *            the id of the parent of the datapoints
	 * @param startAt
	 *            any datapoints starting from this date, inclusive will be
	 *            deleted over the API
	 * @throws HttpException
	 *             if failed to delete the datapoints over the API
	 */
	public void deleteMultiple(int feedId, String dataStreamId, String startAt) throws HttpException;
}
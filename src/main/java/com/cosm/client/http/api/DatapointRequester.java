package com.cosm.client.http.api;

import java.util.Collection;

import com.cosm.client.http.exception.HttpException;
import com.cosm.client.http.impl.DatapointRequesterImpl;
import com.cosm.client.http.util.exception.ParseToObjectException;
import com.cosm.client.model.Datapoint;

public interface DatapointRequester
{
	/**
	 * @param toCreate
	 *            datapoint to be created over the API
	 * @return the datapoint with created fields populated, on successful
	 *         operation
	 * @throws HttpException
	 *             if failed to create datapoint over the API
	 */
	public Datapoint create(Datapoint toCreate) throws HttpException;

	/**
	 * @param toCreate
	 *            one or many datapoints to be created over the API
	 * @return a collection of datapoints with created fields populated, on
	 *         successful operation
	 * @throws HttpException
	 *             if failed to create datapoint over the API
	 * @see DatapointRequesterImpl#create(int, String, Datapoint) for creating
	 *      and returning one datapoint
	 */
	public Collection<Datapoint> create(Datapoint... toCreate) throws HttpException;

	/**
	 * @param datapointAt
	 *            the id of the datapoint to be retrieved
	 * @return a datapoint object parsed from the json returned from the API
	 * @throws HttpException
	 *             if failed to get datapoint over the API
	 * @throws ParseToObjectException
	 *             if failed to parse the returned json to datapoint
	 */
	public Datapoint get(String datapointAt) throws HttpException, ParseToObjectException;

	/**
	 * @param toUpdate
	 *            datapoint to be updated over the API
	 * @return the datapoint that was passed in, on successful operation
	 * @throws HttpException
	 *             if failed to create datapoint over the API
	 */
	public Datapoint update(Datapoint toUpdate) throws HttpException;

	/**
	 * @param datapointAt
	 *            the id of the datapoint for the given datastreamId and feedId
	 *            to be deleted over the API
	 * @throws HttpException
	 *             if failed to delete the datapoint over the API
	 */
	public void delete(String datapointAt) throws HttpException;

	/**
	 * @param startAt
	 *            any datapoints starting from this date, inclusive will be
	 *            deleted over the API
	 * @throws HttpException
	 *             if failed to delete the datapoints over the API
	 */
	public void deleteMultiple(String startAt) throws HttpException;
}

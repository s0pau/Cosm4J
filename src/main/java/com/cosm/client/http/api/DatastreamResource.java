package com.cosm.client.http.api;

import java.util.Collection;

import com.cosm.client.http.exception.HttpException;
import com.cosm.client.http.impl.DatastreamRequester;
import com.cosm.client.http.util.exception.ParseToObjectException;
import com.cosm.client.model.Datastream;

public interface DatastreamResource
{

	/**
	 * @param feedId
	 *            parent of the datastream
	 * @param toCreate
	 *            datastream to be created datapoint to be created over the API
	 * @return the datastream with created fields populated, on successful
	 *         operation
	 * @throws HttpException
	 *             if failed to create datastream over the API
	 */
	public  Datastream create(int feedId, Datastream toCreate) throws HttpException;

	/**
	 * @param feedId
	 *            parent of the datastream
	 * @param toCreate
	 *            one or more datastreams to be created datapoint to be created
	 *            over the API
	 * @return a collection of datastream with created fields populated, on
	 *         successful operation
	 * @throws HttpException
	 *             if failed to create datastream over the API
	 * @see DatastreamRequester#create(int, Datastream) for creating and
	 *      returning one datastream
	 */
	public  Collection<Datastream> create(int feedId, Datastream... toCreate) throws HttpException;

	/**
	 * @param feedId
	 *            parent of the datastream
	 * @param dataStreamId
	 *            the id of the datastream to be retrieved
	 * @return a datastream object parsed from the json returned from the API
	 * @throws HttpException
	 *             if failed to get datastream over the API
	 * @throws ParseToObjectException
	 *             if failed to parse the returned json to datastream
	 */
	public  Datastream get(int feedId, String dataStreamId) throws HttpException, ParseToObjectException;

	/**
	 * 
	 * @param feedId
	 *            parent of the datastream
	 * @param toUpdate
	 *            datastream to be updated over the API
	 * @return the datastream that was passed in, on successful operation
	 * @throws HttpException
	 *             if failed to create datastream over the API
	 */
	public  Datastream update(int feedId, Datastream toUpdate) throws HttpException;

	/**
	 * @param feedId
	 *            the id of the parent of the datastream
	 * @param dataStreamId
	 *            the datastream to be deleted over the API
	 * @throws HttpException
	 *             if failed to delete the datastream over the API
	 */
	public  void delete(int feedId, String dataStreamId) throws HttpException;

}
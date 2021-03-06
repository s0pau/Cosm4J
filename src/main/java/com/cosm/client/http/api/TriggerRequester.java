package com.cosm.client.http.api;

import java.util.Collection;

import com.cosm.client.http.exception.HttpException;
import com.cosm.client.http.util.exception.ParseToObjectException;
import com.cosm.client.model.Trigger;

public interface TriggerRequester
{
	/**
	 * @param toCreate
	 *            the trigger to be created over the API
	 * @return the trigger that was passed in with the id field updated, on
	 *         successful operation
	 * @throws HttpException
	 *             if failed to create feed over the API
	 * @throws ParseToObjectException
	 *             if failed to parse the returned json to trigger
	 */
	public Trigger create(Trigger toCreate) throws HttpException;

	/**
	 * @param triggerId
	 *            the id of the trigger to be retrieved
	 * @return a feed object parsed from the json returned from the API
	 * @throws HttpException
	 *             if failed to get trigger over the API
	 * @throws ParseToObjectException
	 *             if failed to parse the returned json to trigger
	 */
	public Trigger get(Integer triggerId) throws HttpException, ParseToObjectException;

	/**
	 * 
	 * @param triggerId
	 *            the id of the trigger to be retrieved
	 * @param feedId
	 * @return a collection of trigger objects matching the params, parsed from
	 *         the json returned from the API
	 * @throws HttpException
	 *             if failed to get trigger over the API
	 * @throws ParseToObjectException
	 *             if failed to parse the returned json to trigger
	 */
	public Collection<Trigger> getByFeedId(int feedId) throws HttpException, ParseToObjectException;

	/**
	 * @param toUpdate
	 *            the trigger to be updated over the API
	 * @return the trigger that was passed in, on successful operation
	 * @throws HttpException
	 *             if failed to get trigger over the API
	 */
	public Trigger update(Trigger toUpdate) throws HttpException;

	/**
	 * @param triggerId
	 *            the id of the trigger to be deleted over the API
	 * @throws HttpException
	 *             if failed to delete the trigger over the API
	 */
	public void delete(Integer triggerId) throws HttpException;
}
JCosm
=====

<p>
Java Cosm Client <i>(formerly Pachube)</i> - wrapper for Cosm Api. 
# Overview
This is a RESTful java client for accessing Cosm Api. It uses Jersey Client (implementation of JAX-RS) for handling HTTP requests, while the response is decoupled from the implementation of the HTTP client implementation.   

# Status

TODO issue tracker link

## Maven dependency

To use cosm-java library on Maven-based projects, use following dependency:

    <dependency>
      <groupId>com.cosm.client</groupId>
      <artifactId>cosm-java-client</artifactId>
      <version>0.0.1-SNAPSHOT</version>
    </dependency>

(Look out for the most up-to-date version!)

# Quickstart

## Config
CosmConfig contains the configuration needed for the library. 
User preferences are loaded from config.properties into ConnectedObject, overwriting the default value if user preference is specified. 

## ConnectedObject
ConnectedObject is the interface for all objects that can be directly accessed via Cosm's restful API.

## RESTful Requests

RESTful request can be made to any implementation of ConnectedObjects(model) via the corresponding implementaion of Requesters: 

<ul>
<li>Feed -> FeedRequester</li>
<li>Datastream -> DatastreamRequester</li>
<li>Datapoint -> DatapointRequester</li>
<li>ApiKey -> ApiKeyRequester // TODO </li>
<li>Trigger -> TriggerRequester // TODO</li>
</ul>

<br/>On success, the Requester implementation will return the object post CRUD operation (with the exception of delete, returns void).
<br/>On faliure, the Requester implementation will throw:
<ul>
<li>HttpException, if the response status is not 2xx</li>
<li>ParseToObjectException, if the returned response cannot be parse into ConnectedObject implementations </li>
</ul> 

## Exception

CosmClientException - any exception thrown out of the cosm-java library is a subclass of this exception.

## Formats (CSV, XML, JSON)
// TODO 

## Mobile Feeds
// TODO 

## Mobile Feeds
// TODO 


// TODO General
<br/>- move docs to gh_pages
<br/>- use github maven
<br/>- use slf4j to log 

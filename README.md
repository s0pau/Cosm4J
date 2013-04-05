cosm-java
=========

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

ConnectedObject is the interface for all objects that can be directly accessed/modified via Cosm's restful API.

## CRUD Operation on ConnectedObject

Each ConnectedObject (model) has a corresponding interface for accessing the API in com.cosm.client.http.api package for all CRUD operations:
<ul>
<li>Feed -> FeedRequester</li>
<li>Datastream -> DatastreamRequester</li>
<li>Datapoint -> DatapointRequester</li>
<li>ApiKey -> ApiKeyRequester</li>
<li>Trigger -> TriggerRequester</li>
</ul>
Method calls takes ConnectedObject(s) as parameters and return ConnectedObject(s), therefore user of this library/downstream application does not need to be concerned about parsing or the underlying HTTP request/response details.

<br/>On success, the Requester implementation will return the object post CRUD operation:
<ul>
<li>*create* - returns the ConnectedObject(s) created, fully populated with fields generated post API call</li>
<li>*get (read)* - returns the ConnectedObject(s) retrieved</li>
<li>*update* - returns the updated ConnectedObject</li>
<li>*delete* - returns an empty ConnectedObject with the id only</li>
</ul>

<br/>On faliure, the Requester implementation will throw:
<ul>
<li>HttpException, if the response status is not 2xx</li>
</ul> 

<b>Actual implementation details:</b>
RESTful requests to Cosm's API are managed by DefaultRequestHandler; DefaultResponseHandler handles the response. Request and Response decouples this client from the HTTP client implementation.   
Parsing to and from ConnectedObjects to HTTP request/response body are encapsulated in com.cosm.client.http.util.ParseUtil. 
<ul>
<li>ParseToObjectException, if the returned response cannot be parse into ConnectedObject implementations </li>
<li>ParseFromObjectException, if the ConnectedObject implementation cannot be parse into specified format (e.g. json) </li>
</ul>

## Exception

CosmClientException - any exception thrown out of the cosm-java library is a subclass of this exception.

## Formats (JSON)

Currently only JSON format is supported.

## Mobile Feeds
// TODO 

## TODO General - Coming soon:
<br/>- move docs to gh_pages
<br/>- use github maven
<br/>- use slf4j to log 
<br/>- use maven properties on build

Cosm4J
=========

<p>
Java Cosm Client <i>(formerly Pachube)</i> - wrapper for Cosm Api. 
# Overview
This is a RESTful Java client for accessing Cosm Api. It uses Apache HttpComponent for handling HTTP requests while remaining decoupled. This fully featured Java library parses domain objects to and from Json.

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

To get started:
<ul>
<li>Update API Key and another config in properties</li>
<li>CosmService.instance() will give you access to all operations</li>
</ul>

## AppConfig

AppConfig contains the configuration needed for the library. 
Application settings are loaded from config.properties into AppConfig, overwriting the default value where setting is specified. 

## DomainObject

DomainObject is the model interface for all objects that can be directly accessed/modified via Cosm's RESTful API.
Each model has a equals, hashcode and deepEquals defined to streamline downstream processing.

## CosmService - CRUD Operation on DomainObject

All operations to Cosm API can be accessed via CosmService, which designed to be fluent. For example, here are some examples: 

	CosmService.instance().feed().create(<Feed object>); 
	CosmService.instance().datastream(feedId).create(<Datastream object>); 
	CosmService.instance().datastream(feedId).delete(datastreamId); 


<br/><b><i>Implementation details:</i></b><br/>
Each DomainObject has a corresponding requester interface for accessing the API in com.cosm.client.http.api package for all CRUD operations, and the access to these is made fluent via the user of the CosmService (locator).
<br/>The mapping between the model and the requesters are as follows:
<ul>
<li>Feed -> FeedRequester</li>
<li>Datastream -> DatastreamRequester</li>
<li>Datapoint -> DatapointRequester</li>
<li>ApiKey -> ApiKeyRequester</li>
<li>Trigger -> TriggerRequester</li>
</ul>
Method calls takes DomainObject(s) as parameters and return DomainObject(s), therefore user of this library/downstream application does not need to be concerned about parsing or the underlying HTTP request/response details.

<br/>On success, the Requester implementation will return the object post CRUD operation:
<ul>
<li>create - returns the DomainObject(s) created, fully populated with fields generated post API call</li>
<li>get (read) - returns the DomainObject(s) retrieved</li>
<li>update - returns the updated DomainObject</li>
<li>delete - returns an empty DomainObject with the id only</li>
</ul>

<br/>On failure, the Requester implementation will throw:
<ul>
<li>InvalidRequestException, if the request is invalid</li>
<li>HttpException, if the response status is not 2xx</li>
</ul> 

RESTful requests to Cosm's API are managed by DefaultRequestHandler; DefaultResponseHandler handles the response. Request and Response decouples this client from the HTTP client implementation.   
Parsing to and from DomainObjects to HTTP request/response body are encapsulated in com.cosm.client.http.util.ParseUtil. 
* ParseToObjectException, if the returned response cannot be parse into DomainObject implementations
* ParseFromObjectException, if the DomainObject implementation cannot be parse into specified format (e.g. json)

## Exception Hierarchy

CosmClientException - any exception thrown out of the cosm-java library is a subclass of this exception.

## Formats

Currently only JSON format is supported.

## Configuring HTTPClient

Simple timeouts and retries can be configured via config.properties, which configures the HTTPClient via HttpClientBuilder. 
For example, setting the following in properties file:

	http.connectionTimeout=5000 

is actually the same as writing the following code:

	HttpClientBuilder.getInstance().setConnectionTimeout(5000);
	
The properties file provides an easy accesss.
	
## Example

To make get a feed via the API, for example:

	Feed feed = CosmService.instance().feed().get(123);
	// the returned object will be populate with fields generated by the API

To make get a create several datapoints and then update them for the same datastream via the API, for example:

	Datapoint dp1 = new Datapoint();
	dp1.setAt("2013-01-01T00:00:00.000000Z");
	dp1.setValue("123");

	Datapoint dp2 = new Datapoint();
	dp2.setAt("2013-01-02T00:00:00.000000Z");
	dp2.setValue("456");

	// assuming your API key has permission to write 
	// to the feed:123 and datastream:"test_stream0"
	DatastreamRequester requester = CosmService.instance().datapoint(123, "test_stream-0");
	requester.create(dp1, dp2);
	
	dp1.setValue("234");
	requester.update(dp1);

## Javadoc

Run this to regenerate doc:

	javadoc -d doc/  -sourcepath src/main/java/ -subpackages com.cosm.client  -stylesheetfile src/main/res/doc_stylesheet.css 


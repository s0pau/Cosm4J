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

## CosmObject
CosmObject is the interface for all objects that can be directly accessed via Cosm's restful API.

## RESTful requests

RESTful request can ba made to any implementation of CosmObjects(model) via the corresponding implementaion of Requesters: 

Feed -> FeedRequester
Datastream -> DatastreamRequester
Datapoint -> DatapointRequester
ApiKey -> ApiKeyRequester // TODO 
Trigger -> TriggerRequester // TODO

On success, the Requester implementation will return the object post CRUD operation (with the exception of delete, returns void).
On faliure, the Requester implementation will throw:
<ul>
<li>HttpException, if the response status is not 2xx</li>
<li>ParseToObjectException, if the returned response cannot be parse into CosmObject implementations </li>
</ul> 

## Exception

CosmClientException - any exception thrown out of the cosm-java library is a subclass of this exception.

## Formats (CSV, XML, JSON)
// TODO 

## Mobile Feeds
// TODO 

# TODO
<ul>
<li>Java 7</li>
<li>Maven 3 integration</li>
<li>RESTful client using Jersey (implementation of JAX-RS)</li>
<li>TODO JSON/XML parsing to dataobjects for downstream OO handling</li>
<li>TODO add CSV support</li>
<li>TODO add web socket support</li>
<li>TODO add trigger support</li>
<li>TODO add slf4j logging</li>
<li>TODO add google-guice dependency injection</li>
</ul>


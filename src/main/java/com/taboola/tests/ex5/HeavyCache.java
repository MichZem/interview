package com.taboola.tests.ex5;

/**
 * Question 5
 * <p>
 * You are tasked with improving the efficiency of a cache heavy system, which has the following properties/architecture:
 * <p>
 * The system has 2 components, a single instance backend and several frontend instances.
 * <p>
 * The backend generates data and writes it to a relational database that is replicated to
 * <p>
 * multiple data centers.
 * <p>
 * The frontends handle client requests (common web traffic based) by reading data from the database and serving it. <br/>
 * Data is stored in a local cache for an hour before it expires and has to be retrieved again. The cache’s eviction policy is LRU based.
 * <p>
 * There are two issues with the implementation above:
 * <p>
 * It turns out that many of the database accesses are redundant because the underlying
 * <p>
 * data didn't actually change.
 * <p>
 * On the other hand, a change isn't reflected until the cache TTL elapses, causing
 * <p>
 * staleness issues.
 * <p>
 * Offer a solution that fixes both of these problems. How would the solution change if the data was stored in Cassandra and not a classic database?
 *
 *
 *
 * <b><i>MY ANSWER: </i></b> <br/>
 *
 * I would have modified the eviction policy : instead of dropping data that is older than 1 hour, we should evict data that has not been accessed for more than one hour <br/>
 * This will solve the first issue (but will worse the issue n.2 )<br/>
 *
 * As for the 2nd issue (change isn't reflected till cache TTL) , we might want to have a notification layer between backend / frontend services : <br/>
 * each data that has been modified will issue a notification that will reach each frontend instances ... I dont like this solution (not really scalable) <br/>
 * Otherwise to handle in a second DB table a list of key that have been recently modified <br/>
 * Periodically (but in a greater frequency than the 1 hour cycle), the frontend can ask whether some data have been modified since OFFSET (like "give me all the data keys that have been
 * modified since the last T time " ) <br/>
 * Then, for each key, the frontend should reload from DB the new data <br/>
 *
 * Another alternative is to use  ... cassandra , which enables a huge amount a READ operation : each READ is an O1 operation (like a Hash) <br/>
 */
public class HeavyCache {
}

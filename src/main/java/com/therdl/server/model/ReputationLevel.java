package com.therdl.server.model;

/**
 * Reputation is Likes given in the forums. Reputation can be positive or negative. After you reach a certain percentage and level of positive rep you become vouched for.
 * Reputation will be organised in ranks: New, Beginner, Journeyman, Veteran, Vouched.
 *	At each level rights on the RDL increase. Rights will include: 
 *	- post a SNIP, reference, coment SNIP
 *	- post a FastCap, ref, comment
 * - post a Material, ref comment
 * - post a Service Offer, ref comment
 * @author Alex
 *
 */
public enum ReputationLevel {
	 NEW, BEGINNER, JOURNEYMAN, VETERAN, VOUCHED; 	
}

package com.auction.data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class AuctionUnit implements Serializable{
	/**
	 * 
	 */
	//it contains data of a particular auction
	private static final long serialVersionUID = -6997160052045340683L;
	private int id;
	private String unitDesc = null;
	private int startprice;
	private int minprice;
	private String closeReason = null;
	private int closedStatus;
	private String sellerName = null;
	private String winner = null;
	private int currentBid;
	private String bidderName=null;
	private String bidderEmail = null;
	private Map<String, Integer> auctionBidderList = new HashMap<String, Integer>();
	

	public String getBidderName() {
		return bidderName;
	}

	public void setBidderName(String bidderName) {
		this.bidderName = bidderName;
	}

	public String getBidderEmail() {
		return bidderEmail;
	
	}

	public void setBidderEmail(String bidderEmail) {
		this.bidderEmail = bidderEmail;
	}

	public int getCurrentBid() {
		return currentBid;
	}

	public void setCurrentBid(int currentBid) {
		this.currentBid = currentBid;
	}

	public String getWinner() {
		return winner;
	}

	public void setWinner(String winner) {
		this.winner = winner;
	}

	public String getSellerName() {
		return sellerName;
	}

	public void setSellerName(String sellerName) {
		this.sellerName = sellerName;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUnitDesc() {
		return unitDesc;
	}

	public void setUnitDesc(String unitDesc) {
		this.unitDesc = unitDesc;
	}

	public int getStartprice() {
		return startprice;
	}

	public void setStartprice(int startprice) {
		this.startprice = startprice;
	}

	public int getMinprice() {
		return minprice;
	}

	public void setMinprice(int minprice) {
		this.minprice = minprice;
	}

	public String getCloseReason() {
		return closeReason;
	}

	public void setCloseReason(String closeReason) {
		this.closeReason = closeReason;
	}

	public int getClosedStatus() {
		return closedStatus;
	}

	public void setClosedStatus(int closedStatus) {
		this.closedStatus = closedStatus;
	}

	@Override
	public String toString() {
		return "id= " + id + "\nDescription is " + unitDesc
				+ "\nStarting Price is " + startprice
				+ "\nMinimum Bidding Price " + minprice
				+ "\nClosing Status "+closedStatus
				+"\nClosing Reason "+closeReason
				+"\nSeller Name "+sellerName;
	}

	public Map<String, Integer> getAuctionBidderList() {
		return auctionBidderList;
	}

	public void addAuctionBidder(String bidderName,int price) {
		this.auctionBidderList.put(bidderName, price);
	}

}

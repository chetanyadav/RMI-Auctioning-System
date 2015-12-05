package com.auction.remote; // whats this entire java file for? it's called package. just to segragate .java files
// what do we do in this package?
//e.g. you have got mp3 files, txt files, doc files and avi files.
//you dont put everything in the same folder. right? you create diff folder for storing similar files.
//here we create package "similar to folder" to put classes those are similar.

import java.rmi.RemoteException; // and why do we achieve in this class? i.e why do we implement the auctionbidder?
//AuctionBidder is the interface that declares some method. this class will have to implement those methods
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import com.auction.data.AuctionData;
import com.auction.data.AuctionUnit;

public class AuctionBidderImpl extends UnicastRemoteObject implements AuctionBidder {
	//And the bidder cant directly access methods of d interface so we create this impl. class?
	private String name;
	/**
	 * 
	 */
	private static final long serialVersionUID = -3829659235331477856L;

	public AuctionBidderImpl(String bidderName) throws RemoteException {
		super();
		this.name = bidderName;
	}

	@Override
	public int bid(int id, int price, String name, String email)  {
		int result =0; 
		//get the item from the list
		AuctionUnit AuctionUnit = AuctionData.getAuctionUnit(id);
		if(AuctionUnit!=null){
			//validate bid
			int minPrice = AuctionUnit.getMinprice();
			int currentBid = AuctionUnit.getCurrentBid();
			if(price<minPrice){
				result=3; 
			}else if(currentBid>price){
				result=2;
			} else{
				AuctionUnit.setCurrentBid(price);
				AuctionUnit.setBidderName(name);
				AuctionUnit.setBidderEmail(email);
				AuctionUnit.addAuctionBidder(name, price);
				result=1;
			}
		}else{
			result=4;
		}
				
		return result;
	}

	@Override
	public List<AuctionUnit> getAll() {
		// get the item list
		return AuctionData.getActiveAuctionUnits();
	}

	@Override
	public String getMyName() throws RemoteException {
		// TODO Auto-generated method stub
		return this.name;
	}

}

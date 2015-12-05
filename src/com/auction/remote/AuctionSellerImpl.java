package com.auction.remote;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import com.auction.data.AuctionData;
import com.auction.data.AuctionUnit;

public class AuctionSellerImpl extends UnicastRemoteObject implements AuctionSeller {
	private String sellerName = null;
	
	public AuctionSellerImpl(String name) throws RemoteException {
		super();
		this.sellerName = name;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -4833762887200190674L;

	@Override
	public int create(AuctionUnit auctionUnit) {
		//add an action item to the list
		boolean added = AuctionData.addAuctionUnit(auctionUnit);
		if(added){
			return 1;
		}else{
			return 0;
		}
	}

	@Override
	public void close(int id, String closeReason) {		
		//get the item from list
		AuctionUnit unit = AuctionData.getAuctionUnit(id, sellerName);
		//set close status
		unit.setClosedStatus(1);
		//set close reason
		unit.setCloseReason(closeReason);
		//update the list
		AuctionData.updateAuctionUnit(unit);
	}

	@Override
	public AuctionUnit getAuctionUnit(int id) {
		//get the item from list
		AuctionUnit unit = AuctionData.getAuctionUnit(id, sellerName);
		return unit;
	}

	@Override
	public String getMyName() {
		//get seller's name
		return sellerName;
	}

	
	@Override
	public int closeWinner(int id, String winnerName) throws RemoteException {
		//get the item from list
		AuctionUnit unit = AuctionData.getAuctionUnit(id, sellerName);
		if(unit!=null){
			int currentPrice = unit.getCurrentBid();
			int minPrice = unit.getMinprice();
			if(currentPrice>minPrice){
				//set it's close status
				unit.setClosedStatus(1);
				//set auction winner
				unit.setWinner(winnerName);
				//update the list
				AuctionData.updateAuctionUnit(unit);
				return 1;
			}else{
				return 0;
			}
			
		}else{
			
			return -1;
		}
		
		
	}

	@Override
	public List<AuctionUnit> getAuctionUnits()
			throws RemoteException {
		return AuctionData.getAuctionUnit(sellerName);
	}

	@Override
	public int getNextId() throws RemoteException {
		
		return AuctionData.getListSize()+1;
	}

}

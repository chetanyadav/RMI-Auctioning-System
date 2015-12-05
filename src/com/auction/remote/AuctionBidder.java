package com.auction.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import com.auction.data.AuctionUnit;

public interface AuctionBidder extends Remote{
	public int bid(int id, int price, String name, String email) throws RemoteException;
	public List<AuctionUnit> getAll() throws RemoteException;
	public String getMyName() throws RemoteException;
	
	//bidder can perform these three actions only so we put these methods in the interface.
}

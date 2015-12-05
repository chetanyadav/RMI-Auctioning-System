package com.auction.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import com.auction.data.AuctionUnit;

public interface AuctionSeller extends Remote{
	public int create(AuctionUnit AuctionUnit) throws RemoteException;
	public void close(int id, String closeReason) throws RemoteException;
	public int closeWinner(int id, String winnerName) throws RemoteException;
	public AuctionUnit getAuctionUnit(int id) throws RemoteException;
	public List<AuctionUnit> getAuctionUnits() throws RemoteException;
	public String getMyName() throws RemoteException;
	public int getNextId() throws RemoteException;
}

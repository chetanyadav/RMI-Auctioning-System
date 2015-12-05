package com.auction.data;
//What are AUctionData and AuctionUnit? What do they contain?
//AuctionData contains the list of AuctionUnit and various methods that works on AuctionUnit
//so everytime a new auction is created, we store that auction in this list.
//So basically, auctionunit tells us everything about an auction(like winner of auction, bid etc.) and the auctiondata tells us
//how many such items are there?
//right. auction data is the collection of auction unit.
//TO ADD Or UPDATE an AUCTION to the STORE

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class AuctionData {
	
	private static List<AuctionUnit> auctionUnits = new ArrayList<AuctionUnit>();
	
	static {

		try {
			FileInputStream auctionsIn = new FileInputStream("auctions.ser");
			ObjectInputStream auctionObjIn = new ObjectInputStream(
					auctionsIn);
			Object obj = auctionObjIn.readObject();
			if (obj != null) {
				auctionUnits = (List<AuctionUnit>) obj;
				//auctionId = auctionUnits.size();
			}
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		} catch (ClassNotFoundException e) {
		}
	}
	
	
	private static void saveAuctionData(){
		try {
			FileOutputStream fileOut = new FileOutputStream("auctions.ser");
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(auctionUnits);
			out.close();
			fileOut.close();
		} catch (IOException i) {
			i.printStackTrace();
		}
	}
	
	/*
	 * this method add an auction unit to the auction store
	 */
	public static boolean addAuctionUnit(AuctionUnit auctionUnit){
		boolean added = false;
		boolean found = false;
		for (AuctionUnit unit : auctionUnits) {
			int id = unit.getId();
			int auctionUnitId = auctionUnit.getId();
			if(id == auctionUnitId){
				found = true;
				break;
			}
		}
		if(!found){
			auctionUnits.add(auctionUnit);
			added = true;
		}
		
		saveAuctionData();
		return added;
	}
	
	/*
	 * this method updates an unit present in the store
	 */
	public static void updateAuctionUnit(AuctionUnit auctionUnit){
		int id = auctionUnit.getId();
		AuctionUnit matchingUnit = null;
		for (AuctionUnit unit : auctionUnits) {
			int curId = unit.getId();
			if(curId == id){
				matchingUnit = unit;
				break;
			}
		}
		
		//if unit found, replace it with new unit
		if(matchingUnit!=null){
			auctionUnits.remove(matchingUnit);
			auctionUnits.add(auctionUnit);
		}
		saveAuctionData();
	}
	
	/*
	 * this method returs a list of all the active auction units
	 */
	
	public static List<AuctionUnit> getActiveAuctionUnits() {
		List<AuctionUnit> units = new ArrayList<AuctionUnit>();
		
		for (AuctionUnit auctionUnit : auctionUnits) {
			int closed = auctionUnit.getClosedStatus();
			if(closed!=1){
				units.add(auctionUnit);
			}
		}
		
		return units;
	}
	
	/*
	 * this method returns a list of all the closed auction units
	 */
	public static List<AuctionUnit> getClosedAuctionUnits() {
		List<AuctionUnit> units = new ArrayList<AuctionUnit>();
		
		for (AuctionUnit auctionUnit : auctionUnits) {
			int closed = auctionUnit.getClosedStatus();
			if(closed==1){
				units.add(auctionUnit);
			}
		}
		
		return units;
	}
	
	/*
	 * this method returns an auction unit based on unit i and seller name
	 */
	public static AuctionUnit getAuctionUnit(int id, String sellerName) {
		AuctionUnit unit = null;
		
		for (AuctionUnit auctionUnit : auctionUnits) {
			int curId = auctionUnit.getId();
			if(curId==id){				
				if(auctionUnit.getSellerName().equalsIgnoreCase(sellerName)){
					unit = auctionUnit;
				}
			}
		}
		
		return unit;
	}
	
	
	/*
	 * this method returns an auction unit based on id
	 */
	public static AuctionUnit getAuctionUnit(int id) {
		AuctionUnit unit = null;
		
		for (AuctionUnit auctionUnit : auctionUnits) {
			int curId = auctionUnit.getId();
			if(curId==id){				
				unit = auctionUnit;
			}
		}
		
		return unit;
	}
	
	/*
	 * this method returns all the auction units based on a seller's name
	 */
	public static List<AuctionUnit> getAuctionUnit(String sellerName) {
		List<AuctionUnit> units = new ArrayList<AuctionUnit>();
		
		for (AuctionUnit auctionUnit : auctionUnits) {
			String name = auctionUnit.getSellerName();
			if(name.equalsIgnoreCase(sellerName)){
				units.add(auctionUnit);
			}
		}
		
		return units;
	}
	
	public static int getListSize(){
		return auctionUnits.size();
	}

	
}

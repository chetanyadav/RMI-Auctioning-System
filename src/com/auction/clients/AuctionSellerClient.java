package com.auction.clients;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.auction.data.AuctionUnit;
import com.auction.remote.AuctionSeller;

public class AuctionSellerClient {

	private static String sellerName = null;
	private static AuctionSeller seller = null;

	/*this method adds an auction into the system
	 * 
	 */
	private static void add() {

		BufferedReader reader = new BufferedReader(new InputStreamReader(
				System.in));
		try {

			
			//get the item id
			int id = seller.getNextId();

			System.out.print("Enter description of item >> ");
			String lineContent = reader.readLine();
			//get the item description
			String unitDesc = lineContent;

			System.out.print("Enter Starting Price >> ");
			lineContent = reader.readLine();
			//get the item starting price
			int startPrice = Integer.valueOf(lineContent);

			System.out.print("Enter Reserve Price >> ");
			lineContent = reader.readLine();
			//get the item reserve price
			int minPrice = Integer.valueOf(lineContent);

			//create a new auction item object
			AuctionUnit auctionUnit = new AuctionUnit();
			auctionUnit.setId(id);
			auctionUnit.setUnitDesc(unitDesc);
			auctionUnit.setStartprice(startPrice);
			auctionUnit.setMinprice(minPrice);
			auctionUnit.setSellerName(sellerName);
			int added = seller.create(auctionUnit);
			if(added ==1){
				System.out.println("Auction Item added");
			}else{
				System.out.println("Can not add the item with same ID");
			}

		} catch (Exception e) {
			System.out.println("Auction Item not added. Retry!");
		}

	}

	/*this method closes an auction
	 * 
	 */
	private static void closeReason() {
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				System.in));
		try {

			System.out.print("Enter the Item's id >> ");
			String lineContent = reader.readLine();
			//get the item id
			int id = Integer.valueOf(lineContent);

			System.out.print("Enter Closing Reason >> ");
			lineContent = reader.readLine();

			//close the auction
			seller.close(id, lineContent);
			System.out.println("Auction Item Closed");

		} catch (Exception e) {
			System.out.println("Auction Item Not Closed. Retry!");
		}

	}

	/*this ethos closes an auction when the winner is selected
	 * 
	 */
	private static void closeWinner() {
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				System.in));
		try {

			System.out.print("Enter the Item's id >> ");
			String lineContent = reader.readLine();
			//get the item id
			int id = Integer.valueOf(lineContent);
			
			AuctionUnit auctionUnit = seller.getAuctionUnit(id);
			Map<String, Integer> auctionBidderList = auctionUnit.getAuctionBidderList();
			Set<String> keySet = auctionBidderList.keySet();
			if(keySet.size() > 0 ){
				System.out.println("List of Bidders :-");
			}
			for (String key : keySet) {
				int price = auctionBidderList.get(key);
				System.out.println(key+"\t\t"+price);
			}

			System.out.print("Enter Winner's Name >> ");
			//get the winner's name
			lineContent = reader.readLine();
			//closer the bid
			int close = seller.closeWinner(id, lineContent);
			if(close== -1){
				System.out.println("Item not Found");
			}else if(close ==0){
				System.out.println("Min Price Bid has not been placed yet");
			}else if(close ==1){
				System.out.println("Auction Item Closed");
			}
			

		} catch (Exception e) {
			System.out.println("Auction Item Not Closed. Retry!");
		}

	}

	/* this method displays a particular auction details based on it's id
	 * 
	 */
	private static void get() {
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				System.in));
		try {

			System.out.println("Enter the Item's id");
			String lineContent = reader.readLine();
			//get the item id
			int id = Integer.valueOf(lineContent);

			//get the auction item
			AuctionUnit auctionUnit = seller.getAuctionUnit(id);

			System.out.println("Below is the Aution Item state");
			//display the details
			System.out.println(auctionUnit);

		} catch (Exception e) {
			System.out.println("Exception occurred. Retry!");
		}
	}

	/* this method displays details about all the currently active auction items
	 * 
	 */
	private static void getAll() {

		try {
			//get list of all the auction items
			List<AuctionUnit> items = seller.getAuctionUnits();
			for (AuctionUnit AuctionUnit : items) {
				System.out.println("Item id: " + AuctionUnit.getId());
				System.out.println("Item Desc: " + AuctionUnit.getUnitDesc());
				int closeStatus = AuctionUnit.getClosedStatus();
				if (closeStatus != 1) {
					System.out.println("Item's Current Bid is: "
							+ AuctionUnit.getCurrentBid());
				} else {
					String winnerName = AuctionUnit.getWinner();
					if (winnerName == null) {
						System.out.println("Closing reason is: "
								+ AuctionUnit.getCloseReason());
					} else {
						System.out.println("Winner is: " + winnerName);
					}
				}
			}
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				System.in));
		System.out.print("Enter the Seller Name >> ");
		try {
			//get the seller name
			sellerName = reader.readLine();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		try {
			//look up the remote seller object
			Object obj = Naming.lookup(sellerName);
			if (obj != null) {
				seller = (AuctionSeller) obj;
			}

			boolean breakopr = false;
			while (true) {
				if (breakopr) {
					System.out.println("Getting out of operations "
							+ seller.getMyName());
					break;
				}

				//present a menu to the seller
				System.out.println("Select Any of the Below Options: ");
				System.out.println("1 - Add an item");
				System.out.println("2 - Close an item by Winner");
				System.out.println("3 - Close an item by Reason");
				System.out.println("4 - Select an item");
				System.out.println("5 - See all the associated items");
				System.out.println("9 - Quit");
				System.out.println();
				System.out.print("Enter your choice >>");
				

				String choice;
				try {
					choice = reader.readLine();
					int choiceVal = 0;
					if("9".equals(choice)){
						choiceVal =9;
					}else if("1".equals(choice)){
						choiceVal =1;
					}else if("2".equals(choice)){
						choiceVal =2;
					}else if("3".equals(choice)){
						choiceVal =3;
					}else if("4".equals(choice)){
						choiceVal =4;
					}else if("5".equals(choice)){
						choiceVal =5;
					}
					switch (choiceVal) {
					case 9:
						breakopr = true;
						break;

					case 1:
						add();
						break;

					case 2:
						closeWinner();
						break;

					case 3:
						closeReason();
						break;

					case 4:
						get();
						break;

					case 5:
						getAll();
						break;

					default:
						break;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}

			}

		} catch (NotBoundException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		}

	}
}

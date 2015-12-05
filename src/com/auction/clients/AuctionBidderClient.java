package com.auction.clients;//here all methods for d bidder are contained

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;

import com.auction.data.AuctionUnit;
import com.auction.remote.AuctionBidder;

public class AuctionBidderClient {
	
	private static String buyerName = null; // what are these 2 lines for? they are class variable
	private static AuctionBidder buyer = null;// why do we make class var.? for storing the state. 
	
	/* this method allows buyer to place bid on the auction item
	 * 
	 */
	private static void bid() {

		BufferedReader reader = new BufferedReader(new InputStreamReader(
				System.in));
		try {

			System.out.println("Enter id of Item you want to bid for");
			String lineContent = reader.readLine();
			//get the item id
			int id = Integer.valueOf(lineContent);

			
			System.out.println("Enter Bidding Price");
			lineContent = reader.readLine();
			//get the bidding price
			int biddingPrice = Integer.valueOf(lineContent);

			System.out.println("Enter Buyer Name");
			lineContent = reader.readLine();
			//get buyer's name
			String buyerName = lineContent;
			
			System.out.println("Enter Buyer Email");
			lineContent = reader.readLine();
			//get buyer's email id
			String buyerEmail = lineContent;

			//place bid
			int result = buyer.bid(id, biddingPrice, buyerName, buyerEmail); //result of the bid action.
			if(result == 2){
				System.out.println("Sorry Bid is not high enough : Bid Not placed");
			}else if(result == 3){
				System.out.println("Sorry Minimum Price not met : Bid Not placed");
			}
			else if(result==1){
				System.out.println("Bid placed on Auction Item");
			}else if(result == 4){
				System.out.println("Sorry Item not Found");
			}

		} catch (Exception e) {
			System.out.println("Unfortunetly, bid not placed on Auction Item. Retry");
		}

	}
	
	/* this method displays all the active auctions on which a buyer could place bid.
	 * 
	 */
	private static void seeAll(){
		try {
			//get list of the auctions
			List<AuctionUnit> units = buyer.getAll();// this list stores all the auctions.
			for (AuctionUnit unit : units) { // working of this line?it's enhanced for loop 
				//display all the info about the item
				System.out.println("Item id: "+unit.getId()+" Item Desc: "+unit.getUnitDesc());
				int currentBid = unit.getCurrentBid();
				if(currentBid != 0){
					System.out.println("Current Bid: "+currentBid);
				}
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				System.in));
		System.out.print("Enter the Buyer Name >> ");
		try {
			//get the buyer's name
			buyerName = reader.readLine();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		try {
			//look up the buyer's remote interface
			Object obj = Naming.lookup(buyerName);
			if (obj != null) {
				buyer = (AuctionBidder) obj;
			}

			boolean breakopr = false;
			while (true) {
				if (breakopr) {
					break;
				}

				//present a menu to the buyer to do various operations
				System.out.println("Select Any of the Below Options: ");
				System.out.println("1 - Bid an item");
				System.out.println("2 - See all Active items");
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
					}
					switch (choiceVal) {
					case 9:
						breakopr = true;
						break;

					case 1:
						bid();
						break;

					case 2:
						seeAll();
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

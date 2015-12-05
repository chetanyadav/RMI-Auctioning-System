package com.auction.test;
//Basically what is the idea behind this file? What does it help us achieve?
//1. it starts the rmi server.
//2. it either reads the bidder/seller .ser file and registers in the registry
//3. provides a menu through which client could add bidder/seller.
//thats it. so this class is actually a gateway to our auction system app.
//basically without registering either the seller or d buyer, we cant progress with the auctioning system.

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;

import com.auction.remote.AuctionBidder;
import com.auction.remote.AuctionBidderImpl;
import com.auction.remote.AuctionSeller;
import com.auction.remote.AuctionSellerImpl;

public class AuctionClientMain {
	private static List<AuctionSeller> sellers= new ArrayList<AuctionSeller>(); //sellers gets all methods of AuctionSeller
	private static List<AuctionBidder> buyers= new ArrayList<AuctionBidder>(); //similarly
	private static Registry registry = null;
	static {

		try {
			if (registry == null) {
				// create a registry
				registry = LocateRegistry.createRegistry(1099);
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	private static void read() { // here, what do we read in this method? it reads the bidder.ser file and loads the data.
		try {
			FileInputStream fileBidderIn = new FileInputStream("bidder.ser");
			ObjectInputStream bidderObjectIn = new ObjectInputStream(
					fileBidderIn);
				try{
					Object obj = bidderObjectIn.readObject();
					if (obj != null) {
						List<AuctionBidder> e = (List<AuctionBidder>) obj;
						
						for (AuctionBidder bidder : e) { // it's for loop. it reads all bidders from the file and binds to the registry
							String bidderName = bidder.getMyName();
							registry.rebind(bidderName, bidder);
							buyers.add(bidder);
							System.out.println("Following Bidder is Bound "
									+ bidderName);
						}
					} 
				}catch (EOFException e) {
					
				}
				
			bidderObjectIn.close();
			fileBidderIn.close();

			FileInputStream fileSellerIn = new FileInputStream("seller.ser"); // here same thing but with seller
			ObjectInputStream sellerObjectIn = new ObjectInputStream(
					fileSellerIn);
				try{
					Object obj = sellerObjectIn.readObject();
					if (obj != null) {
						List<AuctionSeller> e = (List<AuctionSeller>) obj;
						for (AuctionSeller seller : e) {
							String sellerName = seller.getMyName();
							registry.rebind(sellerName, seller);
							sellers.add(seller);
							System.out.println("Following Seller is Bound "
									+ sellerName);
						}
						
					} 
				}catch (EOFException e) {
				}
				
			sellerObjectIn.close();
			fileSellerIn.close();

		} catch (IOException i) {
//			i.printStackTrace();
		} catch (ClassNotFoundException c) {
//			c.printStackTrace();
		} 

	}

	public static void main(String[] args) {
		read();
		bind();
	}

	/**
	 * this method adds a seller into the system by their name.
	 * 
	 */
	private static void addSeller() {
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				System.in));
		try {
			System.out.println("Enter seller name : ");
			// get seller's name
			String sellerName = reader.readLine();
			AuctionSeller e = new AuctionSellerImpl(sellerName);
			// bind the seller to the rmi registry, now buyer can access methods of the seller
			registry.rebind(sellerName, e);
			sellers.add(e); //what's this add here? it adds the seller to the list. and we save this list to the .ser file
			save();// where is this add method? not save, add? above this line. "sellers" above is the list of seller.
			//so everytime we create a new seller we add that seller to the list and save the list to the file. oh ok!!!
			

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * this method adds a bidder into the system by their name.
	 * 
	 */
	private static void addBidder() {
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				System.in));
		try {
			System.out.println("Enter buyer name : ");
			// get 'Bidders name
			String bidderName = reader.readLine();
			AuctionBidder e = new AuctionBidderImpl(bidderName);
			// bind the bidder to the rmi registry
			registry.rebind(bidderName, e);
			buyers.add(e);
			save();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void save(){
		try {
			FileOutputStream fileOut = new FileOutputStream("bidder.ser");
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(buyers);
			out.close();
			fileOut.close();
//			System.out.println("Serialized data is saved in bidder.ser");
		} catch (IOException i) {
			i.printStackTrace();
		}
		
		try {
			FileOutputStream fileOut = new FileOutputStream("seller.ser");
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(sellers);
			out.close();
			fileOut.close();
			System.out.println("Serialized data is saved in seller.ser");
		} catch (IOException i) {
			i.printStackTrace();
		}
	}

	private static void bind() {

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					System.in));
			boolean breakopr = false;
			while (true) {
				if (breakopr) {
					break;
				}

				// present a menu to system admin for registering Bidder/Sellers
				// into the system
				System.out.println("Select Any of the Below Options: ");
				System.out.println("1 - Add a Seller");
				System.out.println("2 - Add a Buyer");
				System.out.println("3 - quit");
				System.out.println();
				System.out.print("Enter your choice >>");

				String choice;
				choice = reader.readLine();
				int choiceVal = 0;
				if("1".equals(choice)){
					choiceVal =1;
				}else if("2".equals(choice)){
					choiceVal =2;
				}else if("3".equals(choice)){
					choiceVal =3;
				}
				switch (choiceVal) {
				case 3:
					save();
					breakopr = true;
					break;

				case 1:
					addSeller();
					break;

				case 2:
					addBidder();
					break;

				default:
					break;
				}
			}

		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

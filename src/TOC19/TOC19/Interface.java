package TOC19;

/*
*    TOC19 is a simple program to run TOC payments within a small group. 
*    Copyright (C) 2014  Jarrah Gosbell
*
*    This program is free software: you can redistribute it and/or modify
*    it under the terms of the GNU General Public License as published by
*    the Free Software Foundation, either version 3 of the License, or
*    (at your option) any later version.
*
*    This program is distributed in the hope that it will be useful,
*    but WITHOUT ANY WARRANTY; without even the implied warranty of
*    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*    GNU General Public License for more details.
*
*    You should have received a copy of the GNU General Public License
*    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

/**
* @author Jarrah Gosbell
*/


import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.InnerShadow;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public final class Interface extends Application
{
	/** The class which the user logs into and which handles all interaction with the program. */
	private final WorkingUser workingUser; // Place for all data to go through
	/** the number of horizontal pixels, defaulted to 1024 but set by the settings class */
	private static int horizontalSize = 1024;
	/** the number of vertical pixels, defaulted to 576 but set by the settings class */
	private static int verticalSize = 576;
	/** The text size of the program, set by the settings class */
	private final int textSize;


    /**
     * Create an interface instance with it's parameters set by the config file
      * @throws IOException
     */
	public Interface() throws IOException
	{
		Settings config = new Settings();
		workingUser = new WorkingUser();
		String[] settings = config.interfaceSettings();
		horizontalSize = Integer.parseInt(settings[0]);
		verticalSize = Integer.parseInt(settings[1]);
		textSize = Integer.parseInt(settings[2]);
		//initalize the variables created above
		
	}

    /**
     * The user part of the GUI
     * @param primaryStage The base stage of the program
     */
	@Override
	public void start(Stage primaryStage)
	{
		//Add some graphical exception handling xD
		Thread.setDefaultUncaughtExceptionHandler((t, e) -> Platform.runLater(() -> showErrorDialog(e)));
		Thread.currentThread().setUncaughtExceptionHandler((t, e1) -> showErrorDialog(e1));

		// create the layout
		primaryStage.setTitle("TOC19"); // set the window title. 
		GridPane grid = new GridPane(); // create the layout manager
//	    grid.setGridLinesVisible(true); // used for debugging object placement
		grid.setAlignment(Pos.CENTER); 
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(15, 15, 15, 15)); // window borders

		// create the thread which will be used for logging the user out after a given time. 
		// create label for input
		Text inputLabel = new Text("Enter your PMKeyS");
		inputLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, textSize));
		grid.add(inputLabel, 0,0); // place in top left hand corner

		// create input textfield
		TextField input = new TextField();
		grid.add(input, 1,0); // place to the right of the input label
		input.requestFocus(); // make this the focus for the keyboard when the program starts
		Text userLabel = new Text("Error"); 
		
		// create label and text field for totalOutput
		Text totalLabel = new Text("				Total:");
		totalLabel.setTextAlignment(TextAlignment.RIGHT);
		grid.add(totalLabel, 2,8); // place at the bottum right, before total and purchase. 
		TextField total = new TextField(String.valueOf("$" + workingUser.getPrice())); // create a textfield with the price of the currant checkout. 
		total.setEditable(false); // stop the user thinking they can change the total price. 
		grid.add(total, 3,8); // add to the right of total label. 
				
		// create button to enter data from input
		Button enterBarCode = new Button("OK"); // button linked to action on input text field.
    	grid.add(enterBarCode, 2,0, 2,1); // add to the direct right of the input text field

        //create product error text
        Text productError = new Text();
        grid.add(productError, 1,8);
		
		
		// create the lists for the checkout. 
		
		SplitPane checkoutOut = new SplitPane();
		checkoutOut.setPrefHeight(500);
		ListView<String> itemList = new ListView<>();
		ObservableList<String> items = FXCollections.observableArrayList();
		items.setAll(workingUser.getCheckOutNames());
		itemList.setItems(items);
		ListView<String> priceList = new ListView<>();
		ObservableList<String> prices = FXCollections.observableArrayList();
		prices.setAll(workingUser.getCheckOutPrices());
		priceList.setItems(prices);
		checkoutOut.getItems().addAll(itemList, priceList);
		checkoutOut.setDividerPositions(0.8f);
		itemList.setSelectionModel(priceList.getSelectionModel());
		itemList.getSelectionModel().selectedItemProperty().addListener(
				(ObservableValue<? extends String> ov, String old_val, String selectedOption) -> {
					priceList.scrollTo(itemList.getSelectionModel().getSelectedIndex());
				});
		priceList.getSelectionModel().selectedItemProperty().addListener(
				(ObservableValue<? extends String> ov, String old_val, String selectedOption) -> {
					itemList.scrollTo(priceList.getSelectionModel().getSelectedIndex());
				});
				
		grid.add(checkoutOut, 0, 1, 7, 7);

//		bind(itemList, priceList);
        //listen on enter product barcode button
		enterBarCode.setOnAction((ActionEvent e) -> {
				if(!workingUser.userLoggedIn()) { // treat the input as a PMKeyS
					int userError;
                    userError = PMKeySEntered(input.getText()); // take the text, do user logon stuff with it.
			
			
			
					if(workingUser.userLoggedIn()) {
						Thread thread = new Thread(new Runnable()
						{

							@Override
							public void run()
							{
								try {
									Thread.sleep(1); // after this time, log the user out.
									workingUser.logOut(); // set user number to -1 and delete any checkout made. 

									grid.getChildren().remove(userLabel); // make it look like no user is logged in
									inputLabel.setText("Enter your PMKeyS"); // set the input label to something appropriate. 
									total.setText(String.valueOf("$" + workingUser.getPrice())); // set the total price to 0.00.
								}
								catch (InterruptedException e) {
									// do nothing here. 
								}
							}
						});
						thread.setDaemon(true);
						thread.setPriority(Thread.MIN_PRIORITY);
						thread.start();
						thread.interrupt();
						flashColour(input, 1500, Color.AQUAMARINE);
						userLabel.setText(workingUser.userName(userError) + "—$" + workingUser.getUserBill()); // find the name of those who dare log on.
						inputLabel.setText("Enter Barcode"); // change the label to suit the next action. 
						grid.getChildren().remove(userLabel); // remove any error labels which may have appeared. 
						grid.add(userLabel, 3,0); // add the new user label
						// the above two are done as we do not know whether a user label exists there. Adding two things to the same place causes an exception. 
						input.clear(); // clear the PMKeyS from the input ready for product bar codes. 
						
					}
					else {
						input.clear(); // there was an error with the PMKeyS, get ready for another. 
						userLabel.setText(workingUser.userName(userError)); // tell the user there was a problem. Maybe this could be done better.
						grid.getChildren().remove(userLabel); // Remove a userlabel, as above. 
						grid.add(userLabel, 3,0); // add it again, as above.
						flashColour(input, 1500, Color.RED);
					}
				}
				else {
					boolean correct = productEntered(input.getText());
					if (correct) {
                        productError.setText("");
						items.setAll(workingUser.getCheckOutNames());
						itemList.setItems(items);
						prices.setAll(workingUser.getCheckOutPrices());
						priceList.setItems(prices);
						total.setText(String.valueOf("$" + workingUser.getPrice()));
						input.clear();
						flashColour(input, 500, Color.AQUAMARINE);
					}
					else{
                        productError.setText("Could not read that product");
						input.clear();
						flashColour(input, 500, Color.RED);
                    }
				}
		});
		input.setOnKeyPressed((KeyEvent ke) -> { // the following allows the user to hit enter rather than OK. Works exactly the same as hitting OK. 
			if (ke.getCode().equals(KeyCode.ENTER)) {
				if(!workingUser.userLoggedIn()) {
					int userError = PMKeySEntered(input.getText());

					if(workingUser.userLoggedIn()) {
						userLabel.setText(workingUser.userName(userError) + "—$" + workingUser.getUserBill());
						inputLabel.setText("Enter Barcode");
						grid.getChildren().remove(userLabel);
						grid.add(userLabel, 3,0);
						input.clear();
						flashColour(input, 1500, Color.AQUAMARINE);
						checkoutOut.setDividerPositions(0.8f);
					}
					else {
						input.clear();
						userLabel.setText(workingUser.userName(userError));
						grid.getChildren().remove(userLabel);
						grid.add(userLabel, 3,0);
						input.clear();
						flashColour(input, 1500, Color.RED);
					}
				}
				else {
					boolean correct = productEntered(input.getText());
					if (correct) {
						items.setAll(workingUser.getCheckOutNames());
						itemList.setItems(items);
						prices.setAll(workingUser.getCheckOutPrices());
						priceList.setItems(prices);
						total.setText(String.valueOf("$" + workingUser.getPrice()));
						input.clear();
						flashColour(input, 500, Color.AQUAMARINE);
					}
					else{
						input.clear();
						flashColour(input, 500, Color.RED);
					}
				}
			}
		});

                
                // create and listen on admin button
		Button adminMode = new Button("Enter Admin Mode"); // button which will bring up the admin mode. 
		adminMode.setOnAction((ActionEvent e) -> {
			enterPassword(); // method which will work the admin mode features. 
		});
		grid.add(adminMode, 0,8); // add the button to the bottum left of the screen. 

        Button removeProduct = new Button("Remove"); // button which will bring up the admin mode.
        removeProduct.setOnAction((ActionEvent e) -> {
            int index = itemList.getSelectionModel().getSelectedIndex();
            if(index >= 0) {
                workingUser.deleteProduct(index);
                prices.setAll(workingUser.getCheckOutPrices());
                priceList.setItems(prices);
                items.setAll(workingUser.getCheckOutNames());
                itemList.setItems(items); //TODO: add select top.
                total.setText(String.valueOf("$" + workingUser.getPrice()));
                itemList.scrollTo(index);
				flashColour(removeProduct, 1500, Color.AQUAMARINE);
            }
			else flashColour(removeProduct, 1500, Color.RED);
        });
        grid.add(removeProduct, 2,8); // add the button to the bottum left of the screen.

                // create and listen on purchase button
        Button purchase = new Button("Purchase"); // button which will add the cost of the items to the users bill
        purchase.setOnAction((ActionEvent e) -> {
			if(workingUser.userLoggedIn()) {
				workingUser.buyProducts(); // add the cost to the bill.
				grid.getChildren().remove(userLabel); // make it look like the user has been logged out.
				inputLabel.setText("Enter your PMKeyS"); // Set the input label to something better for user login.
				total.setText(String.valueOf(workingUser.getPrice())); //set total to the working users price, which after logout is 0.00
				input.clear(); // clear the input ready for a PMKeyS
				items.setAll(workingUser.getCheckOutNames());
				itemList.setItems(items);
				prices.setAll(workingUser.getCheckOutPrices());
				priceList.setItems(prices);
				checkoutOut.setDividerPositions(0.8f);
				flashColour(purchase, 1500, Color.AQUAMARINE);
			}
			else {
				flashColour(purchase, 1500, Color.RED);
				flashColour(input, 1500, Color.RED);
			}
		});
        grid.add(purchase, 4,8, 2,1); // add the button to the bottum right corner, next to the total price. 
                
		Button cancel = new Button("Cancel");
		cancel.setOnAction((ActionEvent e) -> {
			workingUser.logOut(); // set user number to -1 and delete any checkout made. 
			grid.getChildren().remove(userLabel); // make it look like no user is logged in
			inputLabel.setText("Enter your PMKeyS"); // set the input label to something appropriate. 
			items.setAll(workingUser.getCheckOutNames());
			itemList.setItems(items);
			prices.setAll(workingUser.getCheckOutPrices());
			priceList.setItems(prices);
			total.setText(String.valueOf(workingUser.getPrice())); // set the total price to 0.00.
			checkoutOut.setDividerPositions(0.8f);
		});
		grid.add(cancel, 4,0, 2,1); // add the button to the right of the user name. 
		Platform.setImplicitExit(false);
		primaryStage.setOnCloseRequest((WindowEvent event) -> {
			event.consume();
		});
		Scene primaryScene = new Scene(grid, horizontalSize, verticalSize); // create the scene at the given size
		primaryStage.setScene(primaryScene);
		
		primaryStage.show();
	}

    /**
     * Log the user into working user given their PMKeyS
     * @param input The users PMKeyS as a string
     * @return The error from logging the user in.
     */
	private int PMKeySEntered(String input)
	{
		return workingUser.getPMKeyS(input);
	}

    /**
     * Add a product to the checkout
     * @param input The barcode of the product as a string
     * @return A Boolean value of whether the action worked
     */
	private boolean productEntered(String input)
	{
		return workingUser.addToCart(input);
	}

    /**
     * Allows the user to enter the password for the admin user.
     * Will start the admin stage if the password is entered correctly.
     */
	private void enterPassword()
	{
		Stage passwordStage = new Stage();
		passwordStage.setTitle("Password");
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(15, 15, 15, 15));
		Text PWLabel = new Text("Enter password");
		PasswordField PW = new PasswordField();
		grid.add(PWLabel, 0,0);
		grid.add(PW, 1,0);
		PW.setOnAction((ActionEvent e) -> {
			if(!workingUser.passwordsEqual(PW.getText())) {
				flashColour(PW, 1500, Color.RED);
				PW.setText("");
			}
			else {
				enterAdminMode(passwordStage);
			}
		});
		Scene passwordScene = new Scene(grid, 400, 200);
		passwordStage.setScene(passwordScene);
		passwordStage.show();
	}

    /**
     * Will open the admin panel of the program.
     * @param lastStage The stage which opened this stage
     */
	private void enterAdminMode(Stage lastStage)
	{
		lastStage.hide();
		Stage adminStage = new Stage();
		adminStage.setTitle("TOC19");
		SplitPane split = new SplitPane();
		VBox rightPane = new VBox();
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(15, 15, 15, 15));
		ListView<String> optionList = new ListView<>();
		ObservableList<String> items = FXCollections.observableArrayList();
		final String[] PersonSettingsList = {"Add Person", "Remove Person", "Change a Person", "List People", "Lock People Out", "Save Person Database"};
		final String[] ProductSettingsList = {"Add Products", "Remove Products", "Change a Product","Enter Stock Counts", "List Products", "Save Product Database"};
		final String[] AdminSettingsList = {"Reset Bills", "Change Password", "Save Databases To USB", "Close The Program"};
		items.setAll(PersonSettingsList);
		optionList.setItems(items);
		
		grid.add(optionList, 0,0, 1, 7);
		Button people = new Button("People");
		people.setOnAction((ActionEvent e) -> {
			items.setAll(PersonSettingsList);
			optionList.setItems(items);
			optionList.getSelectionModel().select(0);
		});
		Button products = new Button("Products");
		products.setOnAction((ActionEvent e) -> {
			items.setAll(ProductSettingsList);
			optionList.setItems(items);
			optionList.getSelectionModel().select(0);
		});
		Button admin = new Button("Admin");
		admin.setOnAction((ActionEvent e) -> {
			items.setAll(AdminSettingsList);
			optionList.setItems(items);
			optionList.getSelectionModel().select(0);
		});
		Button logout = new Button("Logout");
		logout.setOnAction((ActionEvent e) -> {
			adminStage.close();
		});
		ToolBar buttonBar = new ToolBar(people, products, admin, logout);
		rightPane.getChildren().addAll(buttonBar, grid);
		split.getItems().addAll(optionList, rightPane);
		split.setDividerPositions(0.2f);
		optionList.getSelectionModel().selectedItemProperty().addListener(
            (ObservableValue<? extends String> ov, String old_val, String selectedOption) -> {
				if(selectedOption == null) {
				}
				else if( selectedOption.equals("Add Person")) {
					grid.getChildren().clear();
					Text nameLabel = new Text("Name:");
					grid.add(nameLabel, 0,0);
					TextField nameEntry = new TextField();
					nameEntry.requestFocus();
					grid.add(nameEntry, 1, 0);
					Text PMKeySLabel = new Text("PMKeyS:");
					grid.add(PMKeySLabel, 0,1);
					TextField PMKeySEntry = new TextField();
					grid.add(PMKeySEntry, 1,1);
					nameEntry.setOnAction((ActionEvent e) -> {
						PMKeySEntry.requestFocus();
					});
					PMKeySEntry.setOnAction((ActionEvent e) -> {
						long PMKeyS = 7000000;
						try {
							PMKeyS = Long.parseLong(PMKeySEntry.getText());
						}
						catch (NumberFormatException e1) {
							Log.print(e1);
						}
						if(PMKeyS != 7000000) {
							workingUser.addPersonToDatabase(nameEntry.getText(), PMKeyS);
							nameEntry.clear();
							PMKeySEntry.clear();
							nameEntry.requestFocus();
							flashColour(nameEntry, 1500, Color.AQUAMARINE);
							flashColour(PMKeySEntry, 1500, Color.AQUAMARINE);
						}
						else {
							flashColour(PMKeySEntry, 1500, Color.RED);
						}
					});
					
				}
				else if(selectedOption.equals("Remove Person")) {
					grid.getChildren().clear();
					Button remove = new Button("Remove");
					grid.add(remove, 1,0);
					ListView<String> personList = new ListView<>();
					ObservableList<String> persons = FXCollections.observableArrayList();
					persons.setAll(workingUser.getUserNames());
					personList.setItems(persons);
					grid.add(personList,0,0);
					remove.setOnAction((ActionEvent e) -> {
						String index = personList.getSelectionModel().getSelectedItem();
						try {
							workingUser.removePerson(index);
							flashColour(remove, 1500, Color.AQUAMARINE);
						} catch (IOException e1) {
							e1.printStackTrace();
							flashColour(remove, 1500, Color.RED);
						} catch (InterruptedException e1) {
							e1.printStackTrace();
							flashColour(remove, 1500, Color.RED);
						}
						persons.setAll(workingUser.getUserNames());
					});
					
				}
				else if(selectedOption.equals("Change a Person")){
					grid.getChildren().clear();
					ListView<String> personList = new ListView<>();
					ObservableList<String> person = FXCollections.observableArrayList();
					person.setAll(workingUser.getUserNames());
					personList.setItems(person);
					grid.add(personList, 0, 0, 1, 4);
					Text nameLabel = new Text("Name:");
					grid.add(nameLabel, 1, 0);
					TextField nameEntry = new TextField();
					nameEntry.requestFocus();
					grid.add(nameEntry, 2, 0);
					Text pmkeysLabel = new Text("PMKeyS:");
					grid.add(pmkeysLabel, 1,1);
					TextField pmkeys = new TextField();
					grid.add(pmkeys, 2, 1);
					personList.getSelectionModel().selectedItemProperty().addListener(
							                                                                 (ObservableValue<? extends String> vo, String oldVal, String selectedPerson) -> {
								                                                                 if(selectedPerson != null) {
																									 nameEntry.setText(selectedPerson);
																									 String pmkeysVal = String.valueOf(workingUser.getUser(selectedPerson).getBarCode());
																									 pmkeys.setText(pmkeysVal);
																								 }
							                                                                 });
					nameEntry.setOnAction((ActionEvent e) -> {
						pmkeys.requestFocus();
					});

					pmkeys.setOnAction((ActionEvent e) -> {
						long pmkeysNew = -1;
						try {
							pmkeysNew = Long.parseLong(pmkeys.getText());
						}
						catch (NumberFormatException e1) {
							flashColour(pmkeys, 1500, Color.RED);
						}
						if (pmkeysNew != -1) {
							Person oldPerson = workingUser.getUser(personList.getSelectionModel().getSelectedItem());

							workingUser.changeDatabasePerson(personList.getSelectionModel().getSelectedItem(), nameEntry.getText(), pmkeysNew, oldPerson.getBarCode());
							nameEntry.clear();
							pmkeys.clear();
							nameEntry.requestFocus();
							flashColour(nameEntry, 1500, Color.AQUAMARINE);
							flashColour(pmkeys, 1500, Color.AQUAMARINE);
							//Now need to update the form
							String selectedIndex = personList.getSelectionModel().getSelectedItem();
							person.setAll(workingUser.getUserNames());
							personList.setItems(person);
							personList.getSelectionModel().select(selectedIndex);
						}
					});
				}
				else if(selectedOption.equals("List People")) {
					grid.getChildren().clear();
					ScrollPane users = null;
					try {
						users = workingUser.printDatabase("Person");
					} catch (IOException e) {
						Log.print(e);
					}
					grid.add(users, 0, 0);
				}
				else if(selectedOption.equals("Save Person Database")) {
					grid.getChildren().clear();
					Button save = new Button("Save Person Database");
					Text saveLabel = new Text("Save database to adminPersonDatabase.txt?");
					grid.add(saveLabel, 0,0);
					grid.add(save, 0, 1);
					save.setOnAction((ActionEvent e) -> {
						try {
							workingUser.adminWriteOutDatabase("Person");
							flashColour(save, 3000, Color.AQUAMARINE);
						} catch (IOException e1) {
							e1.printStackTrace();
							flashColour(save, 3000, Color.RED);
						}

					});
				}
				else if(selectedOption.equals("Lock People Out")) {
					grid.getChildren().clear();
					ListView<String> personList = new ListView<>();
					ObservableList<String> persons = FXCollections.observableArrayList();
					persons.setAll(workingUser.getUserNames());
					personList.setItems(persons);
					grid.add(personList,0,0);
					ChoiceBox canBuy = new ChoiceBox();
					canBuy.getItems().addAll("unlocked", "Locked");
					grid.add(canBuy, 1,0);
					personList.getSelectionModel().selectedItemProperty().addListener(
							(ObservableValue<? extends String> vo, String oldVal, String selectedProduct) -> {
								if (workingUser.userCanBuyAdmin(personList.getSelectionModel().getSelectedItem())) {
									canBuy.getSelectionModel().select(0);
								} else canBuy.getSelectionModel().select(1);
							});
					canBuy.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number> () {
						@Override
						public void changed(ObservableValue ov, Number value, Number newValue) {
							workingUser.setUserCanBuy(personList.getSelectionModel().getSelectedItem(), canBuy.getSelectionModel().getSelectedIndex() == 0);
							flashColour(canBuy, 1500, Color.AQUAMARINE);
						}
					});
				}
				else if( selectedOption.equals("Add Products")) {
					grid.getChildren().clear();
					Text nameLabel = new Text("Name:");
					grid.add(nameLabel, 0, 0);
					TextField nameEntry = new TextField();
					nameEntry.requestFocus();
					grid.add(nameEntry, 1, 0);
					Text BarCodeLabel = new Text("Barcode:");
					grid.add(BarCodeLabel, 0, 1);
					TextField BarCodeEntry = new TextField();
					grid.add(BarCodeEntry, 1, 1);
					Text priceLabel = new Text("Price: $");
					grid.add(priceLabel, 0, 2);
					TextField priceEntry = new TextField();
					grid.add(priceEntry, 1, 2);
					nameEntry.setOnAction((ActionEvent e) -> {
						BarCodeEntry.requestFocus();
					});
					BarCodeEntry.setOnAction((ActionEvent e) -> {
						priceEntry.requestFocus();
					});
					priceEntry.setOnAction((ActionEvent e) -> {
						long barCode = -1;
						try {
							barCode = Long.parseLong(BarCodeEntry.getText());
						} catch (NumberFormatException e1) {
							flashColour(BarCodeEntry, 1500, Color.RED);
						}
						long price = -1;
						try {
							price = (long) (Double.parseDouble(priceEntry.getText()) * 100);
						}
						catch (NumberFormatException e1) {
							flashColour(priceEntry, 1500, Color.RED);
						}
						if(barCode != -1 && price != -1) {
							workingUser.addProductToDatabase(nameEntry.getText(), barCode, price);
							nameEntry.clear();
							BarCodeEntry.clear();
							priceEntry.clear();
							nameEntry.requestFocus();
							flashColour(nameEntry, 1500, Color.AQUAMARINE);
							flashColour(priceEntry, 1500, Color.AQUAMARINE);
							flashColour(BarCodeEntry, 1500, Color.AQUAMARINE);
						}
					});
					
				}
				else if(selectedOption.equals("Remove Products")) {
					grid.getChildren().clear();
					Button remove = new Button("Remove");
					ListView<String> productList = new ListView<>();
					ObservableList<String> product = FXCollections.observableArrayList();
					product.setAll(workingUser.getProductNames());
					productList.setItems(product);
					grid.add(productList,0,0);
					remove.setOnAction((ActionEvent e) -> {
						String index = productList.getSelectionModel().getSelectedItem();
						try {
							workingUser.removeProduct(index);
							flashColour(remove, 1500, Color.AQUAMARINE);
						} catch (IOException | InterruptedException e1) {
							e1.printStackTrace();
							flashColour(remove, 1500, Color.RED);
						}
                        product.setAll(workingUser.getProductNames());
					});
					grid.add(remove, 1,0);
					product.setAll(workingUser.getProductNames());
				}
				else if( selectedOption.equals("Change a Product")) {
					grid.getChildren().clear();
					ListView<String> productList = new ListView<>();
					ObservableList<String> product = FXCollections.observableArrayList();
					product.setAll(workingUser.getProductNames());
					productList.setItems(product);
					grid.add(productList,0,0, 1, 4);
					Text nameLabel = new Text("Name:");
					grid.add(nameLabel, 1,0);
					TextField nameEntry = new TextField();
					nameEntry.requestFocus();
					grid.add(nameEntry, 2, 0);
					Text BarCodeLabel = new Text("Barcode:");
					grid.add(BarCodeLabel, 1,1);
					TextField barCodeEntry = new TextField();
					grid.add(barCodeEntry, 2,1);
					Text priceLabel = new Text("Price: $");
					grid.add(priceLabel, 1,2);
					TextField priceEntry = new TextField();
					grid.add(priceEntry, 2,2);
					productList.getSelectionModel().selectedItemProperty().addListener(
					(ObservableValue<? extends String> vo, String oldVal, String selectedProduct) -> {
						nameEntry.setText(selectedProduct);
						String BC = String.valueOf(workingUser.getProductBarCode(productList.getSelectionModel().getSelectedItem()));
						barCodeEntry.setText(BC);
						String price = Double.toString(workingUser.getProductPrice(productList.getSelectionModel().getSelectedItem())/100);
						priceEntry.setText(price);
					});
					nameEntry.setOnAction((ActionEvent e) -> {
						barCodeEntry.requestFocus();
					});
					barCodeEntry.setOnAction((ActionEvent e) -> {
						priceEntry.requestFocus();
					});
					priceEntry.setOnAction((ActionEvent e) -> {
						long barCode = -1;
						try {
							barCode = Long.parseLong(barCodeEntry.getText());
						}
						catch (NumberFormatException e1) {
							flashColour(barCodeEntry, 1500, Color.RED);
						}
						long price = -1;
						try {
							price = (long) (Double.parseDouble(priceEntry.getText()) * 100);
						}
						catch (NumberFormatException e1) {
							flashColour(priceEntry, 1500, Color.RED);
						}
						if(barCode != -1 && price != -1) {
							workingUser.changeDatabaseProduct(nameEntry.getText(), workingUser.getProductName(productList.getSelectionModel().getSelectedItem()), price,
									barCode, workingUser.getProductBarCode(productList.getSelectionModel().getSelectedItem()));
							nameEntry.clear();
							barCodeEntry.clear();
							priceEntry.clear();
							nameEntry.requestFocus();
							flashColour(nameEntry, 1500, Color.AQUAMARINE);
							flashColour(barCodeEntry, 1500, Color.AQUAMARINE);
							flashColour(priceEntry, 1500, Color.AQUAMARINE);


							//Now need to update the form
							String selectedProduct = productList.getSelectionModel().getSelectedItem();

							nameEntry.setText(selectedProduct);
							String BC = String.valueOf(workingUser.getProductBarCode(productList.getSelectionModel().getSelectedItem()));
							barCodeEntry.setText(BC);
							String price2 = Double.toString(workingUser.getProductPrice(productList.getSelectionModel()
									.getSelectedItem()) / 100);
							priceEntry.setText(price2);
							product.setAll(workingUser.getProductNames());
							productList.setItems(product);
						}
				});
					
				}
				else if( selectedOption.equals("Enter Stock Counts")) {
					grid.getChildren().clear();
					ListView<String> productList = new ListView<>();
					ObservableList<String> product = FXCollections.observableArrayList();
					product.setAll(workingUser.getProductNames());
					productList.setItems(product);
					grid.add(productList,0,0, 1, 4);
					Text numberLabel = new Text("Number:");
					grid.add(numberLabel, 1,0);
					TextField numberEntry = new TextField();
					grid.add(numberEntry, 2,0);
					
					productList.getSelectionModel().selectedItemProperty().addListener(
					(ObservableValue<? extends String> vo, String oldVal, String selectedProduct) -> {
						String numberOfProduct = Integer.toString(workingUser.getProductNumber(productList.getSelectionModel().getSelectedItem()));
						numberEntry.setText(numberOfProduct);
						numberEntry.requestFocus();
						
					});
					numberEntry.setOnAction((ActionEvent e) -> {
						workingUser.setNumberOfProducts(productList.getSelectionModel().getSelectedItem(), Integer.parseInt(numberEntry.getText()));
						productList.getSelectionModel().select(productList.getSelectionModel().getSelectedIndex() + 1);
						numberEntry.requestFocus();
						flashColour(numberEntry, 1500, Color.AQUAMARINE);
					});
						
					
				}
				else if(selectedOption.equals("List Products")) {
					grid.getChildren().clear();
					ScrollPane productList = null;
					try {
						productList = workingUser.printDatabase("Product");
					} catch (IOException e) {
						Log.print(e);
					}
					grid.add(productList, 0, 0);
				}
				else if(selectedOption.equals("Save Product Database")) {
					grid.getChildren().clear();
					Button save = new Button("Save Product Database");
					Text saveLabel = new Text("Save database to adminProductDatabase.txt?");
					grid.add(saveLabel, 0,0);
					grid.add(save, 0,1);
					save.setOnAction((ActionEvent e) -> {
						try {
							workingUser.adminWriteOutDatabase("Product");
							flashColour(save, 3000, Color.AQUAMARINE);
						} catch (IOException e1) {
							Log.print(e1);
							flashColour(save, 3000, Color.RED);
						}
					});
				}
				else if(selectedOption.equals("Reset Bills")) {
					grid.getChildren().clear();
					Button save = new Button("Reset Bills");
					Text saveLabel = new Text("Are you sure you would like to reset the bills? \nThis cannot be undone.");
					saveLabel.setTextAlignment(TextAlignment.CENTER);
					grid.add(saveLabel, 0, 0, 2, 1);
					grid.add(save, 1, 1);
					save.setOnAction((ActionEvent e) -> {
							workingUser.resetBills();
                            flashColour(save, 1500, Color.AQUAMARINE);
							save.setDisable(true);
							save.setText("Bills Reset");
					});
				}
				else if(selectedOption.equals("Change Password")) {
					grid.getChildren().clear();
					Text oldLabel = new Text("Enter old password");
					Text newLabel = new Text("Enter new password");
					Text againLabel = new Text("Enter new password again");
					PasswordField oldPW = new PasswordField();
					PasswordField newPW = new PasswordField();
					PasswordField newPW2 = new PasswordField();
					grid.add(oldLabel, 0,0);
					grid.add(oldPW, 1,0);
					Text error = new Text();
					oldPW.setOnAction((ActionEvent e) -> {
						if (!workingUser.passwordsEqual(oldPW.getText())) {
							error.setText("Password incorrect");
							oldPW.setText("");
							grid.getChildren().remove(error);
							grid.add(error, 2, 0);
							flashColour(oldPW, 1500, Color.RED);
						} else {
							grid.add(newLabel, 0, 1);
							grid.add(newPW, 1, 1);
							grid.add(againLabel, 0, 2);
							grid.add(newPW2, 1, 2);
							newPW.requestFocus();
							grid.getChildren().remove(error);
						}
					});
					newPW.setOnAction((ActionEvent e) -> {
						newPW2.requestFocus();
					});
					newPW2.setOnAction((ActionEvent e) -> {
						if(newPW.getText() != null && !newPW.getText().equals("") && newPW.getText().equals(newPW2.getText())) {
							workingUser.setAdminPassword(workingUser.getSecurePassword(newPW.getText()));
							Text changed = new Text("Success");
							grid.add(changed, 1,3);
							flashColour(newPW, 1500, Color.AQUAMARINE);
							flashColour(newPW2, 1500, Color.AQUAMARINE);
						}
						else {
							error.setText("Passwords do not match");
							grid.getChildren().remove(error);
							grid.add(error, 1, 3);
						}
					});
				}
				else if(selectedOption.equals("Save Databases To USB")) { //TODO: Bring admin stage to front after

					DirectoryChooser fc = new DirectoryChooser();


					grid.getChildren().clear();
					Text fileLabel = new Text("Save Directory");
					TextField filePath = new TextField("");
					filePath.setEditable(true);
					Button saveDirBtn = new Button("Choose Save Directory");
					Button saveBtn = new Button("Save Databases to Selected Directories");

					grid.add(saveBtn,1,5);
					grid.add(fileLabel,0,0);
					grid.add(filePath,0,1);
					grid.add(saveDirBtn,1,1);

					saveDirBtn.setOnAction((ActionEvent e) -> {
						File returnVal = fc.showDialog(adminStage);

						if (returnVal != null) {
							filePath.setText(returnVal.getPath());
							flashColour(saveDirBtn, 1500, Color.AQUAMARINE);
						}else{
							flashColour(saveDirBtn, 1500, Color.RED);
						}
					});

					saveBtn.setOnAction((ActionEvent e) -> {
						try {
							workingUser.adminWriteOutDatabase("Person"); //adminPersonDatabase.csv
							workingUser.adminWriteOutDatabase("Product"); //adminProductDatabase.csv

							File adminPersonFile = new File(Compatibility.getFilePath("adminPersonDatabase.csv"));
							File adminProductFile = new File(Compatibility.getFilePath("adminProductDatabase.csv"));
							if(filePath.getText() != "" || filePath.getText() != null) {
								File destPers = new File(filePath.getText() + "/adminPersonDatabase.csv");
								File destProd = new File(filePath.getText() + "/adminProductDatabase.csv");
								Files.copy(adminPersonFile.toPath(), destPers.toPath(), StandardCopyOption.REPLACE_EXISTING);
								Files.copy(adminProductFile.toPath(), destProd.toPath(), StandardCopyOption.REPLACE_EXISTING);
								flashColour(saveBtn, 3000, Color.AQUAMARINE);
							}
							else {
								flashColour(saveBtn, 3000, Color.RED);
								flashColour(filePath, 3000, Color.RED);
							}

						} catch (IOException e1) {
							Log.print(e1);
							flashColour(saveBtn, 3000, Color.RED);
						}
					});

				}
                else if(selectedOption.equals("Close The Program")) {
                    grid.getChildren().clear();
                    Button save = new Button("Close The Program");
                    grid.add(save, 1,1);
                    save.setOnAction((ActionEvent e) -> {
	                    flashColour(save,1500, Color.AQUAMARINE);
                            System.exit(0);
                    });
                }
		});
		Scene adminScene = new Scene(split, horizontalSize, verticalSize);
		adminStage.setScene(adminScene);
		adminStage.show();
		adminStage.toFront();
                
	}

	/**
	 * Flash the given node a given colour for a given time
	 * @param node The node to be flashed
	 * @param duration the duration in ms for the node to be flashed
	 * @param colour The colour (from Color) that you wish to flash.
	 */
	public static void flashColour(Node node, int duration, Color colour){

		InnerShadow shadow = new InnerShadow();
		shadow.setRadius(25d);
		shadow.setColor(colour);
		node.setEffect(shadow);

		Timeline time = new Timeline();

		time.setCycleCount(1);

		List<KeyFrame> frames = new ArrayList<>();
		frames.add(new KeyFrame(Duration.ZERO, new KeyValue(shadow.radiusProperty(),25)));
		frames.add(new KeyFrame(new Duration(duration), new KeyValue(shadow.radiusProperty(),0)));
		time.getKeyFrames().addAll(frames);

		time.playFromStart();
	}

	/**
	 * Bind the scrollbars of two listviews
	 * @param lv1 The first listview to bind
	 * @param lv2 The second Listview to bind
	 */
	public static void bind(ListView lv1, ListView lv2) { //TODO: this does not work.
		ScrollBar bar1 = null;
		ScrollBar bar2 = null;

		for (Node node : lv1.lookupAll(".scroll-bar")) {
			if (node instanceof ScrollBar && ((ScrollBar)node).getOrientation().equals(Orientation.VERTICAL)) {
				bar1 = (ScrollBar)node;
			}
		}
		for (Node node : lv2.lookupAll(".scroll-bar")) {
			if (node instanceof ScrollBar && ((ScrollBar)node).getOrientation().equals(Orientation.VERTICAL)) {
				bar2 = (ScrollBar)node;
			}
		}
		if (bar1 == null || bar2 == null) return;

		final ScrollBar fbar1 = bar1;
		final ScrollBar fbar2 = bar2;
		if (fbar1 != null) {
			fbar1.valueProperty().addListener(new ChangeListener<Number>() {
				@Override
				public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
					fbar2.setValue(newValue.doubleValue());
				}
			});
		}
		if (fbar2 != null) {
			fbar2.valueProperty().addListener(new ChangeListener<Number>() {
				@Override
				public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
					fbar1.setValue(newValue.doubleValue());
				}
			});
		}
	}


	/**
     * The main method of the program
     * @param args No arguments needed, -w int for width, -h int for height, both in pixels.
     */
	public static void main(String[] args)
	{
		for(int i = args.length-1; i > 0; i--) {
			if(args[i].equals("-w") && i!= args.length-1) {
				horizontalSize = Integer.parseInt(args[i+1]);
			}
			else if(args[i].equals("-h") && i != args.length-1) {
				verticalSize = Integer.parseInt(args[i+1]);
			}
		}
		Application.launch(args);
	}

	protected static void showErrorDialog(Throwable e) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle("Exception Management");
		alert.setHeaderText("I caught me a stacktrace xD \nPlease show this to one of the system admins");
		alert.setContentText(e.getLocalizedMessage());


		// Create expandable Exception.
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		String exceptionText = sw.toString();

		Label label = new Label("The exception stacktrace was:");

		TextArea textArea = new TextArea(exceptionText);
		textArea.setEditable(false);
		textArea.setWrapText(true);

		textArea.setMaxWidth(Double.MAX_VALUE);
		textArea.setMaxHeight(Double.MAX_VALUE);
		GridPane.setVgrow(textArea, Priority.ALWAYS);
		GridPane.setHgrow(textArea, Priority.ALWAYS);

		GridPane expContent = new GridPane();
		expContent.setMaxWidth(Double.MAX_VALUE);
		expContent.add(label, 0, 0);
		expContent.add(textArea, 0, 1);

		// Set expandable Exception into the dialog pane.
		alert.getDialogPane().setExpandableContent(expContent);

		alert.showAndWait();
	}
}

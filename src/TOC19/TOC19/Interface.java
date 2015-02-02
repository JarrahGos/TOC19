package TOC19;

/***
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

/* 
* Author: Jarrah Gosbell 
* Student Number: z5012558
* Class: Interface
* Description: This program will allow the user to interact with the program, creating, deleting and modifying products and checkOuts.
*/
// GUI Inports
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import TOC19.Settings;

import java.io.IOException;

public final class Interface extends Application
{
	// Create the necessary instance variables.
	private final WorkingUser workingUser; // Place for all data to go through
	private ScrollPane nameDataOut; // output of product data in the interface. 
	private ScrollPane priceDataOut; // output the price of the product.
	private static int horizontalSize = 1024;
	private static int verticalSize = 576;
	private final int textSize;
	Settings config;

	private int logicalSize;
//	Timer timeOut = new Timer(60000000, new actionListener());
		
	public Interface() throws IOException
	{
		this.config = new Settings();
		workingUser = new WorkingUser();
		String[] settings = config.interfaceSettings();
		horizontalSize = Integer.parseInt(settings[0]);
		verticalSize = Integer.parseInt(settings[1]);
		textSize = Integer.parseInt(settings[2]);
		//initalize the variables created above
		
	}	
	@Override
	public void start(Stage primaryStage)
	{
		// create the layout
		primaryStage.setTitle("TOC19"); // set the window title. 
		workingUser.addDatabases(); // import the user and product data
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
				
//		for(Node node: priceList.lookupAll(".scroll-bar")) {
//			if (node instanceof ScrollBar) {
//				ScrollBar bar = (ScrollBar) node;
//				bar.valueProperty().addListener((ObservableValue<? extends Number> value, Number oldValue, Number newValue) -> {
//					System.out.println(bar.getOrientation() + " " + newValue);	
//				});
//			}
//		}
		grid.add(checkoutOut, 0,1,7,7);
               // work checkout output
//		Text nameData = new Text(workingUser.getCheckOutNames()); // the data which will be output by the checkout
//		nameData.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
//		nameDataOut = new ScrollPane(nameData); // create a scroll pane to handle the data. 
//		nameDataOut.setPrefViewportHeight(400);
//		nameDataOut.setPrefViewportWidth(550);
//		nameDataOut.setVbarPolicy(ScrollBarPolicy.NEVER); // ensure that there is a scroll bar.
//		grid.add(nameDataOut, 0,1,4,7); // place front and centre. 
//		// Do the same with the price
//		Text priceData = new Text(workingUser.getCheckOutPrices());
//		priceData.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
//		priceDataOut = new ScrollPane(priceData); // create a scroll pane to handle the data. 
//		priceDataOut.setPrefViewportHeight(400); // was 400
//		priceDataOut.setPrefViewportWidth(100);
//		priceDataOut.setVbarPolicy(ScrollBarPolicy.ALWAYS); // ensure that there is a scroll bar.
//		grid.add(priceDataOut, 4,1,2,7); // place front and centre. 
		
		// listen for changes to the scroll bar value
//		DoubleProperty vPosition = new SimpleDoubleProperty();
//			vPosition.bind(priceDataOut.vvalueProperty());
//			vPosition.addListener(new ChangeListener() {
//				@Override
//				public void changed(ObservableValue arg0, Object arg1, Object arg2) {
////					 nameDataOut.setVvalue((double) arg2);
//					nameDataOut.setVvalue(priceDataOut.getVvalue()); // this is going in the right direction, but does not work. 
//			
//				}
//			});
			


        //listen on enter product barcode button
		enterBarCode.setOnAction((ActionEvent e) -> {
				if(!workingUser.userLoggedIn()) { // treat the input as a PMKeyS
					PMKeySEntered(input.getText()); // take the text, do user logon stuff with it. 
			
			
			
					if(workingUser.userLoggedIn()) {
						Thread thread = new Thread(new Runnable()
						{

							@Override
							public void run()
							{
								try {
									Thread.sleep(90000); // after this time, log the user out. 
									workingUser.logOut(); // set user number to -1 and delete any checkout made. 
									System.out.println("logging Out");
									grid.getChildren().remove(userLabel); // make it look like no user is logged in
									inputLabel.setText("Enter your PMKeyS"); // set the input label to something appropriate. 
//									nameData.setText(workingUser.getCheckOutNames()); // clear the data of the checkout. 
//									priceData.setText(workingUser.getCheckOutPrices());
//									nameDataOut = new ScrollPane(nameData); // clear the scroll pane output. 
//									priceDataOut = new ScrollPane(priceData);
									total.setText(String.valueOf(workingUser.getPrice())); // set the total price to 0.00. 
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
						userLabel.setText(workingUser.userName()); // find out the name of those who dare log on. 
						inputLabel.setText("Enter Barcode"); // change the label to suit the next action. 
						grid.getChildren().remove(userLabel); // remove any error labels which may have appeared. 
						grid.add(userLabel, 3,0); // add the new user label
						// the above two are done as we do not know whether a user label exists there. Adding two things to the same place causes an exception. 
						input.clear(); // clear the PMKeyS from the input ready for product bar codes. 
						
					}
					else {
						input.clear(); // there was an error with the PMKeyS, get ready for another. 
						userLabel.setText("Error"); // tell the user there was a problem. Maybe this could be done better. 
						grid.getChildren().remove(userLabel); // Remove a userlabel, as above. 
						grid.add(userLabel, 3,0); // add it again, as above. 
					}
				}
				else {
					boolean correct = productEntered(input.getText());
					if (correct) {
//						nameData.setText(workingUser.getCheckOutNames());
//						nameDataOut = new ScrollPane(nameData);
//						priceData.setText(workingUser.getCheckOutPrices());
//						priceDataOut = new ScrollPane(priceData);
						items.setAll(workingUser.getCheckOutNames());
						itemList.setItems(items);
						prices.setAll(workingUser.getCheckOutPrices());
						priceList.setItems(prices);
						total.setText(String.valueOf("$" + workingUser.getPrice()));
						input.clear();
					}
					else 
						input.clear();
				}
		});
		input.setOnKeyPressed((KeyEvent ke) -> { // the following allows the user to hit enter rather than OK. Works exactly the same as hitting OK. 
			if (ke.getCode().equals(KeyCode.ENTER)) {
				if(!workingUser.userLoggedIn()) {
					PMKeySEntered(input.getText());
			
			
			
					if(workingUser.userLoggedIn()) {
						userLabel.setText(workingUser.userName());
						inputLabel.setText("Enter Barcode");
						grid.getChildren().remove(userLabel);
						grid.add(userLabel, 3,0);
						input.clear();
					}
					else {
						input.clear();
						userLabel.setText("error");
						grid.getChildren().remove(userLabel);
						grid.add(userLabel, 3,0);
						input.clear();
					}
				}
				else {
					boolean correct = productEntered(input.getText());
					if (correct) {
//						nameData.setText(workingUser.getCheckOutNames());
//						nameDataOut = new ScrollPane(nameData);
//						priceData.setText(workingUser.getCheckOutPrices());
//						priceDataOut = new ScrollPane(priceData);
						items.setAll(workingUser.getCheckOutNames());
						itemList.setItems(items);
						prices.setAll(workingUser.getCheckOutPrices());
						priceList.setItems(prices);
						total.setText(String.valueOf("$" + workingUser.getPrice()));
						input.clear();
					}
					else 
						input.clear();
				}
			}
		});

                
                // create and listen on admin button
		Button adminMode = new Button("Enter Admin Mode"); // button which will bring up the admin mode. 
		adminMode.setOnAction((ActionEvent e) -> {
			enterPassword(); // method which will work the admin mode features. 
//			primaryStage.hide(); // hide the user side of things. 
		});
		grid.add(adminMode, 0,8); // add the button to the bottum left of the screen. 
                
                // create and listen on purchase button
        Button purchase = new Button("Purchase"); // button which will add the cost of the items to the users bill
        purchase.setOnAction((ActionEvent e) -> {
			workingUser.buyProducts(); // add the cost to the bill. 
			grid.getChildren().remove(userLabel); // make it look like the user has been logged out. 
			inputLabel.setText("Enter your PMKeyS"); // Set the input label to something better for user login. 
			total.setText(String.valueOf(workingUser.getPrice())); //set total to the working users price, which after logout is 0.00
			input.clear(); // clear the input ready for a PMKeyS
//			nameData.setText(workingUser.getCheckOutNames()); // clear the data of the scroll pane, as the checkout will be clear. 
//			priceData.setText(workingUser.getCheckOutPrices());
//			nameDataOut = new ScrollPane(nameData); // replace the now clear scroll pane. 
//			priceDataOut = new ScrollPane(nameData);
			items.setAll(workingUser.getCheckOutNames());
			itemList.setItems(items);
			prices.setAll(workingUser.getCheckOutPrices());
			priceList.setItems(prices);
		});
        grid.add(purchase, 4,8, 2,1); // add the button to the bottum right corner, next to the total price. 
                
		Button cancel = new Button("Cancel");
		cancel.setOnAction((ActionEvent e) -> {
			workingUser.logOut(); // set user number to -1 and delete any checkout made. 
			grid.getChildren().remove(userLabel); // make it look like no user is logged in
			inputLabel.setText("Enter your PMKeyS"); // set the input label to something appropriate. 
//			nameData.setText(workingUser.getCheckOutNames()); // clear the data of the checkout. 
//			priceData.setText(workingUser.getCheckOutPrices());
//			nameDataOut = new ScrollPane(nameData); // clear the scroll pane output. 
//			priceDataOut = new ScrollPane(priceData);
			items.setAll(workingUser.getCheckOutNames());
			itemList.setItems(items);
			prices.setAll(workingUser.getCheckOutPrices());
			priceList.setItems(prices);
			total.setText(String.valueOf(workingUser.getPrice())); // set the total price to 0.00. 
		});
		grid.add(cancel, 4,0, 2,1); // add the button to the right of the user name. 
		Platform.setImplicitExit(false);
		primaryStage.setOnCloseRequest((WindowEvent event) -> {
			event.consume();
		});
		Scene primaryScene = new Scene(grid, horizontalSize, verticalSize); // create the scene at 800x600
		primaryStage.setScene(primaryScene);
		
		primaryStage.show();
	}
	private void PMKeySEntered(String input) 
	{
		workingUser.getPMKeyS(input);
	}
	private boolean productEntered(String input)
	{
		return workingUser.addToCart(input);
	}
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
		Text error = new Text();
		PW.setOnAction((ActionEvent e) -> {
			if(!workingUser.passwordsEqual(PW.getText())) {
				error.setText("Password incorrect");
				PW.setText("");
				grid.getChildren().remove(error);
				grid.add(error, 1,2);
			}
			else {
				enterAdminMode(passwordStage);
			}
		});
		Scene passwordScene = new Scene(grid, 400, 200);
		passwordStage.setScene(passwordScene);
		passwordStage.show();
	}
	private void enterAdminMode(Stage lastStage)
	{
		lastStage.hide();
		Stage adminStage = new Stage();
		adminStage.setTitle("TOC19");
		SplitPane split = new SplitPane();
		VBox rightPane = new VBox();
		
		GridPane grid = new GridPane();
//		grid.setGridLinesVisible(true);
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(15, 15, 15, 15));
		ListView<String> optionList = new ListView<>();
		ObservableList<String> items = FXCollections.observableArrayList();
		items.setAll("Add Person", "Remove Person", "List People", "Lock People Out", "Save Person Database");
		optionList.setItems(items);
		
		grid.add(optionList, 0,0, 1, 7);
		Button people = new Button("People");
		people.setOnAction((ActionEvent e) -> {
			items.setAll("Add Person", "Remove Person", "List People", "Lock People Out", "Save Person Database");
			optionList.setItems(items);
			optionList.getSelectionModel().select(0);
		});
		Button products = new Button("Products");
		products.setOnAction((ActionEvent e) -> {
			items.setAll("Add Products", "Remove Products", "Change a Product","Enter Stock Counts", "List Products", "Save Product Database");
			optionList.setItems(items);
			optionList.getSelectionModel().select(0);
		});
		Button admin = new Button("Admin");
		admin.setOnAction((ActionEvent e) -> {
			items.setAll("Reset Bills", "Change Password", "Save Databases To USB", "Close The Program");
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
						long PMKeyS = Long.parseLong(PMKeySEntry.getText());
						workingUser.addPersonToDatabase(nameEntry.getText(), PMKeyS);
						nameEntry.clear();
						PMKeySEntry.clear();
						nameEntry.requestFocus();
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
						int index = personList.getSelectionModel().getSelectedIndex();
						try {
							workingUser.removePerson(index);
						} catch (IOException e1) {
							e1.printStackTrace();
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
						persons.setAll(workingUser.getUserNames());
					});
					
				}
				else if(selectedOption.equals("List People")) {
					grid.getChildren().clear();
					ScrollPane users = workingUser.printDatabase("Person");
					grid.add(users, 0,0);
				}
				else if(selectedOption.equals("Save Person Database")) {
					grid.getChildren().clear();
					Button save = new Button("Save Person Database");
					Text saveLabel = new Text("Save database to adminPersonDatabase.txt?");
					grid.add(saveLabel, 0,0);
					grid.add(save, 0,1);
					save.setOnAction((ActionEvent e) -> {
						workingUser.adminWriteOutDatabase("Person");
						saveLabel.setText("saved");
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
					canBuy.getItems().addAll("true", "false");
					grid.add(canBuy, 1,0);
					personList.getSelectionModel().selectedItemProperty().addListener(
					(ObservableValue<? extends String> vo, String oldVal, String selectedProduct) -> {
						if(workingUser.userCanBuy(personList.getSelectionModel().getSelectedIndex())) {
							canBuy.getSelectionModel().select(0);
						}
						else canBuy.getSelectionModel().select(1);
					});
					canBuy.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number> () {
						@Override
						public void changed(ObservableValue ov, Number value, Number newValue) {
							workingUser.setUserCanBuy(personList.getSelectionModel().getSelectedIndex(), canBuy.getSelectionModel().getSelectedIndex() == 0);
						}
					});
				}
				else if( selectedOption.equals("Add Products")) {
					grid.getChildren().clear();
					Text nameLabel = new Text("Name:");
					grid.add(nameLabel, 0,0);
					TextField nameEntry = new TextField();
					nameEntry.requestFocus();
					grid.add(nameEntry, 1, 0);
					Text BarCodeLabel = new Text("Barcode:");
					grid.add(BarCodeLabel, 0,1);
					TextField BarCodeEntry = new TextField();
					grid.add(BarCodeEntry, 1,1);
					Text priceLabel = new Text("Price: $");
					grid.add(priceLabel, 0,2);
					TextField priceEntry = new TextField();
					grid.add(priceEntry, 1,2);
					nameEntry.setOnAction((ActionEvent e) -> {
						BarCodeEntry.requestFocus();
					});
					BarCodeEntry.setOnAction((ActionEvent e) -> {
						priceEntry.requestFocus();
					});
					priceEntry.setOnAction((ActionEvent e) -> {
						long barCode = Long.parseLong(BarCodeEntry.getText());
						long price = (long)(Double.parseDouble(priceEntry.getText())*100);
						workingUser.addProductToDatabase(nameEntry.getText(), barCode, price);
						nameEntry.clear();
						BarCodeEntry.clear();
						priceEntry.clear();
						nameEntry.requestFocus();
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
						int index = productList.getSelectionModel().getSelectedIndex();
						try {
							workingUser.removeProduct(index);
						} catch (IOException e1) {
							e1.printStackTrace();
						} catch (InterruptedException e1) {
							e1.printStackTrace();
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
						String BC = Long.toString(workingUser.getProductBarCode(productList.getSelectionModel().getSelectedIndex()));
						barCodeEntry.setText(BC);
						String price = Double.toString(workingUser.getProductPrice(productList.getSelectionModel().getSelectedIndex()));
						priceEntry.setText(price);
					});
					nameEntry.setOnAction((ActionEvent e) -> {
						barCodeEntry.requestFocus();
					});
					barCodeEntry.setOnAction((ActionEvent e) -> {
						priceEntry.requestFocus();
					});
					priceEntry.setOnAction((ActionEvent e) -> {
						long barCode = Long.parseLong(barCodeEntry.getText());
						long price = (long)(Double.parseDouble(priceEntry.getText())*100);
						workingUser.addProductToDatabase(nameEntry.getText(), barCode, price);
						nameEntry.clear();
						barCodeEntry.clear();
						priceEntry.clear();
						nameEntry.requestFocus();
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
						String numberOfProduct = Integer.toString(workingUser.getProductNumber(productList.getSelectionModel().getSelectedIndex()));
						numberEntry.setText(numberOfProduct);
						numberEntry.requestFocus();
						
					});
					numberEntry.setOnAction((ActionEvent e) -> {
						workingUser.setNumberOfProducts(productList.getSelectionModel().getSelectedIndex(), Integer.parseInt(numberEntry.getText()));
						productList.getSelectionModel().select(productList.getSelectionModel().getSelectedIndex() + 1);
						numberEntry.requestFocus();
					});
						
					
				}
				else if(selectedOption.equals("List Products")) {
					grid.getChildren().clear();
					ScrollPane productList = workingUser.printDatabase("Product");
					grid.add(productList, 0,0);
				}
				else if(selectedOption.equals("Save Product Database")) {
					grid.getChildren().clear();
					Button save = new Button("Save Product Database");
					Text saveLabel = new Text("Save database to adminProductDatabase.txt?");
					grid.add(saveLabel, 0,0);
					grid.add(save, 0,1);
					save.setOnAction((ActionEvent e) -> {
						workingUser.adminWriteOutDatabase("Product");
						saveLabel.setText("saved");
					});
				}
				else if(selectedOption.equals("Reset Bills")) {
					grid.getChildren().clear();
					Button save = new Button("Reset Bills");
					Text saveLabel = new Text("Are you sure you would like to reset the bills? \nThis cannot be undone.");
					saveLabel.setTextAlignment(TextAlignment.CENTER);
					grid.add(saveLabel, 0,0,2,1);
					grid.add(save, 1,1);
					save.setOnAction((ActionEvent e) -> {
						workingUser.adminWriteOutDatabase("Person");
						saveLabel.setText("saved");
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
						if(!workingUser.passwordsEqual(oldPW.getText())) {
							error.setText("Password incorrect");
							oldPW.setText("");
							grid.getChildren().remove(error);
							grid.add(error, 2,0);
						}
						else {
							grid.add(newLabel, 0,1);
							grid.add(newPW,1,1);
							grid.add(againLabel, 0,2);
							grid.add(newPW2, 1,2);
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
						}
						else {
							error.setText("Passwords do not match");
							grid.getChildren().remove(error);
							grid.add(error, 1,3);
						}
					});
				}
				else if(selectedOption.equals("Save Databases To USB")) {
					grid.getChildren().clear();
					Text sorry = new Text("This feature is not yet functioning");
					grid.add(sorry, 0,0);
				}
				else if(selectedOption.equals("Close The Program")) {
					System.exit(0);
				}
		});
		Scene adminScene = new Scene(split, horizontalSize, verticalSize);
		adminStage.setScene(adminScene);
		adminStage.show();
                
	}
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
}

//package TOC19;
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
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
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

public final class Interface extends Application
{
	// Create the necessary instance variables.
	private final WorkingUser workingUser; // Place for all data to go through
	private ScrollPane nameDataOut; // output of product data in the interface. 
	private ScrollPane priceDataOut; // output the price of the product.

	private int logicalSize;
//	Timer timeOut = new Timer(60000000, new actionListener());
		
	public Interface() 
	{
		workingUser = new WorkingUser();
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

		// create label for input
		Text inputLabel = new Text("Enter your PMKeyS");
		inputLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
		grid.add(inputLabel, 0,0); // place in top left hand corner

		// create input textfield
		TextField input = new TextField();
		grid.add(input, 1,0); // place to the right of the input label
		input.requestFocus(); // make this the focus for the keyboard when the program starts
		Text userLabel = new Text("Error"); 
		
		// create label and text field for totalOutput
		Text totalLabel = new Text("				Total:");
		totalLabel.setTextAlignment(TextAlignment.RIGHT);
		grid.add(totalLabel, 3,8); // place at the bottum right, before total and purchase. 
		TextField total = new TextField(String.valueOf("$" + workingUser.getPrice())); // create a textfield with the price of the currant checkout. 
		total.setEditable(false); // stop the user thinking they can change the total price. 
		grid.add(total, 4,8); // add to the right of total label. 
				
		// create button to enter data from input
		Button enterBarCode = new Button("OK"); // button linked to action on input text field.
    	grid.add(enterBarCode, 2,0); // add to the direct right of the input text field
		
               // work checkout output
		Text nameData = new Text(workingUser.getCheckOutNames()); // the data which will be output by the checkout
		nameData.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
		nameDataOut = new ScrollPane(nameData); // create a scroll pane to handle the data. 
		nameDataOut.setPrefViewportHeight(400);
		nameDataOut.setPrefViewportWidth(550);
		nameDataOut.setVbarPolicy(ScrollBarPolicy.NEVER); // ensure that there is a scroll bar.
		grid.add(nameDataOut, 0,1,4,7); // place front and centre. 
		// Do the same with the price
		Text priceData = new Text(workingUser.getCheckOutPrices());
		priceData.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
		priceDataOut = new ScrollPane(priceData); // create a scroll pane to handle the data. 
		priceDataOut.setPrefViewportHeight(400); // was 400
		priceDataOut.setPrefViewportWidth(100);
		priceDataOut.setVbarPolicy(ScrollBarPolicy.ALWAYS); // ensure that there is a scroll bar.
		grid.add(priceDataOut, 4,1,2,7); // place front and centre. 
		
		// listen for changes to the scroll bar value
		DoubleProperty vPosition = new SimpleDoubleProperty();
			vPosition.bind(priceDataOut.vvalueProperty());
			vPosition.addListener(new ChangeListener() {
				@Override
				public void changed(ObservableValue arg0, Object arg1, Object arg2) {
//					 nameDataOut.setVvalue((double) arg2);
					nameDataOut.setVvalue(priceDataOut.getVvalue()); // this is going in the right direction, but does not work. 

				}
			});
			
			

        //listen on enter product barcode button
		enterBarCode.setOnAction((ActionEvent e) -> {
				if(workingUser.userNumber() == -1) { // treat the input as a PMKeyS
					PMKeySEntered(input.getText()); // take the text, do user logon stuff with it. 
			
			
			
					if(workingUser.userNumber() != -1) {
						userLabel.setText(workingUser.userName()); // find out the name of those who dare log on. 
						inputLabel.setText("Enter Barcode"); // change the label to suit the next action. 
						grid.getChildren().remove(userLabel); // remove any error labels which may have appeared. 
						grid.add(userLabel, 4,0); // add the new user label
						// the above two are done as we do not know whether a user label exists there. Adding two things to the same place causes an exception. 
						input.clear(); // clear the PMKeyS from the input ready for product bar codes. 
					}
					else {
						input.clear(); // there was an error with the PMKeyS, get ready for another. 
						userLabel.setText("Error"); // tell the user there was a problem. Maybe this could be done better. 
						grid.getChildren().remove(userLabel); // Remove a userlabel, as above. 
						grid.add(userLabel, 4,0); // add it again, as above. 
					}
				}
				else {
					boolean correct = productEntered(input.getText());
					if (correct) {
						nameData.setText(workingUser.getCheckOutNames());
						nameDataOut = new ScrollPane(nameData);
						priceData.setText(workingUser.getCheckOutPrices());
						priceDataOut = new ScrollPane(priceData);
						total.setText(String.valueOf("$" + workingUser.getPrice()));
						input.clear();
					}
					else 
						input.clear();
				}
		});
		input.setOnKeyPressed((KeyEvent ke) -> { // the following allows the user to hit enter rather than OK. Works exactly the same as hitting OK. 
			if (ke.getCode().equals(KeyCode.ENTER)) {
				if(workingUser.userNumber() == -1) {
					PMKeySEntered(input.getText());
			
			
			
					if(workingUser.userNumber() != -1) {
						userLabel.setText(workingUser.userName());
						inputLabel.setText("Enter Barcode");
						grid.getChildren().remove(userLabel);
						grid.add(userLabel, 4,0);
						input.clear();
					}
					else {
						input.clear();
						userLabel.setText("error");
						grid.getChildren().remove(userLabel);
						grid.add(userLabel, 4,0);
						input.clear();
					}
				}
				else {
					boolean correct = productEntered(input.getText());
					if (correct) {
						nameData.setText(workingUser.getCheckOutNames());
						nameDataOut = new ScrollPane(nameData);
						priceData.setText(workingUser.getCheckOutPrices());
						priceDataOut = new ScrollPane(priceData);
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
			enterAdminMode(new Stage()); // method which will work the admin mode features. 
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
			nameData.setText(workingUser.getCheckOutNames()); // clear the data of the scroll pane, as the checkout will be clear. 
			priceData.setText(workingUser.getCheckOutPrices());
			nameDataOut = new ScrollPane(nameData); // replace the now clear scroll pane. 
			priceDataOut = new ScrollPane(nameData);
		});
        grid.add(purchase, 5,8); // add the button to the bottum right corner, next to the total price. 
                
		Button cancel = new Button("Cancel");
		cancel.setOnAction((ActionEvent e) -> {
			workingUser.logOut(); // set user number to -1 and delete any checkout made. 
			grid.getChildren().remove(userLabel); // make it look like no user is logged in
			inputLabel.setText("Enter your PMKeyS"); // set the input label to something appropriate. 
			nameData.setText(workingUser.getCheckOutNames()); // clear the data of the checkout. 
			priceData.setText(workingUser.getCheckOutPrices());
			nameDataOut = new ScrollPane(nameData); // clear the scroll pane output. 
			priceDataOut = new ScrollPane(priceData);
			total.setText(String.valueOf(workingUser.getPrice())); // set the total price to 0.00. 
		});
		grid.add(cancel, 5,0); // add the button to the right of the user name. 
                
                
                
		
		Scene primaryScene = new Scene(grid, 800, 600); // create the scene at 800x600
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
	private void enterAdminMode(Stage primaryStage)
	{
		primaryStage.hide();
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
		optionList.setItems(items);
		
		grid.add(optionList, 0,0, 1, 7);
		Button people = new Button("People");
		people.setOnAction((ActionEvent e) -> {
			items.setAll("Add Person", "Remove Person", "List People", "Save Person Database");
			optionList.setItems(items);
		});
//		grid.add(people, 2,0);
		Button products = new Button("Products");
		products.setOnAction((ActionEvent e) -> {
			items.setAll("Add Products", "Remove Products", "Change a Product","Enter Stock Counts", "List Products", "Save Product Database");
			optionList.setItems(items);
		});
//		grid.add(products, 3,0);
		Button admin = new Button("Admin");
		admin.setOnAction((ActionEvent e) -> {
			items.setAll("Reset Bills", "Change Password", "Save Databases To USB", "Close The Program");
			optionList.setItems(items);
		});
//		grid.add(admin, 4,0);
		Button logout = new Button("Logout");
		logout.setOnAction((ActionEvent e) -> {
			adminStage.close();
		});
//		grid.add(logout, 5,0);
		ToolBar buttonBar = new ToolBar(people, products, admin, logout);
		rightPane.getChildren().addAll(buttonBar, grid);
		split.getItems().addAll(optionList, rightPane);
		split.setDividerPositions(0.2f);
		optionList.getSelectionModel().selectedItemProperty().addListener(
            (ObservableValue<? extends String> ov, String old_val, String selectedOption) -> {
				if( selectedOption.equals("Add Person")) {
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
						workingUser.removePerson(index);
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
					grid.add(remove, 1,0);
					ListView<String> productList = new ListView<>();
					ObservableList<String> product = FXCollections.observableArrayList();
					product.setAll(workingUser.getProductNames());
					productList.setItems(product);
					grid.add(productList,0,0);
					remove.setOnAction((ActionEvent e) -> {
						int index = productList.getSelectionModel().getSelectedIndex();
						workingUser.removeProduct(index);
						product.setAll(workingUser.getUserNames());
					});
					grid.add(remove, 2,2);
					items.setAll(workingUser.getProductNames());
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
					
					productList.getSelectionModel().selectedItemProperty().addListener(
					(ObservableValue<? extends String> vo, String oldVal, String selectedProduct) -> {
						String numberOfProduct = Integer.toString(workingUser.getProductNumber(productList.getSelectionModel().getSelectedIndex()));
						numberEntry.setText(numberOfProduct);
						
					});
					numberEntry.setOnAction((ActionEvent e) -> {
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
		});
		Scene adminScene = new Scene(split, 800, 600);
		adminStage.setScene(adminScene);
		adminStage.show();
                
	}
	public static void main(String[] args)
	{
		Application.launch(args);
	}
}
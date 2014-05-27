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
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.control.TextField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Button;
import javafx.scene.Scene;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.stage.Stage;

public final class Interface extends Application
{
	// Create the necessary instance variables.
	private final WorkingUser workingUser; // Place for all data to go through
	private ScrollPane dataOut; // output of product data in the interface. 

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
		Text userLabel = new Text("Error"); // this should never be shown. 
		
		// create label and text field for totalOutput
		Text totalLabel = new Text("Total:");
		grid.add(totalLabel, 3,8); // place at the bottum right, before total and purchase. 
		TextField total = new TextField(String.valueOf("$" + workingUser.getPrice())); // create a textfield with the price of the currant checkout. 
		total.setEditable(false); // stop the user thinking they can change the total price. 
		grid.add(total, 4,8); // add to the right of total label. 
				
		// create button to enter data from input
		Button enterBarCode = new Button("OK"); // button linked to action on input text field.
    	grid.add(enterBarCode, 2,0); // add to the direct right of the input text field
		
               // work checkout output
		Text data = new Text(workingUser.getCheckOut()); // the data which will be output by the checkout
		data.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
		dataOut = new ScrollPane(data); // create a scroll pane to handle the data. 
		dataOut.setVbarPolicy(ScrollBarPolicy.ALWAYS); // ensure that there is a scroll bar.
		grid.add(dataOut, 0,1,8,7); // place front and centre. 
		
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
					productEntered(input.getText()); // if the user is logged in (!= -1) it must be a product. 
				
					data.setText(workingUser.getCheckOut()); // set the scrollpane to have the new text. 
					dataOut = new ScrollPane(data); // as above
					total.setText(String.valueOf("$" + workingUser.getPrice())); // print the price out at the bottum of the screen. 
				
					input.clear(); // give the user a clear input for the next item. 
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
					}
				}
				else {
					productEntered(input.getText());
				
					data.setText(workingUser.getCheckOut());
					dataOut = new ScrollPane(data);
					total.setText(String.valueOf("$" + workingUser.getPrice()));
				
					input.clear();
				}
			}
		});

                
                // create and listen on admin button
		Button adminMode = new Button("Enter Admin Mode"); // button which will bring up the admin mode. 
		adminMode.setOnAction((ActionEvent e) -> {
			enterAdminMode(new Stage()); // method which will work the admin mode features. 
			primaryStage.hide(); // hide the user side of things. 
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
			data.setText(workingUser.getCheckOut()); // clear the data of the scroll pane, as the checkout will be clear. 
			dataOut = new ScrollPane(data); // replace the now clear scroll pane. 
		});
        grid.add(purchase, 5,8); // add the button to the bottum right corner, next to the total price. 
                
		Button cancel = new Button("cancel");
		cancel.setOnAction((ActionEvent e) -> {
			workingUser.logOut(); // set user number to -1 and delete any checkout made. 
			grid.getChildren().remove(userLabel); // make it look like no user is logged in
			inputLabel.setText("Enter your PMKeyS"); // set the input label to something appropriate. 
			data.setText(workingUser.getCheckOut()); // clear the data of the checkout. 
						dataOut = new ScrollPane(data); // clear the scroll pane output. 
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
	private void productEntered(String input)
	{
		workingUser.addToCart(input);
	}
	private void enterAdminMode(Stage primaryStage)
	{
            primaryStage.hide();
            Stage adminStage = new Stage();
            adminStage.setTitle("TOC19");
            GridPane grid = new GridPane();
            grid.setGridLinesVisible(true);
            grid.setAlignment(Pos.CENTER);
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(25, 25, 25, 25));
            
            // add working code in here
            Scene adminScene = new Scene(grid, 800, 600);
            adminStage.setScene(adminScene);
            adminStage.show();
                
	}
	public static void main(String[] args)
	{
		Application.launch(args);
	}
}
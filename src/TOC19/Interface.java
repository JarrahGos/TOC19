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
	private final WorkingUser workingUser;
	private ScrollPane dataOut;

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
		primaryStage.setTitle("TOC19");
		workingUser.addDatabases();
		GridPane grid = new GridPane();
//	    grid.setGridLinesVisible(true);
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(15, 15, 15, 15));

		// create label for input
		Text inputLabel = new Text("Enter your PMKeyS");
		inputLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
		grid.add(inputLabel, 0,0);

		// create input textfield
		TextField input = new TextField();
		grid.add(input, 1,0);
		input.requestFocus();
		Text userLabel = new Text("Error"); 
		
		// create label and text field for totalOutput
		Text totalLabel = new Text("Total:");
		grid.add(totalLabel, 3,8); 
		TextField total = new TextField(String.valueOf("$" + workingUser.getPrice()));
		grid.add(total, 4,8); 
				
		// create button to enter data from input
		Button enterBarCode = new Button("OK");
		Button enterPMKeyS = new Button("OK");
		// action if PMKeyS button is pressed
		enterPMKeyS.setOnAction((ActionEvent e) -> {
			PMKeySEntered(input.getText());
			
			
			
			if(workingUser.userNumber() != -1) {
				userLabel.setText(workingUser.userName());
				inputLabel.setText("Enter Barcode");
				//grid.add(inputLabel, 0,0);
				grid.add(userLabel, 4,0);
				grid.getChildren().remove(enterPMKeyS);
				grid.add(enterBarCode, 2,0);
			}
			else {
				input.clear();
//					grid.add(input, 1,0);
			}
		});
    	grid.add(enterPMKeyS, 2,0);
		
               // work checkout output
		Text data = new Text(workingUser.getCheckOut());
		data.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
		dataOut = new ScrollPane(data);
		dataOut.setVbarPolicy(ScrollBarPolicy.ALWAYS);
		grid.add(dataOut, 0,1,8,7);
		
        //listen on enter product barcode button
		enterBarCode.setOnAction((ActionEvent e) -> {
			productEntered(input.getText());
			
			data.setText(workingUser.getCheckOut());
			dataOut = new ScrollPane(data);
//				grid.add(dataOut, 1,1,4,7);
			total.setText(String.valueOf("$" + workingUser.getPrice()));
			grid.add(enterBarCode, 2,0);
			
			input.clear();
		});
		input.setOnKeyPressed((KeyEvent ke) -> {
			if (ke.getCode().equals(KeyCode.ENTER)) {
				if(workingUser.userNumber() == -1) {
					PMKeySEntered(input.getText());
			
			
			
					if(workingUser.userNumber() != -1) {
						userLabel.setText(workingUser.userName());
						inputLabel.setText("Enter Barcode");
						//grid.add(inputLabel, 0,0);
						grid.getChildren().remove(userLabel);
						grid.add(userLabel, 4,0);
						grid.getChildren().remove(enterPMKeyS);
						grid.add(enterBarCode, 2,0);
						input.clear();
					}
					else {
						input.clear();
						userLabel.setText("error");
						grid.getChildren().remove(userLabel);
						grid.add(userLabel, 4,0);
//						grid.add(input, 1,0);
					}
				}
				else {
					productEntered(input.getText());
				
					data.setText(workingUser.getCheckOut());
					dataOut = new ScrollPane(data);
//					grid.add(dataOut, 1,1,4,7);
					total.setText(String.valueOf("$" + workingUser.getPrice()));
					//grid.add(enterBarCode, 2,0);
				
					input.clear();
//					grid.add(input, 1,2,1,4);
				}
			}
		});

                
                // create and listen on admin button
		Button adminMode = new Button("Enter Admin Mode");
		adminMode.setOnAction((ActionEvent e) -> {
			enterAdminMode(new Stage());
			primaryStage.hide();
		});
		grid.add(adminMode, 0,8);
                
                // create and listen on purchase button
        Button purchase = new Button("Purchase");
        purchase.setOnAction((ActionEvent e) -> {
			workingUser.buyProducts();
			grid.getChildren().remove(userLabel);
			grid.getChildren().remove(enterBarCode);
			inputLabel.setText("Enter your PMKeyS");
			grid.add(enterPMKeyS, 2,0);
			total.setText(String.valueOf(workingUser.getPrice()));
			input.clear();
			data.setText(workingUser.getCheckOut());
			dataOut = new ScrollPane(data);
		});
        grid.add(purchase, 5,8);
                
		Button cancel = new Button("cancel");
		cancel.setOnAction((ActionEvent e) -> {
			workingUser.logOut();
			grid.getChildren().remove(userLabel);
			grid.getChildren().remove(enterBarCode);
			inputLabel.setText("Enter your PMKeyS");
			dataOut = new ScrollPane(data);
			data.setText(workingUser.getCheckOut());
			total.setText(String.valueOf(workingUser.getPrice()));
			grid.add(enterPMKeyS, 2,0);
		});
		grid.add(cancel, 5,0);
                
                
                
		
		Scene primaryScene = new Scene(grid, 800, 600);
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

		Interface intFace = new Interface(); // initalise task
	
//		intFace.start(); // Engage
		Application.launch(args);
//            Stage initial = new Stage();
//            intFace.start(initial);

	}
}
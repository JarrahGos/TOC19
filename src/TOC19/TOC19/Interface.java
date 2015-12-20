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
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Interface extends Application
{
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
		String[] settings = config.interfaceSettings();
		horizontalSize = Integer.parseInt(settings[0]);
		verticalSize = Integer.parseInt(settings[1]);
		textSize = Integer.parseInt(settings[2]);
		//initalize the variables created above
		
	}

	/**
	 * Flash the given node a given colour for a given time
	 *
	 * @param node     The node to be flashed
	 * @param duration the duration in ms for the node to be flashed
	 * @param colour   The colour (from Color) that you wish to flash.
	 */
	public static void flashColour(Node node, int duration, Color colour) {

		InnerShadow shadow = new InnerShadow();
		shadow.setRadius(25d);
		shadow.setColor(colour);
		node.setEffect(shadow);

		Timeline time = new Timeline();

		time.setCycleCount(1);

		List<KeyFrame> frames = new ArrayList<>();
		frames.add(new KeyFrame(Duration.ZERO, new KeyValue(shadow.radiusProperty(), 25)));
		frames.add(new KeyFrame(new Duration(duration), new KeyValue(shadow.radiusProperty(), 0)));
		time.getKeyFrames().addAll(frames);

		time.playFromStart();
	}

	/**
	 * Bind the scrollbars of two listviews
	 *
	 * @param lv1 The first listview to bind
	 * @param lv2 The second Listview to bind
	 */
	public static void bind(ListView lv1, ListView lv2) { //TODO: this does not work.
		ScrollBar bar1 = null;
		ScrollBar bar2 = null;

		for (Node node : lv1.lookupAll(".scroll-bar")) {
			if (node instanceof ScrollBar && ((ScrollBar) node).getOrientation().equals(Orientation.VERTICAL)) {
				bar1 = (ScrollBar) node;
			}
		}
		for (Node node : lv2.lookupAll(".scroll-bar")) {
			if (node instanceof ScrollBar && ((ScrollBar) node).getOrientation().equals(Orientation.VERTICAL)) {
				bar2 = (ScrollBar) node;
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
	 *
	 * @param args No arguments needed, -w int for width, -h int for height, both in pixels.
	 */
	public static void main(String[] args) {
		for (int i = args.length - 1; i > 0; i--) {
			if (args[i].equals("-w") && i != args.length - 1) {
				horizontalSize = Integer.parseInt(args[i + 1]);
			} else if (args[i].equals("-h") && i != args.length - 1) {
				verticalSize = Integer.parseInt(args[i + 1]);
			}
		}
		Application.launch(args);
	}

	/**
	 * Allows the user to enter the password for the admin user.
	 * Will start the admin stage if the password is entered correctly.
	 */

	/**
	 * The user part of the GUI
	 * @param primaryStage The base stage of the program
	 */
	@Override
	public void start(Stage primaryStage) {
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
		grid.add(inputLabel, 0, 0); // place in top left hand corner

		// create input textfield
		TextField input = new TextField();
		grid.add(input, 1, 0); // place to the right of the input label
		input.requestFocus(); // make this the focus for the keyboard when the program starts
		Text userLabel = new Text("Error");

		// create label and text field for totalOutput
		Text totalLabel = new Text("				Total:");
		totalLabel.setTextAlignment(TextAlignment.RIGHT);
		grid.add(totalLabel, 2, 8); // place at the bottum right, before total and purchase.
		TextField total = new TextField(String.valueOf("$" + WorkingUser.getPrice())); // create a textfield with the price of the currant checkout.
		total.setEditable(false); // stop the user thinking they can change the total price.
		grid.add(total, 3, 8); // add to the right of total label.

		// create button to enter data from input
		Button enterBarCode = new Button("OK"); // button linked to action on input text field.
		grid.add(enterBarCode, 2, 0, 2, 1); // add to the direct right of the input text field

		//create product error text
		Text productError = new Text();
		grid.add(productError, 1, 8);


		// create the lists for the checkout.

		SplitPane checkoutOut = new SplitPane();
		checkoutOut.setPrefHeight(500);
		ListView<String> itemList = new ListView<>();
		ObservableList<String> items = FXCollections.observableArrayList();
		items.setAll(WorkingUser.getCheckOutNames());
		itemList.setItems(items);
		ListView<String> priceList = new ListView<>();
		ObservableList<String> prices = FXCollections.observableArrayList();
		prices.setAll(WorkingUser.getCheckOutPrices());
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
			if (!WorkingUser.userLoggedIn()) { // treat the input as a PMKeyS
				int userError;
				userError = PMKeySEntered(input.getText()); // take the text, do user logon stuff with it.


				if (WorkingUser.userLoggedIn()) {
					Thread thread = new Thread(new Runnable() {

						@Override
						public void run() {
							try {
								Thread.sleep(1); // after this time, log the user out.
								WorkingUser.logOut(); // set user number to -1 and delete any checkout made.

								grid.getChildren().remove(userLabel); // make it look like no user is logged in
								inputLabel.setText("Enter your PMKeyS"); // set the input label to something appropriate.
								total.setText(String.valueOf("$" + WorkingUser.getPrice())); // set the total price to 0.00.
							} catch (InterruptedException e) {
								// do nothing here.
							}
						}
					});
					thread.setDaemon(true);
					thread.setPriority(Thread.MIN_PRIORITY);
					thread.start();
					thread.interrupt();
					flashColour(input, 1500, Color.AQUAMARINE);
					userLabel.setText(WorkingUser.userName(userError) + "—$" + WorkingUser.getUserBill()); // find the name of those who dare log on.
					inputLabel.setText("Enter Barcode"); // change the label to suit the next action.
					grid.getChildren().remove(userLabel); // remove any error labels which may have appeared.
					grid.add(userLabel, 3, 0); // add the new user label
					// the above two are done as we do not know whether a user label exists there. Adding two things to the same place causes an exception.
					input.clear(); // clear the PMKeyS from the input ready for product bar codes.

				} else {
					input.clear(); // there was an error with the PMKeyS, get ready for another.
					userLabel.setText(WorkingUser.userName(userError)); // tell the user there was a problem. Maybe this could be done better.
					grid.getChildren().remove(userLabel); // Remove a userlabel, as above.
					grid.add(userLabel, 3, 0); // add it again, as above.
					flashColour(input, 1500, Color.RED);
				}
			} else {
				boolean correct = productEntered(input.getText());
				if (correct) {
					productError.setText("");
					items.setAll(WorkingUser.getCheckOutNames());
					itemList.setItems(items);
					prices.setAll(WorkingUser.getCheckOutPrices());
					priceList.setItems(prices);
					total.setText(String.valueOf("$" + WorkingUser.getPrice()));
					input.clear();
					input.requestFocus();
					flashColour(input, 500, Color.AQUAMARINE);
				} else {
					productError.setText("Could not read that product");
					input.clear();
					input.requestFocus();
					flashColour(input, 500, Color.RED);
				}
			}
			input.requestFocus();
		});
		input.setOnKeyPressed((KeyEvent ke) -> { // the following allows the user to hit enter rather than OK. Works exactly the same as hitting OK.
			if (ke.getCode().equals(KeyCode.ENTER)) {
				if (!WorkingUser.userLoggedIn()) {
					int userError = PMKeySEntered(input.getText());

					if (WorkingUser.userLoggedIn()) {
						userLabel.setText(WorkingUser.userName(userError) + "—$" + WorkingUser.getUserBill());
						inputLabel.setText("Enter Barcode");
						grid.getChildren().remove(userLabel);
						grid.add(userLabel, 3, 0);
						input.clear();
						flashColour(input, 1500, Color.AQUAMARINE);
						input.requestFocus();
						checkoutOut.setDividerPositions(0.8f);
					} else {
						input.clear();
						userLabel.setText(WorkingUser.userName(userError));
						grid.getChildren().remove(userLabel);
						grid.add(userLabel, 3, 0);
						input.clear();
						input.requestFocus();
						flashColour(input, 1500, Color.RED);
					}
				} else {
					boolean correct = productEntered(input.getText());
					if (correct) {
						items.setAll(WorkingUser.getCheckOutNames());
						itemList.setItems(items);
						prices.setAll(WorkingUser.getCheckOutPrices());
						priceList.setItems(prices);
						total.setText(String.valueOf("$" + WorkingUser.getPrice()));
						input.clear();
						input.requestFocus();
						flashColour(input, 500, Color.AQUAMARINE);
					} else {
						input.clear();
						input.requestFocus();
						flashColour(input, 500, Color.RED);
					}
				}
			}
			input.requestFocus();
		});


		// create and listen on admin button
		Button adminMode = new Button("Enter Admin Mode"); // button which will bring up the admin mode.
		adminMode.setOnAction((ActionEvent e) -> {
			WorkingUser.logOut(); // set user number to -1 and delete any checkout made.
			grid.getChildren().remove(userLabel); // make it look like no user is logged in
			inputLabel.setText("Enter your PMKeyS"); // set the input label to something appropriate.
			items.setAll(WorkingUser.getCheckOutNames());
			itemList.setItems(items);
			prices.setAll(WorkingUser.getCheckOutPrices());
			priceList.setItems(prices);
			total.setText(String.valueOf(WorkingUser.getPrice())); // set the total price to 0.00.
			checkoutOut.setDividerPositions(0.8f);
			input.requestFocus();
			AdminInterface.enterPassword(); // method which will work the admin mode features.
			input.requestFocus();
		});
		grid.add(adminMode, 0, 8); // add the button to the bottum left of the screen.

		Button removeProduct = new Button("Remove"); // button which will bring up the admin mode.
		removeProduct.setOnAction((ActionEvent e) -> {
			int index = itemList.getSelectionModel().getSelectedIndex();

			if (index >= 0) {
				WorkingUser.deleteProduct(index);
				prices.setAll(WorkingUser.getCheckOutPrices());
				priceList.setItems(prices);
				items.setAll(WorkingUser.getCheckOutNames());
				itemList.setItems(items); //TODO: add select top.
				total.setText(String.valueOf("$" + WorkingUser.getPrice()));
				itemList.scrollTo(index);

				flashColour(removeProduct, 1500, Color.AQUAMARINE);
			} else flashColour(removeProduct, 1500, Color.RED);
			input.requestFocus();
		});
		grid.add(removeProduct, 2, 8); // add the button to the bottum left of the screen.

		// create and listen on purchase button
		Button purchase = new Button("Purchase"); // button which will add the cost of the items to the users bill
		purchase.setOnAction((ActionEvent e) -> {
			if (WorkingUser.userLoggedIn()) {
				WorkingUser.buyProducts(); // add the cost to the bill.
				grid.getChildren().remove(userLabel); // make it look like the user has been logged out.
				inputLabel.setText("Enter your PMKeyS"); // Set the input label to something better for user login.
				total.setText(String.valueOf(WorkingUser.getPrice())); //set total to the working users price, which after logout is 0.00
				input.clear(); // clear the input ready for a PMKeyS
				items.setAll(WorkingUser.getCheckOutNames());
				itemList.setItems(items);
				prices.setAll(WorkingUser.getCheckOutPrices());
				priceList.setItems(prices);
				checkoutOut.setDividerPositions(0.8f);
				input.requestFocus();
				flashColour(purchase, 1500, Color.AQUAMARINE);
			} else {
				flashColour(purchase, 1500, Color.RED);
				flashColour(input, 1500, Color.RED);
			}
			input.requestFocus();
		});
		grid.add(purchase, 4, 8, 2, 1); // add the button to the bottum right corner, next to the total price.

		Button cancel = new Button("Cancel");
		cancel.setOnAction((ActionEvent e) -> {
			WorkingUser.logOut(); // set user number to -1 and delete any checkout made.
			grid.getChildren().remove(userLabel); // make it look like no user is logged in
			inputLabel.setText("Enter your PMKeyS"); // set the input label to something appropriate.
			items.setAll(WorkingUser.getCheckOutNames());
			itemList.setItems(items);
			prices.setAll(WorkingUser.getCheckOutPrices());
			priceList.setItems(prices);
			input.requestFocus();
			total.setText(String.valueOf(WorkingUser.getPrice())); // set the total price to 0.00.
			checkoutOut.setDividerPositions(0.8f);
			input.requestFocus();
		});
		grid.add(cancel, 4, 0, 2, 1); // add the button to the right of the user name.
		Platform.setImplicitExit(false);
		primaryStage.setOnCloseRequest(WindowEvent::consume);
		Scene primaryScene = new Scene(grid, horizontalSize, verticalSize); // create the scene at the given size
		primaryStage.setScene(primaryScene);

		primaryStage.show();
	}

	/**
	 * Log the user into working user given their PMKeyS
	 *
	 * @param input The users PMKeyS as a string
	 * @return The error from logging the user in.
	 */
	private int PMKeySEntered(String input) {
		return WorkingUser.getPMKeyS(input);
	}

	/**
	 * Add a product to the checkout
	 * @param input The barcode of the product as a string
	 * @return A Boolean value of whether the action worked
	 */
	private boolean productEntered(String input) {
		return WorkingUser.addToCart(input);
	}
}

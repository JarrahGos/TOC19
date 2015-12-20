package TOC19;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

/**
 * Created by Jarrah on 21/12/2015.
 */
public class AdminInterface extends Interface {

    /**
     * the number of horizontal pixels, defaulted to 1024 but set by the settings class
     */
    private static int horizontalSize = 1024;
    /**
     * the number of vertical pixels, defaulted to 576 but set by the settings class
     */
    private static int verticalSize = 576;
    /**
     * The text size of the program, set by the settings class
     */
    private final int textSize;

    /**
     * Create an interface instance with it's parameters set by the config file
     *
     * @throws IOException
     */
    public AdminInterface() throws IOException {
        Settings config = new Settings();
        String[] settings = config.interfaceSettings();
        horizontalSize = Integer.parseInt(settings[0]);
        verticalSize = Integer.parseInt(settings[1]);
        textSize = Integer.parseInt(settings[2]);
    }

    public static void enterPassword() {
        Stage passwordStage = new Stage();
        passwordStage.setTitle("Password");
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(15, 15, 15, 15));
        Text PWLabel = new Text("Enter password");
        PasswordField PW = new PasswordField();
        grid.add(PWLabel, 0, 0);
        grid.add(PW, 1, 0);
        PW.setOnAction((ActionEvent e) -> {
            if (!WorkingUser.passwordsEqual(PW.getText())) {
                flashColour(PW, 1500, Color.RED);
                PW.setText("");
            } else {
                enterAdminMode(passwordStage);
            }
        });
        Scene passwordScene = new Scene(grid, 400, 200);
        passwordStage.setScene(passwordScene);
        passwordStage.show();
    }

    /**
     * Will open the admin panel of the program.
     *
     * @param lastStage The stage which opened this stage
     */
    public static void enterAdminMode(Stage lastStage) {
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
        final String[] ProductSettingsList = {"Add Products", "Remove Products", "Change a Product", "Enter Stock Counts", "List Products", "Save Product Database"};
        final String[] AdminSettingsList = {"Reset Bills", "Change Password", "Save Databases To USB", "Close The Program"};
        items.setAll(PersonSettingsList);
        optionList.setItems(items);

        grid.add(optionList, 0, 0, 1, 7);
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
        logout.setOnAction((ActionEvent e) -> adminStage.close());
        ToolBar buttonBar = new ToolBar(people, products, admin, logout);
        rightPane.getChildren().addAll(buttonBar, grid);
        split.getItems().addAll(optionList, rightPane);
        split.setDividerPositions(0.2f);
        optionList.getSelectionModel().selectedItemProperty().addListener(
                (ObservableValue<? extends String> ov, String old_val, String selectedOption) -> {
                    if (selectedOption == null) {
                    } else if (selectedOption.equals("Add Person")) {
                        addPerson(grid);
                    } else if (selectedOption.equals("Remove Person")) {
                        removePerson(grid);
                    } else if (selectedOption.equals("Change a Person")) {
                        changePerson(grid);
                    } else if (selectedOption.equals("List People")) {
                        listItems(grid, "Person");
                    } else if (selectedOption.equals("Save Person Database")) {
                        SavePersonDatabase(grid);
                    } else if (selectedOption.equals("Lock People Out")) {
                        lockPeopleOut(grid);
                    } else if (selectedOption.equals("Add Products")) {
                        addProducts(grid);
                    } else if (selectedOption.equals("Remove Products")) {
                        RemoveProducts(grid);
                    } else if (selectedOption.equals("Change a Product")) {
                        changeProduct(grid);
                    } else if (selectedOption.equals("Enter Stock Counts")) {
                        enterStockCounts(grid);
                    } else if (selectedOption.equals("List Products")) {
                        listItems(grid, "Product");
                    } else if (selectedOption.equals("Save Product Database")) {
                        saveProductDatabase(grid);
                    } else if (selectedOption.equals("Reset Bills")) {
                        resetBills(grid);
                    } else if (selectedOption.equals("Change Password")) {
                        changePassword(grid);
                    } else if (selectedOption.equals("Save Databases To USB")) { //TODO: Bring admin stage to front after
                        saveDatabases(adminStage, grid);
                    } else if (selectedOption.equals("Close The Program")) {
                        closeProgram(grid);
                    }
                });
        Scene adminScene = new Scene(split, horizontalSize, verticalSize);
        adminStage.setScene(adminScene);
        adminStage.show();
        adminStage.toFront();

    }

    private static void closeProgram(GridPane grid) {
        grid.getChildren().clear();
        Button save = new Button("Close The Program");
        grid.add(save, 1, 1);
        save.setOnAction((ActionEvent e) -> {
            flashColour(save, 1500, Color.AQUAMARINE);
            System.exit(0);
        });
    }

    private static void saveDatabases(Stage adminStage, GridPane grid) {
        DirectoryChooser fc = new DirectoryChooser();


        grid.getChildren().clear();
        Text fileLabel = new Text("Save Directory");
        TextField filePath = new TextField("");
        filePath.setEditable(true);
        Button saveDirBtn = new Button("Choose Save Directory");
        Button saveBtn = new Button("Save Databases to Selected Directories");

        grid.add(saveBtn, 1, 5);
        grid.add(fileLabel, 0, 0);
        grid.add(filePath, 0, 1);
        grid.add(saveDirBtn, 1, 1);

        saveDirBtn.setOnAction((ActionEvent e) -> {
            File returnVal = fc.showDialog(adminStage);

            if (returnVal != null) {
                filePath.setText(returnVal.getPath());
                flashColour(saveDirBtn, 1500, Color.AQUAMARINE);
            } else {
                flashColour(saveDirBtn, 1500, Color.RED);
            }
        });

        saveBtn.setOnAction((ActionEvent e) -> {
            try {
                WorkingUser.adminWriteOutDatabase("Person"); //adminPersonDatabase.csv
                WorkingUser.adminWriteOutDatabase("Product"); //adminProductDatabase.csv

                File adminPersonFile = new File(Compatibility.getFilePath("adminPersonDatabase.csv"));
                File adminProductFile = new File(Compatibility.getFilePath("adminProductDatabase.csv"));
                if (filePath.getText().isEmpty() || filePath.getText() != null) {
                    File destPers = new File(filePath.getText() + "/adminPersonDatabase.csv");
                    File destProd = new File(filePath.getText() + "/adminProductDatabase.csv");
                    Files.copy(adminPersonFile.toPath(), destPers.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    Files.copy(adminProductFile.toPath(), destProd.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    flashColour(saveBtn, 3000, Color.AQUAMARINE);
                } else {
                    flashColour(saveBtn, 3000, Color.RED);
                    flashColour(filePath, 3000, Color.RED);
                }

            } catch (IOException e1) {
                Log.print(e1);
                flashColour(saveBtn, 3000, Color.RED);
            }
        });
    }

    private static void changePassword(GridPane grid) {
        grid.getChildren().clear();
        Text oldLabel = new Text("Enter old password");
        Text newLabel = new Text("Enter new password");
        Text againLabel = new Text("Enter new password again");
        PasswordField oldPW = new PasswordField();
        PasswordField newPW = new PasswordField();
        PasswordField newPW2 = new PasswordField();
        grid.add(oldLabel, 0, 0);
        grid.add(oldPW, 1, 0);
        Text error = new Text();
        oldPW.setOnAction((ActionEvent e) -> {
            if (!WorkingUser.passwordsEqual(oldPW.getText())) {
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
        newPW.setOnAction((ActionEvent e) -> newPW2.requestFocus());
        newPW2.setOnAction((ActionEvent e) -> {
            if (newPW.getText() != null && !newPW.getText().equals("") && newPW.getText().equals(newPW2.getText())) {
                WorkingUser.setAdminPassword(WorkingUser.getSecurePassword(newPW.getText()));
                Text changed = new Text("Success");
                grid.add(changed, 1, 3);
                flashColour(newPW, 1500, Color.AQUAMARINE);
                flashColour(newPW2, 1500, Color.AQUAMARINE);
            } else {
                error.setText("Passwords do not match");
                grid.getChildren().remove(error);
                grid.add(error, 1, 3);
            }
        });
    }

    private static void resetBills(GridPane grid) {
        grid.getChildren().clear();
        Button save = new Button("Reset Bills");
        Text saveLabel = new Text("Are you sure you would like to reset the bills? \nThis cannot be undone.");
        saveLabel.setTextAlignment(TextAlignment.CENTER);
        grid.add(saveLabel, 0, 0, 2, 1);
        grid.add(save, 1, 1);
        save.setOnAction((ActionEvent e) -> {
            WorkingUser.resetBills();
            flashColour(save, 1500, Color.AQUAMARINE);
            save.setDisable(true);
            save.setText("Bills Reset");
        });
    }

    private static void saveProductDatabase(GridPane grid) {
        grid.getChildren().clear();
        Button save = new Button("Save Product Database");
        Text saveLabel = new Text("Save database to adminProductDatabase.txt?");
        grid.add(saveLabel, 0, 0);
        grid.add(save, 0, 1);
        save.setOnAction((ActionEvent e) -> {
            try {
                WorkingUser.adminWriteOutDatabase("Product");
                flashColour(save, 3000, Color.AQUAMARINE);
            } catch (IOException e1) {
                Log.print(e1);
                flashColour(save, 3000, Color.RED);
            }
        });
    }

    private static void listItems(GridPane grid, String product) {
        grid.getChildren().clear();
        ScrollPane productList = null;
        try {
            productList = WorkingUser.printDatabase(product);
        } catch (IOException e) {
            Log.print(e);
        }
        grid.add(productList, 0, 0);
    }

    private static void enterStockCounts(GridPane grid) {
        grid.getChildren().clear();
        ListView<String> productList = new ListView<>();
        ObservableList<String> product = FXCollections.observableArrayList();
        product.setAll(WorkingUser.getProductNames());
        productList.setItems(product);
        grid.add(productList, 0, 0, 1, 4);
        Text numberLabel = new Text("Number:");
        grid.add(numberLabel, 1, 0);
        TextField numberEntry = new TextField();
        grid.add(numberEntry, 2, 0);

        productList.getSelectionModel().selectedItemProperty().addListener(
                (ObservableValue<? extends String> vo, String oldVal, String selectedProduct) -> {
                    String numberOfProduct = Integer.toString(WorkingUser.getProductNumber(productList.getSelectionModel().getSelectedItem()));
                    numberEntry.setText(numberOfProduct);
                    numberEntry.requestFocus();

                });
        numberEntry.setOnAction((ActionEvent e) -> {
            WorkingUser.setNumberOfProducts(productList.getSelectionModel().getSelectedItem(), Integer.parseInt(numberEntry.getText()));
            productList.getSelectionModel().select(productList.getSelectionModel().getSelectedIndex() + 1);
            numberEntry.requestFocus();
            flashColour(numberEntry, 1500, Color.AQUAMARINE);
        });
    }

    private static void changeProduct(GridPane grid) {
        grid.getChildren().clear();
        ListView<String> productList = new ListView<>();
        ObservableList<String> product = FXCollections.observableArrayList();
        product.setAll(WorkingUser.getProductNames());
        productList.setItems(product);
        grid.add(productList, 0, 0, 1, 4);
        Text nameLabel = new Text("Name:");
        grid.add(nameLabel, 1, 0);
        TextField nameEntry = new TextField();
        nameEntry.requestFocus();
        grid.add(nameEntry, 2, 0);
        Text BarCodeLabel = new Text("Barcode:");
        grid.add(BarCodeLabel, 1, 1);
        TextField barCodeEntry = new TextField();
        grid.add(barCodeEntry, 2, 1);
        Text priceLabel = new Text("Price: $");
        grid.add(priceLabel, 1, 2);
        TextField priceEntry = new TextField();
        grid.add(priceEntry, 2, 2);
        productList.getSelectionModel().selectedItemProperty().addListener(
                (ObservableValue<? extends String> vo, String oldVal, String selectedProduct) -> {
                    nameEntry.setText(selectedProduct);
                    String BC = String.valueOf(WorkingUser.getProductBarCode(productList.getSelectionModel().getSelectedItem()));
                    barCodeEntry.setText(BC);
                    String price = Double.toString(WorkingUser.getProductPrice(productList.getSelectionModel().getSelectedItem()) / 100);
                    priceEntry.setText(price);
                });
        nameEntry.setOnAction((ActionEvent e) -> barCodeEntry.requestFocus());
        barCodeEntry.setOnAction((ActionEvent e) -> priceEntry.requestFocus());
        priceEntry.setOnAction((ActionEvent e) -> {
            long barCode = -1;
            try {
                barCode = Long.parseLong(barCodeEntry.getText());
            } catch (NumberFormatException e1) {
                flashColour(barCodeEntry, 1500, Color.RED);
            }
            long price = -1;
            try {
                price = (long) (Double.parseDouble(priceEntry.getText()) * 100);
            } catch (NumberFormatException e1) {
                flashColour(priceEntry, 1500, Color.RED);
            }
            if (barCode != -1 && price != -1) {
                WorkingUser.changeDatabaseProduct(nameEntry.getText(), WorkingUser.getProductName(productList.getSelectionModel().getSelectedItem()), price,
                        barCode, WorkingUser.getProductBarCode(productList.getSelectionModel().getSelectedItem()));
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
                String BC = String.valueOf(WorkingUser.getProductBarCode(productList.getSelectionModel().getSelectedItem()));
                barCodeEntry.setText(BC);
                String price2 = Double.toString(WorkingUser.getProductPrice(productList.getSelectionModel()
                        .getSelectedItem()) / 100);
                priceEntry.setText(price2);
                product.setAll(WorkingUser.getProductNames());
                productList.setItems(product);
            }
        });
    }

    private static void RemoveProducts(GridPane grid) {
        grid.getChildren().clear();
        Button remove = new Button("Remove");
        ListView<String> productList = new ListView<>();
        ObservableList<String> product = FXCollections.observableArrayList();
        product.setAll(WorkingUser.getProductNames());
        productList.setItems(product);
        grid.add(productList, 0, 0);
        remove.setOnAction((ActionEvent e) -> {
            String index = productList.getSelectionModel().getSelectedItem();
            try {
                WorkingUser.removeProduct(index);
                flashColour(remove, 1500, Color.AQUAMARINE);
            } catch (IOException | InterruptedException e1) {
                e1.printStackTrace();
                flashColour(remove, 1500, Color.RED);
            }
            product.setAll(WorkingUser.getProductNames());
        });
        grid.add(remove, 1, 0);
        product.setAll(WorkingUser.getProductNames());
    }

    private static void addProducts(GridPane grid) {
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
        nameEntry.setOnAction((ActionEvent e) -> BarCodeEntry.requestFocus());
        BarCodeEntry.setOnAction((ActionEvent e) -> priceEntry.requestFocus());
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
            } catch (NumberFormatException e1) {
                flashColour(priceEntry, 1500, Color.RED);
            }
            if (barCode != -1 && price != -1) {
                WorkingUser.addProductToDatabase(nameEntry.getText(), barCode, price);
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

    private static void lockPeopleOut(GridPane grid) {
        grid.getChildren().clear();
        ListView<String> personList = new ListView<>();
        ObservableList<String> persons = FXCollections.observableArrayList();
        persons.setAll(WorkingUser.getUserNames());
        personList.setItems(persons);
        grid.add(personList, 0, 0);
        ChoiceBox canBuy = new ChoiceBox();
        canBuy.getItems().addAll("unlocked", "Locked");
        grid.add(canBuy, 1, 0);
        personList.getSelectionModel().selectedItemProperty().addListener(
                (ObservableValue<? extends String> vo, String oldVal, String selectedProduct) -> {
                    if (WorkingUser.userCanBuyAdmin(personList.getSelectionModel().getSelectedItem())) {
                        canBuy.getSelectionModel().select(0);
                    } else canBuy.getSelectionModel().select(1);
                });
        canBuy.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue ov, Number value, Number newValue) {
                WorkingUser.setUserCanBuy(personList.getSelectionModel().getSelectedItem(), canBuy.getSelectionModel().getSelectedIndex() == 0);
                flashColour(canBuy, 1500, Color.AQUAMARINE);
            }
        });
    }

    private static void SavePersonDatabase(GridPane grid) {
        grid.getChildren().clear();
        Button save = new Button("Save Person Database");
        Text saveLabel = new Text("Save database to adminPersonDatabase.txt?");
        grid.add(saveLabel, 0, 0);
        grid.add(save, 0, 1);
        save.setOnAction((ActionEvent e) -> {
            try {
                WorkingUser.adminWriteOutDatabase("Person");
                flashColour(save, 3000, Color.AQUAMARINE);
            } catch (IOException e1) {
                e1.printStackTrace();
                flashColour(save, 3000, Color.RED);
            }

        });
    }

    private static void changePerson(GridPane grid) {
        grid.getChildren().clear();
        ListView<String> personList = new ListView<>();
        ObservableList<String> person = FXCollections.observableArrayList();
        person.setAll(WorkingUser.getUserNames());
        personList.setItems(person);
        grid.add(personList, 0, 0, 1, 4);
        Text nameLabel = new Text("Name:");
        grid.add(nameLabel, 1, 0);
        TextField nameEntry = new TextField();
        nameEntry.requestFocus();
        grid.add(nameEntry, 2, 0);
        Text pmkeysLabel = new Text("PMKeyS:");
        grid.add(pmkeysLabel, 1, 1);
        TextField pmkeys = new TextField();
        grid.add(pmkeys, 2, 1);
        personList.getSelectionModel().selectedItemProperty().addListener(
                (ObservableValue<? extends String> vo, String oldVal, String selectedPerson) -> {
                    if (selectedPerson != null) {
                        nameEntry.setText(selectedPerson);
                        String pmkeysVal = String.valueOf(WorkingUser.getUser(selectedPerson).getBarCode());
                        pmkeys.setText(pmkeysVal);
                    }
                });
        nameEntry.setOnAction((ActionEvent e) -> pmkeys.requestFocus());

        pmkeys.setOnAction((ActionEvent e) -> {
            long pmkeysNew = -1;
            try {
                pmkeysNew = Long.parseLong(pmkeys.getText());
            } catch (NumberFormatException e1) {
                flashColour(pmkeys, 1500, Color.RED);
            }
            if (pmkeysNew != -1) {
                Person oldPerson = WorkingUser.getUser(personList.getSelectionModel().getSelectedItem());

                WorkingUser.changeDatabasePerson(personList.getSelectionModel().getSelectedItem(), nameEntry.getText(), pmkeysNew, oldPerson.getBarCode());
                nameEntry.clear();
                pmkeys.clear();
                nameEntry.requestFocus();
                flashColour(nameEntry, 1500, Color.AQUAMARINE);
                flashColour(pmkeys, 1500, Color.AQUAMARINE);
                //Now need to update the form
                String selectedIndex = personList.getSelectionModel().getSelectedItem();
                person.setAll(WorkingUser.getUserNames());
                personList.setItems(person);
                personList.getSelectionModel().select(selectedIndex);
            }
        });
    }

    private static void removePerson(GridPane grid) {
        grid.getChildren().clear();
        Button remove = new Button("Remove");
        grid.add(remove, 1, 0);
        ListView<String> personList = new ListView<>();
        ObservableList<String> persons = FXCollections.observableArrayList();
        persons.setAll(WorkingUser.getUserNames());
        personList.setItems(persons);
        grid.add(personList, 0, 0);
        remove.setOnAction((ActionEvent e) -> {
            String index = personList.getSelectionModel().getSelectedItem();
            try {
                WorkingUser.removePerson(index);
                flashColour(remove, 1500, Color.AQUAMARINE);
            } catch (IOException | InterruptedException e1) {
                e1.printStackTrace();
                flashColour(remove, 1500, Color.RED);
            }
            persons.setAll(WorkingUser.getUserNames());
        });
    }

    private static void addPerson(GridPane grid) {
        grid.getChildren().clear();
        Text nameLabel = new Text("Name:");
        grid.add(nameLabel, 0, 0);
        TextField nameEntry = new TextField();
        nameEntry.requestFocus();
        grid.add(nameEntry, 1, 0);
        Text PMKeySLabel = new Text("PMKeyS:");
        grid.add(PMKeySLabel, 0, 1);
        TextField PMKeySEntry = new TextField();
        grid.add(PMKeySEntry, 1, 1);
        nameEntry.setOnAction((ActionEvent e) -> PMKeySEntry.requestFocus());
        PMKeySEntry.setOnAction((ActionEvent e) -> {
            long PMKeyS = 7000000;
            try {
                PMKeyS = Long.parseLong(PMKeySEntry.getText());
            } catch (NumberFormatException e1) {
                Log.print(e1);
            }
            if (PMKeyS != 7000000) {
                WorkingUser.addPersonToDatabase(nameEntry.getText(), PMKeyS);
                nameEntry.clear();
                PMKeySEntry.clear();
                nameEntry.requestFocus();
                flashColour(nameEntry, 1500, Color.AQUAMARINE);
                flashColour(PMKeySEntry, 1500, Color.AQUAMARINE);
            } else {
                flashColour(PMKeySEntry, 1500, Color.RED);
            }
        });
    }
}

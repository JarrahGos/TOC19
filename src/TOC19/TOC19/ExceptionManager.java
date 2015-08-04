package TOC19;

import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ExceptionManager {

    private static ExceptionManager instance = new ExceptionManager();
    private List<String[]> blacklist = new ArrayList<>();

    public ExceptionManager() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File("exceptions.blacklist")));
            String line;

            while ((line = reader.readLine()) != null) {
                blacklist.add(line.split(";"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ExceptionManager getInstance() {
        return instance;
    }

    public void showErrorDialog(Throwable e) {

        // Create expandable Exception.
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        String exceptionText = sw.toString();

        if (isBlacklisted(exceptionText)) return;

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Exception Management");
        alert.setHeaderText("I caught me a stacktrace xD \nPlease show this to one of the system admins");
        alert.setContentText(e.getLocalizedMessage());

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

    private boolean isBlacklisted(String exceptionText) {
        for (String[] set : blacklist) {
            boolean flag = true;
            for (String str : set) {
                if (!exceptionText.contains(str)) {
                    flag = false;
                    break;
                }
            }
            if (flag) return true;
        }
        return false;
    }
}

package zad1;



import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import javax.swing.*;


public
class GUI {
    public static void createWiki(String country,String city) {

        JFXPanel panel = new JFXPanel();
        JFrame frame = new JFrame(city);
        Platform.runLater(()->{

            WebView webView = new WebView();
            WebEngine engine = webView.getEngine();
            engine.load("https://"+Service.getCountryCodeFromName(country)+".wikipedia.org/wiki/" + city);
            Pane root = new FlowPane();
            root.getChildren().addAll(webView);
            Scene scene = new Scene(root);
            panel.setScene(scene);
            frame.add(panel);
            frame.setDefaultCloseOperation(3);
            frame.pack();
            frame.setVisible(true);
        });
    }


}

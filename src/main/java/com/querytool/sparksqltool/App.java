package com.querytool.sparksqltool;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import com.querytool.sparksqltool.controller.SecondaryController;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    
    private static Stage stage;

    @Override
    public void start(Stage stage) throws IOException {
    	AppContext.instance().setApp(this);
        loginStage();
    }
    
    public void loginStage() throws IOException {
    	Stage stage = new Stage();
    	this.stage =stage;
    	stage.setResizable(false);
    	Scene scene = new Scene(loadFXML("primary"));
    	stage.setWidth(360);
    	stage.setHeight(400);
    	stage.setScene(scene);
    	stage.show();
    }
    
    public SecondaryController mainStage() throws IOException {
    	Stage stage = new Stage();
    	this.stage =stage;
    	stage.setResizable(false);
    	FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("secondary.fxml"));
    	Scene scene = new Scene(fxmlLoader.load());
    	stage.setScene(scene);
    	stage.setWidth(800);
    	stage.setHeight(600);
    	stage.show();
    	stage.setOnCloseRequest(e->{
    		System.exit(1);
    	});
    	return fxmlLoader.getController();
    }
    
    public void close() {
    	stage.close();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}
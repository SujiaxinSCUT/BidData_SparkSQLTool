package com.querytool.sparksqltool.controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.controls.JFXTextField;
import com.querytool.sparksqltool.App;
import com.querytool.sparksqltool.AppContext;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class PrimaryController implements Initializable{

	@FXML
	private ImageView logo;
	
	@FXML
	private JFXTextField accessKey_field;
	
	@FXML
	private JFXPasswordField secretKey_field;
	
	@FXML
	private JFXTextField serviceEndpoint_field;
	
	@FXML
	private JFXButton connect_btn;
	
	@FXML
	private VBox container;
	
	private Text mesText;
	
	private JFXSpinner spinner;
	

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		logo.setImage(new Image(App.class.getResource("picture/Logo.png").toString()));
		mesText = new Text();
		mesText.setFill(Color.RED);
		mesText.setFont(Font.font(12));
		spinner = new JFXSpinner();
		spinner.setPrefWidth(15);
		spinner.setPrefHeight(15);

	}
	

	
	public void message(String mes) {
		mesText.setText("*"+mes);
		container.getChildren().add(4, mesText);
	}
	
	public void clearContainer() {
		if(container.getChildren().size()==6) {
			container.getChildren().remove(4);
		}
	}
	
	public void addSpinner() {
		container.getChildren().add(4, spinner);
	}
	
	public void changeStatus() {
		connect_btn.setDisable(!connect_btn.isDisable());
		accessKey_field.setDisable(!accessKey_field.isDisable());
		secretKey_field.setDisable(!secretKey_field.isDisable());
		serviceEndpoint_field.setDisable(!serviceEndpoint_field.isDisable());
	}
	
	public void connect() {
		clearContainer();
		String access_key = accessKey_field.getText();
		if(access_key==null||access_key.length()==0) {
			message("username不能为空");
			return;
		}
		String secret_key = secretKey_field.getText();
		if(secret_key==null||secret_key.length()==0) {
			message("password不能为空");
			return;
		}
		String serviceEndpoint = serviceEndpoint_field.getText();
		if(serviceEndpoint==null||serviceEndpoint.length()==0) {
			message("serviceEndpoint不能为空");
			return;
		}
    	changeStatus();
    	addSpinner();
		final ConnectionTask task = new ConnectionTask(serviceEndpoint,access_key,secret_key);
		Thread t = new Thread(task);
        t.setDaemon(true);
        t.start();
        task.runningProperty().addListener(new ChangeListener<Boolean>() {
            @Override public void changed(ObservableValue<? extends Boolean> ov, Boolean wasRunning, Boolean isRunning) {
                if (!isRunning) {
                	changeStatus();
                	if(!task.succeed) {
            			clearContainer();
            			message("连接失败");
                	}
                }
            }
        });
	}
}

class ConnectionTask extends Task<List<String>>{	
	
	public boolean succeed = false;
	
	String url;
	String username;
	String password;
	
	public ConnectionTask(String url,String username,String password) {
		this.url = url;
		this.username = username;
		this.password = password;
	}

	@Override
	protected List<String> call() throws Exception {
		// TODO Auto-generated method stub
		Platform.runLater(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
	        		try {
	        			AppContext.instance().connect(url, username, password);
	        			AppContext.instance().getApp().close();
	        			if(!App.isMainRunning)
	        				AppContext.instance().getApp().mainStage();
	        		} catch (Exception e) {
	        			// TODO Auto-generated catch block
	        			e.printStackTrace();
	        		}
				}
		});
		return null;
	}
	
}


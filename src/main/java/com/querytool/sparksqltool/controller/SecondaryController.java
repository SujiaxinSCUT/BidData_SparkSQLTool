package com.querytool.sparksqltool.controller;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

import com.querytool.sparksqltool.App;
import com.querytool.sparksqltool.AppContext;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class SecondaryController  implements Initializable{

	@FXML
	private VBox viewContainer;
	
	
	private final Node rootIcon = new ImageView(
	        new Image(App.class.getResource("picture/rootIcon.png").toString())
	);
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		new Thread(new RefreshTask()).run();
	}

    public void addTreeViewRoot(String root) throws SQLException {
    	TreeItem<String> rootItem = new TreeItem<> (root, rootIcon);
    	rootItem.setExpanded(true);
    	TreeView<String> tree = new TreeView<> (rootItem); 
    	viewContainer.getChildren().add(tree);
    }
    
    public void refreshTreeView() throws SQLException {
    	ObservableList<Node> trees = viewContainer.getChildren();
    	for(Node tree:trees) {
    		@SuppressWarnings("unchecked")
			TreeView<String> tree_tmp = (TreeView<String>)tree; 
    		TreeItem<String> root = tree_tmp.getRoot();
    		List<String> databases = AppContext.instance().getDatabases(root.getValue());
    		for(String database:databases) {
    			TreeItem<String> child = new TreeItem<String>(database);
    			root.getChildren().add(child);
    			List<String> tables = AppContext.instance().getTables(root.getValue(), database);
    			for(String table:tables) {
    				TreeItem<String> grand_child = new TreeItem<String>(table);
    				child.getChildren().add(grand_child);
    			}
    		}
    	}
    }
    
    public class RefreshTask extends Task<String>{

		@Override
		protected String call() throws Exception {
			// TODO Auto-generated method stub
			    	try {
						refreshTreeView();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			return null;
		}
    	
    }
}
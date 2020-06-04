package com.querytool.sparksqltool.controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.querytool.sparksqltool.App;
import com.querytool.sparksqltool.AppContext;
import com.querytool.sparksqltool.service.LoadSqlService;
import com.querytool.sparksqltool.service.QueryService;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class SecondaryController  implements Initializable{

	@FXML
	private VBox viewContainer;
	
	@FXML
	private ScrollPane resultSet;
	
	@FXML
	private TextArea inputArea;
	
	@FXML
	private JFXButton runButton;
	
	@FXML
	private JFXButton stopButton;
	
	@FXML
	private JFXComboBox<String> conBox;
	
	@FXML
	private JFXComboBox<String> dbBox;
	
	private final Node rootIcon = new ImageView(
	        new Image(App.class.getResource("picture/rootIcon.png").toString())
	);
	
	private final Node runIcon = new ImageView(
	        new Image(App.class.getResource("picture/run.png").toString())
	);
	
	private final Node stopIcon = new ImageView(
	        new Image(App.class.getResource("picture/stop.png").toString())
	);
	
	private Map<String,List<String>> dbMap = new HashMap<>();
	
	private Map<String,List<String>> tbMap = new HashMap<>();
	
	private ExecutorService pool = Executors.newFixedThreadPool(20);
	
	private List<List<String>> data = null;
	
	private ObservableList<String> roots = FXCollections.observableArrayList();
	
	private ObservableList<String> dbs = FXCollections.observableArrayList();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		runButton.setGraphic(runIcon);
		stopButton.setGraphic(stopIcon);
		stopButton.setDisable(true);
		runButton.setDisable(true);
		
		inputArea.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// TODO Auto-generated method stub
				if(newValue==null||newValue.length()==0) {
					runButton.setDisable(true);
				}else runButton.setDisable(false);
			}
		});
		
		runButton.setOnAction(e->{
			String selectedText = inputArea.getSelectedText();
			if(selectedText==null||selectedText.length()==0) {
				String text = inputArea.getText();
				String[] sqls = text.split(";");
				pool.submit(new QueryRequest(sqls, 
						AppContext.instance().getConnection(conBox.getSelectionModel().getSelectedItem())));
			}
		});
		
		try {
			addTreeViewRoot();
			pool.submit(new RefreshTask());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

    public void addTreeViewRoot() throws SQLException {
    	roots.addAll(AppContext.instance().getConnsName());
    	conBox.setItems(roots);
    	conBox.setValue(roots.get(0));
    	for(String root:roots) {
    		TreeItem<String> rootItem = new TreeItem<> (root, rootIcon);
        	TreeView<String> tree = new TreeView<> (rootItem); 
        	viewContainer.getChildren().add(tree);
    	}
    }
    
    public void addRestultSet(List<List<String>> content) {
    	GridPane pane= new GridPane();
    	pane.getStylesheets().add(App.class.getResource("copyLabel.css").toString());
    	pane.setGridLinesVisible(true);
    	int row = content.size();
    	int col = content.get(0).size();
    	for(int i=0;i<row;i++) {
    		List<String> row_i = content.get(i);
    		for(int j=0;j<col;j++) {
    			TextField field = new TextField(row_i.get(j));
            	field.setEditable(false);
                pane.add(field,j,i);
    		}
    	}
    	resultSet.setContent(pane);
    }
    
    public void refreshTreeView() throws SQLException {
    	ObservableList<Node> trees = viewContainer.getChildren();
    	for(Node tree:trees) {
    		@SuppressWarnings("unchecked")
			TreeView<String> tree_tmp = (TreeView<String>)tree; 
    		TreeItem<String> root = tree_tmp.getRoot();
    		List<String> databases = AppContext.instance().getDatabases(root.getValue());
    		dbMap.put(root.getValue(), databases);
    		for(String database:databases) {
    			System.out.println("===========database:"+database+"============");
    			List<String> tables = AppContext.instance().getTables(root.getValue(), database);    			
    			tbMap.put(database, tables);
    			for(String table:tables) {
    				System.out.println("table:"+table);
    			}
    		}
    	}
    }
    
    public class RefreshTask extends Task<String>{

		@Override
		protected String call() {
			// TODO Auto-generated method stub
			try {
				refreshTreeView();
				pool.submit(new AddViewTask());
			} catch (SQLException e) {
			// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
    	
    }
    
    class AddViewTask extends Task<String>{

		@Override
		protected String call() throws Exception {
			// TODO Auto-generated method stub
			Platform.runLater(new Runnable() {
	            @Override public void run() {
					ObservableList<Node> trees = viewContainer.getChildren();
			    	for(Node tree:trees) {
			    		@SuppressWarnings("unchecked")
						TreeView<String> tree_tmp = (TreeView<String>)tree; 
			    		TreeItem<String> root = tree_tmp.getRoot();
			    		List<String> databases = dbMap.get(root.getValue());
			    		for(String database:databases) {
			    			TreeItem<String> dbItem = new TreeItem<>(database);
			    			root.getChildren().add(dbItem);
			    			List<String> tables = tbMap.get(database);
			    			for(String table:tables) {
			    				TreeItem<String> tbItem = new TreeItem<>(table);
			    				dbItem.getChildren().add(tbItem);
			    			}
			    		}
			    	}
	            }
			});
			return null;
		}
    	
    }
    
    class QueryRequest extends Task<String>{

    	private String[] sqls;
    	
    	private Connection con;
    	
    	public QueryRequest(String[] sqls,Connection con) {
			// TODO Auto-generated constructor stub
    		this.sqls = sqls;
    		this.con = con;
		}
    	
		@Override
		protected String call() throws Exception {
			// TODO Auto-generated method stub
			data = null;
			for(String sql:sqls) {
				if(sql.contains("select")||sql.contains("SELECT")) {
					data = LoadSqlService.loadTables(QueryService.executeQuery(sql, con));
				}else {
					QueryService.execute(sql, con);
				}
			}
			if(data!=null)
				pool.submit(new AddResultSetTask());
			return null;
		}
    	
    }
    
    class AddResultSetTask extends Task<String>{
    	
		@Override
		protected String call() throws Exception {
			// TODO Auto-generated method stub
			Platform.runLater(new Runnable() {
	            @Override public void run() {
	            	addRestultSet(data);
	            }
			});
			return null;
		}
    	
    }
    
    
}
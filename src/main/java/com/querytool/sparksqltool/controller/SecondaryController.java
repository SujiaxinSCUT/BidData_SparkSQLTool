package com.querytool.sparksqltool.controller;

import java.io.IOException;
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
import com.jfoenix.controls.JFXSpinner;
import com.querytool.sparksqltool.App;
import com.querytool.sparksqltool.AppContext;
import com.querytool.sparksqltool.service.LoadSqlService;
import com.querytool.sparksqltool.service.QueryService;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SecondaryController  implements Initializable{

	
	@FXML
	private SplitPane rootContainer;
	@FXML
	private VBox viewContainer;
	
	@FXML
	private VBox leftContainer;
	
	@FXML
	private AnchorPane rightContainer;
	
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
	
	@FXML
	private TextArea logger;
	
	@FXML
	private JFXButton addButton;
	
	@FXML
	private JFXButton refreshButton;
	
	@FXML
	private JFXSpinner listSpinner;
	
	@FXML
	private JFXSpinner querySpinner;
	
	private final Node rootIcon = new ImageView(
	        new Image(App.class.getResource("picture/rootIcon.png").toString())
	);
	
	private final Node runIcon = new ImageView(
	        new Image(App.class.getResource("picture/run.png").toString())
	);
	
	private final Node stopIcon = new ImageView(
	        new Image(App.class.getResource("picture/stop.png").toString())
	);
	
	private final Node addIcon = new ImageView(
	        new Image(App.class.getResource("picture/add.png").toString())
	);
	
	private final Node refreshIcon = new ImageView(
	        new Image(App.class.getResource("picture/refresh.png").toString())
	);
	
	private final Node dbIcon = new ImageView(
	        new Image(App.class.getResource("picture/db.png").toString())
	);
	
	private final Node tbIcon = new ImageView(
	        new Image(App.class.getResource("picture/tb.png").toString())
	);
	
	private Map<String,List<String>> dbMap = new HashMap<>();
	
	private Map<String,List<String>> tbMap = new HashMap<>();
	
	private ExecutorService pool = Executors.newFixedThreadPool(20);
	
	private List<List<String>> data = null;
	
	private ObservableList<String> roots = FXCollections.observableArrayList();
	
	private ObservableList<String> dbs = FXCollections.observableArrayList();
	
	private StringProperty mes = new SimpleStringProperty();
	
	private BooleanProperty isListRunning = new SimpleBooleanProperty();
	
	private BooleanProperty isQueryRunning = new SimpleBooleanProperty();
	
	private static final int partSize = 100;
	
	private int curPageCount = 0;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		runButton.setGraphic(runIcon);
		stopButton.setGraphic(stopIcon);
		addButton.setGraphic(addIcon);
		refreshButton.setGraphic(refreshIcon);
		
		listSpinner.setVisible(false);
		querySpinner.setVisible(false);
		
		stopButton.setDisable(true);
		runButton.setDisable(true);
		
		conBox.setItems(roots);
		dbBox.setItems(dbs);
		
		isListRunning.set(false);
		isQueryRunning.set(false);
		
		isListRunning.addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				// TODO Auto-generated method stub
				if(newValue) {
					leftContainer.setDisable(true);
					listSpinner.setVisible(true);
				}else {
					leftContainer.setDisable(false);
					listSpinner.setVisible(false);
				}
			}
		});
		
		isQueryRunning.addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				// TODO Auto-generated method stub
				if(newValue) {
					rightContainer.setDisable(true);
					querySpinner.setVisible(true);
				}else {
					rightContainer.setDisable(false);
					querySpinner.setVisible(false);
				}
			}
		});
		
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
		
		conBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// TODO Auto-generated method stub
				dbs.clear();
				dbs.addAll(dbMap.get(newValue));
			}
		});
		
		dbBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// TODO Auto-generated method stub
				pool.submit(new QueryRequest(new String[] {"use "+newValue}, 
						AppContext.instance().getConnection(conBox.getSelectionModel().getSelectedItem())));
			}
		});
		
		mes.addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// TODO Auto-generated method stub
				if(newValue!=null&&newValue.length()!=0)
					log(mes.get());
			}
		});
		
		refreshButton.setOnAction(e->{
			try {
		    	roots.clear();
		    	viewContainer.getChildren().clear();
				addTreeViewRoot();
				pool.submit(new RefreshTask());
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		
		addButton.setOnAction(e->{
			try {
				Stage stage = AppContext.instance().getApp().loginStage();
				stage.setOnCloseRequest(e2->{
					if(AppContext.instance().getConnsName().size()>roots.size()) {
				    	roots.clear();
				    	viewContainer.getChildren().clear();
						try {
							addTreeViewRoot();
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						pool.submit(new RefreshTask());
					}
				});
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				log(e1.getMessage());
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
    	int totalPageCount = row/partSize;
    	for(int i=curPageCount*partSize;i<row&&i<(curPageCount+1)*partSize;i++) {
    		List<String> row_i = content.get(i);
    		for(int j=0;j<col;j++) {
    			TextField field = new TextField(row_i.get(j));
            	field.setEditable(false);
                pane.add(field,j,i);
    		}
    	}
    	JFXButton lastPageButton = new JFXButton("上一页");
    	JFXButton nextPageButton = new JFXButton("下一页");
    	
    	if(totalPageCount==0||curPageCount==0) {
    		lastPageButton.setDisable(true);
    	}else lastPageButton.setDisable(false);
    	
    	if(curPageCount==totalPageCount)
    		nextPageButton.setDisable(true);
    	else nextPageButton.setDisable(false);
    	
    	lastPageButton.setOnAction(e->{
    		curPageCount--;
    		addRestultSet(content);
    	});
    	
    	nextPageButton.setOnAction(e->{
    		curPageCount++;
    		addRestultSet(content);
    	});
    	
    	if(col>1) {
    		pane.add(lastPageButton, 0, pane.getRowCount());
    		pane.add(nextPageButton, pane.getColumnCount()-1, pane.getRowCount()-1);
    	}else {
    		pane.add(lastPageButton, 0, pane.getRowCount());
    		pane.add(nextPageButton, 0, pane.getRowCount());
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
    
    public void log(String mes) {
    	logger.appendText(">"+mes+"\r\n");
    }
    
    
    public class RefreshTask extends Task<String>{

		@Override
		protected String call() {
			// TODO Auto-generated method stub
			try {
				isListRunning.set(true);
				refreshTreeView();
				pool.submit(new AddViewTask());
			} catch (SQLException e) {
			// TODO Auto-generated catch block
				e.printStackTrace();
				log(e.getMessage());
			}finally {
				isListRunning.set(false);
			}
			return null;
		}
    	
    }
    
    class AddViewTask extends Task<String>{

		@Override
		protected String call(){
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
		protected String call(){
			// TODO Auto-generated method stub
			data = null;
			try {
				isQueryRunning.set(true);
				for(String sql:sqls) {
					mes.set(sql);
					if(sql==null||sql.length()==0) continue;
					if(sql.contains("select")||sql.contains("SELECT")||sql.contains("show")||sql.contains("SHOW")) {
						data = LoadSqlService.loadTables(QueryService.executeQuery(sql, con));
						curPageCount = 0;
					}else {
						QueryService.execute(sql, con);
					}
					mes.set("succeed");
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				log(e.getMessage());
			}finally {
				isQueryRunning.set(false);
			}
			if(data!=null)
				pool.submit(new AddResultSetTask());
			return null;
		}
    	
    }
    
    class AddResultSetTask extends Task<String>{
    	
		@Override
		protected String call(){
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
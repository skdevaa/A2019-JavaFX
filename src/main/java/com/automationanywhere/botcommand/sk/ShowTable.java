/*
 * Copyright (c) 2019 Automation Anywhere.
 * All rights reserved.
 *
 * This software is the proprietary information of Automation Anywhere.
 * You shall use it only in accordance with the terms of the license agreement
 * you entered into with Automation Anywhere.
 */
/**
 * 
 */
package com.automationanywhere.botcommand.sk;

import static com.automationanywhere.commandsdk.model.AttributeType.TEXT;
import static com.automationanywhere.commandsdk.model.DataType.STRING;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;
import javax.swing.JFrame;
import com.automationanywhere.botcommand.data.Value;
import com.automationanywhere.botcommand.data.model.Schema;
import com.automationanywhere.botcommand.data.model.record.Record;
import com.automationanywhere.botcommand.data.model.table.Row;
import com.automationanywhere.botcommand.data.model.table.Table;
import com.automationanywhere.commandsdk.annotations.BotCommand;
import com.automationanywhere.commandsdk.annotations.CommandPkg;
import com.automationanywhere.commandsdk.annotations.Execute;
import com.automationanywhere.commandsdk.annotations.Idx;
import com.automationanywhere.commandsdk.annotations.Pkg;
import com.automationanywhere.commandsdk.annotations.rules.NotEmpty;
import com.automationanywhere.commandsdk.model.AttributeType;
import com.automationanywhere.commandsdk.model.DataType;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.JFXPanel;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import javafx.util.StringConverter;



/**
 * @author Stefan Karsten
 *
 */

@BotCommand
@CommandPkg(label="Show Table", name="ShowTable", description="Show Table variable", icon="",
node_label="Show Table")
public class ShowTable  {
	
	private static Semaphore sem;
	private JFrame frame = null;
	
    private Integer width;
    private Integer height;
	
	@Execute
	public void action(@Idx(index = "1", type = AttributeType.VARIABLE) @Pkg(label = "Table", default_value_type = DataType.TABLE) @NotEmpty Table table,
					   @Idx(index = "2", type = TEXT) @Pkg(label = "Button Label", default_value_type = STRING) @NotEmpty String label,
					   @Idx(index = "3", type = AttributeType.NUMBER) @Pkg(label = "Width", default_value_type = DataType.NUMBER) @NotEmpty Double width,
					   @Idx(index = "4", type = AttributeType.NUMBER) @Pkg(label = "Height", default_value_type = DataType.NUMBER) @NotEmpty Double height) throws Exception {
		
		
		 this.width = width.intValue();
		 this.height = height.intValue();
		 
		  if (this.sem == null) {
		      this.sem = new Semaphore(1);
		    }
		  String[] args = null;
		  

		  initAndShowGUI(table,label);
		   this.sem.acquire();
		   this.sem.release();
    

	}


    private  void initAndShowGUI(Table table, String label) throws Exception  {
        // This method is invoked on the JavaFX thread
    	 this.sem.acquire();
         this.frame = new JFrame("Table View");  
         
         this.frame.addWindowListener(new java.awt.event.WindowAdapter() {
             @Override
             public void windowClosing(java.awt.event.WindowEvent windowEvent) {
            		quit();
             }
         });

	        frame.setSize(this.width, this.height);
	        frame.setLocation(500, 400);
	        frame.setVisible(true);
	        frame.setAlwaysOnTop(true);
	        final JFXPanel fxPanel = new JFXPanel();
	        fxPanel.setPreferredSize(new Dimension(this.width*10, this.height*10));
	        frame.add(fxPanel);
	        Platform.runLater(new Runnable() {
	            @Override
	            public void run() {
	                initFX(fxPanel,table,label);
	            }
	       });
	        
    }

  private  void initFX(JFXPanel fxPanel,Table table, String buttonlabel) {
	  
    

    	 Button but1= new Button(buttonlabel);
    	 but1.setStyle("-fx-font-size: 12pt;");

    	 
    	 but1.setOnAction((e)->{
    		 this.frame.setVisible(false);
    	     quit();
    	 });
    	 
    	 
         TableView tableView = new TableView<>(generateDataInMap(table));
         tableView.setColumnResizePolicy ( TableView.CONSTRAINED_RESIZE_POLICY);
         tableView.setEditable(false);
         tableView.getSelectionModel().setCellSelectionEnabled(true);
         tableView.prefWidth(50000);
         tableView.prefHeight(30000);
    	 

    	 List<Schema> schemas = table.getSchema();
    	 List<TableColumn<Row, String>> columns = new ArrayList<TableColumn<Row, String>>();
    	 if (!schemas.isEmpty())
    	 {
        	 int index = 0;
    		 for (Schema s : schemas)
    		 {
    			 int col = index;
    			 TableColumn<Row, String> column = new TableColumn<>(s.getName());
    			 column.setResizable(true);
    			 column.setPrefWidth(30000);
    			 column.setCellValueFactory(new Callback<CellDataFeatures<Row, String>, ObservableValue<String>>() {
    				 public ObservableValue<String> call(CellDataFeatures<Row, String> r) {
    					 // p.getValue() returns the Person instance for a particular TableView row
    					 return new ReadOnlyObjectWrapper(r.getValue().getValues().get(col).get().toString());
    				 }
    			 });
        	     tableView.getColumns().add(column);
        	     index++;
    		 }
    	 }
    	 else
    	 {
    		 int size = table.getRows().get(0).getValues().size();
    		 for (int i=0;i < size;i++)
    		 {
    			 int col = i;
    			 TableColumn<Row, String> column = new TableColumn<>("Col"+new Integer(i+1).toString());
    			 column.setResizable(true);
    			 column.setPrefWidth(30000);
    			 column.setCellValueFactory(new Callback<CellDataFeatures<Row, String>, ObservableValue<String>>() {
    				 public ObservableValue<String> call(CellDataFeatures<Row, String> r) {
    					 // p.getValue() returns the Person instance for a particular TableView row
    					 return new ReadOnlyObjectWrapper(r.getValue().getValues().get(col).get().toString());
    				 }
    			 });
        	     tableView.getColumns().add(column);
    		 }
    		 
    	 }
    

       	 
    	 GridPane grid = new GridPane();
    	 grid.setAlignment(Pos.CENTER);
    	 GridPane innergrid = new GridPane();
    	 innergrid.setAlignment(Pos.CENTER);
    	 innergrid.add(but1,1,1);
    	 grid.add(innergrid, 1,1);
    	 grid.add(tableView,1,0);
         

    	 Scene  scene  =  new  Scene(grid, Color.WHITE);
         fxPanel.setScene(scene);
   
  	 }


	public  void quit() {
		 this.sem.release();
	}
	
	
	
	
    private  ObservableList<Row> generateDataInMap(Table table) {
    	
    	int index =0;
    	ObservableList<Row> allData = FXCollections.observableArrayList();
        for (Row r : table.getRows()) {
              allData.add(r);
        }
        return allData;
    }
}

	



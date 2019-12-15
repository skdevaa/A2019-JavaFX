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
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;
import javax.swing.JFrame;
import com.automationanywhere.botcommand.data.Value;
import com.automationanywhere.commandsdk.annotations.BotCommand;
import com.automationanywhere.commandsdk.annotations.CommandPkg;
import com.automationanywhere.commandsdk.annotations.Execute;
import com.automationanywhere.commandsdk.annotations.Idx;
import com.automationanywhere.commandsdk.annotations.Pkg;
import com.automationanywhere.commandsdk.annotations.rules.NotEmpty;
import com.automationanywhere.commandsdk.model.AttributeType;
import com.automationanywhere.commandsdk.model.DataType;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.JFXPanel;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.MapValueFactory;
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
@CommandPkg(label="Show Dictionary", name="ShowDictionary", description="Show Dictionary variable", icon="",
node_label="Show Dictionary")
public class ShowDictonary  {
	
	private static Semaphore sem;
	private JFrame frame = null;
	
    private Integer width;
    private Integer height;
	
	@Execute
	public void action(@Idx(index = "1", type = AttributeType.VARIABLE) @Pkg(label = "Dictonary", default_value_type = DataType.DICTIONARY) @NotEmpty HashMap<String,Value> dict ,
					   @Idx(index = "2", type = TEXT) @Pkg(label = "Button Label", default_value_type = STRING) @NotEmpty String label,
					   @Idx(index = "3", type = AttributeType.NUMBER) @Pkg(label = "Width", default_value_type = DataType.NUMBER) @NotEmpty Double width,
					   @Idx(index = "4", type = AttributeType.NUMBER) @Pkg(label = "Height", default_value_type = DataType.NUMBER) @NotEmpty Double height) throws Exception {
		
		
		 this.width = width.intValue();
		 this.height = height.intValue();
		 
		  if (this.sem == null) {
		      this.sem = new Semaphore(1);
		    }
		  String[] args = null;
		  

		  initAndShowGUI(dict,label);
		   this.sem.acquire();
		   this.sem.release();
    

	}


    private  void initAndShowGUI(HashMap<String,Value> dict, String label) throws Exception  {
        // This method is invoked on the JavaFX thread
    	 this.sem.acquire();
         this.frame = new JFrame("Dictionary View");  
         
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
	                initFX(fxPanel,dict,label);
	            }
	       });
	        
    }

  private  void initFX(JFXPanel fxPanel,HashMap<String,Value> dict, String buttonlabel) {
	  
    

    	 Button but1= new Button(buttonlabel);
    	 but1.setStyle("-fx-font-size: 12pt;");

    	 
    	 but1.setOnAction((e)->{
    		 this.frame.setVisible(false);
    	     quit();
    	 });

    	 
         TableColumn<Map, String> firstDataColumn = new TableColumn<>("Key");
         TableColumn<Map, String> secondDataColumn = new TableColumn<>("Value");

         TableView tableView = new TableView<>(generateDataInMap(dict));
         tableView.setColumnResizePolicy ( TableView.CONSTRAINED_RESIZE_POLICY);
         tableView.setEditable(false);
         tableView.getSelectionModel().setCellSelectionEnabled(true);
         tableView.prefWidth(50000);
         tableView.prefHeight(30000);
         
         
         firstDataColumn.setCellValueFactory(new MapValueFactory("Key"));
         secondDataColumn.setCellValueFactory(new MapValueFactory("Value"));
         firstDataColumn.setResizable(true);
         secondDataColumn.setResizable(true);
         firstDataColumn.setPrefWidth(30000);
         secondDataColumn.setPrefWidth(50000);

         tableView.getColumns().setAll(firstDataColumn, secondDataColumn);
         

         
         Callback<TableColumn<Map, String>, TableCell<Map, String>>
             cellFactoryForMap = (TableColumn<Map, String> p) -> 
                 new TextFieldTableCell(new StringConverter() {
                     @Override
                         public String toString(Object t) {
                         return t.toString();
                     }
                     @Override
                     public Object fromString(String string) {
                         return string;
                     }
             });
         firstDataColumn.setCellFactory(cellFactoryForMap);
         secondDataColumn.setCellFactory(cellFactoryForMap);
       	 
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
	
	
    private ObservableList<Map> generateDataInMap(HashMap<String, Value> dict) {
        ObservableList<Map> allData = FXCollections.observableArrayList();
        for (HashMap.Entry<String, Value> entry : dict.entrySet()) {
            Map<String, String> dataRow = new HashMap<>();

            dataRow.put("Key",entry.getKey());
            dataRow.put("Value", entry.getValue().toString());
 
            allData.add(dataRow);
        }
        return allData;
    }
}

	



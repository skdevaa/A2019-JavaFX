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
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;
import javax.swing.JFrame;
import com.automationanywhere.botcommand.data.Value;
import com.automationanywhere.botcommand.data.model.record.Record;
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
@CommandPkg(label="Show Record", name="ShowRecord", description="Show Record variable", icon="jfx.svg",
node_label="Show Record")
public class ShowRecord  {
	
	private static Semaphore sem;
	private FXWindow window;
	
	@Execute
	public void action(@Idx(index = "1", type = AttributeType.VARIABLE) @Pkg(label = "Record", default_value_type = DataType.RECORD) @NotEmpty Record record,
					   @Idx(index = "2", type = TEXT) @Pkg(label = "Button Label", default_value_type = STRING) @NotEmpty String label,
					   @Idx(index = "3", type = AttributeType.NUMBER) @Pkg(label = "Width", default_value_type = DataType.NUMBER) @NotEmpty Double width,
					   @Idx(index = "4", type = AttributeType.NUMBER) @Pkg(label = "Height", default_value_type = DataType.NUMBER) @NotEmpty Double height) throws Exception {
		
		

		 
		  if (this.sem == null) {
		      this.sem = new Semaphore(1);
		    }
	    	 this.sem.acquire();
		      window = new FXWindow("Record View", width.intValue(), height.intValue());
	         
	         window.getFrame().addWindowListener(new java.awt.event.WindowAdapter() {
	             @Override
	             public void windowClosing(java.awt.event.WindowEvent windowEvent) {
	            		quit();
	             }
	         });
		     
	         Platform.runLater(new Runnable() {
		            @Override
		            public void run() {
		                initFX(record,label);
		            }
		       });

		   this.sem.acquire();
		   this.sem.release();
    

	}


  private  void initFX(Record record, String buttonlabel) {
	  
    

    	 Button but1= new Button(buttonlabel);
    	 but1.setOnAction((e)->{
    		 window.getFrame().setVisible(false);
    	     quit();
    	 });

    	 
         TableColumn<Map, String> firstDataColumn = new TableColumn<>("Name");
         TableColumn<Map, String> secondDataColumn = new TableColumn<>("Value");

         TableView tableView = new TableView<>(generateDataInMap(record));
         tableView.setColumnResizePolicy ( TableView.CONSTRAINED_RESIZE_POLICY);
         tableView.setEditable(false);
         tableView.getSelectionModel().setCellSelectionEnabled(true);
         tableView.prefWidth(50000);
         tableView.prefHeight(30000);
         
         
         firstDataColumn.setCellValueFactory(new MapValueFactory("Name"));
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
    	 URL url = this.getClass().getResource("/css/styles.css");
	     scene.getStylesheets().add(url.toExternalForm());
    	 window.getPanel().setScene(scene);

   
  	 }


	private  void quit() {
		 this.sem.release();
	}
	
	
    private  ObservableList<Map> generateDataInMap(Record record) {
    	
    	int index =0;
    	ObservableList<Map> allData = FXCollections.observableArrayList();
        for (Value v : record.getValues()) {
            Map<String, String> dataRow = new HashMap<>();

            dataRow.put("Name",record.getSchema().get(index).getName().toString());
            dataRow.put("Value", v.toString());

            allData.add(dataRow);
            index++;
        }
        return allData;
    }
}

	



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
import static com.automationanywhere.commandsdk.model.AttributeType.VARIABLE;
import static com.automationanywhere.commandsdk.model.DataType.STRING;
import static com.automationanywhere.commandsdk.model.DataType.DICTIONARY;

import java.awt.Dimension;
import java.util.HashMap;

import java.util.concurrent.Semaphore;

import javax.swing.JFrame;

import com.automationanywhere.botcommand.data.Value;
import com.automationanywhere.botcommand.data.impl.DictionaryValue;
import com.automationanywhere.botcommand.data.impl.StringValue;

import com.automationanywhere.commandsdk.annotations.BotCommand;
import com.automationanywhere.commandsdk.annotations.CommandPkg;
import com.automationanywhere.commandsdk.annotations.Execute;
import com.automationanywhere.commandsdk.annotations.Idx;
import com.automationanywhere.commandsdk.annotations.Pkg;
import com.automationanywhere.commandsdk.annotations.rules.NotEmpty;
import com.automationanywhere.commandsdk.model.AttributeType;
import com.automationanywhere.commandsdk.model.DataType;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.embed.swing.JFXPanel;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;



/**
 * @author Stefan Karsten
 *
 */

@BotCommand
@CommandPkg(label="Dynamic Form", name="DynamicForm", description="Builds Form based on Dictionay Input", icon="",
node_label="Dynamic Form", return_type =  DICTIONARY, return_sub_type= STRING , return_required = true)
public class DynamicForm  {
	
	private static Semaphore sem;
	private JFrame frame = null;
	
    private Integer width;
    private Integer height;
    private HashMap<String,Value> map;
    private String buttonlabel;
    private GridPane grid;
	
	@Execute
	public DictionaryValue action(@Idx(index = "1", type = VARIABLE) @Pkg(label = "Dictonary", default_value_type = DataType.DICTIONARY) @NotEmpty HashMap<String,Value> dict ,
					   @Idx(index = "2", type = TEXT) @Pkg(label = "Button Label", default_value_type = STRING) @NotEmpty String label,
					   @Idx(index = "3", type = AttributeType.NUMBER) @Pkg(label = "Width", default_value_type = DataType.NUMBER) @NotEmpty Double width,
					   @Idx(index = "4", type = AttributeType.NUMBER) @Pkg(label = "Height", default_value_type = DataType.NUMBER) @NotEmpty Double height) throws Exception {
		
		
		 this.width = width.intValue();
		 this.height = height.intValue();
		 this.map = dict;
		 this.buttonlabel = label;
		 
		  if (this.sem == null) {
		      this.sem = new Semaphore(1);
		    }

		  initAndShowGUI();
		  this.sem.acquire();
		  this.sem.release();
		
		  return new DictionaryValue(this.map);

	}


    private  void initAndShowGUI() throws Exception  {
        // This method is invoked on the JavaFX thread
    	 this.sem.acquire();
         this.frame = new JFrame("Form");  
         
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
	                initFX(fxPanel);
	            }
	       });
	        
    }

  private  void initFX(JFXPanel fxPanel) {
	  
    

    	 Button but1= new Button(this.buttonlabel);
    	 but1.setStyle("-fx-font-size: 12pt;");
    	 but1.setOnAction((e)->{
    		 this.frame.setVisible(false);
    	     quit();
    	 });

    	 
    	 
    	 this.grid = new GridPane();    	
    	 this.grid.setHgap(10);
         this.grid.setVgap(12);
    	 grid.setAlignment(Pos.CENTER);
    	 
    	 HashMap<String,Value> dict = this.map;
    	 int row = 0;
         for (HashMap.Entry<String, Value> entry : dict.entrySet()) {
        	 String label = entry.getKey();
        	 String value = entry.getValue().toString();
        	 Label labelElement = new Label(label);
        	 labelElement.setStyle("-fx-font-size: 12pt;");
        	 TextField textElement = new TextField ();  
        	 textElement.setStyle("-fx-font-size: 12pt;");   
        	 this.grid.add(labelElement, 0,row);
        	 this.grid.add(textElement, 1,row);
        	 row++;

         }
    	 GridPane innergrid = new GridPane();
    	 innergrid.setAlignment(Pos.CENTER);
    	 innergrid.add(but1,1,1);
    	 this.grid.add(innergrid, 1,row);
    	 Scene  scene  =  new  Scene(this.grid, Color.WHITE);
         fxPanel.setScene(scene);

   
  	 }


	private  void quit() {
	    ObservableList<Node> childrens = this.grid.getChildren();
	    int row = 0;
	    int column = 1;
	    StringValue value;
	    String key ;
	    GridPane grid = this.grid;
        for (HashMap.Entry<String, Value> entry : this.map.entrySet()) {
        	value = new StringValue(((TextField)grid.getChildren().get(row*2+1)).getText());
        	key = ((Label)grid.getChildren().get(row*2)).getText();
        	row++ ;
        	this.map.replace(key, value);
        }

		 this.sem.release();
	}
	
	
  
}

	



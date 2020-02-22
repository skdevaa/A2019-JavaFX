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


import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.automationanywhere.botcommand.data.Value;
import com.automationanywhere.botcommand.data.impl.DateTimeValue;
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
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;






/**
 * @author Stefan Karsten
 *
 */

@BotCommand
@CommandPkg(label="Value Prompt", name="ValuePrompt", description="Prompt for Value",  icon="jfx.svg",
node_label="ValuePrompt", return_type=DataType.STRING,  return_label="Value", return_required=true)
public class ValuePrompt  {
	
	private static Semaphore sem;
	private FXWindow window;
	private String value = "";

    private static Logger logger = LogManager.getLogger(ValuePrompt.class);
	
	@Execute
	public StringValue action(@Idx(index = "1", type = AttributeType.TEXT) @Pkg(label = "Label", default_value_type = DataType.STRING) @NotEmpty String label,
			                    @Idx(index = "2", type = AttributeType.TEXT) @Pkg(label = "Button Label", default_value_type = DataType.STRING) @NotEmpty String buttonlabel ) throws Exception
	{
		

		  if (this.sem == null) {
		      this.sem = new Semaphore(1);
		    }
		  
		  
	    this.sem.acquire();
	    window = new FXWindow("Prompt Value",700,200);
	    window.getFrame().addWindowListener(new java.awt.event.WindowAdapter() {
	             @Override
	             public void windowClosing(java.awt.event.WindowEvent windowEvent) {
	            	 quit();
	             }
	         });

	   Platform.runLater(new Runnable() {
		            @Override
		            public void run() {
		        	  try {
						initFX(label,buttonlabel);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		            }
		       });

      this.sem.acquire();
      this.sem.release();
      

		   
  	 return new StringValue(this.value);
    
	}


  private  void initFX(String label, String buttonlabel) throws Exception {
	  
	  
	  GridPane grid = new GridPane();    	
	  grid.setHgap(10);
      grid.setVgap(12);
      grid.setAlignment(Pos.CENTER);
  

 	  Label labelfield = new Label();
 	  labelfield.setText(label);
 	  Button ok= new Button(buttonlabel);
 	  ok.setPrefWidth(100);
 	  
 	  TextField field = new TextField();
 	  field.setStyle("-fx-pref-width:500px");

 	  GridPane innergridbut = new GridPane();
 	  innergridbut.setAlignment(Pos.CENTER);
      HBox hbButtons = new HBox();
 	  hbButtons.getChildren().addAll(ok);
 	  hbButtons.setSpacing(10.0);
 	  innergridbut.add(hbButtons, 0,1);
 	  
 	  GridPane innergridstart = new GridPane();
 	  innergridstart.setAlignment(Pos.CENTER);
      HBox hbstart = new HBox();
 	  hbstart.getChildren().addAll(labelfield,field);
 	  hbstart.setSpacing(10.0);
 	  innergridstart.add(hbstart, 0,1);
 	  
 	  grid.add(innergridstart, 0,0);
 	  grid.add(innergridbut, 0,1);

 	 field.textProperty().addListener((obs, oldText, newText) -> 
 			this.value = newText
  	 );

 	 ok.setOnAction((e)->{
 		 window.getFrame().setVisible(false);
 		 quit();

 	 });
 	 
    	 
      Scene  scene  =  new  Scene(grid, Color.WHITE);
 	  URL url = this.getClass().getResource("/css/styles.css");
	  scene.getStylesheets().add(url.toExternalForm());
 	  window.getPanel().setScene(scene);


   }

  
	private  void quit() {
		 this.sem.release();
	}
}

	



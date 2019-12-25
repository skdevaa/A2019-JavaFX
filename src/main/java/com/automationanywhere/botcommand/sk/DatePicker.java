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


import static com.automationanywhere.commandsdk.model.DataType.STRING;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.automationanywhere.botcommand.data.Value;
import com.automationanywhere.botcommand.data.impl.DateTimeValue;
import com.automationanywhere.botcommand.data.impl.DictionaryValue;
import com.automationanywhere.botcommand.data.impl.NumberValue;
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
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;






/**
 * @author Stefan Karsten
 *
 */

@BotCommand
@CommandPkg(label="Date Picker", name="DatePicker", description="Choose a Date",  icon="jfx.svg",
node_label="DatePicker", return_type=DataType.DICTIONARY,  return_sub_type= DataType.DATETIME, return_label="Date", return_required=true)
public class DatePicker  {
	
	private static Semaphore sem;
	private FXWindow window;
	private String dirname="";
	private Map<String, Value> dates;
    private static Logger logger = LogManager.getLogger(DatePicker.class);
	
	@Execute
	public DictionaryValue action(@Idx(index = "1", type = AttributeType.CHECKBOX) @Pkg(label = "Start-End Date", default_value_type = DataType.BOOLEAN) @NotEmpty Boolean startend,
			                    @Idx(index = "2", type = AttributeType.CHECKBOX) @Pkg(label = "Show Week No.", default_value_type = DataType.BOOLEAN) @NotEmpty Boolean showweek) throws Exception
	{
		
		this.dates= new HashMap<String, Value>();

		  if (this.sem == null) {
		      this.sem = new Semaphore(1);
		    }
		  
		  
	    this.sem.acquire();
	    window = new FXWindow("Date Picker",500,200);
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
						initFX(startend,showweek);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		            }
		       });

      this.sem.acquire();
      this.sem.release();
      

		   
  	 return new DictionaryValue(this.dates);
    
	}


  private  void initFX(Boolean startend,Boolean showweek) throws Exception {
	  
	  
	  GridPane grid = new GridPane();    	
	  grid.setHgap(10);
      grid.setVgap(12);
      grid.setAlignment(Pos.CENTER);
  

	  javafx.scene.control.DatePicker datepicker1 = new javafx.scene.control.DatePicker();
	  javafx.scene.control.DatePicker datepicker2 = new javafx.scene.control.DatePicker();
	  
	  datepicker1.setShowWeekNumbers(showweek);
	  datepicker2.setShowWeekNumbers(showweek);

 	  Label label1 = new Label();
 	  label1.setText("Start");
 	  label1.setStyle("-fx-pref-width:80px");
 	  Label label2 = new Label();
 	  label2.setText("End");
 	  label2.setStyle("-fx-pref-width:80px");
 	  Button ok= new Button("OK");
 	  ok.setPrefWidth(100);
 	  Button cancel= new Button("Cancel");
 	  cancel.setPrefWidth(100);
 	  GridPane innergridbut = new GridPane();
 	  innergridbut.setAlignment(Pos.CENTER);
      HBox hbButtons = new HBox();
 	  hbButtons.getChildren().addAll(ok,cancel);
 	  hbButtons.setSpacing(10.0);
 	  innergridbut.add(hbButtons, 0,1);
 	  
 	  GridPane innergridstart = new GridPane();
 	  innergridstart.setAlignment(Pos.CENTER);
      HBox hbstart = new HBox();
 	  hbstart.getChildren().addAll(label1,datepicker1);
 	  hbstart.setSpacing(10.0);
 	  innergridstart.add(hbstart, 0,1);
 	  
 	  GridPane innergridend = new GridPane();
 	  innergridend.setAlignment(Pos.CENTER);
      HBox hbend = new HBox();
 	  hbend.getChildren().addAll(label2,datepicker2);
 	  hbend.setSpacing(10.0);
 	  innergridend.add(hbend, 0,1);

 	  if (startend)
 	  {
 		  grid.add(innergridstart, 0,0);
 		  grid.add(innergridend, 0,1);
 	 	  grid.add(innergridbut, 0,2);

 	  }
 	  else
 	  {
 		  grid.add(innergridstart, 0,1);
 	 	  grid.add(innergridbut, 0,2);

 	  }
 	  
 	  
  	 datepicker1.setOnAction((e)->{
			LocalDate date = datepicker1.getValue();
		    this.setDate("start", date);
  	 });  

  	 

 	 datepicker2.setOnAction((e)->{
			LocalDate date = datepicker2.getValue();
			this.setDate("end", date);
 	 });  


 	 ok.setOnAction((e)->{
 		 window.getFrame().setVisible(false);
 		 quit();

 	 });
 	 
 	 cancel.setOnAction((e)->{
 		 window.getFrame().setVisible(false);
 		 quit();

 	 });
 	  
    	 
      Scene  scene  =  new  Scene(grid, Color.WHITE);
 	  URL url = this.getClass().getResource("/css/styles.css");
	  scene.getStylesheets().add(url.toExternalForm());
 	  window.getPanel().setScene(scene);
     

     


   }
 
    private void setDate(String key, LocalDate date) {
    	
		ZoneId systemDefault = ZoneId.systemDefault();
		LocalDateTime datetime = date.atTime(0, 0 ,0);
		ZonedDateTime datetimevalue = datetime.atZone(systemDefault);
		DateTimeValue datevalue = new DateTimeValue();
		datevalue.set(datetimevalue);
    	this.dates.put(key, datevalue);
    }
    
  

	private  void quit() {
		 this.sem.release();
	}
}

	



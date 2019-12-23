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
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Semaphore;

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

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;





/**
 * @author Stefan Karsten
 *
 */

@BotCommand
@CommandPkg(label="FXML Form", name="FXMLForm", description="FXML Form",  icon="jfx.svg",
node_label="FXML Form",
return_type=DataType.DICTIONARY, return_label="Result", return_sub_type= STRING , return_required=true)
public class FXMLForm  {
	
	private String pressed = null;
	private static Semaphore sem;
	private HashMap<String,Value>  dict;
	private Parent root ;
	private FXWindow window;

	
	@Execute
	public DictionaryValue action(@Idx(index = "1", type = AttributeType.FILE) @Pkg(label = "FXML File", default_value_type = DataType.FILE) @NotEmpty String fxml,
			                      @Idx(index = "2", type = AttributeType.TEXT) @Pkg(label = "Title", default_value_type = DataType.STRING) @NotEmpty String title,
								  @Idx(index = "3", type = AttributeType.NUMBER) @Pkg(label = "Width", default_value_type = DataType.NUMBER) @NotEmpty Double width,
								  @Idx(index = "4", type = AttributeType.NUMBER) @Pkg(label = "Height", default_value_type = DataType.NUMBER) @NotEmpty Double height,
							   	  @Idx(index = "5", type = AttributeType.DICTIONARY) @Pkg(label = "Dictionary", default_value_type = DataType.DICTIONARY) @NotEmpty HashMap<String,Value> dict) throws Exception {
		
		
		  if (this.sem == null) {
		      this.sem = new Semaphore(1);
		    }	 

		  this.sem.acquire();
		  this.dict = dict;	
		  
		  URL url = Paths.get(fxml).toUri().toURL();
		  FXMLLoader fxmlloader = new FXMLLoader(url);
		  fxmlloader.setController(this); 
	      this.root = fxmlloader.load();

	      window = new FXWindow(title, width.intValue(), height.intValue());
	     

	      window.getFrame().addWindowListener(new java.awt.event.WindowAdapter() {
	             @Override
	             public void windowClosing(java.awt.event.WindowEvent windowEvent) {
	            	 quit();
	             }
	         });

          Platform.runLater(new Runnable() {
		            @Override
		            public void run() {
		                initFX();
		            }
		   });

		   this.sem.acquire();
		   this.sem.release();
    
		return new DictionaryValue(this.dict);
	}


  private  void initFX() {
	  
	   List<String> bools = Arrays.asList("true","false");
	   List<Node> childs = getAllNodesInParent(this.root);
	   for (Node child : childs)
	   {
		   if (child.getId() != null)
		   {
			   
			   String Id = child.getId().toString();
			   if (this.dict.containsKey(Id))
			   {		
				   if (child instanceof TextField) {
					  ((TextField)child).setText(this.dict.get(Id).get().toString());
					
				   }
				   
				   if (child instanceof PasswordField) {
					  ((PasswordField)child).setText(this.dict.get(Id).get().toString());
					
				   }
				   
				   if (child instanceof CheckBox) {
					   String bool = this.dict.get(Id).get().toString();
					   if (bools.contains(bool))
					   {
					     ((CheckBox)child).setSelected(Boolean.parseBoolean(bool));
					   }
				   }
				   
				   
				   if (child instanceof ComboBox) {
					   if (this.dict.get(Id).get() != null)
					   {	   
					      String[] options = this.dict.get(Id).get().toString().split(",");
					      for (int i =0; i<options.length;i++)
					      {
					    	  ((ComboBox<String>)child).getItems().add(options[i]);
					      }
					   }
				   }
				   
				   if (child instanceof TextArea) {
						  ((TextArea)child).setText(this.dict.get(Id).get().toString());
					
				   }
				   
				   if (child instanceof javafx.scene.control.DatePicker) {
						  ((javafx.scene.control.DatePicker)child).setValue(LocalDate.now());
					
				   }
				   
			   }
						 
		    }   
	   }
	  
	  
	    
	     Scene scene = new Scene(this.root);
    	 URL url = this.getClass().getResource("/css/styles.css");
	     scene.getStylesheets().add(url.toExternalForm());
    	 window.getPanel().setScene(scene);
  	 }
	     
  
  @FXML
  protected  void save() {
	  
	   List<Node> childs = getAllNodesInParent(this.root);
	   for (Node child : childs)
	   {
		   if (child.getId() != null)
		   {
			   
			   String Id = child.getId().toString();
			   if (this.dict.containsKey(Id))
			   {		
				   if (child instanceof TextField) {
					   this.dict.put(Id, new StringValue(((TextField)child).getText()));
					
				   }
				   
				   if (child instanceof PasswordField) {
					   this.dict.put(Id, new StringValue(((PasswordField)child).getText()));
					
				   }
				   
				   if (child instanceof CheckBox) {
					   this.dict.put(Id, new StringValue(new Boolean(((CheckBox)child).isSelected()).toString()));
					
				   }
				   
				   if (child instanceof TextArea) {
					   this.dict.put(Id, new StringValue(((TextArea)child).getText()));
					
				   }
				   
				   if (child instanceof RadioButton) {
					   ToggleGroup group = ((RadioButton)child).getToggleGroup();
					   this.dict.put(Id, new StringValue(((RadioButton)group.getSelectedToggle()).getText()));
					
				   }
				   
				   if (child instanceof ToggleButton) {
					   ToggleGroup group = ((ToggleButton)child).getToggleGroup();
					   this.dict.put(Id, new StringValue(((ToggleButton)group.getSelectedToggle()).getText()));
					
				   }
				   
				   if (child instanceof javafx.scene.control.DatePicker) {
					   LocalDate date = ((javafx.scene.control.DatePicker)child).getValue();
					   this.dict.put(Id, new StringValue(date.toString()));
					
				   }
				   
				   if (child instanceof ComboBox) {
					   String selected = ((javafx.scene.control.ComboBox)child).getSelectionModel().getSelectedItem().toString();
					   this.dict.put(Id, new StringValue(selected));
					
				   }
				   
			   }
						 
		    }   
	   }
	   window.getFrame().setVisible(false);
	   this.sem.release();
	}

  @FXML
  protected void quit() {
	  	 window.getFrame().setVisible(false);
		 this.sem.release();
	}



public static List<Node> getAllNodesInParent(Parent parent) {
	  List<Node> ret = new ArrayList<>();
	  for (Node child : parent.getChildrenUnmodifiable()) {
	    ret.add(child);
	    if (child instanceof Parent) {
	      ret.addAll(getAllNodesInParent((Parent) child));
	    }
	  }
	  return ret;
	}	
}


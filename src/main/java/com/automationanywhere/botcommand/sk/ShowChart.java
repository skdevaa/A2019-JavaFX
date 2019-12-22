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

import static com.automationanywhere.commandsdk.model.AttributeType.SELECT;
import static com.automationanywhere.commandsdk.model.AttributeType.TEXT;
import static com.automationanywhere.commandsdk.model.DataType.STRING;
import java.awt.Dimension;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;
import javax.swing.JFrame;
import com.automationanywhere.botcommand.data.Value;
import com.automationanywhere.botcommand.data.model.Schema;
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
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.chart.Axis;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
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
@CommandPkg(label="Chart", name="Chart", description="Show Chart", icon="jfx.svg",
node_label="Chart")
public class ShowChart  {
	
	private static Semaphore sem;
	private FXWindow window;
	
	@Execute
	public void action(@Idx(index = "1", type = AttributeType.VARIABLE) @Pkg(label = "Chart Data1", default_value_type = DataType.RECORD) @NotEmpty Record record1,
			           @Idx(index = "2" ,type = AttributeType.VARIABLE) @Pkg(label = "Chart Data2", default_value_type = DataType.RECORD)  Record record2,
					   @Idx(index = "3", type = SELECT, options = {
				  		 @Idx.Option(index = "3.1", pkg = @Pkg(label = "Line Chart", value = "line")),
						 @Idx.Option(index = "3.2", pkg = @Pkg(label = "Pie Chart", value = "pie")),
						 @Idx.Option(index = "3.3", pkg = @Pkg(label = "Bar Chart", value = "bar"))
					   }) @Pkg(label = "ChartType", default_value = "line", default_value_type = STRING) @NotEmpty String charttype,
					   @Idx(index = "4", type = TEXT) @Pkg(label = "Chart Label", default_value_type = STRING) @NotEmpty String chartlabel,
					   @Idx(index = "5", type = TEXT) @Pkg(label = "X-Axis Label", default_value_type = STRING)  String xlabel,
					   @Idx(index = "6", type = TEXT) @Pkg(label = "Y-Axist Label", default_value_type = STRING)  String ylabel,
					   @Idx(index = "7", type = AttributeType.NUMBER) @Pkg(label = "Width", default_value_type = DataType.NUMBER) @NotEmpty Double width,
					   @Idx(index = "8", type = AttributeType.NUMBER) @Pkg(label = "Height", default_value_type = DataType.NUMBER) @NotEmpty Double height) throws Exception {
		
		

		 
		  if (this.sem == null) {
		      this.sem = new Semaphore(1);
		    }
	    	 this.sem.acquire();
		      window = new FXWindow("Chart", width.intValue(), height.intValue());
	         
	         window.getFrame().addWindowListener(new java.awt.event.WindowAdapter() {
	             @Override
	             public void windowClosing(java.awt.event.WindowEvent windowEvent) {
	            		quit();
	             }
	         });
		     
	         Platform.runLater(new Runnable() {
		            @Override
		            public void run() {
		            	switch (charttype) {
		            		case "pie":
		            			initPieChart(record1,chartlabel);
		            			break;
		            		case "bar":
		            			initBarChart(record1,record2,chartlabel,xlabel,ylabel);
		            			break;
		            		default:
			            		initLineChart(record1,record2,chartlabel,xlabel,ylabel);
			            }
		            }
		       });

		   this.sem.acquire();
		   this.sem.release();
    

	}


  private  void initLineChart(Record record1, Record record2, String chartlabel,String xlabel,String ylabel) {
	  
    

    	 Button but1= new Button("OKAY");
    	 but1.setOnAction((e)->{
    		 window.getFrame().setVisible(false);
    	     quit();
    	 });

    	 
         //defining the axes
         CategoryAxis xAxis = new CategoryAxis();
         Axis<Number> yAxis = new NumberAxis();
         xlabel = (xlabel == null) ? "" : xlabel;
         ylabel = (ylabel == null) ? "" : ylabel;
         xAxis.setLabel(xlabel);
         yAxis.setLabel(ylabel);
         //creating the chart
         LineChart<String, Double> lineChart = new LineChart(xAxis, yAxis);
         lineChart.setData(getXYChartData(record1,record2));                
         lineChart.setTitle(chartlabel);
         lineChart.setLegendVisible(false);


    	 Scene  scene  =  new  Scene(lineChart, Color.WHITE);
    	 URL url = this.getClass().getResource("/css/styles.css");
	     scene.getStylesheets().add(url.toExternalForm());
    	 window.getPanel().setScene(scene);
  

   
  	 }
  
  
  private  void initBarChart(Record record1, Record record2, String chartlabel,String xlabel,String ylabel) {
	  
	    

 	 Button but1= new Button("OKAY");
 	 but1.setOnAction((e)->{
 		 window.getFrame().setVisible(false);
 	     quit();
 	 });

 	 
      //defining the axes
      CategoryAxis xAxis = new CategoryAxis();
      Axis<Number> yAxis = new NumberAxis();
      xlabel = (xlabel == null) ? "" : xlabel;
      ylabel = (ylabel == null) ? "" : ylabel;
      xAxis.setLabel(xlabel);
      yAxis.setLabel(ylabel);
      //creating the chart
      BarChart<String, Double> barChart = new BarChart(xAxis, yAxis);
      barChart.setData(getXYChartData(record1,record2));                
      barChart.setTitle(chartlabel);
      barChart.setLegendVisible(false);
      


 	 Scene  scene  =  new  Scene(barChart, Color.WHITE);
 	 URL url = this.getClass().getResource("/css/styles.css");
	     scene.getStylesheets().add(url.toExternalForm());
 	 window.getPanel().setScene(scene);

	 }
  
  
  private  void initPieChart(Record record, String chartLabel) {
	  
	    

 	 Button but1= new Button("OKAY");
 	 but1.setOnAction((e)->{
 		 window.getFrame().setVisible(false);
 	     quit();
 	 });

 	 
 	   PieChart chart = new PieChart(getPieChartData(record));
 	   chart.setTitle(chartLabel);
 	   chart.setLegendSide(Side.LEFT);
 	   chart.setLabelLineLength(10);
 	   chart.setLabelsVisible(false);
 	   Scene  scene  =  new  Scene(chart, Color.WHITE);
 	   URL url = this.getClass().getResource("/css/styles.css");
	   scene.getStylesheets().add(url.toExternalForm());
 	   window.getPanel().setScene(scene);


	 }
  
 

  
  private ObservableList<XYChart.Series<String, Double>> getXYChartData(Record record1, Record record2) {

      ObservableList<XYChart.Series<String, Double>> result = FXCollections.observableArrayList();
    
      Series<String, Double>  series1 = new Series<String, Double>();
      
      List<Value> yvalues1 = record1.getValues();
      List<Schema> xvalues1 = record1.getSchema();
      
      int index = 0;
      for ( Schema x : xvalues1)
      {	
          series1.getData().add(new Data<String, Double>(x.getName().toString(), Double.parseDouble(yvalues1.get(index++).toString())));
      }
      result.add(series1);
      if (record2 != null)
      {
          Series<String, Double>  series2 = new Series<String, Double>();
          List<Value> yvalues2 = record2.getValues();
          List<Schema> xvalues2 = record2.getSchema();
          
          index = 0;
          for ( Schema x : xvalues2)
          {	
              series2.getData().add(new Data<String, Double>(x.getName().toString(), Double.parseDouble(yvalues2.get(index++).toString())));
          }
      result.add(series2);
      }
      return result;
    }
  
  
  
  private ObservableList<PieChart.Data> getPieChartData(Record record) {

      ObservableList<PieChart.Data> result = FXCollections.observableArrayList();
    
 
      
      List<Value> yvalues = record.getValues();
      List<Schema> xvalues = record.getSchema();
      
      int index = 0;
      for ( Schema x : xvalues)
      {	
    	  result.add(new PieChart.Data(x.getName().toString(), Double.parseDouble(yvalues.get(index++).toString())));
      }
      return result;
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

	



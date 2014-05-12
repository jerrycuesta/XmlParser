/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dynacardfx;

import java.net.URL;
import java.util.Scanner;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import com.jerry.dynacard.DynaCard;
import java.io.File;
import java.util.ArrayList;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import com.jerry.javafx.dialog.Dialog;
import java.nio.file.Path;

/**
 *
 * @author jerry
 */
public class DynaCardFx extends Application {

    final String title = "DynaCardFx";
    int dropCount = 0;

    private static DynaCard DynaCardFromResource(String filename) throws Exception {
        DynaCard card;
        String text;

        try {
            URL xmlUrl = DynaCardFx.class.getResource(filename);
            text = new Scanner(DynaCardFx.class.getResourceAsStream(filename), "UTF-8").useDelimiter("\\A").next();
        } catch (Exception ex) {
            System.out.println("DynaCardFromResource: " + ex.toString());
            throw(ex);
        }

        card = new DynaCard(text);
        try {
            card.Load();
        } catch (Exception ex) {
            System.out.println("DynaCardFromFile: " + ex.toString());
            throw(ex);
        }

        return card;
    }

    private static DynaCard DynaCardFromFile(String filename) throws Exception {
        DynaCard card;
        String text;

        try {
            text = new Scanner(new File(filename)).useDelimiter("\\A").next();
        } catch (Exception ex) {
            System.out.println("Failed to load card text: " + filename);
            throw(ex);
        }

        card = new DynaCard(text);
        try {
            card.Load();
        } catch (Exception ex) {
            System.out.println("DynaCardFromFile: " + ex.toString());
            throw(ex);
        }

        return card;
    }

    private LineChart<Number, Number> CreateChart() {
        //defining the axes
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Position");
        yAxis.setLabel("Load");

        //creating the chart
        LineChart<Number, Number> chart
                = new LineChart<>(xAxis, yAxis);

        chart.setTitle(title);

        return chart;
    }

    private XYChart.Series CreateSeries(DynaCard card, String cardName) {
        XYChart.Series series = new XYChart.Series();
        series.setName(cardName);

        for (DynaCard.LoadPosPair pair : card.SurfacePoints) {
            series.getData().add(new XYChart.Data(pair.pos, pair.load));
        }

        return series;
    }

    @Override
    public void start(Stage _stage) {

        String path = "C:\\Users\\jerry\\Documents\\DynaCards";
        String name = "JERRY811.DDS.TX.RD.WP_SIM_MP.DynaCard.0.20140210181959290.xml";
        String xmlText;
        String cardName = "ShutdownCard.xml";

        final Stage stage = _stage;

        stage.setTitle(cardName);
        final LineChart<Number, Number> lineChart = CreateChart();
        final Scene scene = new Scene(lineChart, 800, 600);
        final ArrayList<XYChart.Series> allSeries = new ArrayList<>();

        DynaCard card;
        try {
            card = DynaCardFromFile(path + "\\" + name);
            allSeries.add(CreateSeries(card, name));
            lineChart.getData().add(allSeries.get(0));
        }
        catch (Exception ex) {
            String message = ex.getMessage();
            Dialog.showError(title, ex.getMessage(), scene.getWindow());
        }
                        
        scene.setOnDragOver(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                Dragboard db = event.getDragboard();
                if (db.hasFiles()) {
                    event.acceptTransferModes(TransferMode.COPY);
                } else {
                    event.consume();
                }
            }
        });

        // Dropping over surface
        scene.setOnDragDropped(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                System.out.println("Drop Event: " + Integer.toString(dropCount++));
                Dragboard db = event.getDragboard();
                boolean success = false;
                if (db.hasFiles()) {
                    success = true;

                    for (XYChart.Series series : allSeries) {
                        lineChart.getData().remove(series);
                    }

                    for (File file : db.getFiles()) {
                        String filePath = file.getAbsolutePath();
                        try {
                            DynaCard card = DynaCardFromFile(filePath);
                            XYChart.Series series = CreateSeries(card, filePath);
                            allSeries.add(series);
                            lineChart.getData().add(series);
                        } catch (Exception ex) {
                            String message = file.getName() +":\n" + ex.getMessage();
                            System.out.println("del " + filePath);
                            Dialog.showError(title, message, scene.getWindow());
                        }
                    }
                }
            }
        });

        stage.setScene(scene);
        stage.show();
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}

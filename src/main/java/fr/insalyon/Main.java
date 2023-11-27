package fr.insalyon;

import fr.insalyon.xml.XMLParser;
import fr.insalyon.model.*;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.Parent;
import javafx.stage.Stage;

import static java.lang.Float.max;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        int mapWidth = 600;
        int mapHeight = 600;
        /* Loading map data */
        CityMap map = XMLParser.parseFile("data/xml/largeMap.xml");

        //Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("Main.fxml"));
        //Scene scene = new Scene(root, 1000, 700);
        //primaryStage.setTitle("Calculateur de tours de livraison en vélo");
        //primaryStage.setScene(scene);
        //primaryStage.show();
        VBox root = new VBox();
        Canvas canvas = new Canvas(mapHeight, mapWidth);
        fillMap(map, canvas);

        root.getChildren().add(canvas);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    private void fillMap(CityMap map, Canvas canvas) {
        // Getting max lat and long coordinates
        float maxLat = map.getIntersections().stream().map(Intersection::getLatitude).max(Float::compare).get();
        float maxLong = map.getIntersections().stream().map(Intersection::getLongitude).max(Float::compare).get();
        float minLat = map.getIntersections().stream().map(Intersection::getLatitude).min(Float::compare).get();
        float minLong = map.getIntersections().stream().map(Intersection::getLongitude).min(Float::compare).get();
        float latDiff = maxLat - minLat;
        float longDiff = maxLong - minLong;
        float coeff = max(latDiff, longDiff);
        // Scaling Graphic Context
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.scale(1, -1);
        gc.translate(0, -(latDiff * (float)(canvas.getHeight()) / coeff));
        // Scaling coordinates
        map.getIntersections().forEach(intersection -> {
            intersection.setLatitude((intersection.getLatitude() - minLat) * (float)(canvas.getHeight()) / coeff);
            intersection.setLongitude((intersection.getLongitude() - minLong) * (float)(canvas.getWidth()) / coeff);
        });
        map.getIntersections().forEach(intersection -> intersection.getOutwardSegments().forEach(segment -> drawSegment(gc, segment)));
    }

    private void drawSegment(GraphicsContext gc, Segment segment) {
        gc.setStroke(Color.BLUE);
        gc.strokeLine(segment.getOrigin().getLongitude(), segment.getOrigin().getLatitude(), segment.getDestination().getLongitude(), segment.getDestination().getLatitude());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
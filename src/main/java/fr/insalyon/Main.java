package fr.insalyon;

import fr.insalyon.xml.CityMapXMLParser;
import fr.insalyon.model.*;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.Parent;
import javafx.stage.Stage;

import static java.lang.Float.max;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Optional;

public class Main extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		/* Loading map data */
		InputStream input = new FileInputStream("data/xml/smallMap.xml");
		CityMapXMLParser parser = new CityMapXMLParser(input);
		CityMap map = parser.parse();

		Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("Main.fxml"));
		Scene scene = new Scene(root, 1000, 700);
		Canvas canvas = (Canvas) scene.lookup("#canvas_map");
		fillMap(map, canvas);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        Path path = new Path(map.getWarehouse(), map.getIntersectionById(251047560l), map.getWarehouse().getOutwardSegments().stream().toList());
        drawPath(gc, path);

		primaryStage.setTitle("Calculateur de tours de livraison en vélo");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	private <T> T getOptionalValue(Optional<T> value) {
		if (value.isEmpty()) {
			throw new IllegalArgumentException("No value found");
		}
		return value.get();
	}

	private float getMaxLatitude(CityMap map) {
		Optional<Float> max = map.getIntersections()
				                 .stream()
								 .map(Intersection::getLatitude)
								 .max(Float::compare);
		return getOptionalValue(max);
	}

	private float getMinLatitude(CityMap map) {
		Optional<Float> max = map.getIntersections()
				                 .stream()
								 .map(Intersection::getLatitude)
								 .min(Float::compare);
		return getOptionalValue(max);
	}

	private float getMaxLongitude(CityMap map) {
		Optional<Float> max = map.getIntersections()
				                 .stream()
								 .map(Intersection::getLongitude)
								 .max(Float::compare);
		return getOptionalValue(max);
	}

	private float getMinLongitude(CityMap map) {
		Optional<Float> max = map.getIntersections()
				                 .stream()
								 .map(Intersection::getLongitude)
								 .min(Float::compare);
		return getOptionalValue(max);
	}

	private void fillMap(CityMap map, Canvas canvas) {
		// Getting max lat and long coordinates
		float maxLat = getMaxLatitude(map);
		float maxLong = getMaxLongitude(map);
		float minLat = getMinLatitude(map);
		float minLong = getMinLongitude(map);
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

	private void drawPath(GraphicsContext gc, Path path){
		gc.setStroke(Color.RED);
		for (Segment segment : path.getSegments()) {
			gc.strokeLine(segment.getOrigin().getLongitude(), segment.getOrigin().getLatitude(), segment.getDestination().getLongitude(), segment.getDestination().getLatitude());
		}
	}

	private void drawSegment(GraphicsContext gc, Segment segment) {
		gc.setStroke(Color.BLUE);
		gc.strokeLine(segment.getOrigin().getLongitude(), segment.getOrigin().getLatitude(), segment.getDestination().getLongitude(), segment.getDestination().getLatitude());
	}

	public static void main(String[] args) {
		launch(args);
	}
}
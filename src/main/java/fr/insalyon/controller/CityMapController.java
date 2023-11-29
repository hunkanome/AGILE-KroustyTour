package fr.insalyon.controller;

import static java.lang.Float.max;

import fr.insalyon.model.CityMap;
import fr.insalyon.model.Path;
import fr.insalyon.model.Segment;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class CityMapController {
	@FXML
	private Canvas canvasMap;
    /**
     * Used to keep track of the last click X coordinate to make dragging possible
     */
    private double lastClickX = -1;
    /**
     * Used to keep track of the last click Y coordinate to make dragging possible
     */
    private double lastClickY = -1;

	public void initialize(CityMap map) {
		fillMap(map, canvasMap, 1, 0, 0);
		canvasMap.setOnScroll(event -> {
			clearCanvas();
			double zoomFactor = event.getDeltaY() > 0 ? 1.02 : 0.98;
			fillMap(map, canvasMap, zoomFactor, 0,0);
		});
        canvasMap.setOnMousePressed(event -> {
            lastClickX = event.getX();
            lastClickY = event.getY();
        });
        canvasMap.setOnMouseDragged(event -> {
            clearCanvas();
            fillMap(map, canvasMap, 1,event.getX() - lastClickX, event.getY() - lastClickY);
            lastClickX = event.getX();
            lastClickY = event.getY();
        });
	}

	private void fillMap(CityMap map, Canvas canvas, double zoomFactor, double xTranslation, double yTranslation) {
		// Calculating max size of map
		float latDiff = map.getMaxLatitude() - map.getMinLatitude();
		float longDiff = map.getMaxLongitude() - map.getMinLongitude();
		float coeff = max(latDiff, longDiff);
		GraphicsContext gc = canvas.getGraphicsContext2D();
        // Zooming by the specified amount (no effect is 1)
		gc.scale(zoomFactor, zoomFactor);
        // translating by the specified amounts (no effect if 0)
        gc.translate(xTranslation, yTranslation);
		gc.setFill(Color.RED);
		gc.fillRect(0, 0, canvasMap.getWidth(), canvasMap.getHeight());
		// Scaling coordinates
		map.getIntersections().forEach(intersection -> intersection.getOutwardSegments().forEach(segment -> {
            // Calculating better coordinates to display on map
			double yOrigin = (map.getMaxLatitude() - segment.getOrigin().getLatitude()) / coeff * canvas.getHeight();
			double xOrigin = (segment.getOrigin().getLongitude() - map.getMinLongitude()) / coeff * canvas.getWidth();
			double yDestination = (map.getMaxLatitude() - segment.getDestination().getLatitude()) / coeff * canvas.getHeight();
			double xDestination = (segment.getDestination().getLongitude() - map.getMinLongitude()) / coeff * canvas.getWidth();
			drawLine(gc, xOrigin, yOrigin, xDestination, yDestination);
		}));
	}

	private void drawPath(GraphicsContext gc, Path path){
		gc.setStroke(Color.RED);
		for (Segment segment : path.getSegments()) {
			gc.strokeLine(segment.getOrigin().getLongitude(), segment.getOrigin().getLatitude(), segment.getDestination().getLongitude(), segment.getDestination().getLatitude());
		}
	}

	private void drawLine(GraphicsContext gc, double x1, double y1, double x2, double y2) {
		gc.setStroke(Color.BLUE);
		gc.strokeLine(x1, y1, x2, y2);
	}

	private void clearCanvas() {
		canvasMap.getGraphicsContext2D().clearRect(0, 0, canvasMap.getWidth(), canvasMap.getHeight());
	}
}

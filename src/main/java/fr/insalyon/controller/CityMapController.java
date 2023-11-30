package fr.insalyon.controller;

import static java.lang.Float.max;

import fr.insalyon.model.*;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.List;

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

	private DataModel data;

	public void initialize(CityMap map) {
		this.data = new DataModel(map);

		fillMap(map, this.canvasMap, 1, 0, 0);
	}

	private void fillMap(CityMap map, Canvas canvas, double zoomFactor, double xTranslation, double yTranslation) {
		// Calculating max size of map
		float latDiff = map.getMaxLatitude() - map.getMinLatitude();
		float longDiff = map.getMaxLongitude() - map.getMinLongitude();
		float coefficient = max(latDiff, longDiff);
		GraphicsContext gc = canvas.getGraphicsContext2D();
        // Zooming by the specified amount (no effect if 1)
		gc.scale(zoomFactor, zoomFactor);
        // translating by the specified amounts (no effect if 0)
        gc.translate(xTranslation, yTranslation);
		gc.setFill(Color.RED);
		gc.fillRect(0, 0, canvasMap.getWidth(), canvasMap.getHeight());
		// Scaling coordinates
		map.getIntersections().forEach(intersection -> intersection.getOutwardSegments().forEach(segment -> {
            // Calculating better coordinates to display on map
			double yOrigin = (map.getMaxLatitude() - segment.getOrigin().getLatitude()) / coefficient * canvas.getHeight();
			double xOrigin = (segment.getOrigin().getLongitude() - map.getMinLongitude()) / coefficient * canvas.getWidth();
			double yDestination = (map.getMaxLatitude() - segment.getDestination().getLatitude()) / coefficient * canvas.getHeight();
			double xDestination = (segment.getDestination().getLongitude() - map.getMinLongitude()) / coefficient * canvas.getWidth();
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
		canvasMap.getGraphicsContext2D().clearRect(0, 0, canvasMap.getWidth()+10, canvasMap.getHeight()+10);
	}

	@FXML
	private void saveMousePosition(MouseEvent event){
		lastClickX = event.getX();
		lastClickY = event.getY();
	}

	@FXML
	private void zoomOnScroll(ScrollEvent event) {
		clearCanvas();
		double zoomFactor = event.getDeltaY() > 0 ? 1.03 : 0.97;
		fillMap(data.getMap(), canvasMap, zoomFactor, 0,0);
	}

	@FXML
	private void moveOnDrag(MouseEvent event) {
		clearCanvas();
		fillMap(data.getMap(), canvasMap, 1, event.getX() - lastClickX, event.getY() - lastClickY);
		lastClickX = event.getX();
		lastClickY = event.getY();
	}
}

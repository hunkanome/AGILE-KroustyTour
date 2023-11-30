package fr.insalyon.controller;

import fr.insalyon.geometry.CoordinateTransformer;
import fr.insalyon.geometry.Position;
import fr.insalyon.model.CityMap;
import fr.insalyon.model.Path;
import fr.insalyon.model.Segment;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.input.MouseEvent;

public class CityMapController {
	@FXML
	private Canvas canvasMap;
	private CityMap cityMap;
	/**
	 * Used to keep track of the last click X coordinate to make dragging possible
	 */
	private double lastClickX = -1;
	/**
	 * Used to keep track of the last click Y coordinate to make dragging possible
	 */
	private double lastClickY = -1;

	public void initialize(CityMap map) {
		cityMap = map;
		fillMap(1, 0, 0);
	}

	private void fillMap(double zoomFactor, double xTranslation, double yTranslation) {
		GraphicsContext gc = canvasMap.getGraphicsContext2D();
		// Zooming by the specified amount (no effect if 1)
		gc.scale(zoomFactor, zoomFactor);
		// translating by the specified amounts (no effect if 0)
		gc.translate(xTranslation, yTranslation);
		gc.setFill(Color.RED);
		gc.fillRect(0, 0, canvasMap.getWidth(), canvasMap.getHeight());
		// Scaling coordinates
		CoordinateTransformer transformer = new CoordinateTransformer(cityMap.getNorthWestMostCoordinates(),
				cityMap.getSouthEastMostCoordinates(), (float) canvasMap.getWidth(), (float) canvasMap.getHeight());

		gc.setStroke(Color.BLUE);
		cityMap.getIntersections().forEach(intersection -> intersection.getOutwardSegments().forEach(segment -> {
			// Calculating better coordinates to display on map
			Position origin = transformer.transformToPosition(segment.getOrigin().getCoordinates());
			Position destination = transformer.transformToPosition(segment.getDestination().getCoordinates());
			drawLine(gc, origin, destination);
		}));
	}

	private void drawPath(GraphicsContext gc, Path path) {
		gc.setStroke(Color.RED);
		CoordinateTransformer transformer = new CoordinateTransformer(cityMap.getNorthWestMostCoordinates(),
				cityMap.getSouthEastMostCoordinates(), (float) canvasMap.getWidth(), (float) canvasMap.getHeight());
		for (Segment segment : path.getSegments()) {
			Position origin = transformer.transformToPosition(segment.getOrigin().getCoordinates());
			Position destination = transformer.transformToPosition(segment.getDestination().getCoordinates());
			drawLine(gc, origin, destination);
		}
	}

	private void drawLine(GraphicsContext gc, Position origin, Position destination) {
		this.drawLine(gc, origin.getX(), origin.getY(), destination.getX(), destination.getY());
	}

	private void drawLine(GraphicsContext gc, double x1, double y1, double x2, double y2) {
		gc.strokeLine(x1, y1, x2, y2);
	}

	private void clearCanvas() {
		canvasMap.getGraphicsContext2D().clearRect(0, 0, canvasMap.getWidth() + 10, canvasMap.getHeight() + 10);
	}

	@FXML
	private void saveMousePosition(MouseEvent event) {
		lastClickX = event.getX();
		lastClickY = event.getY();
	}

	@FXML
	private void zoomOnScroll(ScrollEvent event) {
		clearCanvas();
		double zoomFactor = event.getDeltaY() > 0 ? 1.03 : 0.97;
		fillMap(zoomFactor, 0, 0);
	}

	@FXML
	private void moveOnDrag(MouseEvent event) {
		clearCanvas();
		fillMap(1, event.getX() - lastClickX, event.getY() - lastClickY);
		lastClickX = event.getX();
		lastClickY = event.getY();
	}
}

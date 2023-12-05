package fr.insalyon.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import fr.insalyon.algorithm.CityMapMatrix;
import fr.insalyon.algorithm.TSP;
import fr.insalyon.algorithm.TSP1;
import fr.insalyon.geometry.CoordinateTransformer;
import fr.insalyon.geometry.Position;
import fr.insalyon.model.CityMap;
import fr.insalyon.model.DataModel;
import fr.insalyon.model.Delivery;
import fr.insalyon.model.Intersection;
import fr.insalyon.model.Path;
import fr.insalyon.model.Segment;
import fr.insalyon.model.Tour;
import fr.insalyon.xml.BadlyFormedXMLException;
import fr.insalyon.xml.CityMapXMLParser;
import fr.insalyon.xml.XMLParserException;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

public class CityMapController {
	@FXML
	private Canvas canvasMap;

	@FXML
	private AnchorPane canvasContainer;

	@FXML
	private Label selectedSegmentLabel;

	/**
	 * Used to keep track of the last click X coordinate to make dragging possible
	 */
	private double lastClickX = -1;

	/**
	 * Used to keep track of the last click Y coordinate to make dragging possible
	 */
	private double lastClickY = -1;

	private double prevScaleFactor = 1;
	private double scaleFactor = 1;

	private Position prevTranslationFactor = new Position(0f, 0f);
	private Position translationFactor = new Position(0f, 0f);

	private CoordinateTransformer transformer;

	private DataModel dataModel;
	private CityMapMatrix cityMapMatrix;
	static final int WAIT_TIME = 10;
	private TSP tsp = new TSP1();
	private MainController parentController;

	public void initialize(DataModel dataModel, MainController mainController) {
		this.parentController = mainController;
		this.dataModel = dataModel;
		this.dataModel.cityMapProperty().addListener(this::onCityMapUpdate);
		this.dataModel.selectedDeliveryProperty().addListener(this::onSelectedDeliveryUpdate);
		this.dataModel.selectedTourProperty().addListener(this::onSelectedTourUpdate);
		this.dataModel.selectedIntersectionProperty().addListener(this::onSelectedIntersection);

		if (this.dataModel.getCityMap() != null) {
			transformer = new CoordinateTransformer(dataModel.getCityMap().getNorthWestMostCoordinates(),
					dataModel.getCityMap().getSouthEastMostCoordinates(), (float) canvasMap.getWidth(),
					(float) canvasMap.getHeight());
			drawMap();
		}
	}

	private void drawMap() {
		updateCanvasProperties();
		if (this.dataModel.getCityMap() != null) {
			clearCanvas();

			GraphicsContext gc = canvasMap.getGraphicsContext2D();
			gc.setStroke(Color.BLUE);
			dataModel.getCityMap().getIntersections()
					.forEach(intersection -> intersection.getOutwardSegments().forEach(segment -> {
						// Calculating better coordinates to display on map
						Position origin = transformer.transformToPosition(segment.getOrigin().getCoordinates());
						Position destination = transformer
								.transformToPosition(segment.getDestination().getCoordinates());
						drawLine(gc, origin, destination);
					}));

			// draw the selected intersection
			if (dataModel.getSelectedIntersection() != null) {
				drawPoint(transformer.transformToPosition(dataModel.getSelectedIntersection().getCoordinates()),
						Color.BLACK);
			}
			
			// draw the warehouse
			drawPoint(transformer.transformToPosition(dataModel.getCityMap().getWarehouse().getCoordinates()), Color.GREEN);
			
			// TODO : draw all the tours

			// draw all the deliveries
			dataModel.getTours().forEach(tour -> {
				tour.getDeliveriesList().forEach(delivery -> {
					drawPoint(transformer.transformToPosition(delivery.getLocation().getCoordinates()), Color.BLACK);
				});
			});

			// draw the selected delivery
			if (dataModel.getSelectedDelivery() != null) {
				drawPoint(
						transformer.transformToPosition(dataModel.getSelectedDelivery().getLocation().getCoordinates()),
						Color.RED);
			}
		}
	}

	/**
	 * Updates the canvas scale and translation
	 */
	private void updateCanvasProperties() {
		GraphicsContext gc = canvasMap.getGraphicsContext2D();

		if (this.prevTranslationFactor != this.translationFactor) {
			gc.translate(-this.prevTranslationFactor.getX(), -this.prevTranslationFactor.getY());
			gc.translate(this.translationFactor.getX(), this.translationFactor.getY());
			this.prevTranslationFactor = this.translationFactor.copy();
		}

		if (this.prevScaleFactor != this.scaleFactor) {
			gc.scale(1 / this.prevScaleFactor, 1 / this.prevScaleFactor);
			gc.scale(this.scaleFactor, this.scaleFactor);
			this.prevScaleFactor = this.scaleFactor;
		}
	}

	private void drawPoint(Position position, Color color) {
		GraphicsContext gc = canvasMap.getGraphicsContext2D();
		gc.setFill(color);
		gc.fillOval(position.getX() - 3, position.getY() - 3, 6, 6);
	}

	private void drawPath(Path path) {
		GraphicsContext gc = canvasMap.getGraphicsContext2D();
		gc.setStroke(Color.RED);
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
		GraphicsContext gc = canvasMap.getGraphicsContext2D();
		int offset = 10; // the cleaned zoned is a bit bigger than the canvas size to avoid artifacts
		gc.clearRect(-offset, -offset, canvasMap.getWidth() + offset * 2, canvasMap.getHeight() + offset * 2);
	}

	@FXML
	private void selectIntersection(MouseEvent event) {
		if (dataModel.getCityMap() != null) {
			Position clickPosition = new Position((float) event.getX(), (float) event.getY());
			clickPosition.divide(this.scaleFactor);
			clickPosition.substract(this.translationFactor);
			Position intersectionPosition;
			Intersection selectedIntersection = null;
			float distance;
			float distanceMin = 15;

			for (Intersection intersection : dataModel.getCityMap().getIntersections()) {
				intersectionPosition = transformer.transformToPosition(intersection.getCoordinates());
				distance = clickPosition.distanceTo(intersectionPosition);
				if (distance < distanceMin) {
					distanceMin = distance;
					selectedIntersection = intersection;
				}
			}

			if (selectedIntersection != null) {
				// TODO : if a delivery is at this intersection, select it instead
				this.dataModel.setSelectedIntersection(selectedIntersection);
				drawMap();
			}
		}
	}

	@FXML
	private void saveMousePosition(MouseEvent event) {
		lastClickX = event.getX();
		lastClickY = event.getY();
	}

	@FXML
	private void zoomOnScroll(ScrollEvent event) {
		double zoomFactor = event.getDeltaY() > 0 ? 1.03 : 0.97;
		this.prevScaleFactor = this.scaleFactor;
		this.scaleFactor *= zoomFactor;
		drawMap();
	}

	@FXML
	private void moveOnDrag(MouseEvent event) {
		this.prevTranslationFactor = this.translationFactor.copy();
		float xFactor = (float) ((event.getX() - lastClickX) / this.scaleFactor);
		float yFactor = (float) ((event.getY() - lastClickY) / this.scaleFactor);
		this.translationFactor.setX(this.translationFactor.getX() + xFactor);
		this.translationFactor.setY(this.translationFactor.getY() + yFactor);
		drawMap();
		lastClickX = event.getX();
		lastClickY = event.getY();
	}

	/**
	 * Open the given file and load it onto the map
	 * 
	 * @param path - the path to the file to load
	 */
	public boolean loadCityMapXMLFile(String path) {
		File mapFile = new File(path);
		if (!mapFile.isFile()) {
			this.parentController.displayToolBarMessage("The item is not a file");
		} else if (!mapFile.canRead()) {
			this.parentController.displayToolBarMessage("The file is not readable");
		} else {
			try {
				canvasContainer.setStyle("-fx-background-color: lightgrey");
				FileInputStream input = new FileInputStream(mapFile);
				CityMapXMLParser parser = new CityMapXMLParser(input);
				CityMap newMap = parser.parse();
				dataModel.setMap(newMap);
				return true;
			} catch (BadlyFormedXMLException | XMLParserException e) {
				this.parentController.displayToolBarMessage(e);
			} catch (FileNotFoundException e) {
				this.parentController.displayToolBarMessage("The file " + path + " could not be found.");
			}
		}
		return false;
	}

	/**
	 * @see <a href=
	 *      "https://stackoverflow.com/questions/32534113/javafx-drag-and-drop-a-file-into-a-program">JavaFx
	 *      Drag and Drop a file INTO a program</a>
	 */
	@FXML
	private void handleFileDropped(DragEvent event) {
		Dragboard db = event.getDragboard();
		boolean success = false;
		if (db.hasFiles()) {
			List<File> files = db.getFiles();
			if (files.size() == 1) {
				success = this.loadCityMapXMLFile(files.get(0).getAbsolutePath());
			} else {
				this.parentController.displayToolBarMessage("Only one file can be loaded");
			}
		}
		/*
		 * let the source know whether the string was successfully transferred and used
		 */
		event.setDropCompleted(success);
		event.consume();
	}

	/**
	 * @see <a href=
	 *      "https://stackoverflow.com/questions/32534113/javafx-drag-and-drop-a-file-into-a-program">JavaFx
	 *      Drag and Drop a file INTO a program</a>
	 */
	@FXML
	private void handleFileOver(DragEvent event) {
		if (event.getGestureSource() != canvasContainer && event.getDragboard().hasFiles()) {
			/* allow for both copying and moving, whatever user chooses */
			event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
			canvasContainer.setStyle("-fx-background-color: darkgrey");
		}
		event.consume();
	}

	@FXML
	private void handleFileExit(DragEvent event) {
		if (event.getGestureSource() != canvasContainer && event.getDragboard().hasFiles()) {
			/* allow for both copying and moving, whatever user chooses */
			event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
			canvasContainer.setStyle("-fx-background-color: lightgrey");
		}
		event.consume();
	}

	public void computeShortestPathTours() {
		for (Tour tour : dataModel.getTours()) {
			this.cityMapMatrix = new CityMapMatrix(dataModel.getCityMap(), tour.getDeliveriesList());
			this.tsp.searchSolution(WAIT_TIME * 1000, this.cityMapMatrix.getGraph());
		}
	}

	private void onCityMapUpdate(ObservableValue<? extends CityMap> observable, CityMap oldValue, CityMap newValue) {
		transformer = new CoordinateTransformer(dataModel.getCityMap().getNorthWestMostCoordinates(),
				dataModel.getCityMap().getSouthEastMostCoordinates(), (float) canvasMap.getWidth(),
				(float) canvasMap.getHeight());
		this.prevScaleFactor = this.scaleFactor;
		this.scaleFactor = 1;
		this.prevTranslationFactor = this.translationFactor.copy();
		this.translationFactor = new Position(0f, 0f);
		drawMap();
		this.parentController.displayToolBarMessage("Map loaded successfully");
	}

	private void onSelectedDeliveryUpdate(ObservableValue<? extends Delivery> observable, Delivery oldValue,
			Delivery newValue) {
		// TODO show the delivery on the map
		System.out.println("Changement de delivery");
		drawMap();
	}

	private void onSelectedTourUpdate(ObservableValue<? extends Tour> observable, Tour oldValue, Tour newValue) {
		// TODO show the tour on the map
		if (newValue == null) {
			System.out.println("plus de tour");
		} else {
			System.out.println("Changement de tour");
		}
	}

	private void onSelectedIntersection(ObservableValue<? extends Intersection> observable, Intersection oldValue,
			Intersection newValue) {
		drawMap();
	}

}

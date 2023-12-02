package fr.insalyon.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import fr.insalyon.geometry.CoordinateTransformer;
import fr.insalyon.geometry.Position;
import fr.insalyon.model.CityMap;
import fr.insalyon.model.DataModel;
import fr.insalyon.model.Path;
import fr.insalyon.model.Segment;
import fr.insalyon.xml.BadlyFormedXMLException;
import fr.insalyon.xml.CityMapXMLParser;
import fr.insalyon.xml.XMLParserException;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
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

	public void initialize(CityMap map) {
		this.dataModel = new DataModel(map); // TODO create somewhere else and pass it in the args
		transformer = new CoordinateTransformer(dataModel.getMap().getNorthWestMostCoordinates(),
				dataModel.getMap().getSouthEastMostCoordinates(), (float) canvasMap.getWidth(),
				(float) canvasMap.getHeight());
		drawMap();
	}

	private void drawMap() {
		updateCanvasProperties();
		GraphicsContext gc = canvasMap.getGraphicsContext2D();

		gc.setStroke(Color.BLUE);
		dataModel.getMap().getIntersections()
				.forEach(intersection -> intersection.getOutwardSegments().forEach(segment -> {
					// Calculating better coordinates to display on map
					Position origin = transformer.transformToPosition(segment.getOrigin().getCoordinates());
					Position destination = transformer.transformToPosition(segment.getDestination().getCoordinates());
					drawLine(gc, origin, destination);

				}));
	}

	/**
	 * Updates the canvas scale and translation
	 */
	private void updateCanvasProperties() {
		GraphicsContext gc = canvasMap.getGraphicsContext2D();

		if (this.prevTranslationFactor != this.translationFactor) {
			gc.translate(-this.prevTranslationFactor.getX(), -this.prevTranslationFactor.getY());
			gc.translate(this.translationFactor.getX(), this.translationFactor.getY());
			this.prevTranslationFactor = this.translationFactor.clone();
		}

		if (this.prevScaleFactor != this.scaleFactor) {
			gc.scale(1 / this.prevScaleFactor, 1 / this.prevScaleFactor);
			gc.scale(this.scaleFactor, this.scaleFactor);
			this.prevScaleFactor = this.scaleFactor;
		}
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
	private void saveMousePosition(MouseEvent event) {
		lastClickX = event.getX();
		lastClickY = event.getY();
	}

	@FXML
	private void zoomOnScroll(ScrollEvent event) {
		clearCanvas();
		double zoomFactor = event.getDeltaY() > 0 ? 1.03 : 0.97;
		this.prevScaleFactor = this.scaleFactor;
		this.scaleFactor *= zoomFactor;
		drawMap();
	}

	@FXML
	private void moveOnDrag(MouseEvent event) {
		clearCanvas();
		this.prevTranslationFactor = this.translationFactor.clone();
		float xFactor = (float) (event.getX() - lastClickX);
		float yFactor = (float) (event.getY() - lastClickY);
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
	public void loadCityMapXMLFile(String path) {
		// TODO : handle errors
		File mapFile = new File(path);
		if (!mapFile.isFile()) {
			System.err.println("The file you dropped is not a file");
		} else if (!mapFile.canRead()) {
			System.err.println("The file you dropped is not readable");
		} else {
			try {
				FileInputStream input = new FileInputStream(mapFile);
				CityMapXMLParser parser = new CityMapXMLParser(input);
				CityMap newMap = parser.parse();
				dataModel.setMap(newMap);
				transformer = new CoordinateTransformer(dataModel.getMap().getNorthWestMostCoordinates(),
						dataModel.getMap().getSouthEastMostCoordinates(), (float) canvasMap.getWidth(),
						(float) canvasMap.getHeight());
				clearCanvas();
				this.prevScaleFactor = this.scaleFactor;
				this.scaleFactor = 1;
				this.prevTranslationFactor = this.translationFactor.clone();
				this.translationFactor = new Position(0f, 0f);
				drawMap();
				canvasContainer.setStyle("-fx-background-color: lightgrey");
			} catch (BadlyFormedXMLException e) {
				e.printStackTrace();
			} catch (XMLParserException e) {
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
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
			if (files.size() != 1) {
				System.err.println("You can only drop one file at a time");
			} else {
				this.loadCityMapXMLFile(files.get(0).getAbsolutePath());
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
}

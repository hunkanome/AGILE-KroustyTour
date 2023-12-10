package fr.insalyon.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import fr.insalyon.controller.command.CommandList;
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
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

public class CityMapController implements Controller {
	@FXML
	private Canvas canvasMap;

	@FXML
	private AnchorPane canvasContainer;

	@FXML
	private Label selectedSegmentLabel;
	
	private DataModel dataModel;
	
	private MainController parentController;
	
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

	@Override
	public void initialize(DataModel dataModel, MainController parentController, CommandList commandList) {
		this.dataModel = dataModel;
		this.parentController = parentController;

		this.dataModel.cityMapProperty().addListener(this::onCityMapUpdate);
		this.dataModel.selectedDeliveryProperty().addListener(this::onSelectedDeliveryUpdate);
		this.dataModel.selectedTourProperty().addListener(this::onSelectedTourUpdate);
		this.dataModel.selectedIntersectionProperty().addListener(this::onSelectedIntersection);

		if (this.dataModel.getCityMap() != null) {
			transformer = new CoordinateTransformer(this.dataModel.getCityMap().getNorthWestMostCoordinates(),
					this.dataModel.getCityMap().getSouthEastMostCoordinates(), (float) this.canvasContainer.getWidth(),
					(float) this.canvasContainer.getHeight());
			drawCanvas();
		}
	}

	private void drawCanvas() {
		//updateCanvasProperties();
		if (this.dataModel.getCityMap() != null) {
			clearCanvas();
			drawCityMap();
			drawSelectedIntersection();
			// TODO : draw all the tours
			drawAllDeliveries();
			drawSelectedDelivery();
			drawWarehouse();
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

	private void clearCanvas() {
		this.canvasContainer.getChildren().clear();
	}

	private void drawCityMap() {
		dataModel.getCityMap().getIntersections()
				.forEach(intersection -> intersection.getOutwardSegments().forEach(segment -> {
					// Calculating better coordinates to display on map
					Position origin = transformer.transformToDragAndZoomPosition(
							segment.getOrigin().getCoordinates(),
							this.translationFactor,
							this.scaleFactor
					);
					Position destination = transformer.transformToDragAndZoomPosition(
							segment.getDestination().getCoordinates(),
							this.translationFactor,
							this.scaleFactor
					);

					Line line = new Line(origin.getX(), origin.getY(), destination.getX(), destination.getY());
					line.setStroke(Color.BLUE);
					line.setUserData(segment);
					line.setOnMouseEntered(event -> selectedSegmentLabel.setText(segment.getName()));
					canvasContainer.getChildren().add(line);
				}));
	}

	private void drawWarehouse() {
		Image img  = new Image("file:images/warehouse.png");
		Position imgPosition = transformer.transformToDragAndZoomPosition(
				dataModel.getCityMap().getWarehouse().getCoordinates(),
				this.translationFactor,
				this.scaleFactor
		);

		ImageView imageView = new ImageView(img);
		imageView.setX(imgPosition.getX()-12);
		imageView.setY(imgPosition.getY()-12);
		imageView.setFitHeight(25);
		imageView.setFitWidth(25);
		this.canvasContainer.getChildren().add(imageView);
	}

	private void drawSelectedIntersection() {
		if (dataModel.getSelectedIntersection() != null) {
			Image img  = new Image("file:images/pointGPS.png");
			Position imgPosition = transformer.transformToDragAndZoomPosition(
					dataModel.getSelectedIntersection().getCoordinates(),
					this.translationFactor,
					this.scaleFactor
			);

			ImageView imageView = new ImageView(img);
			imageView.setX(imgPosition.getX()-12);
			imageView.setY(imgPosition.getY()-25);
			imageView.setFitHeight(25);
			imageView.setFitWidth(25);
			this.canvasContainer.getChildren().add(imageView);
		}
	}
	private void drawAllDeliveries() {
		dataModel.getTours()
				.forEach(tour -> tour.getDeliveriesList()
						.forEach(delivery -> drawPoint(
								transformer.transformToDragAndZoomPosition(delivery.getLocation().getCoordinates(),
										this.translationFactor, this.scaleFactor),
								Color.BLACK)
						)
				);
	}

	private void drawSelectedDelivery() {
		if (dataModel.getSelectedDelivery() != null) {
			drawPoint(transformer.transformToDragAndZoomPosition(dataModel.getSelectedDelivery().getLocation().getCoordinates(),
							this.translationFactor, this.scaleFactor),
					Color.RED);
		}
	}

	private void drawPoint(Position position, Color color) {
		Circle point = new Circle(4, color);
		point.setCenterX(position.getX());
		point.setCenterY(position.getY());

		this.canvasContainer.getChildren().add(point);
	}

	private void drawPath(Path path) {
		for (Segment segment : path.getSegments()) {
			Position origin = transformer.transformToDragAndZoomPosition(segment.getOrigin().getCoordinates(),
					this.translationFactor, this.scaleFactor);
			Position destination = transformer.transformToDragAndZoomPosition(segment.getDestination().getCoordinates(),
					this.translationFactor, this.scaleFactor);

			Line line = new Line(origin.getX(), origin.getY(), destination.getX(), destination.getY());
			line.setStroke(Color.RED);
			line.setUserData(segment);
			line.setOnMouseEntered(event -> selectedSegmentLabel.setText(segment.getName()));
			canvasContainer.getChildren().add(line);
		}
	}

	@FXML
	private void selectIntersection(MouseEvent event) {
		if (dataModel.getCityMap() != null) {
			Position clickPosition = new Position((float) event.getX(), (float) event.getY());
			clickPosition = transformer.transformToDragAndZoomPosition(clickPosition, this.translationFactor,
					this.scaleFactor);
			clickPosition.divide(this.scaleFactor);
			clickPosition.substract(this.translationFactor);
			Position intersectionPosition;
			Intersection selectedIntersection = null;
			float distance;
			float distanceMin = 15;

			for (Intersection intersection : dataModel.getCityMap().getIntersections()) {
				intersectionPosition = transformer.transformToDragAndZoomPosition(intersection.getCoordinates(),
						this.translationFactor, this.scaleFactor);
				distance = clickPosition.distanceTo(intersectionPosition);
				if (distance < distanceMin) {
					distanceMin = distance;
					selectedIntersection = intersection;
				}
			}

			if (selectedIntersection != null) {
				Delivery selectedDelivery = getDeliveryAt(selectedIntersection);
				if (selectedDelivery == null) {
					this.dataModel.setSelectedIntersection(selectedIntersection);
				} else {
					this.dataModel.setSelectedDelivery(selectedDelivery);
				}
				drawCanvas();
			}
		}
	}
	
	private Delivery getDeliveryAt(Intersection selectedIntersection) {
		for (Tour tour : dataModel.getTours()) {
			for (Delivery delivery : tour.getDeliveriesList()) {
				if (delivery.getLocation().equals(selectedIntersection)) {
					return delivery;
				}
			}
		}
		return null;
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
		drawCanvas();
	}

	@FXML
	private void moveOnDrag(MouseEvent event) {
		this.prevTranslationFactor = this.translationFactor.copy();
		float xFactor = (float) ((event.getX() - lastClickX) / this.scaleFactor);
		float yFactor = (float) ((event.getY() - lastClickY) / this.scaleFactor);
		this.translationFactor.setX(this.translationFactor.getX() + xFactor);
		this.translationFactor.setY(this.translationFactor.getY() + yFactor);
		drawCanvas();
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


	private void onCityMapUpdate(ObservableValue<? extends CityMap> observable, CityMap oldValue, CityMap newValue) {
		transformer = new CoordinateTransformer(dataModel.getCityMap().getNorthWestMostCoordinates(),
				dataModel.getCityMap().getSouthEastMostCoordinates(), (float) canvasContainer.getWidth(),
				(float) canvasContainer.getHeight());
		this.prevScaleFactor = this.scaleFactor;
		this.scaleFactor = 1;
		this.prevTranslationFactor = this.translationFactor.copy();
		this.translationFactor = new Position(0f, 0f);
		drawCanvas();
		this.parentController.displayToolBarMessage("Map loaded successfully");
	}

	private void onSelectedDeliveryUpdate(ObservableValue<? extends Delivery> observable, Delivery oldValue,
			Delivery newValue) {
		// TODO show the delivery on the map
		System.out.println("Changement de delivery");
		drawCanvas();
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
		drawCanvas();
	}

}
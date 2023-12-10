package fr.insalyon.controller.command;

import fr.insalyon.algorithm.CityMapMatrix;
import fr.insalyon.algorithm.TSP1;
import fr.insalyon.model.Delivery;
import fr.insalyon.model.Path;
import fr.insalyon.model.Tour;

import java.util.ArrayList;
import java.util.List;

public class AddDeliveryCommand implements Command {

	private final CityMapMatrix cityMapMatrix;

	private final Delivery delivery;

	private final Tour tour;

	public AddDeliveryCommand(CityMapMatrix cityMapMatrix, Tour tour, Delivery delivery) {
		this.cityMapMatrix = cityMapMatrix;
		this.tour = tour;
		this.delivery = delivery;
	}

	@Override
	public void doCommand() {
		this.cityMapMatrix.addDelivery(this.delivery);
		updateTour();
	}

	@Override
	public void undoCommand() {
		this.cityMapMatrix.removeDelivery(this.delivery);
		updateTour();
	}

	private void updateTour() {
		TSP1 tsp1 = new TSP1();
		tsp1.searchSolution(5000, cityMapMatrix.getGraph());
		Integer[] solution = tsp1.getBestSol();

		List<Delivery> deliveries = new ArrayList<>(solution.length - 1);
		List<Path> paths = new ArrayList<>(solution.length - 1);

		deliveries.add(this.cityMapMatrix.getGraph().getDelivery(0));
		for (int i = 1; i < solution.length; i++) {
			deliveries.add(this.cityMapMatrix.getGraph().getDelivery(i));
			paths.add(this.cityMapMatrix.getGraph().getCost()[i - 1][i]);
		}

		this.tour.setPathList(paths);
		this.tour.getDeliveriesList().clear();
		this.tour.getDeliveriesList().addAll(deliveries);
	}
}

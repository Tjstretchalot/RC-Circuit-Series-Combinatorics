package me.timothy.physics.rc.gui;

import java.util.List;
import java.util.stream.Collectors;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import me.timothy.physics.rc.Capacitor;
import me.timothy.physics.rc.Circuit;
import me.timothy.physics.rc.RCCircuitGenerator;
import me.timothy.physics.rc.Resistor;

/**
 * Entry class to the program. Provides a graphical interface for the
 * circuit generator.
 * 
 * @author Timothy Moore
 */
public class RCCircuitGeneratorMain extends Application {
	private ObservableList<Resistor> resistors;
	private ObservableList<Capacitor> capacitors;
	private ObservableList<Circuit> circuits;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		resistors = FXCollections.observableArrayList();
		capacitors = FXCollections.observableArrayList();
		circuits = FXCollections.observableArrayList();
		
		initListContents();
		
		primaryStage.setTitle("RC Circuit Series Combinatrics");
		
		VBox primaryVBox = new VBox(20);
		primaryVBox.setPadding(new Insets(25, 25, 25, 25));
		HBox allowedResistorsAndCapacitors = new HBox(50);
		allowedResistorsAndCapacitors.setAlignment(Pos.CENTER);
		
		VBox allowedResistors = new VBox(10);
		Text allowedResistorsTitle = new Text("Available Resistors");
		allowedResistorsTitle.setFont(Font.font("Helvetica", FontWeight.BOLD, 12));
		allowedResistors.getChildren().add(allowedResistorsTitle);
		final ListView<Resistor> resistorsView = new ListView<>();
		resistorsView.setItems(resistors);
		resistorsView.setPrefHeight(100);
		allowedResistors.getChildren().add(resistorsView);
		HBox allowedResistorsAddRemove = new HBox(20);
		HBox allowedResistorsAdd = new HBox(10);
		final TextField resistorsAddField = new TextField();
		resistorsAddField.setTextFormatter(getNumberFormatter(false));
		allowedResistorsAdd.getChildren().add(resistorsAddField);
		Button resistorsAddButton = new Button("Add");
		resistorsAddButton.setOnAction(e -> {
			if(resistorsAddField.getText().length() != 0) {
				final int newResistanceOhms = Integer.valueOf(resistorsAddField.getText());
				if(!resistors.stream().anyMatch(r -> r.getResistanceOhms() == newResistanceOhms)) {
					resistors.add(new Resistor(newResistanceOhms));
					resistorsAddField.setText("");
				}
			}
		});
		allowedResistorsAdd.getChildren().add(resistorsAddButton);
		allowedResistorsAddRemove.getChildren().add(allowedResistorsAdd);
		Button resistorsRemoveButton = new Button("Remove");
		resistorsRemoveButton.setOnAction(e -> {
			if(!resistorsView.getSelectionModel().isEmpty()) {
				resistors.removeAll(resistorsView.getSelectionModel().getSelectedItems());
			}
		});
		allowedResistorsAddRemove.getChildren().add(resistorsRemoveButton);
		allowedResistors.getChildren().add(allowedResistorsAddRemove);
		allowedResistorsAndCapacitors.getChildren().add(allowedResistors);
		
		VBox allowedCapacitors = new VBox(10);
		Text allowedCapacitorsTitle = new Text("Available Capacitors");
		allowedCapacitorsTitle.setFont(Font.font("Helvetica", FontWeight.BOLD, 12));
		allowedCapacitors.getChildren().add(allowedCapacitorsTitle);
		final ListView<Capacitor> capacitorsView = new ListView<>();
		capacitorsView.setItems(capacitors);
		capacitorsView.setPrefHeight(100);
		allowedCapacitors.getChildren().add(capacitorsView);
		HBox allowedCapacitorsAddRemove = new HBox(20);
		HBox allowedCapacitorsAdd = new HBox(10);
		final TextField capacitorsAddField = new TextField();
		capacitorsAddField.setTextFormatter(getNumberFormatter(false));
		allowedCapacitorsAdd.getChildren().add(capacitorsAddField);
		Button capacitorsAddButton = new Button("Add");
		capacitorsAddButton.setOnAction(e -> {
			if(capacitorsAddField.getText().length() > 0) {
				int newCapac = Integer.valueOf(capacitorsAddField.getText());
				if(!capacitors.stream().anyMatch(c -> c.getCapacitanceMicroFarads() == newCapac)) {
					capacitors.add(new Capacitor(newCapac));
					capacitorsAddField.setText("");
				}
			}
		});
		allowedCapacitorsAdd.getChildren().add(capacitorsAddButton);
		allowedCapacitorsAddRemove.getChildren().add(allowedCapacitorsAdd);
		Button capacitorsRemoveButton = new Button("Remove");
		capacitorsRemoveButton.setOnAction(e -> {
			if(!capacitorsView.getSelectionModel().isEmpty()) {
				capacitors.removeAll(capacitorsView.getSelectionModel().getSelectedItems());
			}
		});
		allowedCapacitorsAddRemove.getChildren().add(capacitorsRemoveButton);
		allowedCapacitors.getChildren().add(allowedCapacitorsAddRemove);
		allowedResistorsAndCapacitors.getChildren().add(allowedCapacitors);
		
		primaryVBox.getChildren().add(allowedResistorsAndCapacitors);
		
		GridPane settingsGrid = new GridPane();
		settingsGrid.setAlignment(Pos.CENTER);
		settingsGrid.setHgap(10);
		settingsGrid.setVgap(10);
		settingsGrid.setPadding(new Insets(25, 0, 25, 0));
		
		Text resistorsText = new Text("Resistors:");
		settingsGrid.add(resistorsText, 0, 0);
		
		final TextField resistorsField = new TextField("3");
		resistorsField.setTextFormatter(getNumberFormatter(false));
		settingsGrid.add(resistorsField, 1, 0);
		
		Text capacitorsText = new Text("Capacitors:");
		settingsGrid.add(capacitorsText, 0, 1);
		
		final TextField capacitorsField = new TextField("1");
		capacitorsField.setTextFormatter(getNumberFormatter(false));
		settingsGrid.add(capacitorsField, 1, 1);
		
		Text tauText = new Text("Time Constant:");
		settingsGrid.add(tauText, 2, 0);
		
		final TextField tauField = new TextField();
		tauField.setTextFormatter(getNumberFormatter(true));
		settingsGrid.add(tauField, 3, 0);
		
		Text tauTolerance = new Text("Time Constant Tolerance:");
		settingsGrid.add(tauTolerance, 2, 1);
		
		final TextField tauToleranceField = new TextField();
		tauToleranceField.setTextFormatter(getNumberFormatter(true));
		settingsGrid.add(tauToleranceField, 3, 1);
		
		Button calculate = new Button("Calculate");
		calculate.setOnAction(e -> calculate(
				Integer.valueOf(resistorsField.getText()),
				Integer.valueOf(capacitorsField.getText()),
				tauField.getText().length() > 0 ? Double.valueOf(tauField.getText()) : null,
				tauToleranceField.getText().length() > 0 ? Double.valueOf(tauToleranceField.getText()) : null
				));
		
		HBox calcBox = new HBox();
		calcBox.setAlignment(Pos.CENTER);
		calcBox.getChildren().add(calculate);
		settingsGrid.add(calcBox, 4, 0, 1, 2);
		primaryVBox.getChildren().add(settingsGrid);
		
		VBox circuitsBox = new VBox(10);
		Text circuitsLabel = new Text("Circuits");
		circuitsLabel.setFont(Font.font("Helvetica", FontWeight.BOLD, 12));
		circuitsBox.getChildren().add(circuitsLabel);
		
		TableView<Circuit> circuitsView = new TableView<>();
		circuitsView.setPrefWidth(1000);
		circuitsView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		TableColumn<Circuit, String> resistorsColumn = new TableColumn<>("Resistors (Ohms)");
		resistorsColumn.setCellValueFactory(circuit -> {
			return new SimpleStringProperty(circuit.getValue().resistors.stream().map(r -> r.toString()).collect(Collectors.joining(", ")));
		});
		circuitsView.getColumns().add(resistorsColumn);
		TableColumn<Circuit, String> capacitorsColumn = new TableColumn<>("Capacitors (uF)");
		capacitorsColumn.setCellValueFactory(circuit -> {
			return new SimpleStringProperty(circuit.getValue().capacitors.stream().map(c -> c.toString()).collect(Collectors.joining(", ")));
		});
		circuitsView.getColumns().add(capacitorsColumn);
		TableColumn<Circuit, String> effectiveResistanceColumn = new TableColumn<>("Effective Resistance (Ohms)");
		effectiveResistanceColumn.setCellValueFactory(circuit -> {
			return new SimpleStringProperty(circuit.getValue().getEffectiveResistanceOhms() + "");
		});
		circuitsView.getColumns().add(effectiveResistanceColumn);
		TableColumn<Circuit, String> effectiveCapacitanceColumn = new TableColumn<>("Effective Capacitance (uF)");
		effectiveCapacitanceColumn.setCellValueFactory(circuit -> {
			return new SimpleStringProperty(circuit.getValue().getEffectiveCapacitanceMicroFarads() + "");
		});
		circuitsView.getColumns().add(effectiveCapacitanceColumn);
		TableColumn<Circuit, String> timeConstantColumn = new TableColumn<>("Time Constant (s)");
		timeConstantColumn.setCellValueFactory(circuit -> {
			return new SimpleStringProperty(String.format("%1$,.8f", circuit.getValue().getTimeConstant()));
		});
		circuitsView.getColumns().add(timeConstantColumn);
		circuitsView.setItems(circuits);
		circuitsBox.getChildren().add(circuitsView);
		primaryVBox.getChildren().add(circuitsBox);
		
		Scene scene = new Scene(primaryVBox);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	/**
	 * Provides some reasonable defaults
	 */
	private void initListContents() {
		resistors.add(new Resistor(25));
		resistors.add(new Resistor(50));
		resistors.add(new Resistor(100));
		resistors.add(new Resistor(200));
		resistors.add(new Resistor(250));
		
		capacitors.add(new Capacitor(100));
		capacitors.add(new Capacitor(250));
		capacitors.add(new Capacitor(450));
	}

	private TextFormatter<String> getNumberFormatter(boolean allowPeriod) {
		return new TextFormatter<>(change -> {
			String text = change.getText();
			
			boolean foundPeriod = false;
			for(int i = 0; i < text.length(); i++) {
				if(!Character.isDigit(text.charAt(i))) {
					if(foundPeriod) {
						return null;
					}
						
					
					if(text.charAt(i) == '.' && allowPeriod) {
						foundPeriod = true;
					}else {
						return null;
					}
				}
			}
			
			if(foundPeriod) {
				if(change.getControlText().indexOf('.') >= 0) 
					return null;
			}
			
			return change;
		});
	}
	
	public void calculate(int numResis, int numCapac, Double tau, Double tauTolerance) {
		System.out.printf("calculate(%d, %d, %s, %s)\n", numResis, numCapac, tau == null ? null : tau.toString(), 
				tauTolerance == null ? null : tauTolerance.toString());
		RCCircuitGenerator generator = new RCCircuitGenerator();
		generator.setResistors(resistors.stream().collect(Collectors.toList()));
		generator.setCapacitors(capacitors.stream().collect(Collectors.toList()));
		
		List<Circuit> newCircuits = generator.getAllPossibleCircuits(numResis, numCapac);
		if(tau != null) {
			newCircuits = RCCircuitGenerator.filterCircuitsByTimeConstant(newCircuits, tau.doubleValue(), tauTolerance == null ? 0 : tauTolerance.doubleValue());
		}
		
		circuits.clear();
		circuits.addAll(newCircuits);
		System.out.printf("Got %d circuits\n", circuits.size());
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}

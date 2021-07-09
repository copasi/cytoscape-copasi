package org.copasi.cytoscape.internal.tasks;

import java.awt.Color;
import java.awt.Paint;
import java.util.Arrays;
import java.util.List;

import org.cytoscape.model.CyColumn;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNetworkFactory;
import org.cytoscape.model.CyNetworkManager;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyTable;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.ProvidesTitle;
import org.cytoscape.work.TaskFactory;
import org.cytoscape.work.TaskIterator;
import org.cytoscape.work.TaskMonitor;
import org.cytoscape.work.Tunable;

import org.COPASI.*;

import java.lang.System;

public class MyAppTask extends AbstractTask {
	final CyNetworkManager netManager;
	final CyNetworkFactory netFactory;

	public MyAppTask(CyNetworkManager networkManager, CyNetworkFactory networkFactory) {
		super();
		netManager = networkManager;
		netFactory = networkFactory;
	}

	public void run(TaskMonitor monitor) {
		CyNetwork myNetwork = netFactory.createNetwork();
		CyNode node1 = myNetwork.addNode();
		CyNode node2 = myNetwork.addNode();
		CyEdge edge1 = myNetwork.addEdge(node1, node2, false);

		myNetwork.getRow(node1).set(CyNetwork.NAME, "Node1");
		myNetwork.getRow(node2).set(CyNetwork.NAME, "Node2");
		myNetwork.getRow(edge1).set(CyNetwork.NAME, "Edge2");
		myNetwork.getRow(myNetwork).set(CyNetwork.NAME, "NetworkName");

		// Create our new columns
		CyTable nodeTable = myNetwork.getDefaultNodeTable();
		if (nodeTable.getColumn("Hello") == null) {
			nodeTable.createListColumn("Hello", String.class, false);
		}
		if (nodeTable.getColumn("World") == null) {
			nodeTable.createColumn("World", Double.class, false);
		}

		// Now, add the data
		myNetwork.getRow(node1).set("Hello", Arrays.asList("One", "Two", "Three"));
		// nodeTable.getRow(node1.getSUID()).set("Hello",Arrays.asList("One", "Two",
		// "Three"));
		myNetwork.getRow(node2).set("Hello", Arrays.asList("Four", "Five", "Six"));
		// nodeTable.getRow(node2.getSUID()).set("Hello",Arrays.asList("One", "Two",
		// "Three"));

		myNetwork.getRow(node1).set("World", 1.0);
		myNetwork.getRow(node2).set("World", 2.0);

		CyNode node3 = myNetwork.addNode();
		myNetwork.getRow(node3).set(CyNetwork.NAME, "COPASI");
		myNetwork.getRow(node3).set("Hello", Arrays.asList(CVersion.getVERSION().getVersion()));

		CyNode node4 = myNetwork.addNode();
		myNetwork.getRow(node4).set(CyNetwork.NAME, "JLP");
		myNetwork.getRow(node4).set("Hello", Arrays.asList(System.getProperty("java.library.path").split(";")));

		netManager.addNetwork(myNetwork);
		monitor.setStatusMessage("Added network");
	}

	@ProvidesTitle
	public String getTitle() {
		return "MyApp Task";
	}

}

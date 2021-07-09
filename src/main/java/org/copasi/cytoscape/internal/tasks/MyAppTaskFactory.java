package org.copasi.cytoscape.internal.tasks;

import org.cytoscape.model.CyNetworkFactory;
import org.cytoscape.model.CyNetworkManager;
import org.cytoscape.model.CyNetworkManager;
import org.cytoscape.work.AbstractTaskFactory;
import org.cytoscape.work.TaskFactory;
import org.cytoscape.work.TaskIterator;

public class MyAppTaskFactory extends AbstractTaskFactory {
	final CyNetworkManager netManager;
	final CyNetworkFactory netFactory;

	public MyAppTaskFactory(CyNetworkManager networkManager, CyNetworkFactory networkFactory) {
		super();
		netManager = networkManager;
		netFactory = networkFactory;
	}

	public TaskIterator createTaskIterator() {
		return new TaskIterator(new MyAppTask(netManager, netFactory));
	}

	public boolean isReady() {
		return true;
	}
}

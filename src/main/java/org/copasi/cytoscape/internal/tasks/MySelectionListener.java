package org.copasi.cytoscape.internal.tasks;

import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.model.CyIdentifiable;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.events.RowsSetEvent;
import org.cytoscape.model.events.RowsSetListener;
import org.cytoscape.model.events.RowSetRecord;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.model.View;
import org.cytoscape.view.presentation.property.BasicVisualLexicon;
import org.cytoscape.view.presentation.property.NodeShapeVisualProperty;
import org.cytoscape.view.presentation.property.values.NodeShape;

public class MySelectionListener implements RowsSetListener {
	CyApplicationManager manager;
	NodeShape shapes[] = { NodeShapeVisualProperty.DIAMOND, NodeShapeVisualProperty.ELLIPSE,
			NodeShapeVisualProperty.HEXAGON, NodeShapeVisualProperty.OCTAGON, NodeShapeVisualProperty.PARALLELOGRAM,
			NodeShapeVisualProperty.RECTANGLE, NodeShapeVisualProperty.ROUND_RECTANGLE,
			NodeShapeVisualProperty.TRIANGLE };
	int shapeCount;

	public MySelectionListener(CyApplicationManager mgr) {
		manager = mgr;
		shapeCount = 0;
	}

	public void handleEvent(RowsSetEvent event) {
		// First see if this even has anything to do with selections
		if (!event.containsColumn(CyNetwork.SELECTED)) {
			// Nope, we're done
			return;
		}

		// For each selected node, get the view in the current network
		// and change the shape
		CyNetworkView currentNetworkView = manager.getCurrentNetworkView();
		CyNetwork currentNetwork = currentNetworkView.getModel();
		if (event.getSource() != currentNetwork.getDefaultNodeTable())
			return;

		for (RowSetRecord record : event.getColumnRecords(CyNetwork.SELECTED)) {
			Long suid = record.getRow().get(CyIdentifiable.SUID, Long.class);
			Boolean value = (Boolean) record.getValue();
			if (value.equals(Boolean.TRUE)) {
				CyNode node = currentNetwork.getNode(suid);
				View<CyNode> nodeView = currentNetworkView.getNodeView(node);
				// Now change the shape
				NodeShape newShape = shapes[shapeCount++];
				if (shapeCount >= shapes.length)
					shapeCount = 0;
				nodeView.setLockedValue(BasicVisualLexicon.NODE_SHAPE, newShape);
			}
		}
		currentNetworkView.updateView();
	}
}

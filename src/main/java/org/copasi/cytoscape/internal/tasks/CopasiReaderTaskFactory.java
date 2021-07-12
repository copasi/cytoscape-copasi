package org.copasi.cytoscape.internal.tasks;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.cytoscape.io.CyFileFilter;
import org.cytoscape.io.read.AbstractInputStreamTaskFactory;
import org.cytoscape.model.CyNetworkFactory;
import org.cytoscape.view.layout.CyLayoutAlgorithmManager;
import org.cytoscape.view.model.CyNetworkViewFactory;
import org.cytoscape.work.TaskIterator;

/**
 * SBMLReaderTaskFactory class
 * TaskFactory for the SBMLReaderTask.
 */
public class CopasiReaderTaskFactory extends AbstractInputStreamTaskFactory {

	CyNetworkFactory networkFactory; 
	CyNetworkViewFactory viewFactory;
	CyLayoutAlgorithmManager cyLayoutAlgorithmManager;

	/** Constructor. */
	public CopasiReaderTaskFactory(CyFileFilter filter,CyNetworkFactory networkFactory, CyNetworkViewFactory viewFactory, CyLayoutAlgorithmManager cyLayoutAlgorithmManager){
		super(filter);
		this.networkFactory = networkFactory;
		this.viewFactory = viewFactory;
		this.cyLayoutAlgorithmManager = cyLayoutAlgorithmManager;
	}
	
    /** Copy InputStream. */
    public static InputStream copyInputStream(InputStream is) throws IOException {
        ByteArrayOutputStream copy = new ByteArrayOutputStream();
        int chunk = 0;
        byte[] data = new byte[1024*1024];
        while((-1 != (chunk = is.read(data)))) {
            copy.write(data, 0, chunk);
        }
        is.close();
        return new ByteArrayInputStream( copy.toByteArray() );
    }


	@Override
	public TaskIterator createTaskIterator(InputStream is, String inputName) {		

		try {
			return new TaskIterator(
				new CopasiFileReaderTask(copyInputStream(is), inputName,
                        networkFactory, viewFactory, cyLayoutAlgorithmManager)
			);
		} catch (IOException e) {
			e.printStackTrace();
            return null;
		}
	}
}

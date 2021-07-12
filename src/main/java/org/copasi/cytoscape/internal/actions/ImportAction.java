package org.copasi.cytoscape.internal.actions;

import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Collection;
import java.util.HashSet;

import org.cytoscape.application.swing.AbstractCyAction;
import org.cytoscape.application.swing.CySwingApplication;
import org.cytoscape.task.read.LoadNetworkFileTaskFactory;
import org.cytoscape.util.swing.FileChooserFilter;
import org.cytoscape.util.swing.FileUtil;
import org.cytoscape.work.SynchronousTaskManager;
import org.cytoscape.work.TaskIterator;

/**
 * Importing SBML networks..
 */
public class ImportAction extends AbstractCyAction{
	private static final long serialVersionUID = 1L;
	
    CySwingApplication cySwingApplication;
    FileUtil fileUtil;

    LoadNetworkFileTaskFactory loadNetworkFileTaskFactory;
    @SuppressWarnings("rawtypes")
    SynchronousTaskManager synchronousTaskManager;

    

	public ImportAction(CySwingApplication cySwingApplication,
						FileUtil fileUtil,
						LoadNetworkFileTaskFactory loadNetworkFileTaskFactory,
						@SuppressWarnings("rawtypes")
    					SynchronousTaskManager synchronousTaskManager)
	{
		super(ImportAction.class.getSimpleName());

		this.cySwingApplication = cySwingApplication;
		this.fileUtil = fileUtil;
		this.loadNetworkFileTaskFactory = loadNetworkFileTaskFactory;
		this.synchronousTaskManager = synchronousTaskManager;
		
		
		
		this.inToolBar = false;
		this.inMenuBar = true;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		// open new file open dialog
		Collection<FileChooserFilter> filters = new HashSet<>();
		String[] extensions = {"", "xml", "cps"};
		filters.add(new FileChooserFilter("COPASI files (*, *.xml, *.cps)", extensions));
	
		File[] files = fileUtil.getFiles(cySwingApplication.getJFrame(), 
				"Open COPASI file", FileDialog.LOAD, filters);
		
		if ((files != null) && (files.length != 0)) 
		{
			for (int i = 0; i < files.length; i++) 
			{
				TaskIterator iterator = loadNetworkFileTaskFactory.createTaskIterator(files[i]);
				synchronousTaskManager.execute(iterator);
			}
		}
	}

}

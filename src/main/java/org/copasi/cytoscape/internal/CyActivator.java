package org.copasi.cytoscape.internal;

import java.util.Properties;

import org.cytoscape.application.swing.CyAction;
import org.cytoscape.application.swing.CySwingApplication;
import org.cytoscape.io.util.StreamUtil;
import org.cytoscape.model.CyNetworkFactory;
import org.cytoscape.service.util.AbstractCyActivator;
import org.cytoscape.task.read.LoadNetworkFileTaskFactory;
import org.cytoscape.util.swing.FileUtil;
import org.cytoscape.view.layout.CyLayoutAlgorithmManager;
import org.cytoscape.view.model.CyNetworkViewFactory;
import org.cytoscape.work.ServiceProperties;
import org.cytoscape.work.SynchronousTaskManager;
import org.osgi.framework.BundleContext;
import org.copasi.cytoscape.internal.actions.ImportAction;
import org.copasi.cytoscape.internal.tasks.CopasiReaderTaskFactory;

/**
 * {@code CyActivator} is a class that is a starting point for OSGi bundles.
 * 
 * A quick overview of OSGi: The common currency of OSGi is the <i>service</i>.
 * A service is merely a Java interface, along with objects that implement the
 * interface. OSGi establishes a system of <i>bundles</i>. Most bundles import
 * services. Some bundles export services. Some do both. When a bundle exports a
 * service, it provides an implementation to the service's interface. Bundles
 * import a service by asking OSGi for an implementation. The implementation is
 * provided by some other bundle.
 * 
 * When OSGi starts your bundle, it will invoke {@CyActivator}'s
 * {@code start} method. So, the {@code start} method is where
 * you put in all your code that sets up your app. This is where you import and
 * export services.
 * 
 * Your bundle's {@code Bundle-Activator} manifest entry has a fully-qualified
 * path to this class. It's not necessary to inherit from
 * {@code AbstractCyActivator}. However, we provide this class as a convenience
 * to make it easier to work with OSGi.
 *
 * Note: AbstractCyActivator already provides its own {@code stop} method, which
 * {@code unget}s any services we fetch using getService().
 */
public class CyActivator extends AbstractCyActivator {
	/**
	 * This is the {@code start} method, which sets up your app. The
	 * {@code BundleContext} object allows you to communicate with the OSGi
	 * environment. You use {@code BundleContext} to import services or ask OSGi
	 * about the status of some service.
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		// Get the services we're going to want to use
		CyNetworkFactory networkFactory = getService(context, CyNetworkFactory.class);
        CySwingApplication cySwingApplication = getService(context, CySwingApplication.class);
        CyNetworkViewFactory cyNetworkViewFactory = getService(context, CyNetworkViewFactory.class);
        CyLayoutAlgorithmManager cyLayoutAlgorithmManager = getService(context, CyLayoutAlgorithmManager.class);
        FileUtil fileUtil = getService(context, FileUtil.class);
        StreamUtil streamUtil = getService(context, StreamUtil.class);
        LoadNetworkFileTaskFactory loadNetworkFileTaskFactory = getService(context, LoadNetworkFileTaskFactory.class);
        @SuppressWarnings("rawtypes")
        SynchronousTaskManager synchronousTaskManager = getService(context, SynchronousTaskManager.class);


		// Configure the service properties first.
		Properties properties = new Properties();
		properties.put(ServiceProperties.PREFERRED_MENU,
			"Apps.COPASI");
		properties.put(ServiceProperties.TITLE, "Import COPASI file");
			
        ImportAction importAction = new ImportAction(cySwingApplication, fileUtil, loadNetworkFileTaskFactory, synchronousTaskManager);
        registerService(context, importAction, CyAction.class, properties);
        
        // COPASI reader 
        CopasiFileFilter copasiFilter = new CopasiFileFilter(streamUtil);
        CopasiReaderTaskFactory copasiReaderTaskFactory = new CopasiReaderTaskFactory(copasiFilter, networkFactory, cyNetworkViewFactory, cyLayoutAlgorithmManager);
        Properties copasiReaderProps = new Properties();
        copasiReaderProps.setProperty("readerDescription", "COPASI file reader (copasi)");
        copasiReaderProps.setProperty("readerId", "copasiNetworkReader");
        registerAllServices(context, copasiReaderTaskFactory, copasiReaderProps);

		
	}
}

package org.copasi.cytoscape.internal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;

import org.cytoscape.io.BasicCyFileFilter;
import org.cytoscape.io.DataCategory;
import org.cytoscape.io.util.StreamUtil;

/**
 * COPASI Filter class.
 * Extends CyFileFilter for integration into the Cytoscape ImportHandler framework.
 */
public class CopasiFileFilter extends BasicCyFileFilter {
	private static final String COPASI_XML_NAMESPACE = "http://www.copasi.org/static/schema";
	private static final int DEFAULT_LINES_TO_CHECK = 5;


	/**
	 * Constructor.
	 */
	public CopasiFileFilter(StreamUtil streamUtil) {
		super(
				new String[] { "xml", "cps", ""},
				new String[] { "text/xml", "application/rdf+xml", "application/xml", "application/x-copasi", "text/plain", "text/copasi", "text/copasi+xml" },
				"COPASI network reader",
				DataCategory.NETWORK,
				streamUtil
		);
	}

	/**
	 * Indicates which URI the SBMLFileFilter accepts.
	 */
	@Override
	public boolean accepts(URI uri, DataCategory category) {
		if (!category.equals(DataCategory.NETWORK)) {
			return false;
		}

		try {
			return accepts(streamUtil.getInputStream(uri.toURL()), category);
		} catch (IOException e){
			return false;
		}
	}

    /**
     * Indicates which streams the CopasiFileFilter accepts.
     */
	@Override
	public boolean accepts(InputStream stream, DataCategory category) {
		if (!category.equals(DataCategory.NETWORK)) {
			return false;
		}
		try {
			return checkHeader(stream);
		} catch (IOException e) {
			return false;
		}
	}

	/**
	 * Checks if the header contains the COPASI namespace definition.
     */
	private boolean checkHeader(InputStream stream) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		int linesToCheck = DEFAULT_LINES_TO_CHECK;
		while (linesToCheck > 0) {
			String line = reader.readLine();
			if (line != null && line.contains(COPASI_XML_NAMESPACE)) {
				return true;
			}
			linesToCheck--;
		}
		return false;
	}

}

/**
 * Copyright (C) 2001-2018 by RapidMiner and the contributors
 * 
 * Complete list of developers available at our web site:
 * 
 * http://rapidminer.com
 * 
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU Affero General Public License as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License along with this program.
 * If not, see http://www.gnu.org/licenses/.
*/
package com.rapidminer.operator.meta;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.rapidminer.Process;
import com.rapidminer.RapidMiner;
import com.rapidminer.io.process.XMLTools;
import com.rapidminer.operator.Operator;
import com.rapidminer.operator.ResultObjectAdapter;
import com.rapidminer.operator.performance.PerformanceVector;
import com.rapidminer.tools.Tools;


/**
 * A set of parameters generated by a <code>ParameterOptimizationOperator</code>.
 * 
 * @author Simon Fischer, Ingo Mierswa
 */
public class ParameterSet extends ResultObjectAdapter implements Iterable<ParameterValue> {

	private static final long serialVersionUID = -2615523039124583537L;

	private final List<ParameterValue> parameterValues = new ArrayList<>();

	private PerformanceVector performance;

	/** Private constructor for the read() method. */
	private ParameterSet() {}

	/**
	 * Constructs a new ParameterSet. The three arrays must have equal length. For each <i>i</i>,
	 * the ParameterSet specifies the value <code>values[i]</code> for the parameter
	 * <code>parameters[i]</code> of the operator named <code>operators[i]</code>.
	 */
	public ParameterSet(Operator[] operators, String[] parameters, String[] values, PerformanceVector value) {
		this(Arrays.stream(operators).map(Operator::getName).toArray(String[]::new), parameters, values, value);
	}

	/**
	 * Constructs a new ParameterSet. The three arrays must have equal length. For each <i>i</i>,
	 * the ParameterSet specifies the value <code>values[i]</code> for the parameter
	 * <code>parameters[i]</code> of the operator named <code>operators[i]</code>.
	 *
	 * @since 8.0
	 */
	public ParameterSet(String[] operatorNames, String[] parameters, String[] values, PerformanceVector performance) {
		if (operatorNames.length != parameters.length || operatorNames.length != values.length) {
			throw new IllegalArgumentException("The arrays operators, parameters, and values must be of equal size!");
		}
		for (int i = 0; i < operatorNames.length; i++) {
			parameterValues.add(new ParameterValue(operatorNames[i], parameters[i], values[i]));
		}
		this.performance = performance;
	}

	/**
	 * Associate a {@link PerformanceVector} with an existing {@link ParameterSet}.
	 * 
	 * @since 8.0
	 */
	public ParameterSet(ParameterSet set, PerformanceVector performance) {
		this.parameterValues.addAll(set.parameterValues);
		this.performance = performance;
	}

	@Override
	public String getName() {
		return "ParameterSet";
	}

	@Override
	public String toString() {
		StringBuilder str = new StringBuilder("Parameter set:");
		str.append(Tools.getLineSeparator());
		if (performance != null) {
			str.append(Tools.getLineSeparator());
			str.append("Performance: ");
			str.append(performance);
			str.append(Tools.getLineSeparator());
		}
		parameterValues.forEach(pv -> str.append(pv + Tools.getLineSeparator()));
		return str.toString();
	}

	/**
	 * Returns the performance of this parameter set. Attension: This may be null if the
	 * ParameterSet was read from a file.
	 */
	public PerformanceVector getPerformance() {
		return performance;
	}

	/**
	 * Applies all parameters in the set to their operators. The entries in the nameMap can be used
	 * if the names of the operators in two processes (one parameter optimization process and one
	 * application process) are not the same. Each entry in nameMap maps the name read from the file
	 * to the name of the operator in the process definition.
	 */
	public void applyAll(Process process, Map<String, String> nameTranslation) {
		parameterValues.forEach(pv -> pv.apply(process, nameTranslation));
	}

	public void save(File file) throws IOException {
		try (FileWriter fw = new FileWriter(file); PrintWriter out = new PrintWriter(fw)) {
			writeParameterSet(out, Tools.getDefaultEncoding());
		}
	}

	public String getExtension() {
		return "par";
	}

	public String getFileDescription() {
		return "parameter set file";
	}

	public Iterator<ParameterValue> getParameterValues() {
		return this.parameterValues.iterator();
	}

	/**
	 * @since 8.0
	 */
	@Override
	public Iterator<ParameterValue> iterator() {
		return this.parameterValues.iterator();
	}

	/**
	 * Returns the number of parameter values.
	 *
	 * @since 8.0
	 */
	public int size() {
		return parameterValues.size();
	}

	public void writeParameterSet(PrintWriter out, Charset encoding) {
		out.println("<?xml version=\"1.0\" encoding=\"" + encoding + "\"?>");
		out.println("<parameterset version=\"" + RapidMiner.getShortVersion() + "\">");
		parameterValues.forEach(pv -> out.println("    <parameter operator=\"" + pv.getOperator() + "\" key=\""
				+ pv.getParameterKey() + "\" value=\"" + pv.getParameterValue() + "\"/>"));
		out.println("</parameterset>");
	}

	/** Reads a parameter set from a file. */
	public static ParameterSet readParameterSet(InputStream in) throws IOException {
		ParameterSet parameterSet = new ParameterSet();
		Document document = null;
		try {
			document = XMLTools.createDocumentBuilder().parse(in);
		} catch (SAXException e) {
			throw new IOException(e.getMessage());
		}

		Element parametersElement = document.getDocumentElement();
		if (!parametersElement.getTagName().equals("parameterset")) {
			throw new IOException("Outer tag of parameter set file must be <parameterset>");
		}

		NodeList parameters = parametersElement.getChildNodes();
		for (int i = 0; i < parameters.getLength(); i++) {
			Node node = parameters.item(i);
			if (node instanceof Element) {
				Element parameterTag = (Element) node;
				String tagName = parameterTag.getTagName();
				if (!tagName.equals("parameter")) {
					throw new IOException("Only tags <parameter> are allowed, was " + tagName);
				}
				String operatorName = parameterTag.getAttribute("operator");
				String parameterKey = parameterTag.getAttribute("key");
				String parameterValue = parameterTag.getAttribute("value");
				parameterSet.parameterValues.add(new ParameterValue(operatorName, parameterKey, parameterValue));
			}
		}
		return parameterSet;
	}
}

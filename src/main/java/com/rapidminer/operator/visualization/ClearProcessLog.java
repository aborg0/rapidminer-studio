/**
 * Copyright (C) 2001-2017 by RapidMiner and the contributors
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
package com.rapidminer.operator.visualization;

import com.rapidminer.operator.Operator;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.ports.DummyPortPairExtender;
import com.rapidminer.operator.ports.PortPairExtender;
import com.rapidminer.parameter.ParameterType;
import com.rapidminer.parameter.ParameterTypeBoolean;
import com.rapidminer.parameter.ParameterTypeString;

import java.util.List;


/**
 * This operator can be used to clear a data table generated by a {@link ProcessLogOperator}.
 * 
 * @author Ingo Mierswa
 */
public class ClearProcessLog extends Operator {

	public static final String PARAMETER_LOG_NAME = "log_name";

	public static final String PARAMETER_DELETE_TABLE = "delete_table";

	private PortPairExtender dummyPorts = new DummyPortPairExtender("through", getInputPorts(), getOutputPorts());

	public ClearProcessLog(OperatorDescription description) {
		super(description);

		dummyPorts.start();

		getTransformer().addRule(dummyPorts.makePassThroughRule());
	}

	@Override
	public void doWork() throws OperatorException {
		getProcess().clearDataTable(getParameterAsString(PARAMETER_LOG_NAME));
		if (getParameterAsBoolean(PARAMETER_DELETE_TABLE)) {
			getProcess().deleteDataTable(getParameterAsString(PARAMETER_LOG_NAME));
		}

		dummyPorts.passDataThrough();
	}

	@Override
	public List<ParameterType> getParameterTypes() {
		List<ParameterType> types = super.getParameterTypes();

		types.add(new ParameterTypeString(PARAMETER_LOG_NAME, "The name of the log table which should be cleared.", false));
		types.add(new ParameterTypeBoolean(
				PARAMETER_DELETE_TABLE,
				"Indicates if the complete table should be deleted. If this is not checked, only the entries will be deleted.",
				false));

		return types;
	}
}

/**
 * Copyright (C) 2001-2019 by RapidMiner and the contributors
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
package com.rapidminer.operator;

import com.rapidminer.example.ExampleSet;
import com.rapidminer.example.set.HeaderExampleSet;
import com.rapidminer.report.Readable;


/**
 * Model is the interface for all objects which change a data set. For example, a model generated by
 * a learner might add a predicted attribute. Other models can be created during preprocessing, e.g.
 * a transformation model containing the parameters for a z-transformation. Models can be combined
 * by using a {@link GroupedModel}. Please note that all models will automatically wrapped into a
 * ContainerModel if they are not already part of it. All models can be applied with a ModelApplier
 * operator.
 * 
 * @author Ingo Mierswa
 */
public interface Model extends ResultObject, Readable {

	/**
	 * This method has to return the HeaderExampleSet containing the signature of the example set
	 * during training time containing all informations about attributes. This is important for
	 * checking the compatibility of the examples on application time. Note that the AbstractModel
	 * already implements all necessary functionality.
	 */
	public HeaderExampleSet getTrainingHeader();
	
	/**
	 * Applies the model on the given {@link ExampleSet}. Please note that the delivered {@link ExampleSet} might
	 * be the same as the input {@link ExampleSet}. This is, however, not always the case.
	 */
	public ExampleSet apply(ExampleSet testSet) throws OperatorException;

	/**
	 * This method can be used to allow additional parameters. Most models do not support parameters
	 * during application.
	 */
	public void setParameter(String key, Object value) throws OperatorException;

	/**
	 * Returns true if this model is updatable. Please note that only models which return true here
	 * must implement the method {@link #updateModel(ExampleSet)}.
	 */
	public boolean isUpdatable();

	/**
	 * Updates the model according to the given example set. This method might throw an
	 * {@link UserError} if the model is not updatable. In that case the method
	 * {@link #isUpdatable()} should deliver false.
	 */
	public void updateModel(ExampleSet updateExampleSet) throws OperatorException;

}

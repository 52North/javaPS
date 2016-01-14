/**
 * ﻿Copyright (C) 2007 - 2014 52°North Initiative for Geospatial Open Source
 * Software GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.n52.javaps.algorithm;

import java.util.List;
import java.util.Map;

import org.n52.javaps.io.GeneratorFactory;
import org.n52.javaps.io.ParserFactory;
import org.n52.javaps.io.data.IData;

/**
 * @author Bastian Schaeffer, University of Muenster,	Theodor Foerster, ITC
 *
 */
public interface IAlgorithm  {

	Map<String, IData> run(Map<String, List<IData>> inputData) throws Exception;//TODO was ExceptionReport

	List<String> getErrors();

	ProcessDescription getDescription();//TODO could be replaced by descriptor..

	/** Returns some well-known name for the process.
	 *
	 *  @return Returns some well-known name for the process or algorithm
	 *  if that exists, else returns an empty String, never null.
	 *  The fully-qualified class name is gotten via getName();
	 */
	String getWellKnownName();

	/**
	 * Checks if the processDescription complies to the process itself and fits any schema or other dependencies.
	 *
	 * @param version The version of the ProcessDescription to check
	 * @return true if the ProcessDescription is valid, false otherwise
	 */
	boolean processDescriptionIsValid(String version);

	Class< ? > getInputDataType(String id);

	Class< ? > getOutputDataType(String id);

	void setGeneratorFactory(GeneratorFactory generatorFactory);

	void setParserFactory(ParserFactory parserFactory);

}

/*
 * Copyright 2016-2018 52°North Initiative for Geospatial Open Source
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

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.n52.javaps.algorithm.AbstractAlgorithm;
import org.n52.javaps.commons.observerpattern.IObserver;
import org.n52.javaps.commons.observerpattern.ISubject;
import org.n52.javaps.description.TypedComplexInputDescription;
import org.n52.javaps.description.TypedComplexOutputDescription;
import org.n52.javaps.description.TypedLiteralInputDescription;
import org.n52.javaps.description.TypedProcessDescription;
import org.n52.javaps.description.impl.TypedComplexOutputDescriptionImpl;
import org.n52.javaps.description.impl.TypedProcessDescriptionFactory;
import org.n52.javaps.io.InputHandlerRepository;
import org.n52.javaps.io.OutputHandlerRepository;
import org.n52.javaps.io.complex.ComplexData;
import org.n52.javaps.io.literal.LiteralType;
import org.n52.shetland.ogc.ows.OwsAllowedValues;
import org.n52.shetland.ogc.ows.OwsAnyValue;
import org.n52.shetland.ogc.ows.OwsCode;
import org.n52.shetland.ogc.ows.OwsKeyword;
import org.n52.shetland.ogc.ows.OwsLanguageString;
import org.n52.shetland.ogc.ows.OwsMetadata;
import org.n52.shetland.ogc.ows.OwsValue;
import org.n52.shetland.ogc.wps.Format;
import org.n52.shetland.ogc.wps.description.ProcessInputDescription;
import org.n52.shetland.ogc.wps.description.ProcessOutputDescription;

public abstract class AbstractSelfDescribingAlgorithm extends AbstractAlgorithm implements ISubject {

    private String title;

    private String _abstract;

    private String version;

    private boolean storeSupported;

    private boolean statusSupported;

    private Collection<ProcessInputDescription> inputs = new ArrayList<>();

    private Collection<ProcessOutputDescription> outputs = new ArrayList<>();

    private TypedProcessDescriptionFactory descriptionFactory = new TypedProcessDescriptionFactory();

    private List<IObserver> observers = new ArrayList<IObserver>();

    private Object state = null;

    private OutputHandlerRepository generatorRepository;

    private InputHandlerRepository parserRepository;

    private OwsMetadata metadata;

    private String getTitle() {
        return title;
    }

    protected void setTitle(String title) {
        this.title = title;
    }

    private String getAbstract() {
        return _abstract;
    }

    protected void setAbstract(String _abstract) {
        this._abstract = _abstract;
    }

    private String getVersion() {
        return version;
    }

    protected void setVersion(String version) {
        this.version = version;
    }

    @Override
    protected TypedProcessDescription createDescription() {

        TypedProcessDescription description = new TypedProcessDescriptionFactory().process().withIdentifier(this
                .getClass().getName()).withTitle(getTitle()).withAbstract(getAbstract())
                .withMetadata(getMetadata())
                .withInput(getInputs())// TODO process could have no inputs
                .withOutput(getOutputs()).withVersion(getVersion()).statusSupported(getStatusSupported())
                .storeSupported(getStoreSupported()).build();

        return description;

        // ProcessDescriptionsDocument document =
        // ProcessDescriptionsDocument.Factory.newInstance();
        // ProcessDescriptions processDescriptions =
        // document.addNewProcessDescriptions();
        // ProcessDescriptionType processDescription =
        // processDescriptions.addNewProcessDescription();
        // processDescription.setStatusSupported(getStatusSupported());
        // processDescription.setStoreSupported(getStoreSupported());
        // processDescription.setProcessVersion("1.0.0");
        //
        // //1. Identifier
        // processDescription.addNewIdentifier().setStringValue(this.getClass().getName());
        // processDescription.addNewTitle().setStringValue(this.getClass().getCanonicalName());
        //
        // //2. Inputs
        // List<String> identifiers = this.getInputIdentifiers();
        // DataInputs dataInputs = null;
        // if(identifiers.size()>0){
        // dataInputs = processDescription.addNewDataInputs();
        // }
        //
        // for(String identifier : identifiers){
        // InputDescriptionType dataInput = dataInputs.addNewInput();
        // dataInput.setMinOccurs(getMinOccurs(identifier));
        // dataInput.setMaxOccurs(getMaxOccurs(identifier));
        // dataInput.addNewIdentifier().setStringValue(identifier);
        // dataInput.addNewTitle().setStringValue(identifier);
        //
        // Class<?> inputDataTypeClass = this.getInputDataType(identifier);
        // Class<?>[] interfaces = inputDataTypeClass.getInterfaces();
        //
        // //we have to add this because of the new AbstractLiteralDataBinding
        // class
        // if(interfaces.length == 0){
        // interfaces = inputDataTypeClass.getSuperclass().getInterfaces();
        // }
        //
        // for(Class<?> implementedInterface : interfaces){
        // if(implementedInterface.equals(ILiteralData.class)){
        // LiteralInputType literalData = dataInput.addNewLiteralData();
        // String inputClassType = "";
        //
        // Constructor<?>[] constructors = inputDataTypeClass.getConstructors();
        // for(Constructor<?> constructor : constructors){
        // Class<?>[] parameters = constructor.getParameterTypes();
        // if(parameters.length==1){
        // inputClassType = parameters[0].getSimpleName();
        // }
        // }
        //
        // if(inputClassType.length()>0){
        // DomainMetadataType datatype = literalData.addNewDataType();
        // datatype.setReference("xs:"+inputClassType.toLowerCase());
        // literalData.addNewAnyValue();
        // }
        // }else if(implementedInterface.equals(IBBOXData.class)){
        // SupportedCRSsType bboxData = dataInput.addNewBoundingBoxData();
        // String[] supportedCRSAray = getSupportedCRSForBBOXInput(identifier);
        // for(int i = 0; i<supportedCRSAray.length; i++){
        // if(i==0){
        // Default defaultCRS = bboxData.addNewDefault();
        // defaultCRS.setCRS(supportedCRSAray[0]);
        // if(supportedCRSAray.length==1){
        // CRSsType supportedCRS = bboxData.addNewSupported();
        // supportedCRS.addCRS(supportedCRSAray[0]);
        // }
        // }else{
        // if(i==1){
        // CRSsType supportedCRS = bboxData.addNewSupported();
        // supportedCRS.addCRS(supportedCRSAray[1]);
        // }else{
        // bboxData.getSupported().addCRS(supportedCRSAray[i]);
        // }
        // }
        // }
        //
        //
        //
        //
        // }else if(implementedInterface.equals(IComplexData.class)){
        // SupportedComplexDataInputType complexData =
        // dataInput.addNewComplexData();
        // List<IParser> parsers = ParserFactory.getInstance().getAllParsers();
        // List<IParser> foundParsers = new ArrayList<IParser>();
        // for(IParser parser : parsers) {
        // Class<?>[] supportedClasses = parser.getSupportedDataBindings();
        // for(Class<?> clazz : supportedClasses){
        // if(clazz.equals(inputDataTypeClass)){
        // foundParsers.add(parser);
        // }
        //
        // }
        // }
        //
        // addInputFormats(complexData, foundParsers);
        //
        // }
        // }
        // }
        //
        // //3. Outputs
        // ProcessOutputs dataOutputs =
        // processDescription.addNewProcessOutputs();
        // List<String> outputIdentifiers = this.getOutputIdentifiers();
        // for(String identifier : outputIdentifiers){
        // OutputDescriptionType dataOutput = dataOutputs.addNewOutput();
        //
        //
        // dataOutput.addNewIdentifier().setStringValue(identifier);
        // dataOutput.addNewTitle().setStringValue(identifier);
        // dataOutput.addNewAbstract().setStringValue(identifier);
        //
        // Class<?> outputDataTypeClass = this.getOutputDataType(identifier);
        // Class<?>[] interfaces = outputDataTypeClass.getInterfaces();
        //
        // //we have to add this because of the new AbstractLiteralDataBinding
        // class
        // if(interfaces.length == 0){
        // interfaces = outputDataTypeClass.getSuperclass().getInterfaces();
        // }
        // for(Class<?> implementedInterface : interfaces){
        //
        //
        // if(implementedInterface.equals(ILiteralData.class)){
        // LiteralOutputType literalData = dataOutput.addNewLiteralOutput();
        // String outputClassType = "";
        //
        // Constructor<?>[] constructors =
        // outputDataTypeClass.getConstructors();
        // for(Constructor<?> constructor : constructors){
        // Class<?>[] parameters = constructor.getParameterTypes();
        // if(parameters.length==1){
        // outputClassType = parameters[0].getSimpleName();
        // }
        // }
        //
        // if(outputClassType.length()>0){
        // literalData.addNewDataType().setReference("xs:"+outputClassType.toLowerCase());
        // }
        //
        // }else if(implementedInterface.equals(IBBOXData.class)){
        // SupportedCRSsType bboxData = dataOutput.addNewBoundingBoxOutput();
        // String[] supportedCRSAray = getSupportedCRSForBBOXOutput(identifier);
        // for(int i = 0; i<supportedCRSAray.length; i++){
        // if(i==0){
        // Default defaultCRS = bboxData.addNewDefault();
        // defaultCRS.setCRS(supportedCRSAray[0]);
        // if(supportedCRSAray.length==1){
        // CRSsType supportedCRS = bboxData.addNewSupported();
        // supportedCRS.addCRS(supportedCRSAray[0]);
        // }
        // }else{
        // if(i==1){
        // CRSsType supportedCRS = bboxData.addNewSupported();
        // supportedCRS.addCRS(supportedCRSAray[1]);
        // }else{
        // bboxData.getSupported().addCRS(supportedCRSAray[i]);
        // }
        // }
        // }
        //
        // }else if(implementedInterface.equals(IComplexData.class)){
        //
        // SupportedComplexDataType complexData =
        // dataOutput.addNewComplexOutput();
        //
        // List<IGenerator> generators =
        // GeneratorFactory.getInstance().getAllGenerators();
        // List<IGenerator> foundGenerators = new ArrayList<IGenerator>();
        // for(IGenerator generator : generators) {
        // Class<?>[] supportedClasses = generator.getSupportedDataBindings();
        // for(Class<?> clazz : supportedClasses){
        // if(clazz.equals(outputDataTypeClass)){
        // foundGenerators.add(generator);
        // }
        //
        // }
        // }
        //
        // addOutputFormats(complexData, foundGenerators);
        //
        // }
        // }
        // }
        //
        // ProcessDescription superProcessDescription = new
        // ProcessDescription();
        //
        // superProcessDescription.addProcessDescriptionForVersion(document.getProcessDescriptions().getProcessDescriptionArray(0),
        // "1.0.0");
        //
        // return superProcessDescription;
    }

    protected void addComplexInputDescription(String id,
            String title,
            String _abstract,
            List<String> keywordList,
            Set<OwsMetadata> metadata,
            Format defaultFormat,
            Set<Format> supportedFormats,
            BigInteger maximumMegaBytes,
            BigInteger minOccurs,
            BigInteger maxOccurs,
            Class<? extends ComplexData<?>> dataBinding) {

        List<OwsKeyword> keywords = new ArrayList<>();

        for (String keyword : keywordList) {
            OwsKeyword owsKeyword = new OwsKeyword(keyword);
            keywords.add(owsKeyword);
        }

        TypedComplexInputDescription inputDescription = new TypedProcessDescriptionFactory().complexInput()
                .withIdentifier(id).withAbstract(_abstract).withTitle(title).withMinimalOccurence(minOccurs)
                .withKeywords(keywords).withMetadata(metadata.iterator().next())// FIXME
                .withMaximalOccurence(maxOccurs).withMaximumMegabytes(maximumMegaBytes).withDefaultFormat(defaultFormat)
                .withSupportedFormat(supportedFormats).withType(dataBinding).build();

        inputs.add(inputDescription);
    }
    
    protected void addComplexInputDescription(String id,
            String title,
            String _abstract,
            Set<OwsMetadata> metadata,
            Format defaultFormat,
            Set<Format> supportedFormats,
            BigInteger maximumMegaBytes,
            BigInteger minOccurs,
            BigInteger maxOccurs,
            Class<? extends ComplexData<?>> dataBinding) {
        
        TypedComplexInputDescription inputDescription = new TypedProcessDescriptionFactory().complexInput()
                .withIdentifier(id).withAbstract(_abstract).withTitle(title).withMinimalOccurence(minOccurs)
                .withMetadata(metadata.iterator().next())// FIXME
                .withMaximalOccurence(maxOccurs).withMaximumMegabytes(maximumMegaBytes).withDefaultFormat(defaultFormat)
                .withSupportedFormat(supportedFormats).withType(dataBinding).build();
        
        inputs.add(inputDescription);
    }
    
    protected void addComplexInputDescription(String id,
            String title,
            String _abstract,
            Set<OwsMetadata> metadata,
            Format defaultFormat,
            Set<Format> supportedFormats,
            BigInteger maxOccurs,
            Class<? extends ComplexData<?>> dataBinding) {
        
        TypedComplexInputDescription inputDescription = new TypedProcessDescriptionFactory().complexInput()
                .withIdentifier(id).withAbstract(_abstract).withTitle(title)
                .withMetadata(metadata.iterator().next())// FIXME
                .withMaximalOccurence(maxOccurs).withDefaultFormat(defaultFormat)
                .withSupportedFormat(supportedFormats).withType(dataBinding).build();
        
        inputs.add(inputDescription);
    }

    protected void addComplexInputDescription(String id,
            String title,
            String _abstract,
            Format defaultFormat,
            Set<Format> supportedFormats,
            BigInteger maximumMegaBytes,
            BigInteger minOccurs,
            BigInteger maxOccurs,
            Class<? extends ComplexData<?>> dataBinding) {

        TypedComplexInputDescription inputDescription = new TypedProcessDescriptionFactory().complexInput()
                .withIdentifier(id).withAbstract(_abstract).withTitle(title).withMinimalOccurence(minOccurs)
                .withMaximalOccurence(maxOccurs).withMaximumMegabytes(maximumMegaBytes).withDefaultFormat(defaultFormat)
                .withSupportedFormat(supportedFormats).withType(dataBinding).build();

        inputs.add(inputDescription);
    }

    protected void addComplexInputDescription(String id,
            String title,
            String _abstract,
            Format defaultFormat,
            Set<Format> supportedFormats,
            BigInteger maximumMegaBytes,
            BigInteger maxOccurs,
            Class<? extends ComplexData<?>> dataBinding) {

        TypedComplexInputDescription inputDescription = new TypedProcessDescriptionFactory().complexInput()
                .withIdentifier(id).withAbstract(_abstract).withTitle(title).withMaximalOccurence(maxOccurs)
                .withMaximumMegabytes(maximumMegaBytes).withDefaultFormat(defaultFormat).withSupportedFormat(
                        supportedFormats).withType(dataBinding).build();

        inputs.add(inputDescription);
    }

    protected void addComplexInputDescription(String id,
            String title,
            String _abstract,
            Format defaultFormat,
            Set<Format> supportedFormats,
            BigInteger maxOccurs,
            Class<? extends ComplexData<?>> dataBinding) {

        TypedComplexInputDescription inputDescription = new TypedProcessDescriptionFactory().complexInput()
                .withIdentifier(id).withAbstract(_abstract).withTitle(title).withMaximalOccurence(maxOccurs)
                .withDefaultFormat(defaultFormat).withSupportedFormat(supportedFormats).withType(dataBinding).build();

        inputs.add(inputDescription);
    }

    protected void addComplexInputDescription(String id,
            String title,
            String _abstract,
            Format defaultFormat,
            Set<Format> supportedFormats,
            Class<? extends ComplexData<?>> dataBinding) {

        TypedComplexInputDescription inputDescription = new TypedProcessDescriptionFactory().complexInput()
                .withIdentifier(id).withAbstract(_abstract).withTitle(title).withDefaultFormat(defaultFormat)
                .withSupportedFormat(supportedFormats).withType(dataBinding).build();

        inputs.add(inputDescription);
    }

    protected void addComplexInputDescription(String id,
            Format defaultFormat,
            Set<Format> supportedFormats,
            Class<? extends ComplexData<?>> dataBinding) {

        TypedComplexInputDescription inputDescription = new TypedProcessDescriptionFactory().complexInput()
                .withIdentifier(id).withAbstract(_abstract).withTitle(title).withDefaultFormat(defaultFormat)
                .withSupportedFormat(supportedFormats).withType(dataBinding).build();

        inputs.add(inputDescription);
    }

    protected void addComplexInputDescription(String id,
            Class<? extends ComplexData<?>> dataBinding) {

        Set<Format> supportedFormats = this.parserRepository.getSupportedFormats(dataBinding);
        Format defaultFormat = this.parserRepository.getDefaultFormat(dataBinding).orElse(null);

        TypedComplexInputDescription inputDescription = new TypedProcessDescriptionFactory().complexInput()
                .withIdentifier(id).withAbstract(_abstract).withTitle(title).withDefaultFormat(defaultFormat)
                .withSupportedFormat(supportedFormats).withType(dataBinding).build();

        inputs.add(inputDescription);
    }

    protected void addLiteralInputDescription(String id,
            List<String> allowedValues,
            LiteralType<?> dataBinding) {

        TypedLiteralInputDescription inputDescription = descriptionFactory.literalInput().withIdentifier(id).withType(
                dataBinding).withDefaultLiteralDataDomain(descriptionFactory.literalDataDomain().withValueDescription(
                        allowedValues.isEmpty() ? OwsAnyValue.instance()
                                : new OwsAllowedValues(allowedValues.stream().map(OwsValue::new))).withDataType(
                                        dataBinding.getDataType())).build();

        inputs.add(inputDescription);
    }

    protected void addLiteralInputDescription(String id,
            List<String> allowedValues,
            BigInteger minOccurs,
            BigInteger maxOccurs,
            LiteralType<?> dataBinding) {

        TypedLiteralInputDescription inputDescription = descriptionFactory.literalInput().withIdentifier(id)
                .withMinimalOccurence(minOccurs).withMaximalOccurence(maxOccurs).withType(dataBinding)
                .withDefaultLiteralDataDomain(descriptionFactory.literalDataDomain().withValueDescription(allowedValues
                        .isEmpty() ? OwsAnyValue.instance()
                                : new OwsAllowedValues(allowedValues.stream().map(OwsValue::new))).withDataType(
                                        dataBinding.getDataType())).build();

        inputs.add(inputDescription);
    }

    protected void addComplexOutputDescription(String id,
            String title,
            String _abstract,
            List<String> keywords,
            Set<OwsMetadata> metadata,
            Format defaultFormat,
            Set<Format> supportedFormats,
            BigInteger maximumMegabytes,
            Class<? extends ComplexData<?>> dataBinding) {

        TypedComplexOutputDescription outputDescription = new TypedComplexOutputDescriptionImpl(new OwsCode(id),
                new OwsLanguageString(title), new OwsLanguageString(_abstract), null, metadata, defaultFormat,
                supportedFormats, maximumMegabytes, dataBinding);

        outputs.add(outputDescription);
    }

    protected void addComplexOutputDescription(String id,
            String title,
            String _abstract,
            Format defaultFormat,
            Set<Format> supportedFormats,
            Class<? extends ComplexData<?>> dataBinding) {

        TypedComplexOutputDescription outputDescription = descriptionFactory.complexOutput().withIdentifier(id)
                .withAbstract(_abstract).withTitle(title).withDefaultFormat(defaultFormat).withSupportedFormat(
                        supportedFormats).withType(dataBinding).build();

        outputs.add(outputDescription);
    }

    private Collection<ProcessOutputDescription> getOutputs() {
        return outputs;
    }

    private Collection<ProcessInputDescription> getInputs() {
        return inputs;
    }

    private OwsMetadata getMetadata() {        
        return this.metadata;
    }

    protected void setMetadata(OwsMetadata metadata) {
        this.metadata = metadata;
    }

    private boolean getStoreSupported() {
        return storeSupported;
    }

    private boolean getStatusSupported() {
        return statusSupported;
    }

    protected void setStoreSupported(boolean storeSupported) {
        this.storeSupported = storeSupported;
    }

    protected void setStatusSupported(boolean statusSupported) {
        this.statusSupported = statusSupported;
    }

    /**
     * Override this class for BBOX input data to set supported mime types. The
     * first one in the resulting array will be the default one.
     *
     * @param identifier
     *            ID of the input BBOXType
     * @return an array containing Strings representing the supported CRSs for
     *         inputs
     */
    public String[] getSupportedCRSForBBOXInput(String identifier) {
        return new String[0];
    }

    /**
     * Override this class for BBOX output data to set supported mime types. The
     * first one in the resulting array will be the default one.
     *
     * @param identifier
     *            ID of the input BBOXType
     * @return an array containing Strings representing the supported CRSs for
     *         outputs
     */
    public String[] getSupportedCRSForBBOXOutput(String identifier) {
        return new String[0];
    }

    public BigInteger getMinOccurs(String identifier) {
        return new BigInteger("1");
    }

    public BigInteger getMaxOccurs(String identifier) {
        return new BigInteger("1");
    }

    public abstract List<String> getInputIdentifiers();

    public abstract List<String> getOutputIdentifiers();

    public Object getState() {
        return state;
    }

    public void update(Object state) {
        this.state = state;
        notifyObservers();
    }

    public void addObserver(IObserver o) {
        observers.add(o);
    }

    public void removeObserver(IObserver o) {
        observers.remove(o);
    }

    public void notifyObservers() {
        Iterator<IObserver> i = observers.iterator();
        while (i.hasNext()) {
            IObserver o = (IObserver) i.next();
            o.update(this);
        }
    }

    @Override
    public List<String> getErrors() {
        List<String> errors = new ArrayList<String>();
        return errors;
    }

    public void setParserRepository(InputHandlerRepository parserRepository) {
        this.parserRepository = parserRepository;

    }

    public void setGeneratorRepository(OutputHandlerRepository generatorRepository) {
        this.generatorRepository = generatorRepository;
    }

    // private void addInputFormats(SupportedComplexDataInputType complexData,
    // List<IParser> foundParsers) {
    // ComplexDataCombinationsType supportedInputFormat = complexData
    // .addNewSupported();
    //
    // for (int i = 0; i < foundParsers.size(); i++) {
    // IParser parser = foundParsers.get(i);
    //
    // List<FormatEntry> supportedFullFormats =
    // parser.getSupportedFullFormats();
    //
    // if (complexData.getDefault() == null) {
    // ComplexDataCombinationType defaultInputFormat = complexData
    // .addNewDefault();
    // /*
    // * default format will be the first config format
    // */
    // FormatEntry format = supportedFullFormats.get(0);
    // ComplexDataDescriptionType defaultFormat = defaultInputFormat
    // .addNewFormat();
    // defaultFormat.setMimeType(format.getMimeType());
    //
    // String encoding = format.getEncoding();
    //
    // if (encoding != null && !encoding.equals("")) {
    // defaultFormat.setEncoding(encoding);
    // }
    //
    // String schema = format.getSchema();
    //
    // if (schema != null && !schema.equals("")) {
    // defaultFormat.setSchema(schema);
    // }
    //
    // }
    //
    // for (int j = 0; j < supportedFullFormats.size(); j++) {
    // /*
    // * create supportedFormat for each mimetype, encoding, schema
    // * composition mimetypes can have several encodings and schemas
    // */
    // FormatEntry format1 = supportedFullFormats.get(j);
    //
    // /*
    // * add one format for this mimetype
    // */
    // ComplexDataDescriptionType supportedFormat = supportedInputFormat
    // .addNewFormat();
    // supportedFormat.setMimeType(format1.getMimeType());
    // if (format1.getEncoding() != null) {
    // supportedFormat.setEncoding(format1.getEncoding());
    // }
    // if (format1.getSchema() != null) {
    // supportedFormat.setSchema(format1.getSchema());
    // }
    // }
    // }
    // }
    //
    // private void addOutputFormats(SupportedComplexDataType complexData,
    // List<IGenerator> foundGenerators) {
    // ComplexDataCombinationsType supportedOutputFormat = complexData
    // .addNewSupported();
    //
    // for (int i = 0; i < foundGenerators.size(); i++) {
    // IGenerator generator = foundGenerators.get(i);
    //
    // List<FormatEntry> supportedFullFormats =
    // generator.getSupportedFullFormats();
    //
    // if (complexData.getDefault() == null) {
    // ComplexDataCombinationType defaultInputFormat = complexData
    // .addNewDefault();
    // /*
    // * default format will be the first config format
    // */
    // FormatEntry format = supportedFullFormats.get(0);
    // ComplexDataDescriptionType defaultFormat = defaultInputFormat
    // .addNewFormat();
    // defaultFormat.setMimeType(format.getMimeType());
    //
    // String encoding = format.getEncoding();
    //
    // if (encoding != null && !encoding.equals("")) {
    // defaultFormat.setEncoding(encoding);
    // }
    //
    // String schema = format.getSchema();
    //
    // if (schema != null && !schema.equals("")) {
    // defaultFormat.setSchema(schema);
    // }
    //
    // }
    //
    // for (int j = 0; j < supportedFullFormats.size(); j++) {
    // /*
    // * create supportedFormat for each mimetype, encoding, schema
    // * composition mimetypes can have several encodings and schemas
    // */
    // FormatEntry format1 = supportedFullFormats.get(j);
    //
    // /*
    // * add one format for this mimetype
    // */
    // ComplexDataDescriptionType supportedFormat = supportedOutputFormat
    // .addNewFormat();
    // supportedFormat.setMimeType(format1.getMimeType());
    // if (format1.getEncoding() != null) {
    // supportedFormat.setEncoding(format1.getEncoding());
    // }
    // if (format1.getSchema() != null) {
    // supportedFormat.setSchema(format1.getSchema());
    // }
    // }
    // }
    // }

}

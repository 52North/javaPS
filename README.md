# javaPS 
[![Maven Central](https://img.shields.io/maven-central/v/org.n52.wps/javaPS.svg)](https://search.maven.org/search?q=g:org.n52.wps%20and%20a:webapp) 

## Build Status
* Develop: ![Develop Build Status](https://github.com/52North/javaPS/actions/workflows/daily.yml/badge.svg)

## Description

### Next generation standardized web-based geo-processing.

**Interoperable processing framework for web applications/distributed workflow systems**

javaPS enables the deployment of geo-processes on the web in a standardized way. One of its main goals ist the provision of an interoperable processing framework for web applications or distributed workflow systems. It features a pluggable architecture for processes and data encodings.

javaPS implements the [OGC WPS specification, version 2.0](http://docs.opengeospatial.org/is/14-065/14-065.html).

**Features:**

* General Features
  * Full java-based Open Source implementation.
  * Pluggable framework for algorithms and XML data handling and processing frameworks
  * Built upon the [arctic-sea project](https://github.com/52North/arctic-sea)
  * Streaming de-/encoders for inputs and outputs

**Key Technologies:**

* Java
* Spring
* Arctic Sea
* OpenAPI

**Benefits**

* Abstraction of (existing) processing tools
* Support for the majority of standardized geo-data formats
* Lightweight API, following the latest state of the draft OGC API Processes standard

## Quick Start

Use git to clone the javaPS repository:

```
git clone https://github.com/52North/javaPS.git
```

Then just run `mvn clean install` on the repositories root directory.

## User guide/tutorial

Check out the [JavaPS Documentation](http://52north.github.io/javaPS/documentation_markdown/site/index.html).

## Demo

Try out the latest javaPS alpha-release on on our [Geoprocessing Demo Server](http://geoprocessing.demo.52north.org/).

## License

This project is licensed under the Apache Software License, version 2.0.

## Changelog

### Fixed issues ---

- Bugfix, add allowedValues element
- REST Serialize UOM
- Fix missing .jsp file

### Dependency updates ---

- Bump junit from 4.12 to 4.13.1
- Bump version.jackson from 2.10.3 to 2.12.1
- Bump guava from 28.2-jre to 29.0-jre
- Update parent and artic-sea versions
- Bump commons-io from 2.6 to 2.7

### Other ---

- Bump year

## References

* [WPS for Tsunami Simulation](https://riesgos.52north.org/javaps/service?request=GetCapabilities&service=WPS) - WPS service deployed in the RIESGOS project.
* [OGC Testbed 15](https://www.opengeospatial.org/projects/initiatives/testbed15)
* [OGC Routing Pilot](https://www.opengeospatial.org/projects/initiatives/routingpilot)
* [WaCoDiS](https://wacodis.fbg-hsbo.de/)

## Contact

 * Christian Autermann (c.autermann (at) 52north.org)
 * Benjamin Pross (b.pross (at) 52north.org)

## Support

You can get support in the community mailing list and forums:
https://52north.org/discuss/#mailing-lists

## Contribute

Are you are interesting in contributing to javaPS and you want to pull your changes to the 52°North repository to make it available to all?
In that case we need your official permission and for this purpose we have a so called contributors license agreement (CLA) in place. With this agreement you grant us the rights to use and publish your code under an open source license.
A link to the contributors license agreement and further explanations are available here:
https://52north.org/software/licensing/guidelines/

## Credits

 * Christian Autermann, @autermann
 * Benjamin Pross, @bpross-52n

## Contributing Organizations

### Funding projects

 * GLUES
 * TaMIS
 * OGC Testbeds
 * RIESGOS


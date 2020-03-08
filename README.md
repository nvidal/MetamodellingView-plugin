# MetamodellingManager plugin

MetamodellingManager is a tab and view plug-in for the Protege Desktop ontology editor (*versions 5.0.0 and higher*).  The Maven POM file in the top-level directory demonstrates one possible method for packaging plug-in code into the required OSGi bundle format using the [Maven Bundle Plugin](http://felix.apache.org/site/apache-felix-maven-bundle-plugin-bnd.html).

#### Prerequisites

To build and run this plugin, you will need:

+ Apache's [Maven](http://maven.apache.org/index.html).
+ A Protege distribution extended with Metamodelling (5.5.0-metamodelling or higher) [here](https://github.com/nvidal/protege). 
+ A Owl API distribution extended with Metemodelling (4.5.7-metamodelling or higher) [here](https://github.com/nvidal/owlapi).

#### Build and install example plug-ins

1. Get a copy of the code:

        git clone https://github.com/nvidal/MetamodellingView-plugin.git 
    
2. Change into the MetamodellingView-plugin directory.

3. Make sure to have the dependencies owlapi-osgidistribution and protege-editor-owl installed in local Maven repo. To do this you can clone the projects and run the build command, then maven will install those in the local repo.

4. Type mvn clean package.  On build completion, the "target" directory will contain a uy.edu.fing.MetamodellingManager-${version}.jar file.

5. Copy the JAR file from the target directory to the "plugins" subdirectory of your Protege distribution.

 


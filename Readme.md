This project illustrates how to embed Grobid ([grobid](https://raw.github.com/kermitt2/grobid)) in your Java application. This example project is using GROBID Java API for extracting header metadata and citations from a PDF and output the results in BibTex format. 

The  simplest is to install first and build Grobid locally, see this link: [Build the project](http://grobid.readthedocs.org/en/latest/Install-Grobid/)

Via maven, this will deploy the Grobid artifact in your local maven repository. This will also create a jar file under: grobid-core/build/libs/grobid-core-`<current version>`.jar

The grobid library should be available via your local maven repo, but if necessary, you can also copy the Grobid jar library under `grobid-example/lib`:

```console
cp grobid-core/build/libs/grobid-core-`<current version>`.jar `path_to_grobid_example`/grobid-example/lib
```

As an alternative your pom can use the Grobid jar artefact loaded on Jitpack, see below. 

In any cases, you need a local `grobid-home` copy. The paths to __grobid-home__ and to the config file __grobid.yaml__ file must be changed in the project property file:  `grobid-example/grobid-example.properties` according to your installation, for instance: 

```
	grobid_example.pGrobidHome=/Users/lopez/grobid/grobid-home
```

You can then build and test the example project:

```console
mvn install
```

### The example: Extracting header metadata and citations from a PDF in BibTeX format

#### Processing one PDF

Extracting the header of a PDF file and outputting the result in the BibTeX file `out.bib` can be obtained with command:

> mvn exec:exec -Pprocess_header_bibtex -Dpdf=src/test/resources/Wang_paperAVE2008.pdf -Dbib=out.bib 

Extracting the citations of a PDF file and outputting the results in the BibTeX file `out.bib`:

> mvn exec:exec -Pprocess_citation_bibtex -Dpdf=src/test/resources/Wang_paperAVE2008.pdf -Dbib=out.bib

If you would like to run GROBID with consolidation thanks to CrossRef DOI service, add the following parameter (be sure to indicate your CrossRef account credentials in the GROBID main project file grobid/grobid-home/config/grobid.properties):

> mvn exec:exec -Pprocess_citation_bibtex -Dpdf=src/test/resources/Wang_paperAVE2008.pdf -Dbib=out.bib -Dconsolidation=true

#### Processing a repertory of PDFs

Extracting the header of all the PDF files in a given repertory and outputting the result in BibTeX files `.bib`:

> mvn exec:exec -Pprocess_header_bibtex -Dpdf=src/test/resources/pdf/ -Dbib=src/test/resources/bib/

Extracting the citations of all the PDF files in a given repertory and outputting the result in BibTeX files `.bib`:

> mvn exec:exec -Pprocess_citation_bibtex -Dpdf=src/test/resources/pdf/ -Dbib=src/test/resources/bib/

If you would like to run GROBID with consolidation thanks to CrossRef DOI service, add the following parameter (be sure to indicate your CrossRef account credentials in the GROBID main project file grobid/grobid-home/config/grobid.properties):

> mvn exec:exec -Pprocess_citation_bibtex -Dpdf=src/test/resources/pdf/ -Dbib=src/test/resources/bib/ -Dconsolidation=true

We describe below how Grobid is used in this example project. You can use this project as a skeleton to implement your own Grobid process or as an illustration to use Grobid in an existing/new project. 

### Building with maven

When using maven, if you want to call GROBID library without a local build of GROBID, you need to include in your pom file Jitpack as repository and the GROBID dependency, for instance as follow (replace `0.7.3` by the valid `<current version>`):

```xml
    <repositories>
        <repository>
            <id>grobid</id>
            <name>GROBID DIY repo</name>
            <url>https://grobid.s3.eu-west-1.amazonaws.com/repo/</url>
        </repository>
    </repositories>

    <dependency>
        <groupId>org.grobid</groupId>
        <artifactId>grobid-core</artifactId>
        <version>0.7.3</version>
    </dependency>
```

### API call

When using Grobid, you have to initiate a context with the path to the Grobid resources, the following class give an example of usage:

```java
        import org.grobid.core.*;
        import org.grobid.core.data.*;
        import org.grobid.core.factory.*;
        import org.grobid.core.utilities.*;
        import org.grobid.core.engines.Engine;
        import org.grobid.core.main.GrobidHomeFinder;
        ...
        String pdfPath = "mypdffile.pdf";
        ...
		try {
			String pGrobidHome = "/Users/lopez/grobid/grobid-home";

			GrobidHomeFinder grobidHomeFinder = new GrobidHomeFinder(Arrays.asList(pGrobidHome));
            GrobidProperties.getInstance(grobidHomeFinder);

            System.out.println(">>>>>>>> GROBID_HOME=" + GrobidProperties.get_GROBID_HOME_PATH());

			Engine engine = GrobidFactory.getInstance().createEngine();

			// Biblio object for the result
			BiblioItem resHeader = new BiblioItem();
			String tei = engine.processHeader(pdfPath, false, resHeader);
		} 
		catch (Exception e) {
			// if an exception is generated, print a stack trace
			e.printStackTrace();
		} 
```

The context paths (`pGrobidHome`) can be set by a property file, or for a web application by a `web.xml` file (see for instance `grobid-service` in the [grobid](https://github.com/kermitt2/grobid) project).

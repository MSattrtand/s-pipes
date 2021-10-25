package cz.cvut.spipes.constants;

import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;

public class CSVW {

    /**
     * The namespace of the vocabulary as a string
     */
    public static final String uri = "http://www.w3.org/ns/csvw#";

    protected static Resource resource(String local )
    { return ResourceFactory.createResource( uri + local ); }

    protected static Property property(String local )
    { return ResourceFactory.createProperty( uri, local ); }

    public static final Property table = property( "table");
    public static final Property url = property( "url");
    public static final Property row = property( "row");
    public static final Property rowNum = property( "rownum");
    public static final Property describes = property( "describes");
    public static final Property aboutUrl = property("aboutUrl");
    public static final Property propertyUrl = property("propertyUrl");
    public static final Property name = property("name");
    public static final Property title = property("title");
    public static final Property valueUrl = property("valueUrl");
    public static final Property tableSchema = property("tableSchema");
    public static final Property column = property("column");

    public static final Resource TableGroup = resource("TableGroup");
    public static final Resource Table = resource("Table");
    public static final Resource Row = resource("Row");
    public static final Resource TableSchema = resource("TableSchema");

    public static final String uriTemplate = uri + "uriTemplate";

    /**
     returns the URI for this schema
     @return the URI for this schema
     */
    public static String getURI() {
        return uri;
    }
}
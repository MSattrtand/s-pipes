package cz.cvut.spipes.modules;

import cz.cvut.spipes.engine.ExecutionContext;
import cz.cvut.spipes.engine.ExecutionContextFactory;
import cz.cvut.spipes.exception.ResourceNotUniqueException;
import cz.cvut.spipes.util.StreamResourceUtils;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TabularModuleTest {

    private TabularModule module;

    @BeforeEach
    public void setUp() {
        module = new TabularModule();

        module.setReplace(true);
        module.setDelimiter('\t');
        module.setQuoteCharacter('"');
        module.setDataPrefix("http://onto.fel.cvut.cz/data/");
        module.setOutputMode(Mode.STANDARD);

        module.setInputContext(ExecutionContextFactory.createEmptyContext());
    }

    @Test
    public void executeWithSimpleTransformation() throws URISyntaxException, IOException {
        module.setSourceResource(
            StreamResourceUtils.getStreamResource(
                "http://test-file",
                getFilePath("countries.tsv"))
        );

        ExecutionContext outputContext = module.executeSelf();

        assertTrue(outputContext.getDefaultModel().size() > 0);
    }

    @Disabled
    @Test
    public void executeWithDuplicateColumnsThrowsResourceNotUniqueException()
            throws URISyntaxException, IOException {
        module.setSourceResource(StreamResourceUtils.getStreamResource(
                "http://test-file-2",
                getFilePath("duplicate_column_countries.tsv"))
        );

        ResourceNotUniqueException exception = assertThrows(
                ResourceNotUniqueException.class,
                module::executeSelf
        );

        String expectedMessage = "latitude";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void checkDefaultConfigurationAgainstExemplaryModelOutput() throws URISyntaxException, IOException {
        module.setSourceResource(
                StreamResourceUtils.getStreamResource(
                        "http://test-file",
                        getFilePath("countries.tsv"))
        );

        ExecutionContext outputContext = module.executeSelf();
        Model actualModel = outputContext.getDefaultModel();
        Model expectedModel = ModelFactory.createDefaultModel()
                .read(getFilePath("countries-model-output.ttl").toString());

        assertTrue(actualModel.isIsomorphicWith(expectedModel));
    }

    private Path getFilePath(String fileName) throws URISyntaxException {
        return Paths.get(getClass().getResource("/" + fileName).toURI());
    }
}
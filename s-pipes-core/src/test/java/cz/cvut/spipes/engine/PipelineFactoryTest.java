package cz.cvut.spipes.engine;

import cz.cvut.spipes.modules.Module;
import java.util.List;
import org.apache.jena.ontology.OntModel;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import util.JenaTestUtils;

/**
 * Created by Miroslav Blasko on 7.6.16.
 */
public class PipelineFactoryTest {

    @Test
    public void loadPipelines() throws Exception {

        OntModel ontModel = JenaTestUtils.loadOntologyClosureFromResources("/pipeline/config.ttl");

        List<Module> moduleList = PipelineFactory.loadPipelines(ontModel);
        assertEquals(2, moduleList.size(),"Number of output modules of pipeline does not match");

//        Module module = moduleList.get(0);
//        System.out.println("Root module of pipeline is " + module);
//        ExecutionContext newContext = module.execute(ExecutionContextFactory.createContext(ontModel));
//        newContext.getDefaultModel().write(System.out, FileUtils.langTurtle);
    }

}
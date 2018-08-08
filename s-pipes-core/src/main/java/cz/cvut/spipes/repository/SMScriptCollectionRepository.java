package cz.cvut.spipes.repository;

import cz.cvut.spipes.manager.OntologyDocumentManager;
import cz.cvut.spipes.util.JenaPipelineUtils;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.Resource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Know nothing about alternative entity ids -- e.g. prefixed-names, local-names.
 * <p>
 */
public class SMScriptCollectionRepository implements ScriptCollectionRepository {
    private static final Logger LOG = LoggerFactory.getLogger(SMScriptCollectionRepository.class);


    private final OntologyDocumentManager ontoDocManager;
    //private final Set<String> contexts;


    public SMScriptCollectionRepository(OntologyDocumentManager ontoDocManager) {
        this.ontoDocManager = ontoDocManager;

       // contexts = ontoDocManager.getRegisteredOntologyUris();
    }


    @Override
    public @NotNull List<Resource> getModules(@Nullable Collection<String> contexts) {
        if (contexts == null) {
            throw new UnsupportedOperationException();
        }
        return contexts.stream()
                .map(ctx -> JenaPipelineUtils.getAllModulesWithTypes(getContextClosure(ctx)).keySet())
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    @Override
    public @NotNull List<Resource> getModuleTypes(@Nullable Collection<String> contexts) {
        if (contexts == null) {
            throw new NotImplementedException();
        }
        return null;
    }

    @Override
    public @NotNull List<Resource> getFunctions(@Nullable Collection<String> contexts) {
        if (contexts == null) {
            throw new UnsupportedOperationException();
        }
        // TODO move implementation from JenaPipelinesUtils
        return contexts.stream()
                .map(ctx -> JenaPipelineUtils.getAllFunctionsWithReturnModules(getContextClosure(ctx)).keySet())
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    @Override
    public Resource getResource(@NotNull String resourceUri, String context) {
        return getContextClosure(context).getResource(resourceUri);
    }

    @Override
    public Map<String, Set<String>> getAlternativeEntityIds() {
        throw new NotImplementedException();
    }


    private OntModel getContextClosure(@NotNull  String context) {
        OntModel model = ontoDocManager.getOntology(context);
        model.loadImports();
        return model;
    }

    /*

    -- functionOntologySet
            - samostatna entita ?


    1) get modules -- > IDs + context
    2) get funcitons




     */


//    String getFunctions() {
//
//        // input
//
//            // config
//            // everything
//
//        // output
//        //      id + context
//        //
//    }
//
//    String getModules() {
//
//        // input
//
//        // output
//        //
//    }
//
//    String getModuleTypes() {
//
//        // output
//        //      ?! module type + context
//    }
//
//
//    // TODO -- per context,
//    public List<String> getAllModuleTypes() {
//
//    }


}

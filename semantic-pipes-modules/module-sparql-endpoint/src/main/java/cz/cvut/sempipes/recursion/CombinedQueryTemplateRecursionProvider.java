package cz.cvut.sempipes.recursion;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.apache.jena.rdf.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CombinedQueryTemplateRecursionProvider implements QueryTemplateRecursionProvider {

    private static final Logger LOG = LoggerFactory.getLogger(CombinedQueryTemplateRecursionProvider.class);
    private final QueryTemplateRecursionProvider parentProvider;
    private final QueryTemplateRecursionProvider childProvder;
    private final int iterationCount;
    private int parentIteration = -1;
    private int childIteration = -1;
    private int lastIteration = -1;
    private boolean isParentLevelIteration = true;
    Map<String, String> parentTemplate2Query = new HashMap<>();

    public CombinedQueryTemplateRecursionProvider(Integer iterationCount, QueryTemplateRecursionProvider parentProvider, QueryTemplateRecursionProvider childProvder) {
        this.parentProvider = parentProvider;
        this.childProvder = childProvder;
        this.iterationCount = Optional.ofNullable(iterationCount).orElse(-1);
    }


    @Override
    public boolean shouldTerminate(int currentIteration, Model previousInferredModel, Model currentInferredModel) {

        checkRecursionProviderLinearCalls(currentIteration);
        if (isParentLevelIteration) {
            parentIteration++;
        }
        childIteration++;
        LOG.debug("Executing iteration {} --> ({}, {}).", currentIteration, parentIteration, childIteration);

        if (currentIteration == iterationCount) {
            return true;
        }

        // case 0,0
        if (currentIteration == 0) {
            return parentProvider.shouldTerminate(parentIteration, previousInferredModel, currentInferredModel)
                    || childProvder.shouldTerminate(childIteration, previousInferredModel, currentInferredModel);
        }

        // case *.1
        if (isParentLevelIteration) {
            if (parentProvider.shouldTerminate(parentIteration, previousInferredModel, currentInferredModel)) {
                return true;
            }
        }

        boolean childShouldTerminate = childProvder.shouldTerminate(childIteration, previousInferredModel, currentInferredModel);

        // set up next iteration
        if (childIteration == 1) {
            isParentLevelIteration = false;
        }

        if (childShouldTerminate) {
            childIteration = 0;
            isParentLevelIteration = true;
        }

        return false;
    }

    @Override
    public String substituteQueryMarkers(int currentIteration, String queryStr) {
        String parentQueryStr = null;
        if (isParentLevelIteration) {
            parentQueryStr = parentProvider.substituteQueryMarkers(parentIteration, queryStr);
            parentTemplate2Query.put(queryStr, parentQueryStr);
        } else {
            parentQueryStr = parentTemplate2Query.get(queryStr);
            if (parentQueryStr == null) {
                throw new IllegalStateException("Could not find substituted query template within the cache: " + queryStr);
            }
        }

        return childProvder.substituteQueryMarkers(childIteration, parentQueryStr);
    }

    private void checkRecursionProviderLinearCalls(int currentIteration) {
        if (currentIteration != (lastIteration + 1)) {
            throw new IllegalStateException("Calling recursion provider in non-linear way.");
        }
        lastIteration = currentIteration;
    }
}
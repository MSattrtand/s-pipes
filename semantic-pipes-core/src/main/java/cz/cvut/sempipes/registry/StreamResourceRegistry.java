package cz.cvut.sempipes.registry;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Miroslav Blasko on 28.11.16.
 */
public class StreamResourceRegistry {

    private static StreamResourceRegistry instance;
    private Set<String> resourcePrefixMap = new HashSet<>();
    private static final String PERSISTENT_CONTEXT_PREFIX = "http://onto.fel.cvut.cz/resources/";
    private Map<String, StreamResource> id2resourcesMap = new HashMap<>();

    private StreamResourceRegistry(){}

    public static StreamResourceRegistry getInstance(){
        if (instance == null) {
            instance = new StreamResourceRegistry();
            instance.resourcePrefixMap.add(PERSISTENT_CONTEXT_PREFIX);
            return instance;
        }
        return instance;
    }

    public void registerResourcePrefix(String resourcePrefix) {
        resourcePrefixMap.add(resourcePrefix);
    }

    public String getPERSISTENT_CONTEXT_PREFIX() {
        return PERSISTENT_CONTEXT_PREFIX;
    }

    public StreamResource getResourceById(String id) {
        return id2resourcesMap.get(id);
    }

    public StreamResource getResourceByUrl(String url) {
        String id = resourcePrefixMap.stream()
                .filter(url::startsWith)
                .findAny().map(p -> url.substring(p.length()))
                .orElse(null);

        StreamResource res = id2resourcesMap.get(id);
        if (res == null) {
            return null;
        }
        return new StringStreamResource(url, res.getContentAsString()); //TODO remove
    }

    public void registerResource(String id, String content) {
        StreamResource res = new StringStreamResource(id, content);
        id2resourcesMap.put(id, res);
    }
}

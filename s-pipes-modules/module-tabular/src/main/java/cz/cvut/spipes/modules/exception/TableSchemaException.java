package cz.cvut.spipes.modules.exception;

import cz.cvut.spipes.exception.SPipesException;
import cz.cvut.spipes.modules.Module;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class TableSchemaException extends SPipesException {

    public TableSchemaException(@NonNls String message, @NotNull Module module) {
        super(createModuleInfo(module) + "\n" +  message);
    }

    public TableSchemaException(@NonNls String message) {
        super(message);
    }

    private static String createModuleInfo(@NotNull  Module module) {
        return Optional.ofNullable(module.getResource())
                .map(r -> String.format("Execution of module %s failed. ", r))
                .orElse(String.format("Execution of a module with type %s failed.", module.getTypeURI()));
    }
}

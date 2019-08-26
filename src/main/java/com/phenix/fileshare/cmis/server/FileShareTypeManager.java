package com.phenix.fileshare.cmis.server;

import org.apache.chemistry.opencmis.commons.definitions.*;
import org.apache.chemistry.opencmis.commons.enums.CmisVersion;
import org.apache.chemistry.opencmis.commons.exceptions.CmisObjectNotFoundException;
import org.apache.chemistry.opencmis.commons.server.CallContext;
import org.apache.chemistry.opencmis.server.support.TypeDefinitionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages the type definitions for all FileShare repositories.
 */
public class FileShareTypeManager {

    private static final Logger LOG = LoggerFactory.getLogger(FileShareTypeManager.class);

    private static final String NAMESPACE = "http://chemistry.apache.org/opencmis/fileshare";

    private final TypeDefinitionFactory typeDefinitionFactory;
    private final Map<String, TypeDefinition> typeDefinitions;

    public FileShareTypeManager() {
        // set up TypeDefinitionFactory
        typeDefinitionFactory = TypeDefinitionFactory.newInstance();
        typeDefinitionFactory.setDefaultNamespace(NAMESPACE);
        typeDefinitionFactory.setDefaultControllableAcl(false);
        typeDefinitionFactory.setDefaultControllablePolicy(false);
        typeDefinitionFactory.setDefaultQueryable(false);
        typeDefinitionFactory.setDefaultFulltextIndexed(false);
        typeDefinitionFactory.setDefaultTypeMutability(typeDefinitionFactory.createTypeMutability(false, false, false));

        // set up definitions map
        typeDefinitions = new HashMap<String, TypeDefinition>();

        // add base folder type
        MutableFolderTypeDefinition folderType = typeDefinitionFactory
                .createBaseFolderTypeDefinition(CmisVersion.CMIS_1_1);
        removeQueryableAndOrderableFlags(folderType);
        typeDefinitions.put(folderType.getId(), folderType);

        // add base document type
        MutableDocumentTypeDefinition documentType = typeDefinitionFactory
                .createBaseDocumentTypeDefinition(CmisVersion.CMIS_1_1);
        removeQueryableAndOrderableFlags(documentType);
        typeDefinitions.put(documentType.getId(), documentType);
    }

    /**
     * Adds a type definition.
     */
    public synchronized void addTypeDefinition(TypeDefinition type) {
        if (type == null) {
            throw new IllegalArgumentException("Type must be set!");
        }

        if (type.getId() == null || type.getId().trim().length() == 0) {
            throw new IllegalArgumentException("Type must have a valid id!");
        }

        if (type.getParentTypeId() == null || type.getParentTypeId().trim().length() == 0) {
            throw new IllegalArgumentException("Type must have a valid parent id!");
        }

        TypeDefinition parentType = typeDefinitions.get(type.getParentTypeId());
        if (parentType == null) {
            throw new IllegalArgumentException("Parent type doesn't exist!");
        }

        MutableTypeDefinition newType = typeDefinitionFactory.copy(type, true);

        // copy parent type property definitions and mark them as inherited
        for (PropertyDefinition<?> propDef : parentType.getPropertyDefinitions().values()) {
            MutablePropertyDefinition<?> basePropDef = typeDefinitionFactory.copy(propDef);
            basePropDef.setIsInherited(true);
            newType.addPropertyDefinition(basePropDef);
        }

        typeDefinitions.put(newType.getId(), newType);

        if (LOG.isDebugEnabled()) {
            LOG.debug("Added type '{}'.", type.getId());
        }
    }

    /**
     * Removes the queryable and orderable flags from the property definitions
     * of a type definition because this implementations does neither support
     * queries nor can order objects.
     */
    private void removeQueryableAndOrderableFlags(MutableTypeDefinition type) {
        for (PropertyDefinition<?> propDef : type.getPropertyDefinitions().values()) {
            MutablePropertyDefinition<?> mutablePropDef = (MutablePropertyDefinition<?>) propDef;
            mutablePropDef.setIsQueryable(false);
            mutablePropDef.setIsOrderable(false);
        }
    }

    /**
     * Returns the internal type definition.
     */
    public synchronized TypeDefinition getInternalTypeDefinition(String typeId) {
        return typeDefinitions.get(typeId);
    }

    /**
     * Returns all internal type definitions.
     */
    public synchronized Collection<TypeDefinition> getInternalTypeDefinitions() {
        return typeDefinitions.values();
    }

    // --- service methods ---

    public TypeDefinition getTypeDefinition(CallContext context, String typeId) {
        TypeDefinition type = typeDefinitions.get(typeId);
        if (type == null) {
            throw new CmisObjectNotFoundException("Type '" + typeId + "' is unknown!");
        }

        return typeDefinitionFactory.copy(type, true, context.getCmisVersion());
    }

    public TypeDefinitionList getTypeChildren(CallContext context, String typeId, Boolean includePropertyDefinitions,
                                              BigInteger maxItems, BigInteger skipCount) {
        return typeDefinitionFactory.createTypeDefinitionList(typeDefinitions, typeId, includePropertyDefinitions,
                maxItems, skipCount, context.getCmisVersion());
    }

    public List<TypeDefinitionContainer> getTypeDescendants(CallContext context, String typeId, BigInteger depth,
                                                            Boolean includePropertyDefinitions) {
        return typeDefinitionFactory.createTypeDescendants(typeDefinitions, typeId, depth, includePropertyDefinitions,
                context.getCmisVersion());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (TypeDefinition type : typeDefinitions.values()) {
            sb.append('[');
            sb.append(type.getId());
            sb.append(" (");
            sb.append(type.getBaseTypeId().value());
            sb.append(")]");
        }

        return sb.toString();
    }
}

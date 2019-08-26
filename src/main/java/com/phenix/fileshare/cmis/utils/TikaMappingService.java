package com.phenix.fileshare.cmis.utils;


public class TikaMappingService {

    public static final String DEFAULT_DOCUMENT_TYPE = "cmis:document";
    public static final String DEFAULT_FOLDER_TYPE = "cmis:folder";

    private static final String MIMETYPES_PROPERTIES_FILE = "mimetypes.properties";
    private static final String TIKAMAPPING_PROPERTIES_FILE = "tikamapping.properties";

    private static Mapping MIME_TYPES_MAPPING;
    private static Mapping TIKA_MAPPING;

    static {
        MIME_TYPES_MAPPING = new Mapping(MIMETYPES_PROPERTIES_FILE);
        TIKA_MAPPING = new Mapping(TIKAMAPPING_PROPERTIES_FILE);
    }

    /**
     * Returns the type id that is associated with the given MIME type.
     */
    public static String getRepositoryTypeIdFromMIMEType(String mimetype) {
        String typeId = MIME_TYPES_MAPPING.getRight(mimetype);
        return (typeId == null ? DEFAULT_DOCUMENT_TYPE : IdMapping
                .getRepositoryTypeId(typeId));
    }

    /**
     * Returns the property if that matches the Tika metadata name.
     */
    public static String getPropertyIdFromTikaMetadata(
            String metadataName) {
        String propertyId = TIKA_MAPPING.getRight(metadataName);
        return IdMapping.getRepositoryPropertyId(propertyId);
    }
}
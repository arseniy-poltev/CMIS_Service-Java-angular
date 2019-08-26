package com.phenix.fileshare.cmis.utils;

public class IdMapping {

    private static final String TYPES_PROPERTIES_FILE = "type-ids.properties";
    private static final String PROPERTIES_PROPERTIES_FILE = "property-ids.properties";
    private static final String TYPES_QUERY_NAMES_FILE = "type-query-names.properties";
    private static final String PROPERTIES_QUERY_NAMES_FILE = "property-query-names.properties";

    private static Mapping TYPE_ID_MAP = new Mapping(
            TYPES_PROPERTIES_FILE);
    private static Mapping PROPERTY_ID_MAP = new Mapping(
            PROPERTIES_PROPERTIES_FILE);
    private static Mapping TYPE_QUERY_NAME_MAP = new Mapping(
            TYPES_QUERY_NAMES_FILE);
    private static Mapping PROPERTY_QUERY_NAME_MAP = new Mapping(
            PROPERTIES_QUERY_NAMES_FILE);

    public static String getRepositoryTypeId(String id) {
        String result = TYPE_ID_MAP.getRight(id);
        return (result == null ? id : result);
    }

    public static String getRepositoryPropertyId(String id) {
        String result = PROPERTY_ID_MAP.getRight(id);
        return (result == null ? id : result);
    }

    public static String getRepositoryTypeQueryName(String queryName) {
        String result = TYPE_QUERY_NAME_MAP.getRight(queryName);
        return (result == null ? queryName : result);
    }

    public static String getRepositoryPropertyQueryName(String queryName) {
        String result = PROPERTY_QUERY_NAME_MAP.getRight(queryName);
        return (result == null ? queryName : result);
    }
}
package com.phenix.fileshare.cmis.server;

import org.apache.chemistry.opencmis.commons.exceptions.CmisObjectNotFoundException;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages all repositories.
 */
public class FileShareRepositoryManager {

    private final Map<String, FileShareRepository> repositories;

    public FileShareRepositoryManager() {
        repositories = new HashMap<String, FileShareRepository>();
    }

    /**
     * Adds a repository object.
     */
    public void addRepository(FileShareRepository fsr) {
        if (fsr == null || fsr.getRepositoryId() == null) {
            return;
        }

        repositories.put(fsr.getRepositoryId(), fsr);
    }

    /**
     * Gets a repository object by id.
     */
    public FileShareRepository getRepository(String repositoryId) {
        FileShareRepository result = repositories.get(repositoryId);
        if (result == null) {
            throw new CmisObjectNotFoundException("Unknown repository '" + repositoryId + "'!");
        }

        return result;
    }

    /**
     * Returns all repository objects.
     */
    public Collection<FileShareRepository> getRepositories() {
        return repositories.values();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (FileShareRepository repository : repositories.values()) {
            sb.append('[');
            sb.append(repository.getRepositoryId());
            sb.append(" -> ");
            sb.append(repository.getRootDirectory().getAbsolutePath());
            sb.append(']');
        }

        return sb.toString();
    }
}
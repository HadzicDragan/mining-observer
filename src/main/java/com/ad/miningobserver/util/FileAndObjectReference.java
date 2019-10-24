package com.ad.miningobserver.util;

import java.util.List;

/**
 * Class that stores the files that need to be cleanup after the object lookup.
 * The {@code objList} 
 * @param <T> which type of objects are stored in {@code this.objects}
 */
public final class FileAndObjectReference<T> {

    private final List<String> files;
    private final List<T> objects;

    public FileAndObjectReference(
            final List<String> files, 
            final List<T> objList) {
        this.files = files;
        this.objects = objList;
    }

    /**
     * Path location for files.
     * @return {@link List} of files 
     */
    public List<String> getFiles() {
        return files;
    }

    /**
     * Immutable list of objects.
     * @return {@link List} of objects from the generic type {@code T}
     */
    public List<T> getObjects() {
        return objects;
    }
}

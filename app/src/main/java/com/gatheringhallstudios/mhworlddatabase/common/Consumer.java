package com.gatheringhallstudios.mhworlddatabase.common;

/**
 * A placeholder functional interface that takes an argument and returns no results.
 * A stand-in for the java 8 version, as this project was designed to be compatible with java 7.
 * Android Studio's desugar is what allows us to use java 8 features in java 7.
 *
 * We might rename this so that no one accidentally imports the java 8 version, as unfortunately
 * using java 8 apis cause cra
 * @param <T>
 */
public interface Consumer<T> {
    void accept(T t);
}

package org.kulikov.codescanner;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the SavedResultsActivity Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @SavedResultsActivity
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }
}
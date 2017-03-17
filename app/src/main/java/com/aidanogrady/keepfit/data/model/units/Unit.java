package com.aidanogrady.keepfit.data.model.units;

/**
 * The enumerator for the various units of distance available.
 *
 * @author Aidan O'Grady
 * @since 0.7
 */
public enum Unit {
    STEPS("steps"),
    METRES("m"),
    KILOMETRES("km"),
    YARDS("yd"),
    MILES("mi");


    /**
     * The abbreviated name of the unit of measurement.
     */
    private String mName;


    /**
     * Constructs a new
     * @param name the name of the book.
     */
    Unit(String name) {
        this.mName = name;
    }

    @Override
    public String toString() {
        return mName;
    }
}

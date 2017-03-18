package com.aidanogrady.keepfit.data.model.units;

import java.util.HashMap;
import java.util.Map;

/**
 * The UnitsConverter handles the conversion between different measures of units.
 *
 * @author Aidan O'Grady
 * @since 0.7
 */
public class UnitsConverter {
    /**
     * The mapping of units to the base value of 1 metre. All conversions are done via metres for
     * ease of use.
     */
    private static final Map<Unit, Double> UNITS;

    /**
     * The available units.
     */
    public static final Unit[] AVAILABLE_UNITS;

    /**
     * The names of the units.
     */
    public static final String[] AVAILABLE_UNIT_NAMES;

    /**
     * The fixed number of metres per metre.
     */
    public static final double METRES = 1;

    /**
     * The number of metres per kilometre.
     */
    public static final double KILOMETRES = 1000;

    /**
     * The number of metres per yard.
     */
    public static final double YARDS = 0.9144;

    /**
     * The number of metres per mile.
     */
    public static final double MILES = 1609.34;


    /*
      Initialises the Units map.
     */
    static {
        UNITS = new HashMap<>();
        UNITS.put(Unit.METRES, METRES);
        UNITS.put(Unit.KILOMETRES, KILOMETRES);
        UNITS.put(Unit.YARDS, YARDS);
        UNITS.put(Unit.MILES, MILES);

        AVAILABLE_UNITS = Unit.values();
        AVAILABLE_UNIT_NAMES = new String[AVAILABLE_UNITS.length];
        for (int i = 0; i < AVAILABLE_UNIT_NAMES.length; i++) {
            AVAILABLE_UNIT_NAMES[i] = AVAILABLE_UNITS[i].name();
        }
    }


    /**
     * Changes the mapping of steps to metres. Since the user is to control this particular value,
     * it requires special treatment.
     *
     * @param steps the new mapping.
     */
    public static void setSteps(double steps) {
        // The map is defined by metres per unit, but the user defines steps per metre, hence 1 /
        System.out.println("Steps per metre: "  + steps);
        System.out.println("Metres per step: " + (1 / steps));
        UNITS.put(Unit.STEPS, 1 / steps);
    }

    /**
     * Performs a conversion of distance from a source unit to a destination unit.
     *
     * @param to The unit being converted to
     * @param from The unit being converted from
     * @param distance The distance being converted
     * @return converted distance
     */
    public static double convert(Unit to, Unit from, double distance) {
        double metres = distance * UNITS.get(from);
        return metres / UNITS.get(to);
    }
}

package com.udacity.sandwichclub.model;

import java.util.List;

/**
 * The Class contains the Detailed Information for a Sandwich. In addition to the Main Name,
 * there is a List of still known names, the Place where the Sandwich originated,
 * list of ingredients, a Description as well as a Picture of the Sandwich.
 */
public class Sandwich {
    private static final String COMMA_AND_SPACE = ", ";
    private static String NO_INFORMATION_AVAILABLE = "No Information available";


    private String mainName;
    private List<String> alsoKnownAs = null;
    private String placeOfOrigin;
    private String description;
    private String image;
    private List<String> ingredients = null;

    /**
     * No args constructor for use in serialization
     */
    public Sandwich() {
    }

    /**
     * Constructor of the Sandwich class.
     *
     * @param mainName      of the Sandwich
     * @param alsoKnownAs   is a List of still known names
     * @param placeOfOrigin is a place where the Sandwich originated
     * @param description   of the Sandwich
     * @param image         of the Sandwich
     * @param ingredients   is a list of ingredients of the Sandwich
     */
    public Sandwich(String mainName, List<String> alsoKnownAs, String placeOfOrigin, String description, String image, List<String> ingredients) {
        this.mainName = mainName;
        this.alsoKnownAs = alsoKnownAs;
        this.placeOfOrigin = placeOfOrigin;
        this.description = description;
        this.image = image;
        this.ingredients = ingredients;
    }

    public String getMainName() {
        return mainName;
    }

    public String getAlsoKnownAs() {
        return getCommaSeparatedList(alsoKnownAs);
    }

    public String getPlaceOfOrigin() {
        return placeOfOrigin;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }

    public String getIngredients() {
        return getCommaSeparatedList(ingredients);
    }


    /**
     * Convenience method to convert a string list in a comma separated values in one string.
     * If the List does not exist or is empty, then the String "No Information available"
     * is returned.
     *
     * @param list of strings
     * @return comma separated values in one string or "No Information available"
     */
    private String getCommaSeparatedList(List<String> list) {
        if (list == null || list.isEmpty()) {
            return NO_INFORMATION_AVAILABLE;
        }
        return String.join(COMMA_AND_SPACE, list);
    }
}

package com.udacity.sandwichclub.model;

import java.util.List;

/**
 *
 */
public class Sandwich {

    private static final String SPACE = " ";
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
     *
     * @param mainName
     * @param alsoKnownAs
     * @param placeOfOrigin
     * @param description
     * @param image
     * @param ingredients
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
     * @param list
     * @return
     */
    private String getCommaSeparatedList(List<String> list) {
        if (list == null || list.isEmpty()) {
            return NO_INFORMATION_AVAILABLE;
        }
        return String.join(COMMA_AND_SPACE, list);
    }
}

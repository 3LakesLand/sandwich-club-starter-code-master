package com.udacity.sandwichclub.utils;

import android.util.Log;

import com.udacity.sandwichclub.model.Sandwich;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class JsonUtils {
    private static final String ERROR_IN_PARSING_THE_SANDWICH_JSON_STRING = "Error in Parsing the Sandwich JSON String";
    private static final String MAIN_NAME = "mainName";
    private static final String ALSO_KNOWN_AS = "alsoKnownAs";
    private static final String SANDWICH = "sandwich";
    private static final String DESCRIPTION = "description";
    private static final String IMAGE = "image";
    private static final String INGREDIENTS = "ingredients";
    private static final int START_INDEX = 0;
    private static final int ZERO = 0;
    private static final String NAME = "name";
    private static String TAG = JsonUtils.class.getSimpleName();
    private static String NO_INFORMATION_AVAILABLE = "No Information available";

    /**
     *
     * @param sandwichJsonString
     * @return
     */
    public static Sandwich parseSandwichJson(String sandwichJsonString) {
        if (sandwichJsonString != null) {
            try {
                JSONObject sandwichJsonObj = new JSONObject(sandwichJsonString);
                JSONObject mainJsonObj = sandwichJsonObj.getJSONObject(NAME);

                String mainName = mainJsonObj.optString(MAIN_NAME);

                JSONArray alsoKnownJsonArray = mainJsonObj.optJSONArray(ALSO_KNOWN_AS);
                List<String> alsoKnownAs = convertInStringList(alsoKnownJsonArray);

                String placeOfOrigin = sandwichJsonObj.optString(SANDWICH, NO_INFORMATION_AVAILABLE);

                String description = sandwichJsonObj.optString(DESCRIPTION, NO_INFORMATION_AVAILABLE);

                String image = sandwichJsonObj.optString(IMAGE);

                JSONArray ingredientsJsonArray = sandwichJsonObj.optJSONArray(INGREDIENTS);
                List<String> ingredients = convertInStringList(ingredientsJsonArray);

                return new Sandwich(mainName, alsoKnownAs, placeOfOrigin, description, image, ingredients);
            } catch (JSONException e) {
                Log.e(TAG, ERROR_IN_PARSING_THE_SANDWICH_JSON_STRING, e);
            }
        }
        return null;
    }

    /**
     *
     * @param jsonArray
     * @return
     * @throws JSONException
     */
    private static List<String> convertInStringList(JSONArray jsonArray) throws JSONException {
        List<String> resultList = new ArrayList<>();
        if (!(jsonArray == null || jsonArray.length() == ZERO)) {
            for (int i = START_INDEX; i < jsonArray.length(); i++) {
                resultList.add(jsonArray.getString(i).trim());
            }
        }
        return resultList;
    }
}

package com.udacity.sandwichclub.utils;

import android.util.Log;

import com.udacity.sandwichclub.model.Sandwich;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JsonUtils {
    private final static String TAG = JsonUtils.class.getSimpleName();

    public static Sandwich parseSandwichJson(String sandwichJson) {
        if (sandwichJson != null) {
            try {
                JSONObject sandwich = new JSONObject(sandwichJson);
                JSONObject main = sandwich.getJSONObject("name");
                String mainName = main.optString("mainName");
                JSONArray alsoKnownAs = main.getJSONArray("alsoKnownAs");
                ArrayList<String> alsoKnownAsList = new ArrayList<>();
                for (int i = 0; i < alsoKnownAs.length(); i++) {
                    alsoKnownAsList.add(alsoKnownAs.getString(i));
                }
                String placeOfOrigin = sandwich.optString("sandwich");
                String description = sandwich.optString("description");
                String image = sandwich.optString("image");
                JSONArray ingredients = sandwich.getJSONArray("ingredients");
                ArrayList<String> ingredientsList = new ArrayList<>();
                for (int i = 0; i < ingredients.length(); i++) {
                    ingredientsList.add(ingredients.getString(i));
                }
                return new Sandwich(mainName, alsoKnownAsList, placeOfOrigin, description, image,
                        ingredientsList);
            } catch (JSONException e) {
                Log.e(TAG, "Problem parsing the sandwich JSON results", e);
            }
        }
        return null;
    }
}

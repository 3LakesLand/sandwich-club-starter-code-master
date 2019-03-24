package com.udacity.sandwichclub;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * The Class represents the Detailed Description of a Sandwich. The Class is called via an Intent
 * that contains the clicked position from the Sandwich List. The Information to be displayed
 * is extracted from a JSON string. In this information is also the URL of the Sandwich Image.
 * Via an asynchronous Task, the Image is loaded and inserted into the Detailed Display.
 * As long as the Image has not been loaded, a Progress Bar will be displayed.
 * If no Image can be loaded, the Text "No picture is available" will be displayed instead
 * of the Image.
 */
public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int EXTRA_POSITION_NOT_FOUND_IN_INTENT = -1;


    @BindView(R.id.image_iv)
    ImageView image_iv;
    @BindView(R.id.origin_tv)
    TextView origin_tv;
    @BindView(R.id.description_tv)
    TextView description_tv;
    @BindView(R.id.ingredients_tv)
    TextView ingredients_tv;
    @BindView(R.id.also_known_tv)
    TextView also_known_tv;
    @BindView(R.id.load_image_pb)
    ProgressBar progressBar;
    @BindView(R.id.no_picture_tv)
    TextView no_picture_tv;

    //for ButterKnife framework
    private Unbinder unbinder;

    /**
     * The Method creates the surface layout, gets the pressed Sandwich Position out of the Intent,
     * calls the Conversion of the JSON string into a Sandwich object and calls the Loading
     * of the Sandwich pictures. The Sandwich Information is inserted into the Surface.
     *
     * @param savedInstanceState If non-null, this Activity is being re-constructed
     *                           from a previous saved state as given here
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //bind the ImageView, ProgressBar and the TextViews
        unbinder = ButterKnife.bind(this);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        //get the clicked position from the Sandwich List.
        int position = intent.getIntExtra(EXTRA_POSITION, EXTRA_POSITION_NOT_FOUND_IN_INTENT);
        if (position == EXTRA_POSITION_NOT_FOUND_IN_INTENT) {
            closeOnError();
            return;
        }


        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];
        Sandwich sandwich = JsonUtils.parseSandwichJson(json);
        if (sandwich == null) {
            // Sandwich data unavailable
            closeOnError();
            return;
        }

        //Loading of the Sandwich pictures
        new SandwichImageTask().execute(sandwich.getImage());

        origin_tv.setText(sandwich.getPlaceOfOrigin());
        description_tv.setText(sandwich.getDescription());
        ingredients_tv.setText(sandwich.getIngredients());
        also_known_tv.setText(sandwich.getAlsoKnownAs());

        setTitle(sandwich.getMainName());


    }

    /**
     * When the view is destroyed, the binder is reset.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    /**
     * If no Sandwich object could be created, the processing cancels with an info toast.
     */
    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }


    /**
     * The Class loads an Image via the URL that is transmitted and stores it into the Surface.
     * If no Image can be loaded, the Text "No picture is available" will be displayed instead.
     * A Progress Bar is displayed while the Image Is being loaded.
     */
    public class SandwichImageTask extends AsyncTask<String, Void, Bitmap> {
        private static final String NOT_SUCCESSFULLY_RECEIVED_RESPONSE_CODE =
                "The request was mot successfully received. Response Code: ";
        private static final int OK = 200;
        private static final String IMAGE_URL_IS_WRONG = "Image URL is wrong";
        private static final String ERROR_WITH_THE_CONNECTION = "Error with the connection";
        private static final String ERROR_WITH_THE_INPUT_STREAM = "Error with the input stream";
        private static final int FIRST_ELEMENT = 0;
        private static final int READ_TIMEOUT = 5000; /* milliseconds */
        private static final int CONNECT_TIMEOUT = 7000; /* milliseconds */
        private String TAG = SandwichImageTask.class.getSimpleName();

        /**
         * Before Loading the Picture we displayed the Process bar.
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            no_picture_tv.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        }

        /**
         * A correct URL Is created from the URL string. It is used to load the Bitmap of the Image.
         *
         * @param params contain the URL String
         * @return the image bitmap or an error
         */
        @Override
        protected Bitmap doInBackground(String... params) {
            URL url = createUrl(params[FIRST_ELEMENT]);
            return makeHttpRequest(url);
        }

        /**
         * The Process bar is switched off. Depending on whether an Image Bitmap has been loaded,
         * the Image will be displayed or the Text "No picture is available" will be issued.
         *
         * @param bitmap of the image
         */
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (progressBar != null) {
                progressBar.setVisibility(View.GONE);
            }
            if (bitmap != null) {
                image_iv.setImageBitmap(bitmap);
            } else if (no_picture_tv != null) {
                no_picture_tv.setVisibility(View.VISIBLE);
            }
        }

        /**
         * A correct URL Is created from the URL string.
         *
         * @param stringUrl string of the url
         * @return URL object or an error
         */
        private URL createUrl(String stringUrl) {
            URL url = null;
            try {
                url = new URL(stringUrl);
            } catch (MalformedURLException e) {
                Log.e(TAG, IMAGE_URL_IS_WRONG, e);
            }
            return url;
        }

        /**
         * The URL obejct is used to load the Bitmap of the Image.
         *
         * @param url URL object
         * @return the Bitmap of the Image or an error (bitmap is null)
         */
        private Bitmap makeHttpRequest(URL url) {
            Bitmap bitmap = null;
            if (url != null) {
                HttpURLConnection connection = null;
                InputStream inputStream = null;
                try {
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setReadTimeout(READ_TIMEOUT);
                    connection.setConnectTimeout(CONNECT_TIMEOUT);
                    connection.setDoInput(true);

                    if (connection.getResponseCode() == OK) {
                        inputStream = connection.getInputStream();
                        bitmap = BitmapFactory.decodeStream(inputStream);
                    } else {
                        Log.e(TAG, NOT_SUCCESSFULLY_RECEIVED_RESPONSE_CODE
                                + connection.getResponseCode());
                    }
                } catch (IOException e) {
                    Log.e(TAG, ERROR_WITH_THE_CONNECTION, e);
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (IOException e) {
                            Log.e(TAG, ERROR_WITH_THE_INPUT_STREAM, e);
                        }
                    }
                }
            }
            return bitmap;
        }
    }
}

package com.udacity.sandwichclub;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;


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

    //for ButterKnife framework
    private Unbinder unbinder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        unbinder = ButterKnife.bind(this);


        int position = DEFAULT_POSITION;
        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        } else {
            position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
            if (position == DEFAULT_POSITION) {
                // EXTRA_POSITION not found in intent
                closeOnError();
                return;
            }
        }



        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];
        Sandwich sandwich = JsonUtils.parseSandwichJson(json);
        if (sandwich == null) {
            // Sandwich data unavailable
            closeOnError();
            return;
        }

        new SandwichImageTask().execute(sandwich.getImage());

        origin_tv.setText(sandwich.getPlaceOfOrigin());
        description_tv.setText(sandwich.getDescription());
        ingredients_tv.setText(sandwich.getIngredients().toString());
        also_known_tv.setText(sandwich.getAlsoKnownAs().toString());

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

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }



    private class SandwichImageTask extends AsyncTask<String, Void, RequestCreator> {
        private String TAG = SandwichImageTask.class.getSimpleName();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected RequestCreator doInBackground(String... params) {
            String imageURL = params[0];
            RequestCreator requestCreator = null;
            try {
                requestCreator = Picasso.with(DetailActivity.this)
                        .load(imageURL);
            } catch (IllegalArgumentException e) {
                Log.e(TAG,"image URL is wrong");
            }
            return requestCreator;
        }

        @Override
        protected void onPostExecute(RequestCreator requestCreator) {
            if (requestCreator != null) {
                requestCreator.into(image_iv);
            } else {
                //TODO: set placeholder...
            }
        }


    }
}

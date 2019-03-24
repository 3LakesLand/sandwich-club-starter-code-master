package com.udacity.sandwichclub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * The Class shows a List of Sandwiches to display. The Detailed View can be accessed
 * via the selected Sandwich (Intent call).
 */
public class MainActivity extends AppCompatActivity {

    /**
     * The List of Sandwich Names is shown in the Surface.
     *
     * @param savedInstanceState If non-null, this Activity is being re-constructed
     *                           from a previous saved state as given here
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String[] sandwiches = getResources().getStringArray(R.array.sandwich_names);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, sandwiches);

        ListView listView = findViewById(R.id.sandwiches_listview);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                launchDetailActivity(position);
            }
        });
    }

    /**
     * Call Of DetailActivity via Intent. The just clicked position from the Sandwich list
     * is handed over to the Intent.
     *
     * @param position clicked position of the sandwich list
     */
    private void launchDetailActivity(int position) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(DetailActivity.EXTRA_POSITION, position);
        startActivity(intent);
    }
}

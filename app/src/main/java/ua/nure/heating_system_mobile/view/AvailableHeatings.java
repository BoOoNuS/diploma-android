package ua.nure.heating_system_mobile.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;
import java.util.stream.Collectors;

import ua.nure.heating_system_mobile.R;
import ua.nure.heating_system_mobile.model.Heating;
import ua.nure.heating_system_mobile.requestor.GatewayRequestor;

/**
 * @author Stanislav_Vorozhka
 */
public class AvailableHeatings extends Activity {

    private ListView heatingsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);

        final Intent intent = new Intent(this, HeatingSettingsActivity.class);
        long userId = getIntent().getLongExtra("userId", 0);
        validateUserId(userId);
        intent.putExtra("userId", userId);

        List<Heating> userHeatings = new GatewayRequestor().getUserHeatings(userId);
        List<String> heatingDescriptionsIds = userHeatings.stream().map(heating -> heating.getDescription() + "/" + heating.getId()).collect(Collectors.toList());

        setContentView(R.layout.activity_available_heatings);
        heatingsListView = findViewById(R.id.heatings);
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, heatingDescriptionsIds);
        heatingsListView.setAdapter(adapter);

        heatingsListView.setOnItemClickListener((parent, view, position, id) -> {
            String item = adapter.getItem(position);
            int indexOfSlash = item.indexOf("/");
            String heatingDescription = item.substring(0, indexOfSlash);
            long heatingId = Long.valueOf(item.substring(indexOfSlash + 1, item.length()));

            intent.putExtra("heatingId", heatingId);
            startActivity(intent);
        });
    }

    private void validateUserId(long userId) {
        if (userId == 0) {
            Toast.makeText(getApplicationContext(), "Incorrect user id, try to re-login", Toast.LENGTH_LONG).show();
        }
    }
}

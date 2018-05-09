package ua.nure.heating_system_mobile.view;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.IOException;

import lombok.extern.slf4j.Slf4j;
import ua.nure.heating_system_mobile.R;
import ua.nure.heating_system_mobile.model.HeatingConfiguration;
import ua.nure.heating_system_mobile.requestor.GatewayRequestor;

/**
 * @author Stanislav_Vorozhka
 */
@Slf4j
public class HeatingSettingsActivity extends AppCompatActivity {

    private SeekBar temperatureSeekBar;
    private SeekBar humiditySeekBar;
    private SeekBar exchangeSeekBar;
    private Button acceptButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heating_settings);

        Intent intent = getIntent();
        final long userId = intent.getLongExtra("userId", 0);
        final long heatingId = intent.getLongExtra("heatingId", 0);

        validateUserId(userId);
        validateHeatingId(heatingId);

        temperatureSeekBar = findViewById(R.id.temperatureSeekBar);
        humiditySeekBar = findViewById(R.id.humiditySeekBar);
        exchangeSeekBar = findViewById(R.id.exchangeSeekBar);
        acceptButton = findViewById(R.id.acceptButtpn);

        acceptButton.setOnClickListener(v -> {
            int temperature = temperatureSeekBar.getProgress();
            int humidity = humiditySeekBar.getProgress();
            int exchange = exchangeSeekBar.getProgress();
            new ChangeHeatingConfigurationTask(userId, heatingId, temperature, humidity, (byte) exchange).execute((Void) null);
        });
    }

    private void validateHeatingId(long heatingId) {
        if (heatingId == 0) {
            Toast.makeText(getApplicationContext(), "Incorrect heating id, try to chose heating again", Toast.LENGTH_LONG).show();
        }
    }

    private void validateUserId(long userId) {
        if (userId == 0) {
            Toast.makeText(getApplicationContext(), "Incorrect user id, try to re-login", Toast.LENGTH_LONG).show();
        }
    }

    private class ChangeHeatingConfigurationTask extends AsyncTask<Void, Void, Boolean> {

        private final long userId;
        private final long heatingId;
        private final float temperature;
        private final float humidity;
        private final byte exchange;

        private ChangeHeatingConfigurationTask(long userId, long heatingId, float temperature, float humidity, byte exchange) {
            this.userId = userId;
            this.heatingId = heatingId;
            this.temperature = temperature;
            this.humidity = humidity;
            this.exchange = exchange;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                new GatewayRequestor().reConfigHeating(userId, heatingId, new HeatingConfiguration(temperature, humidity, exchange));
            } catch (IOException e) {
                log.warn("HeatingSettingsActivity#ChangeHeatingConfigurationTask#reConfigHeating cannot connect to server", e);
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {

            if (success) {
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
            }
        }
    }
}

package fr.xiaomi.powercontrol;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {
    @Override protected void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_main);
        MaterialButton shutdown = findViewById(R.id.shutdownButton);
        MaterialButton reboot = findViewById(R.id.rebootButton);
        shutdown.setOnClickListener(v -> openConfirm(PowerConfirmActivity.ACTION_SHUTDOWN));
        reboot.setOnClickListener(v -> openConfirm(PowerConfirmActivity.ACTION_REBOOT));
        TextView widgets = findViewById(R.id.widgetHint);
        widgets.setText("2 widgets disponibles : Arrêt et Redémarrage. Maintiens le doigt sur l’écran d’accueil, puis Widgets > Xiaomi Power Control.");
    }
    private void openConfirm(String action) {
        Intent i = new Intent(this, PowerConfirmActivity.class);
        i.putExtra(PowerConfirmActivity.EXTRA_ACTION, action);
        startActivity(i);
    }
}

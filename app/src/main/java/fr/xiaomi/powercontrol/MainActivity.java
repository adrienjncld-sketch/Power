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
        bind(R.id.shutdownButton, PowerConfirmActivity.ACTION_SHUTDOWN);
        bind(R.id.rebootButton, PowerConfirmActivity.ACTION_REBOOT);
        bind(R.id.recoveryButton, PowerConfirmActivity.ACTION_RECOVERY);
        bind(R.id.fastbootButton, PowerConfirmActivity.ACTION_FASTBOOT);
        bind(R.id.softRebootButton, PowerConfirmActivity.ACTION_SOFT_REBOOT);
        bind(R.id.systemUiButton, PowerConfirmActivity.ACTION_SYSTEM_UI);
        TextView widgets = findViewById(R.id.widgetHint);
        widgets.setText("Deux widgets sont disponibles : Arrêt et Redémarrage. Depuis l’écran d’accueil, ouvre le sélecteur de widgets puis Xiaomi Power Control.");
    }

    private void bind(int id, String action) {
        MaterialButton button = findViewById(id);
        button.setOnClickListener(v -> {
            Intent intent = new Intent(this, PowerConfirmActivity.class);
            intent.putExtra(PowerConfirmActivity.EXTRA_ACTION, action);
            startActivity(intent);
        });
    }
}

package fr.xiaomi.powercontrol;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class PowerConfirmActivity extends AppCompatActivity {
    public static final String EXTRA_ACTION = "power_action";
    public static final String ACTION_SHUTDOWN = "shutdown";
    public static final String ACTION_REBOOT = "reboot";

    @Override protected void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_confirm);
        String action = getIntent().getStringExtra(EXTRA_ACTION);
        boolean shutdown = ACTION_SHUTDOWN.equals(action);
        ((TextView)findViewById(R.id.confirmTitle)).setText(shutdown ? "Éteindre le téléphone ?" : "Redémarrer le téléphone ?");
        ((TextView)findViewById(R.id.confirmDescription)).setText(shutdown
            ? "Le Xiaomi 14T Pro va s’éteindre complètement."
            : "Le Xiaomi 14T Pro va redémarrer.");
        MaterialButton confirm = findViewById(R.id.confirmButton);
        MaterialButton cancel = findViewById(R.id.cancelButton);
        confirm.setText(shutdown ? "ÉTEINDRE" : "REDÉMARRER");
        confirm.setOnClickListener(v -> {
            Intent service = new Intent(this, RootPowerService.class);
            service.putExtra(EXTRA_ACTION, action);
            androidx.core.content.ContextCompat.startForegroundService(this, service);
            finish();
        });
        cancel.setOnClickListener(v -> finish());
    }
}

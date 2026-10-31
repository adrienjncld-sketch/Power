package fr.xiaomi.powercontrol;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;

public class PowerConfirmActivity extends AppCompatActivity {
    public static final String EXTRA_ACTION = "power_action";
    public static final String ACTION_SHUTDOWN = "shutdown";
    public static final String ACTION_REBOOT = "reboot";
    public static final String ACTION_RECOVERY = "recovery";
    public static final String ACTION_FASTBOOT = "fastboot";
    public static final String ACTION_SOFT_REBOOT = "soft_reboot";
    public static final String ACTION_SYSTEM_UI = "system_ui";

    @Override protected void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_confirm);
        String action = getIntent().getStringExtra(EXTRA_ACTION);
        String title;
        String description;
        String button;
        switch (action == null ? "" : action) {
            case ACTION_SHUTDOWN:
                title = "Éteindre le téléphone ?";
                description = "Le Xiaomi 14T Pro va s’éteindre complètement.";
                button = "ÉTEINDRE";
                break;
            case ACTION_RECOVERY:
                title = "Redémarrer en Recovery ?";
                description = "Le téléphone va redémarrer dans le recovery installé.";
                button = "DÉMARRER EN RECOVERY";
                break;
            case ACTION_FASTBOOT:
                title = "Redémarrer en Fastboot ?";
                description = "Le téléphone va ouvrir le mode Fastboot. Un ordinateur peut être nécessaire pour en sortir.";
                button = "DÉMARRER EN FASTBOOT";
                break;
            case ACTION_SOFT_REBOOT:
                title = "Redémarrage logiciel ?";
                description = "Android va redémarrer sans éteindre complètement le téléphone.";
                button = "SOFT REBOOT";
                break;
            case ACTION_SYSTEM_UI:
                title = "Relancer System UI ?";
                description = "L’interface HyperOS peut disparaître quelques secondes pendant son redémarrage.";
                button = "RELANCER L’INTERFACE";
                break;
            default:
                action = ACTION_REBOOT;
                title = "Redémarrer le téléphone ?";
                description = "Le Xiaomi 14T Pro va redémarrer normalement.";
                button = "REDÉMARRER";
        }
        ((TextView)findViewById(R.id.confirmTitle)).setText(title);
        ((TextView)findViewById(R.id.confirmDescription)).setText(description);
        MaterialButton confirm = findViewById(R.id.confirmButton);
        MaterialButton cancel = findViewById(R.id.cancelButton);
        confirm.setText(button);
        confirm.setOnClickListener(v -> {
            Intent service = new Intent(this, RootPowerService.class);
            service.putExtra(EXTRA_ACTION, action);
            androidx.core.content.ContextCompat.startForegroundService(this, service);
            finish();
        });
        cancel.setOnClickListener(v -> finish());
    }
}

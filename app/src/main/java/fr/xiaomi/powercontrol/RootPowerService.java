package fr.xiaomi.powercontrol;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class RootPowerService extends Service {
    private static final String CHANNEL = "power_control";
    @Override public void onCreate() {
        super.onCreate();
        NotificationManager nm = getSystemService(NotificationManager.class);
        if (Build.VERSION.SDK_INT >= 26) nm.createNotificationChannel(new NotificationChannel(CHANNEL, "Commandes d’alimentation", NotificationManager.IMPORTANCE_LOW));
    }
    @Override public int onStartCommand(Intent intent, int flags, int startId) {
        Notification n = new Notification.Builder(this, CHANNEL)
            .setSmallIcon(R.drawable.ic_power)
            .setContentTitle("Xiaomi Power Control")
            .setContentText("Exécution de la commande système…")
            .setOngoing(true).build();
        startForeground(7, n);
        String action = intent == null ? "" : intent.getStringExtra(PowerConfirmActivity.EXTRA_ACTION);
        new Thread(() -> {
            String command = PowerConfirmActivity.ACTION_SHUTDOWN.equals(action) ? "reboot -p" : "reboot";
            try {
                Process p = new ProcessBuilder("su", "-c", command).redirectErrorStream(true).start();
                StringBuilder output = new StringBuilder();
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
                    String line; while ((line = reader.readLine()) != null) output.append(line).append('\n');
                }
                int code = p.waitFor();
                if (code != 0) {
                    android.os.Handler h = new android.os.Handler(getMainLooper());
                    h.post(() -> Toast.makeText(this, "Commande root refusée. Vérifie l’autorisation Magisk.", Toast.LENGTH_LONG).show());
                }
            } catch (Exception e) {
                android.os.Handler h = new android.os.Handler(getMainLooper());
                h.post(() -> Toast.makeText(this, "Accès root indisponible : " + e.getMessage(), Toast.LENGTH_LONG).show());
            } finally {
                stopForeground(STOP_FOREGROUND_REMOVE);
                stopSelf(startId);
            }
        }, "root-power-command").start();
        return START_NOT_STICKY;
    }
    @Override public IBinder onBind(Intent intent) { return null; }
}

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
        NotificationManager manager = getSystemService(NotificationManager.class);
        if (Build.VERSION.SDK_INT >= 26) {
            manager.createNotificationChannel(new NotificationChannel(
                CHANNEL, "Commandes d’alimentation", NotificationManager.IMPORTANCE_LOW));
        }
    }

    @Override public int onStartCommand(Intent intent, int flags, int startId) {
        Notification notification = new Notification.Builder(this, CHANNEL)
            .setSmallIcon(R.drawable.ic_power)
            .setContentTitle("Xiaomi Power Control")
            .setContentText("Exécution de la commande système…")
            .setOngoing(true)
            .build();
        startForeground(7, notification);

        String action = intent == null ? "" : intent.getStringExtra(PowerConfirmActivity.EXTRA_ACTION);
        String command = commandFor(action);
        if (command == null) {
            stopForeground(STOP_FOREGROUND_REMOVE);
            stopSelf(startId);
            return START_NOT_STICKY;
        }

        final String rootCommand = command;
        new Thread(() -> {
            try {
                Process process = new ProcessBuilder("su", "-c", rootCommand)
                    .redirectErrorStream(true).start();
                StringBuilder output = new StringBuilder();
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(process.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) output.append(line).append('\n');
                }
                int result = process.waitFor();
                if (result != 0) showToast("Commande refusée. Vérifie l’autorisation root dans Magisk.");
            } catch (Exception error) {
                showToast("Accès root indisponible : " + error.getMessage());
            } finally {
                stopForeground(STOP_FOREGROUND_REMOVE);
                stopSelf(startId);
            }
        }, "xiaomi-root-power").start();
        return START_NOT_STICKY;
    }

    private String commandFor(String action) {
        if (PowerConfirmActivity.ACTION_SHUTDOWN.equals(action)) return "reboot -p";
        if (PowerConfirmActivity.ACTION_REBOOT.equals(action)) return "reboot";
        if (PowerConfirmActivity.ACTION_RECOVERY.equals(action)) return "reboot recovery";
        if (PowerConfirmActivity.ACTION_FASTBOOT.equals(action)) return "reboot bootloader";
        if (PowerConfirmActivity.ACTION_SOFT_REBOOT.equals(action)) return "setprop ctl.restart zygote";
        if (PowerConfirmActivity.ACTION_SYSTEM_UI.equals(action)) return "killall com.android.systemui";
        return null;
    }

    private void showToast(String message) {
        new android.os.Handler(getMainLooper()).post(
            () -> Toast.makeText(this, message, Toast.LENGTH_LONG).show());
    }

    @Override public IBinder onBind(Intent intent) { return null; }
}

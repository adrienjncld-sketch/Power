package fr.xiaomi.powercontrol;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class RebootWidgetProvider extends AppWidgetProvider {
    @Override public void onUpdate(Context context, AppWidgetManager manager, int[] ids) {
        for (int id : ids) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_reboot);
            Intent intent = new Intent(context, PowerConfirmActivity.class);
            intent.putExtra(PowerConfirmActivity.EXTRA_ACTION, PowerConfirmActivity.ACTION_REBOOT);
            PendingIntent pi = PendingIntent.getActivity(context, 102, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
            views.setOnClickPendingIntent(R.id.widgetReboot, pi);
            manager.updateAppWidget(id, views);
        }
    }
}

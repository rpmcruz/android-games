package com.cruz.cruzsolitaire2;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;

public class Game extends AppCompatActivity
{
    private GamePanel panel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        panel = new GamePanel(this);
        setContentView(panel);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 0, 0, "New Game");
        menu.add(0, 1,1, "About");
/*		menu.add(0, 2,2, "Set Horizontal");
		menu.add(0, 3,3, "Set Vertical");*/
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case 0: panel.restart(); break;
            case 1: showAboutDialog(); break;
/*			case 2: setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); break;
			case 3: setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); break;*/
        }
        return true;
    }

    public static void showMessageDialog(Context context, String title, String text, String button) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle(title);
        dialog.setMessage(text);
        dialog.setPositiveButton(button, null);
        dialog.show();
    }

    private void showAboutDialog() {
        showMessageDialog(this, "Developed by", "\u00A9 2015 Ricardo Cruz <ricardo.pdm.cruz@gmail.com>", "OK");
    }
}
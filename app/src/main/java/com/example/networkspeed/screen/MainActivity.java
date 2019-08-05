package com.example.networkspeed.screen;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.AdapterView.*;
import android.view.View;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.networkspeed.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements OnItemSelectedListener {
    private Spinner spinner;
    ArrayList<String> paths = new ArrayList<String>();
    ArrayList<String> execApp = new ArrayList<String>();
    PackageManager pm;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);
        Intent intent = new Intent(this.getIntent());

        pm = getPackageManager();
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
        paths.add("Application Lists");
        execApp.add("");

        for (ApplicationInfo packageInfo : packages) {
            String tmp;
            tmp = (String) (packageInfo != null ? pm.getApplicationLabel(packageInfo) : "Unknown");
            if(!tmp.startsWith("com")) {
                paths.add(tmp);
                execApp.add(packageInfo.packageName);
            }
        }

        spinner = (Spinner)findViewById(R.id.platform_list);
        ArrayAdapter<String>adapter = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_spinner_item, paths);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
        Intent startApp = pm.getLaunchIntentForPackage(execApp.get(position));
        if(startApp != null) {
            try {
                startActivity(startApp);
            } catch (ActivityNotFoundException err) {
                Toast t = Toast.makeText(getApplicationContext(),
                        "Application not found", Toast.LENGTH_SHORT);
                t.show();
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // TODO Auto-generated method stub
    }

    public static String getApplicationName(Context context) {
        ApplicationInfo applicationInfo = context.getApplicationInfo();
        int stringId = applicationInfo.labelRes;
        return stringId == 0 ? applicationInfo.nonLocalizedLabel.toString() : context.getString(stringId);
    }
}

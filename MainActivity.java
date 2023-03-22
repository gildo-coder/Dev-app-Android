package com.example.dgild;


import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    //adapter view
    private  ListView mList=null;
    //Repertoire actuel
    private File mCurrentFile=null;
    private FileAdapter adapter;
    private List<File> list;
    private Button retour=null;
    private Button mButtonPref=null;
    private Preference mPreference=null;
    int mColor;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //recuper notre list view (interface graphique)
       // mList=getListView(); pour ListActivity
        mList=findViewById(R.id.main_listView);
        retour=findViewById(R.id.main_b);
        mButtonPref=findViewById(R.id.main_pref);
        /*mPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                String key=preference.getKey();

                if(newValue!=null && key.equals("repertoireColorPref") ){
                    mColor = (Integer) newValue;
                    return true;
                }
                return false;
            }
        });*/

        //recuperer notre repertoire racine (memoire interne)
       // mCurrentFile= Environment.getRootDirectory();
        mCurrentFile=Environment.getRootDirectory();
        //titre de notre activity
        setTitle(mCurrentFile.getAbsolutePath());
        //recuperation de fichiers et dossier
        File[] fichiers=mCurrentFile.listFiles();
        //on met dans une list plus efficace
        list=new ArrayList<>();
        mColor= Color.RED;
        for(File f:fichiers){
            list.add(f);
        }
        adapter=new FileAdapter(this, android.R.layout.simple_list_item_1, list);
        mList.setAdapter(adapter);
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                File fichier=adapter.getItem(position);
                if(fichier.isDirectory()){
                    //on met à jour le repertoire courant
                    update(fichier);

                }
                if(fichier.isFile()){
                    //lecture du fichier
                    lireFichier(fichier);
                }
            }
        });
       /* mList.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                menu.add("Ouvrir ou lire");
                menu.add("Supprimer");
                menu.add("Detail");

            }
        });*/
        mList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Intent activity=new Intent(MainActivity.this, MenuActivity.class);
                activity.putExtra("ke", "eli");
                try {
                    startActivity(activity);
                    return true;
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                return false;
            }
        });
       retour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              File pFille=  mCurrentFile.getParentFile();
              update(pFille);
            }
        });
       mButtonPref.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent secondeActivity=new Intent(MainActivity.this, PrefActivity.class);
               secondeActivity.putExtra("KEY", "red");
               startActivity(secondeActivity);
           }
       });
    }
    void update(File fichier){
        mCurrentFile=fichier;
        setTitle(mCurrentFile.getAbsolutePath());
        File[] f=mCurrentFile.listFiles();
        adapter.clear();
        for(File r: f)
            adapter.add(r);


    }
    void lireFichier(File fichier){
        Intent autreActivity=new Intent(Intent.ACTION_VIEW);
        autreActivity.setDataAndType(Uri.fromFile(fichier), "audio/mp3");
        try {
            startActivity(autreActivity);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
   public void onSharedPreferenceChanged(SharedPreferences preferences, String key){
        mColor=preferences.getInt("repertoireColorPref", Color.BLACK);
    }
    //class interne
    class FileAdapter extends ArrayAdapter<File>{
        List<File> list;
        LayoutInflater mInflate;
        //constructeur
        public FileAdapter(Context context, int  layout, List<File> list){
            super(context, layout, list);
            this.list=list;
            mInflate=LayoutInflater.from(context);
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            TextView vue;  //pane correspond au Text
            //convertView : panel correspond au position position
            File item=list.get(position);
            //File items=getItem(position);
            if(convertView!=null)
                vue=(TextView) convertView;
            else
                 vue=(TextView) mInflate.inflate(android.R.layout.simple_list_item_1, null);
            if(item.isDirectory()){
                vue.setTextColor(mColor);
            }
            else
                vue.setTextColor(Color.BLACK);
            vue.setText(item.getName());

            return vue;
        }
    }

}
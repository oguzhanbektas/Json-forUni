package com.bektasoguzhan.jsonilveilceler;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<String> districtNames = new ArrayList<String>();
    ListView mDistrictListView;
    Button mSearchButton;
    EditText mSearchText;
    TextView mProvinceTextView;
    String search = "", province;
    String[] postaKodlariX = new String[50];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDistrictListView = findViewById(R.id.districtListView);
        mSearchButton = findViewById(R.id.buttonSearch);
        mProvinceTextView = findViewById(R.id.provinceTextView);
        mSearchText = findViewById(R.id.searchText);
        //Deneme Yayını
       /* districtNames.add("Bayrampaşa");
        districtNames.add("Gaziosmanpaşa");
        districtNames.add("Kağıthane");
        districtNames.add("Fatih");
        */
        mDistrictListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    Toast.makeText(getApplicationContext(), postaKodlariX[position], Toast.LENGTH_LONG).show();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    public String getJson() {
        String json = null;
        try {
            // turkey.json ' u açtık
            InputStream is = getAssets().open("turkey.json");
            //Json un içinde birşey var mı ?
            int size = is.available();
            System.out.println("Size -->" + size);
            byte[] buffer = new byte[size];
            // Dizideki değerleri okuduk
            is.read(buffer);
            is.close();
            // convert byte to string
            json = new String(buffer, "UTF-8");
            //districtNames.add(json);
            // System.out.println(json);
        } catch (IOException ex) {
            ex.printStackTrace();
            return json;
        }

        return json;
    }

    void obj_list(String search) {
        try {
            // Tekrardan string öğeleri JSON objesine döndürüldü
            JSONObject jsonObject = new JSONObject(getJson());
            // Json array e atandı.
            JSONArray array = jsonObject.getJSONArray("array");
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                String plaka = object.getString("plaka");
                if (plaka.equals(search)) {//Eğer JSONdaki il ile aranan il plakası tutuyorsa buradaki if döngüsüne girer.
                    province = object.getString("il");
                    String district = object.getString("ilce");
                    String postakodu = object.getString("postakodlari");
                    //Kesme işlemleri için regexden yardım alındı.
                    String[] postakodlari = postakodu.split("\",");
                    String[] districts = district.split("\",");
                    String araDeger = "";
                    String araDegerPostaKodu = "";
                    for (int k = 0; k < districts.length; k++) {
                        araDeger += districts[k] + " ";
                    }
                    for (int x = 0; x < postakodlari.length; x++) {
                        araDegerPostaKodu += postakodlari[x];
                    }
                    //Denemek için yazıldı
                    //districtNames.add(araDeger);
                    postakodlari = araDegerPostaKodu.split("\"");
                    districts = araDeger.split("\"");
                    for (int z = 1; z < districts.length - 1; z++) {
                        districtNames.add(districts[z]);
                    }
                    for (int g = 1; g < postakodlari.length - 1; g++) {
                        try {
                            postaKodlariX[g - 1] = postakodlari[g];
                        } catch (Exception ex) {

                        }
                        //  postaKodlariX[g - 1] = postakodlari[g];
                    }
                }

            }
            mProvinceTextView.setText(province);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void search(View view) {
        search = mSearchText.getText().toString();
        int value = Integer.parseInt(search);
        if (search.length() == 0) {
            Toast.makeText(this, "Lütfen posta kodu giriniz.", Toast.LENGTH_LONG).show();
        } else if (search.length() < 5) {
            Toast.makeText(this, "Lütfen Doğru Değer giriniz.", Toast.LENGTH_LONG).show();
        } else if (search.length() > 5) {
            Toast.makeText(this, "Lütfen Doğru Değer giriniz.", Toast.LENGTH_LONG).show();
        } else if (value > 81000) {
            Toast.makeText(this, "Lütfen Doğru Değer giriniz.", Toast.LENGTH_LONG).show();
        } else {
            districtNames.clear();
            obj_list(search);
        }
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, districtNames);
        mDistrictListView.setAdapter(arrayAdapter);
    }
}
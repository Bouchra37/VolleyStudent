package ma.projet.projetws;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import com.google.gson.reflect.TypeToken;
import android.app.AlertDialog;
import android.content.DialogInterface;


import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import ma.projet.projetws.beans.Etudiant;

public class AddEtudiant extends AppCompatActivity implements View.OnClickListener {
    private EditText nom;
    private EditText prenom;
    private Spinner ville;
    private RadioButton m;
    private RadioButton f;
    private Button add;

    RequestQueue requestQueue;
    String insertUrl = "http://192.168.1.114/projetVolley/ws/createEtudiant.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_etudiant);

        nom = findViewById(R.id.nom);
        prenom = findViewById(R.id.prenom);
        ville = findViewById(R.id.ville);
        add = findViewById(R.id.add);
        m = findViewById(R.id.m);
        f = findViewById(R.id.f);
        add.setOnClickListener(this);
        Button retourButton = findViewById(R.id.btnRetour);

        retourButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddEtudiant.this, ListActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onClick(View v) {
        Log.d("ok", "ok");
        if (v == add) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
            StringRequest request = new StringRequest(Request.Method.POST,
                    insertUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //Log.d(TAG, response);
                    Type type = new TypeToken<Collection<Etudiant>>() {
                    }.getType();
                    Collection<Etudiant> etudiants = new Gson().fromJson(response, type);
                    for (Etudiant e : etudiants) {
                       // Log.d(TAG, e.toString());
                    }

                    AlertDialog.Builder builder = new AlertDialog.Builder(AddEtudiant.this);
                    builder.setMessage("Ajout avec succès")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    nom.setText("");
                                    prenom.setText("");
                                    ville.setSelection(0);
                                    m.setChecked(false);
                                    f. setChecked(false);
                                }
                            });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    String sexe = "";
                    if (m.isChecked())
                        sexe = "homme";
                    else
                        sexe = "femme";
                    HashMap<String, String> params = new HashMap<String, String>();
                    params.put("nom", nom.getText().toString());
                    params.put("prenom", prenom.getText().toString());
                    params.put("ville", ville.getSelectedItem().toString());
                    params.put("sexe", sexe);
                    return params;
                }
            };
            requestQueue.add(request);
        }
    }
}

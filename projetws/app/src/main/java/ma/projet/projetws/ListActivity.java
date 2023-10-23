package ma.projet.projetws;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ma.projet.projetws.adapter.EtudiantAdapter;
import ma.projet.projetws.beans.Etudiant;

public class ListActivity extends AppCompatActivity {

    private static final String URL_LOAD = "http://192.168.1.114/projetVolley/ws/loadEtudiant.php";
    private static final String URL_DELETE = "http://192.168.1.114/projetVolley/controller/deleteEtudiant.php";

    private ListView listView;
    private EtudiantAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        listView = findViewById(R.id.list);
        adapter = new EtudiantAdapter(this, R.layout.item, new ArrayList<>());
        listView.setAdapter(adapter);
        Button addButton = findViewById(R.id.btnAdd);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListActivity.this, AddEtudiant.class);
                startActivity(intent);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showDeleteConfirmationDialog(position);
            }
        });

        loadStudents();
    }

    private void showDeleteConfirmationDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.confirm_delete);
        builder.setMessage(R.string.confirm_delete_message);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Etudiant etudiantToDelete = adapter.getItem(position);
                int studentId = etudiantToDelete.getId();
                deleteStudent(studentId, position);
                adapter.clear();
                loadStudents();
            }
        });
        builder.setNegativeButton(R.string.no, null);
        builder.show();
    }

    private void deleteStudent(int studentId, final int position) {
        String url = URL_DELETE + "?id=" + studentId;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                            adapter.remove(adapter.getItem(position));
                            adapter.notifyDataSetChanged();

                            Toast.makeText(ListActivity.this, R.string.delete_success, Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley Error", "Erreur de suppression : " + error.toString());
                        Toast.makeText(ListActivity.this, R.string.delete_failed, Toast.LENGTH_LONG).show();
                    }
                }
        );

        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void loadStudents() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_LOAD,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                int id = jsonObject.getInt("id");
                                String etudnom = jsonObject.getString("nom");
                                String etudprenom = jsonObject.getString("prenom");
                                String etudville = jsonObject.getString("ville");
                                String etudsexe = jsonObject.getString("sexe");
                                Etudiant etudiant = new Etudiant(id, etudnom, etudprenom, etudville, etudsexe);
                                adapter.add(etudiant);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }
        );

        Volley.newRequestQueue(this).add(stringRequest);
    }
}

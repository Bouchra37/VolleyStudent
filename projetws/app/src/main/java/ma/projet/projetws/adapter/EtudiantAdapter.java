package ma.projet.projetws.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import ma.projet.projetws.R;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import ma.projet.projetws.beans.Etudiant;

public class EtudiantAdapter extends ArrayAdapter<Etudiant> {

    private static class ViewHolder {
        TextView id;
        TextView nom;
        TextView prenom;
        TextView ville;
        TextView sexe;
    }

    public EtudiantAdapter(@NonNull Context context, int resource, @NonNull List<Etudiant> etudiants) {
        super(context, resource, etudiants);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        View item = convertView;

        if (item == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            item = inflater.inflate(R.layout.item, parent, false);

            holder = new ViewHolder();
            holder.id = item.findViewById(R.id.id);
            holder.nom = item.findViewById(R.id.nom);
            holder.prenom = item.findViewById(R.id.prenom);
            holder.ville = item.findViewById(R.id.ville);
            holder.sexe = item.findViewById(R.id.sexe);

            item.setTag(holder);
        } else {
            holder = (ViewHolder) item.getTag();
        }

        Etudiant st = getItem(position);
        if (st != null) {
            holder.id.setText(String.valueOf(st.getId()));
            holder.nom.setText(st.getNom());
            holder.prenom.setText(st.getPrenom());
            holder.ville.setText(st.getVille());
            holder.sexe.setText(st.getSexe());
        }

        return item;
    }

}

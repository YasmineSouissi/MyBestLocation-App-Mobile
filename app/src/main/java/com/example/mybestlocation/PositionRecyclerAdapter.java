package com.example.mybestlocation;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mybestlocation.ui.home.MapsActivity;

import java.util.ArrayList;

public class PositionRecyclerAdapter extends RecyclerView.Adapter<PositionRecyclerAdapter.PositionViewHolder> {

    private Context context;
    private ArrayList<Position> positionList;
    private PositionServices positionServices = new PositionServices();

    public PositionRecyclerAdapter(Context context, ArrayList<Position> positionList) {
        this.context = context;
        this.positionList = positionList;
    }

    @NonNull
    @Override
    public PositionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_position, parent, false);
        return new PositionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PositionViewHolder holder, int position) {
        Position currentPosition = positionList.get(position);

        // Remplir les données de la position
        holder.tvPseudo.setText(currentPosition.getPseudo());
        holder.tvLatitude.setText(String.format("Latitude: %.6f", currentPosition.latitude));
        holder.tvLongitude.setText(String.format("Longitude: %.6f", currentPosition.longitude));

        // Action du bouton de suppression
        holder.ivSupp.setOnClickListener(v -> {
            Log.d("POSITION_DELETE", "Suppression demandée pour la position: ID = " + currentPosition.idposition);
            new DeleteTask(currentPosition.idposition, holder.getAdapterPosition()).execute();
        });
        holder.ivMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MapsActivity.class);

                intent.putExtra("position_id", currentPosition.idposition);
                Log.d("Locations to MapsActivity", "Position ID sent: " + currentPosition.getIdUser());
                //intent.putExtra("user_name", currentPosition.getIdUser());
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return positionList.size();
    }

    public static class PositionViewHolder extends RecyclerView.ViewHolder {
        TextView tvPseudo, tvLatitude, tvLongitude;
        ImageView ivSupp;
        ImageView ivMap;

        public PositionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPseudo = itemView.findViewById(R.id.tv_pseudo);
            tvLatitude = itemView.findViewById(R.id.tv_latitude);
            tvLongitude = itemView.findViewById(R.id.tv_longitude);
            ivSupp = itemView.findViewById(R.id.iv_delete); // Bouton de suppression
            ivMap= itemView.findViewById(R.id.iv_map_locations);
        }
    }


    public class DeleteTask extends AsyncTask<Integer, Void, Boolean> {
        private static final String TAG_DELETE = "PositionDeleteTask";
        private int positionId;
        private int adapterPosition;

        public DeleteTask(int positionId, int adapterPosition) {
            this.positionId = positionId;
            this.adapterPosition = adapterPosition;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d(TAG_DELETE, "Démarrage de la suppression de la position ID = " + positionId);
        }

        @Override
        protected Boolean doInBackground(Integer... voids) {
            PositionServices positionServices = new PositionServices();
            try {
                return positionServices.deletePosition(positionId);
            } catch (Exception e) {
                Log.e(TAG_DELETE, "Erreur lors de la suppression", e);
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean isDeleted) {
            super.onPostExecute(isDeleted);
            if (isDeleted) {
                Log.d(TAG_DELETE, "Position supprimée avec succès");
                Toast.makeText(context, "Position supprimée avec succès", Toast.LENGTH_SHORT).show();

                // Supprimer la position de la liste
                positionList.remove(adapterPosition);

                // Notifier l'adaptateur
                notifyItemRemoved(adapterPosition);
                notifyItemRangeChanged(adapterPosition, positionList.size());
            } else {
                Log.d(TAG_DELETE, "Échec de la suppression de la position");
                Toast.makeText(context, "Échec de la suppression de la position", Toast.LENGTH_SHORT).show();
            }
        }
    }

}

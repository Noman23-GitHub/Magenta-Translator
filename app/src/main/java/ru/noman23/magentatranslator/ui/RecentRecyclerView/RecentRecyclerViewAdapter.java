package ru.noman23.magentatranslator.ui.RecentRecyclerView;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ru.noman23.magentatranslator.R;
import ru.noman23.magentatranslator.network.translate.TranslateEntity;

public class RecentRecyclerViewAdapter extends RecyclerView.Adapter<RecentViewHolder> {

    private List<TranslateEntity> translateEntities;

    public RecentRecyclerViewAdapter(List<TranslateEntity> translateEntities) {
        this.translateEntities = translateEntities;
    }

    @NonNull
    @Override
    public RecentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_recent_view, parent, false);
        return new RecentViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return translateEntities.size();
    }

    @Override
    public void onBindViewHolder(@NonNull RecentViewHolder holder, int position) {
        if (translateEntities.get(position) == null) return;

        TranslateEntity translateEntity = translateEntities.get(position);

        if (translateEntity.getDef().size() <= 0 || translateEntity.getDef().get(0).getTr().size() <= 0)
            return;

        holder.mFromTextView.setText(translateEntity.getDef().get(0).getText());
        holder.mToTextView.setText(translateEntity.getDef().get(0).getTr().get(0).getText());
    }
}

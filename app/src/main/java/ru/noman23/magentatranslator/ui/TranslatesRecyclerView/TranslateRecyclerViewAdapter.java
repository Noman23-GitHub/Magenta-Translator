package ru.noman23.magentatranslator.ui.TranslatesRecyclerView;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ru.noman23.magentatranslator.R;
import ru.noman23.magentatranslator.network.translate.TranslateEntity;
import ru.noman23.magentatranslator.network.translate.sup.Def;
import ru.noman23.magentatranslator.network.translate.sup.Syn;
import ru.noman23.magentatranslator.network.translate.sup.Tr;

public class TranslateRecyclerViewAdapter extends RecyclerView.Adapter<TranslateViewHolder> {

    private List<TranslateEntity> translateEntities;

    public TranslateRecyclerViewAdapter(List<TranslateEntity> translateEntities) {
        this.translateEntities = translateEntities;
    }

    @NonNull
    @Override
    public TranslateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_main_view, parent, false);
        return new TranslateViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return translateEntities.size();
    }

    @Override
    public void onBindViewHolder(@NonNull TranslateViewHolder holder, int position) {
        // TODO перелпатить тут код, обаботать все как положено
        if (translateEntities.get(position) == null) return;

        TranslateEntity translateEntity = translateEntities.get(position);

        if (translateEntity.getDef().size() <= 0 || translateEntity.getDef().get(0).getTr().size() <= 0)
            return;

        holder.mFromTextView.setText(translateEntity.getDef().get(0).getText());
        holder.mToTextView.setText(translateEntity.getDef().get(0).getTr().get(0).getText());
        holder.mSpoilerContainerView.removeAllViews();
        holder.mLangDirectionTextView.setText(translateEntity.getLang());

        for (Def def : translateEntity.getDef()) {
            StringBuilder generatedLine = new StringBuilder(def.getText() + " [" + def.getPos() + "]\n");
            if (def.getTr() != null) {
                for (Tr tr : def.getTr()) {
                    generatedLine.append("    ").append(def.getTr().indexOf(tr) + 1).append(". ").append(tr.getText());
                    if (tr.getSyn() != null) {
                        for (Syn syn : tr.getSyn()) {
                            generatedLine.append(", ").append(syn.getText());
                        }
                    }
                    generatedLine.append("\n");
                }
            }

            TextView defView = new TextView(holder.mSpoilerContainerView.getContext());
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            defView.setLayoutParams(params);
            defView.setText(generatedLine.toString());
            holder.mSpoilerContainerView.addView(defView);
        }
    }

    public void clear() {
        translateEntities.clear();
        notifyDataSetChanged();
    }

    public void addItems(List<TranslateEntity> items) {
        translateEntities.addAll(items);
        notifyDataSetChanged();
    }

    public void addItem(TranslateEntity item) {
        translateEntities.add(item);
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        translateEntities.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(TranslateEntity item, int position) {
        translateEntities.add(position, item);
        notifyItemInserted(position);
    }

    public void saveItem(int position) {
        notifyItemChanged(position);
        // TODO ?Кидаем флаг на этот айтем что он TYPE_SAVED (onSwipe)
    }

    public void unsaveItem(int position) {
        notifyItemChanged(position);
        // TODO ?Кидаем флаг на этот айтем что он больше НЕ TYPE_SAVED (onSwipe)
    }

    public List<TranslateEntity> getTranslateEntities() {
        return translateEntities;
    }
}

package ru.noman23.magentatranslator.ui.TranslatesRecyclerView;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.support.v7.widget.helper.ItemTouchHelper.Callback;
import android.util.DisplayMetrics;
import android.view.View;

import javax.inject.Inject;

import ru.noman23.magentatranslator.DaggerMagentaComponent;
import ru.noman23.magentatranslator.R;
import ru.noman23.magentatranslator.activities.MainActivity;
import ru.noman23.magentatranslator.database.tables.TranslatesDao;
import ru.noman23.magentatranslator.database.tables.TranslatesDaoWrapper;
import ru.noman23.magentatranslator.network.translate.TranslateEntity;

import static android.support.v7.widget.helper.ItemTouchHelper.LEFT;

public class TranslateViewSwipeController extends Callback {

    // TODO Тут бардак, нужно прибрать, укомплектовать код отрисовки задников, onSwipe тоже облагородить

    @Inject TranslatesDaoWrapper mDatabase;

    private Activity mActivity;
    private TranslateRecyclerViewAdapter mAdapter;

    public TranslateViewSwipeController(Activity activity, TranslateRecyclerViewAdapter adapter) {
        DaggerMagentaComponent.builder().context(activity).build().inject(this);
        this.mActivity = activity;
        this.mAdapter = adapter;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        // FIXME Костыль чтобы элемент не свайпался на майн активити
        if (mActivity instanceof MainActivity)
            return makeMovementFlags(0, 0);
        // FIXME (TAG: Save) Убран флаг свайпа для сейва RIGHT
        return makeMovementFlags(0, LEFT);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    public static final float ALPHA_FULL = 1.0f;

    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

            View itemView = viewHolder.itemView;
            Paint p = new Paint();
            Bitmap icon;

            if (dX > 0) {
                //color : left side (swiping towards right)
                p.setColor(ContextCompat.getColor(recyclerView.getContext(), R.color.item_green));
                c.drawRect((float) itemView.getLeft(), (float) itemView.getTop(), dX,
                        (float) itemView.getBottom(), p);

                //icon : left side (swiping towards right)
                //icon = BitmapFactory.decodeResource(mActivity.getApplicationContext().getResources(), R.drawable.icon_save_96px);


                Drawable drawable = ContextCompat.getDrawable(recyclerView.getContext(), R.drawable.icon_save_96px);
                //drawable = DrawableCompat.wrap(drawable).mutate();
                //drawable.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(recyclerView.getContext(), R.color.white),
                //        PorterDuff.Mode.SRC_IN));
                drawable.draw(c);

                /*c.drawBitmap(icon,
                        (float) itemView.getLeft() + convertDpToPx(16),
                        (float) itemView.getTop() + ((float) itemView.getBottom() - (float) itemView.getTop() - icon.getHeight()) / 2,
                        p);*/
            } else {

                //color : right side (swiping towards left)
                //p.setARGB(255, 255, 0, 0);
                p.setColor(ContextCompat.getColor(recyclerView.getContext(), R.color.item_red));

                c.drawRect((float) itemView.getRight() + dX, (float) itemView.getTop(),
                        (float) itemView.getRight(), (float) itemView.getBottom(), p);

                /*icon = BitmapFactory.decodeResource(mActivity.getApplicationContext().getResources(), R.drawable.icon_delete_96px);
                c.drawBitmap(icon,
                        (float) itemView.getRight() - convertDpToPx(16) - icon.getWidth(),
                        (float) itemView.getTop() + ((float) itemView.getBottom() - (float) itemView.getTop() - icon.getHeight()) / 2,
                        p);*/
            }

            // Fade out the view when it is swiped out of the parent
            final float alpha = ALPHA_FULL - Math.abs(dX) / (float) viewHolder.itemView.getWidth();
            viewHolder.itemView.setAlpha(alpha);
            viewHolder.itemView.setTranslationX(dX);

        } else {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    }

    private int convertDpToPx(int dp) {
        return Math.round(dp * (mActivity.getResources().getDisplayMetrics().xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        if (!(viewHolder instanceof TranslateViewHolder)) return;

        final TranslateEntity swipedItem = mAdapter.getTranslateEntities().get(viewHolder.getAdapterPosition());
        final int swipedIndex = viewHolder.getAdapterPosition();

        if (direction == LEFT) {
            new Thread(() -> mDatabase.delete(swipedItem)).start();
            mAdapter.removeItem(swipedIndex);

            Snackbar snackbar = Snackbar.make(mActivity.findViewById(R.id.recycler_view), R.string.snackbar_swipe_delete, Snackbar.LENGTH_LONG);
            snackbar.setAction(R.string.snackbar_action_cancel, view -> {
                // FIXME Очевидно, что при ресторе с автоинкрементным ID в бд и сортировкой по ID восстановленных элемент будет первым в списке
                // Добавить дату использования? С текущей архитектурой ничего нормально не заработает :D
                new Thread(() -> mDatabase.insert(swipedItem)).start();
                mAdapter.restoreItem(swipedItem, swipedIndex);
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        } else {
            new Thread(() -> mDatabase.setFlag(swipedItem.getId(), TranslatesDao.TYPE_SAVED)).start();
            mAdapter.saveItem(swipedIndex);

            Snackbar snackbar = Snackbar.make(mActivity.findViewById(R.id.recycler_view), R.string.snackbar_swipe_save, Snackbar.LENGTH_LONG);
            snackbar.setAction(R.string.snackbar_action_cancel, view -> {
                new Thread(() -> mDatabase.removeFlag(swipedItem.getId(), TranslatesDao.TYPE_SAVED)).start();
                mAdapter.unsaveItem(swipedIndex);
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
    }
}

package ru.noman23.magentatranslator.ui.TranslatesRecyclerView;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.support.v7.widget.helper.ItemTouchHelper.Callback;
import android.view.View;

import javax.inject.Inject;

import butterknife.BindColor;
import butterknife.BindDimen;
import butterknife.BindDrawable;
import butterknife.ButterKnife;
import ru.noman23.magentatranslator.DaggerMagentaComponent;
import ru.noman23.magentatranslator.R;
import ru.noman23.magentatranslator.activities.MainActivity;
import ru.noman23.magentatranslator.database.tables.TranslatesDao;
import ru.noman23.magentatranslator.database.tables.TranslatesDaoWrapper;
import ru.noman23.magentatranslator.network.translate.TranslateEntity;

import static android.support.v7.widget.helper.ItemTouchHelper.LEFT;
import static android.support.v7.widget.helper.ItemTouchHelper.RIGHT;

public class TranslateViewSwipeController extends Callback {

    // TODO Тут бардак, нужно прибрать, укомплектовать код отрисовки задников, onSwipe тоже облагородить

    @BindDimen(R.dimen.item_main_swipe_icon_size) int mIconSize;
    @BindDimen(R.dimen.item_main_swipe_icon_margin) int mIconMargin;

    @BindColor(R.color.item_green) int mGreenColor;
    @BindColor(R.color.item_red) int mRedColor;
    @BindColor(R.color.white) int mWhiteColor;

    @BindDrawable(R.drawable.icon_save_96px) Drawable mSaveDrawable;
    @BindDrawable(R.drawable.icon_delete_96px) Drawable mDeleteDrawable;

    @Inject TranslatesDaoWrapper mDatabase;

    private Activity mActivity;
    private TranslateRecyclerViewAdapter mAdapter;

    public TranslateViewSwipeController(Activity activity, TranslateRecyclerViewAdapter adapter) {
        DaggerMagentaComponent.builder().context(activity).build().inject(this);
        ButterKnife.bind(this, activity);
        this.mActivity = activity;
        this.mAdapter = adapter;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        // FIXME Костыль чтобы элемент не свайпался на майн активити
        if (mActivity instanceof MainActivity)
            return makeMovementFlags(0, 0);
        // FIXME (TAG: Save) Убран флаг свайпа для сейва RIGHT
        return makeMovementFlags(0, LEFT | RIGHT);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

            View itemView = viewHolder.itemView;
            Paint p = new Paint();

            if (dX > 0) {
                p.setColor(mGreenColor);
                c.drawRect((float) itemView.getLeft(), (float) itemView.getTop(), dX, (float) itemView.getBottom(), p);

                mSaveDrawable.setBounds(itemView.getLeft() + mIconMargin, itemView.getTop() + (itemView.getBottom() - itemView.getTop() - mIconSize) / 2, itemView.getLeft() + mIconMargin + mIconSize, itemView.getBottom() - (itemView.getBottom() - itemView.getTop() - mIconSize) / 2);
                mSaveDrawable.setColorFilter(new PorterDuffColorFilter(mWhiteColor, PorterDuff.Mode.SRC_IN));
                mSaveDrawable.draw(c);

            } else {
                p.setColor(mRedColor);
                c.drawRect((float) itemView.getRight() + dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom(), p);

                mDeleteDrawable.setBounds(itemView.getRight() - mIconSize - mIconMargin, itemView.getTop() + (itemView.getBottom() - itemView.getTop() - mIconSize) / 2, itemView.getRight() - mIconMargin, itemView.getBottom() - (itemView.getBottom() - itemView.getTop() - mIconSize) / 2);
                mDeleteDrawable.setColorFilter(new PorterDuffColorFilter(mWhiteColor, PorterDuff.Mode.SRC_IN));
                mDeleteDrawable.draw(c);

            }

            viewHolder.itemView.setTranslationX(dX);

        } else {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
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

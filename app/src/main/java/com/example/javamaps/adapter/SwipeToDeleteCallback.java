package com.example.javamaps.adapter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.javamaps.R;
import com.example.javamaps.adapter.PlaceAdapter;
import com.example.javamaps.roomdb.PlaceDao;
import com.example.javamaps.view.model.Place;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SwipeToDeleteCallback extends ItemTouchHelper.SimpleCallback {

    private final Drawable icon;
    private final ColorDrawable background;
    private final Context context;
    private final PlaceAdapter adapter;
    private final PlaceDao placeDao;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    public SwipeToDeleteCallback(Context context, PlaceAdapter adapter, PlaceDao placeDao) {
        super(0, ItemTouchHelper.LEFT);
        this.context = context;
        this.adapter = adapter;
        this.placeDao = placeDao;

        icon = ContextCompat.getDrawable(context, R.drawable.delete_24);
        background = new ColorDrawable(Color.RED);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView,
                          @NonNull RecyclerView.ViewHolder viewHolder,
                          @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getAdapterPosition();
        Place placeToDelete = adapter.getPlaceList().get(position);

        // Room'dan sil
        compositeDisposable.add(
                placeDao.delete(placeToDelete)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {
                            // Listeden sil
                            adapter.getPlaceList().remove(position);
                            adapter.notifyItemRemoved(position);

                            // Snackbar ile geri al seçeneği göster
                            Snackbar.make(viewHolder.itemView, "Konum silindi", Snackbar.LENGTH_LONG)
                                    .setAction("Geri Al", v -> {
                                        // Room'a tekrar ekle
                                        compositeDisposable.add(
                                                placeDao.insert(placeToDelete)
                                                        .subscribeOn(Schedulers.io())
                                                        .observeOn(AndroidSchedulers.mainThread())
                                                        .subscribe(() -> {
                                                            adapter.getPlaceList().add(position, placeToDelete);
                                                            adapter.notifyItemInserted(position);
                                                            Toast.makeText(context, "Konum geri alındı", Toast.LENGTH_SHORT).show();
                                                        }, Throwable::printStackTrace)
                                        );
                                    })
                                    .show();
                        }, throwable -> {
                            throwable.printStackTrace();
                            Toast.makeText(context, "Silme işlemi başarısız", Toast.LENGTH_SHORT).show();
                        })
        );
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView,
                            @NonNull RecyclerView.ViewHolder viewHolder, float dX,
                            float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

        View itemView = viewHolder.itemView;
        int iconMargin = (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;

        if (dX < 0) {
            int iconLeft = itemView.getRight() - iconMargin - icon.getIntrinsicWidth();
            int iconRight = itemView.getRight() - iconMargin;
            int iconTop = itemView.getTop() + iconMargin;
            int iconBottom = iconTop + icon.getIntrinsicHeight();

            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
            background.setBounds(itemView.getRight() + (int) dX, itemView.getTop(),
                    itemView.getRight(), itemView.getBottom());
        } else {
            background.setBounds(0, 0, 0, 0);
            icon.setBounds(0, 0, 0, 0);
        }

        background.draw(c);
        icon.draw(c);
    }
}

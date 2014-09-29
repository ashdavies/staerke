package com.chaos.starke.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.chaos.starke.R;
import com.chaos.starke.core.RoutineActivity;
import com.chaos.starke.models.Routine;

import java.util.ArrayList;

public class RoutineAdapter extends ArrayAdapter<Routine> implements OnItemClickListener {

    private Context context;

    public RoutineAdapter(Context context) {
        super(context, R.layout.card_routine, new ArrayList<Routine>());
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.card_routine, parent, false);
        }

        final Routine routine = getItem(position);

        ImageView thumb = (ImageView) convertView.findViewById(R.id.thumb);
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), Thumbnail(routine.category));
        thumb.setImageBitmap(getRoundBitmap(bitmap));

        TextView name = (TextView) convertView.findViewById(R.id.name);
        name.setText(routine.name);

        TextView category = (TextView) convertView.findViewById(R.id.category);
        if (routine.category != null) {
            category.setText(routine.category.name());
        }

        CheckBox checkbox = (CheckBox) convertView.findViewById(R.id.favourite);
        checkbox.setChecked(routine.favourite == null ? false : routine.favourite);
        checkbox.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckBox checkbox = (CheckBox) view;
                routine.favourite = checkbox.isChecked();
                routine.save();
            }
        });

        return convertView;

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Routine routine = getItem(position);

        Intent intent = new Intent(context, RoutineActivity.class);
        intent.putExtra("routine", routine.getId());
        context.startActivity(intent);

    }

    public static int Thumbnail(Routine.Category category) {
        if (category == Routine.Category.Strength) {
            return R.drawable.ic_strength;
        } else if (category == Routine.Category.Health) {
            return R.drawable.ic_health;
        } else if (category == Routine.Category.Endurance) {
            return R.drawable.ic_endurance;
        } else if (category == Routine.Category.Performance) {
            return R.drawable.ic_performance;
        } else if (category == Routine.Category.Women) {
            return R.drawable.ic_women;
        } else {
            return R.drawable.ic_launcher_square;
        }

    }

    public static Bitmap getRoundBitmap(Bitmap bitmap) {

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getWidth());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, bitmap.getWidth(), bitmap.getWidth(), paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;

    }

}

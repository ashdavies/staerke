package com.chaos.starke.adapters;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.BitmapFactory;
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
import com.chaos.starke.RoutineDetailActivity;
import com.chaos.starke.db.Routine;

public class RoutineAdapter extends ArrayAdapter<Routine> implements OnItemClickListener {

    // Context storage
    private Context context;

    // Call parent and store context
    public RoutineAdapter(Context context, List<Routine> routines) {
        super(context, R.layout.card_routine, routines);
        this.context = context;
    }

    /**
     * Get view
     *
     * @param position
     * @param convertView
     * @param parent
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Inflate view
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.card_routine, parent, false);
        }

        // Get routine
        final Routine routine = getItem(position);

        // Set icon thumb nail
        ImageView thumb = (ImageView) convertView.findViewById(R.id.thumb);
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), Thumbnail(routine.category));
        thumb.setImageBitmap(getRoundBitmap(bitmap));

        // Set routine name
        TextView name = (TextView) convertView.findViewById(R.id.name);
        name.setText(routine.name);

        // Set category
        TextView category = (TextView) convertView.findViewById(R.id.category);
        if (routine.category != null) {
            category.setText(routine.category.name());
        }

        // Set on toggle favourite listener
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

        // Return view
        return convertView;

    }

    /**
     * On card click listener
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        System.out.println("RoutineAdapter: Item click");
        // Get routine
        Routine routine = getItem(position);

        // Create intent with routine id as extra
        Intent intent = new Intent(context, RoutineDetailActivity.class);
        intent.putExtra("routine", routine.getId());
        context.startActivity(intent);

    }

    /**
     * Return the correct thumb nail image
     */
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

    /**
     * Return bitmap with rounded corners
     *
     * @param bitmap
     * @param pixels
     * @return
     */
    public static Bitmap getRoundBitmap(Bitmap bitmap) {

        // Create output bitmap with canvas
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        // Get painters
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getWidth());
        final RectF rectF = new RectF(rect);

        // Redraw as round rectangle
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, bitmap.getWidth(), bitmap.getWidth(), paint);

        // Transfer
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;

    }

}

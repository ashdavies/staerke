package com.chaos.starke.adapters;

import android.graphics.drawable.Drawable;
import android.test.AndroidTestCase;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.chaos.starke.adapters.RoutineAdapter;
import com.chaos.starke.models.Routine;
import com.chaos.starke.starke.R;

public class RoutineAdapterTest extends AndroidTestCase {

    RoutineAdapter adapter;

    @Override
    protected void setUp() throws Exception {

        super.setUp();

        List<Routine> data = new ArrayList<Routine>();
        data.add(new Routine("Test One", Routine.Category.Performance));
        data.add(new Routine("Test Two", Routine.Category.Strength));
        adapter = new RoutineAdapter(getContext(), data);

    }

    @Override
    protected void tearDown() throws Exception {

        super.tearDown();
        adapter.clear();
        adapter = null;

    }

    public void testGetView() {

        Routine routine = adapter.getItem(0);
        View view = adapter.getView(0, null, null);
        assertNotNull(view);

        ImageView thumbView = (ImageView) view.findViewById(R.id.thumb);
        Drawable thumbDrawable = thumbView.getDrawable();
        assertNotNull(thumbDrawable);

        TextView name = (TextView) view.findViewById(R.id.name);
        assertEquals(name.getText(), routine.name);

        TextView category = (TextView) view.findViewById(R.id.category);
        assertEquals(category.getText(), routine.category.name());

        CheckBox favourite = (CheckBox) view.findViewById(R.id.favourite);
        assertEquals((Boolean) favourite.isChecked(), routine.favourite);

    }

    public void testGetThumbnail() {
        for (Routine.Category category : Routine.Category.values()) {
            assertTrue(
                    "Failed to get thumbnail for category " + category.name(),
                    RoutineAdapter.GetThumbnail(category) > 0
            );
        }
    }

}

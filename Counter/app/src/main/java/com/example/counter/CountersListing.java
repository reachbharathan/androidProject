package com.example.counter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.text.InputType;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static com.example.counter.MainActivity.counterDB;


public class CountersListing extends Fragment {

    ArrayList<TagItem> listCounters;
    TagAdapter<TagItem> adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_counters_listing, container, false);

        listCounters = new ArrayList<>();

        counterDB = getActivity().openOrCreateDatabase("COUNTER", Context.MODE_PRIVATE, null);

        Cursor cursor = counterDB.rawQuery("SELECT * FROM countTable", null);

        cursor.moveToFirst();

        if (cursor.getCount() > 0) {

            cursor.moveToFirst();

            int counterNameIndex = cursor.getColumnIndex("counterName");
            int counterValueIndex = cursor.getColumnIndex("counterValue");

            for (int i = 0; i < cursor.getCount(); i++) {
                listCounters.add(new TagItem(cursor.getString(counterNameIndex), cursor.getInt(counterValueIndex)));
                cursor.moveToNext();
            }

        }

        if (listCounters.size() > 0) {
            TextView textView = rootView.findViewById(R.id.noTag);
            textView.setVisibility(View.GONE);
        }

        adapter = new TagAdapter<>(getActivity(), listCounters);

        final ListView listView = rootView.findViewById(R.id.list);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Bundle bundle = new Bundle();
                bundle.putString("CounterName", listCounters.get(i).getTagName());
                bundle.putInt("CounterValue", listCounters.get(i).getTagCount());

                FragmentManager manager = getFragmentManager();
                ViewPager viewPager = new ViewPager();
                viewPager.setArguments(bundle);
                manager.beginTransaction().replace(R.id.container, viewPager).commit();

            }
        });

        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);

        listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode actionMode, int i, long l, boolean b) {
                int checkedCount = listView.getCheckedItemCount();
                actionMode.setTitle(checkedCount + " Selected ");
                adapter.toggleSelection(i);
            }

            @Override
            public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                actionMode.getMenuInflater().inflate(R.menu.counter_menu, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {

                SparseBooleanArray selected = adapter.getSelectedTags();
                for (int i = (selected.size() - 1); i >= 0; i--) {
                    if (selected.valueAt(i)) {

                        switch (menuItem.getItemId()) {
                            case R.id.delete:
                                deleteDB(selected.keyAt(i));
                                adapter.remove(selected.keyAt(i));
                                break;

                            case R.id.edit:
                                dialogBox(selected.keyAt(i));
                                break;

                            default:
                                break;
                        }

                    }
                }

                actionMode.finish();

                return true;
            }

            @Override
            public void onDestroyActionMode(ActionMode actionMode) {
                adapter.removeSelection();
            }
        });

        return rootView;
    }

    private void dialogBox(final int value) {

        final AlertDialog alertBuilder = new AlertDialog.Builder(getContext()).create();

        View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.tag_view, null);

        final EditText nameText = dialogView.findViewById(R.id.tagBoth);
        final EditText numberText = dialogView.findViewById(R.id.number);
        Button button = dialogView.findViewById(R.id.save);

        nameText.setHint("Counter Name");
        nameText.setInputType(InputType.TYPE_CLASS_TEXT);
        numberText.setHint("Counter Value");
        numberText.setInputType(InputType.TYPE_NUMBER_VARIATION_NORMAL);
        button.setText("Save");
        alertBuilder.setIcon(R.drawable.ic_mode_edit_black_24dp);
        alertBuilder.setTitle("Edit Counter");

        nameText.setText(listCounters.get(value).getTagName());
        numberText.setText(String.valueOf(listCounters.get(value).getTagCount()));


        alertBuilder.setView(dialogView);
        alertBuilder.show();


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name;
                int tagValue;
                Boolean valid = true;

                name = String.valueOf(nameText.getText()).trim();

                // Counter Name Blank Check

                if (name.length() == 0) {
                    sendMessage("Counter Name Needed!");
                    valid = false;
                }

                // Counter Value Blank Check

                if (String.valueOf(numberText.getText()).trim().length() == 0) {
                    sendMessage("Counter Value Needed");
                    valid = false;
                }

                // Counter Name Duplicate Check

                if (valid && (!name.equals(listCounters.get(value).getTagName()))) {
                    valid = checkCounterAvailability(name);
                    if (!valid) {
                        sendMessage("Counter Name Already Exists");
                    }
                }

                // If valid from all the above cases, update

                if (valid) {

                    tagValue = Integer.parseInt(String.valueOf(numberText.getText()).trim());

                    updateCounterDB(name, tagValue, listCounters.get(value).getTagName());
                    adapter.setTagItem(value, name, tagValue);

                }

                if (valid) {
                    InputMethodManager manager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    manager.hideSoftInputFromWindow(nameText.getWindowToken(), 0);

                    alertBuilder.cancel();
                }

            }
        });


    }

    private Boolean checkCounterAvailability(String name) {

        Cursor cursor = counterDB.rawQuery("SELECT * FROM countTable WHERE counterName = ?" , new String[]{name});

        cursor.moveToFirst();

        if (cursor.getCount() == 0 ) {
            return true;
        } else {
            return false;
        }

    }

    private void sendMessage(String msg) {

        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();

    }

    private void updateCounterDB(String counterName, int counterValue, String oldCounterName) {

        ContentValues contentValues = new ContentValues();

        contentValues.put("counterName" , counterName);
        contentValues.put("counterValue", counterValue);

        counterDB.update("countTable" , contentValues, "counterName = ? " , new String[]{oldCounterName});

        contentValues.clear();

        if (!counterName.equals(oldCounterName)) {

            contentValues.put("counterName", counterName);

            counterDB.update("tagTable", contentValues, "counterName = ? ", new String[]{oldCounterName});

        }

    }

    private void deleteDB(int index) {

        counterDB.delete("tagTable", " counterName = ?", new String[]{listCounters.get(index).getTagName()});

        counterDB.delete("countTable", " counterName = ?", new String[]{listCounters.get(index).getTagName()});

    }

}

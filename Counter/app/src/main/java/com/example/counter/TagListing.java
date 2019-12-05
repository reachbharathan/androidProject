package com.example.counter;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

import static com.example.counter.MainActivity.counterDB;


public class TagListing extends Fragment {

    static ArrayList<TagItem> listTags;
    TagAdapter<TagItem> adapter;
    ContentValues contentValues = new ContentValues();
    TextView noTagsText;

    String counterName;
    View rootView;
    Boolean isUpdate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_tag_listing, container, false);
        setHasOptionsMenu(true);

        isUpdate = getCounterName();

        listTags = new ArrayList<>();

        noTagsText = rootView.findViewById(R.id.noTag);

        if (isUpdate) {

            MainActivity.counterDB = getActivity().openOrCreateDatabase("COUNTER", Context.MODE_PRIVATE, null);

            Cursor cursor = MainActivity.counterDB.rawQuery("SELECT * FROM tagTable WHERE counterName = ?", new String[]{counterName});

            cursor.moveToFirst();

            if (cursor.getCount() > 0) {

                cursor.moveToFirst();

                int nameIndex = cursor.getColumnIndex("tagName");
                int countIndex = cursor.getColumnIndex("tagValue");

                for (int i = 0; i < cursor.getCount(); i++) {
                    listTags.add(new TagItem(cursor.getString(nameIndex), cursor.getInt(countIndex)));
                    cursor.moveToNext();
                }

            }

        }

        Collections.sort(listTags);

        if (listTags.size() > 0) {
            noTagsText.setVisibility(View.GONE);
        } else {
            noTagsText.setVisibility(View.VISIBLE);
        }

        adapter = new TagAdapter<>(getActivity(), listTags);

        final ListView listView = rootView.findViewById(R.id.list);

        listView.setAdapter(adapter);

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
                actionMode.getMenuInflater().inflate(R.menu.tags_multi_choice, menu);
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
                                if (isUpdate) {
                                    deleteDB(selected.keyAt(i));
                                }
                                adapter.remove(selected.keyAt(i));

                                if (listTags.size() == 0) {
                                    noTagsText.setVisibility(View.VISIBLE);
                                }

                                break;

                            case R.id.edit:
                                dialogBox(5, selected.keyAt(i));
                                break;

                            case R.id.set:
                                dialogBox(4, selected.keyAt(i));

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

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser) {


            if (listTags.size() > 0) {
                noTagsText.setVisibility(View.GONE);
            } else {
                noTagsText.setVisibility(View.VISIBLE);
            }

        }

    }

    private Boolean getCounterName() {

        Bundle extras = this.getArguments();

        if (extras != null) {

            counterName = extras.getString("CounterName", "");
            return true;

        } else {

            return false;

        }

    }

    private void deleteDB(int index) {

        counterDB.delete("tagTable", " counterName = ? AND tagName = ?", new String[]{counterName, listTags.get(index).getTagName()});

    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {

        inflater.inflate(R.menu.tag_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {

            case R.id.sort:
                Collections.sort(listTags);
                adapter.notifyDataSetChanged();
                return true;

            case R.id.deleteAll:
                confirmation();
                return true;

            default:
                return false;
        }


    }

    private void confirmation() {

        if (listTags.size() == 0) {
            sendMessage(" No Tags to Delete!");
            return;
        }

        new android.app.AlertDialog.Builder(getActivity())
                .setMessage("Are you sure you want to Delete all the Tags? ")
                .setTitle("Confirmation")
                .setIcon(android.R.drawable.ic_notification_clear_all)
                .setNegativeButton("No", null)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteAllTags();
                    }
                })
                .show();

    }


    private void deleteAllTags() {

        if (isUpdate) {

            counterDB.delete("tagTable", "counterName = ? ", new String[]{counterName});

        } else {

            listTags.clear();
            adapter.notifyDataSetChanged();

        }

        sendMessage("All Tags Deleted!");

    }


    private void dialogBox(final int boxType, final int value) {

        final AlertDialog dialogBuilder = new AlertDialog.Builder(getContext()).create();

        View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.tag_view, null);

        final EditText nameText = dialogView.findViewById(R.id.tagBoth);
        final EditText numberText = dialogView.findViewById(R.id.number);
        Button button = dialogView.findViewById(R.id.save);

        if (boxType == 4) {

            numberText.setHint("Number");
            button.setText("Set");
            dialogBuilder.setTitle("Tag Value for " + listTags.get(value).getTagName());
            nameText.setVisibility(View.GONE);

        }

        if (boxType == 5) {

            nameText.setHint("Tag Name");
            numberText.setHint("Tag Value");
            button.setText("Save");
            dialogBuilder.setIcon(R.drawable.ic_mode_edit_black_24dp);
            dialogBuilder.setTitle("Edit Tag");

            nameText.setText(listTags.get(value).getTagName());
            numberText.setText(String.valueOf(listTags.get(value).getTagCount()));

        }

        dialogBuilder.setView(dialogView);
        dialogBuilder.show();


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name;
                int tagValue;

                Boolean valid = true;

                switch (boxType) {

                    case (4):

                        // Tag Value Blank Check

                        if (String.valueOf(numberText.getText()).trim().length() == 0) {
                            sendMessage("Tag Value Needed");
                            valid = false;
                        }

                        // Tag Value Duplicate Check

                        if (valid) {
                            valid = (!checkTagAvailability(String.valueOf(numberText.getText()).trim(), 2));
                            if (!valid) {
                                sendMessage("Value Already Tagged!");
                            }
                        }

                        // If valid from all the above cases, update

                        if (valid) {
                            tagValue = Integer.parseInt(String.valueOf(numberText.getText()).trim());

                            if (isUpdate) {
                                updateTagDB(listTags.get(value).getTagName(), tagValue, listTags.get(value).getTagName());
                            }

                            adapter.setTagValue(value, tagValue);

                        }

                        break;


                    case (5):

                        name = String.valueOf(nameText.getText()).trim();

                        // Tag Name Blank Check

                        if (name.length() == 0) {
                            sendMessage("Tag Name Needed");
                            valid = false;
                        }

                        // Tag Value Blank Check

                        if (String.valueOf(numberText.getText()).trim().length() == 0) {
                            sendMessage("Tag Value Needed");
                            valid = false;
                        }

                        // Tag NAme Duplicate Check

                        if ((!name.equals(listTags.get(value).getTagName())) && (valid)) {

                            valid = checkTagAvailability(name, 1);

                            if (!valid) {
                                sendMessage("Tag Name Exists!");
                            }

                        }

                        // Tag Value Duplicate Check

                        if (valid && !String.valueOf(numberText.getText()).trim().equals(String.valueOf(listTags.get(value).getTagCount()))) {

                            valid = checkTagAvailability(String.valueOf(numberText.getText()).trim(), 2);

                            if (!valid) {

                                sendMessage("Value Already Tagged");

                            }

                        }

                        // If valid from all the above cases, update

                        if (valid) {

                            tagValue = Integer.parseInt(String.valueOf(numberText.getText()).trim());

                            if (isUpdate) {
                                updateTagDB(name, tagValue, listTags.get(value).getTagName());
                            }

                            adapter.setTagItem(value, name, tagValue);

                        }

                        break;

                    default:
                        break;

                }

                if (valid) {

                    InputMethodManager manager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    manager.hideSoftInputFromWindow(nameText.getWindowToken(), 0);
                    manager.hideSoftInputFromWindow(numberText.getWindowToken(), 0);

                    dialogBuilder.cancel();

                }

            }
        });

    }

    private Boolean checkTagAvailability(String name, int tagItemNumber) {

        for (int i = 0; i < listTags.size(); i++) {

            if (tagItemNumber == 1) {

                if (listTags.get(i).getTagName().equals(name)) {
                    return false;
                }

            } else if (tagItemNumber == 2) {

                if (String.valueOf(listTags.get(i).getTagCount()).equals(name)) {
                    return false;
                }

            }
        }

        return true;
    }

    private void updateTagDB(String tagName, int tagValue, String oldTagName) {

        if (contentValues != null) {
            contentValues.clear();
        }

        contentValues.put("tagName", tagName);
        contentValues.put("tagValue", tagValue);

        counterDB.update("tagTable", contentValues, "counterName = ? and tagName = ?", new String[]{counterName, oldTagName});

    }

    // Toast Message

    private void sendMessage(String msg) {

        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();

    }
}

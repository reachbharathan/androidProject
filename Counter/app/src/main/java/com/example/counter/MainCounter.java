package com.example.counter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.counter.MainActivity.counterDB;
import static com.example.counter.MainActivity.maximum;
import static com.example.counter.MainActivity.minimum;
import static com.example.counter.MainActivity.step;
import static com.example.counter.TagListing.listTags;

public class MainCounter extends Fragment {


    static int count = 0;
    String counterName = "";
    int countValue = 0;
    Boolean isUpdate;

    TextView counterNameText;
    View rootView;
    ContentValues contentValues = new ContentValues();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_main_counter, container, false);
        setHasOptionsMenu(true);

        TextView plusText = rootView.findViewById(R.id.plus);
        plusText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add();
            }
        });

        TextView minusText = rootView.findViewById(R.id.sub);
        minusText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sub();
            }
        });

        Button resetButton = rootView.findViewById(R.id.reset);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reset();
            }
        });

        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        Button addTagButton = rootView.findViewById(R.id.tag);
        addTagButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTag();
            }
        });

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        counterNameText = rootView.findViewById(R.id.counterName);

        isUpdate = getCounterName();

        // Set Tag Name and count based on Add/update Mode

        if (isUpdate) {

            counterNameText.setText(counterName);
            counterNameText.setVisibility(View.VISIBLE);

            count = countValue;

        } else {

            counterNameText.setVisibility(View.GONE);
            count = minimum;


        }

        setCount();

    }


    private boolean getCounterName() {

        Bundle extras = this.getArguments();

        if (extras != null) {

            counterName = extras.getString("CounterName", "");
            countValue = extras.getInt("CounterValue", 0);
            return true;

        } else {

            return false;

        }

    }

    public void add() {

        if (count + step > maximum) {
            sendMessage("Count too High to increase!");
        } else {
            count = count + step;
        }

        setCount();

    }

    public void sub() {

        if (count - step < minimum) {
            sendMessage("Count too Low to decrease!");
        } else {
            count = count - step;
        }

        setCount();

    }

    // Toast Message

    private void sendMessage(String msg) {

        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();

    }

    // Update Count in UI

    private void setCount() {

        TextView counter = rootView.findViewById(R.id.count);
        counter.setText(String.valueOf(count));

    }

    // Menu Creation

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {

        menuInflater.inflate(R.menu.main_menu, menu);
        super.onCreateOptionsMenu(menu, menuInflater);

    }


    // Menu Option Selection Process

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {

            // Moving the counter directly yot number

            case R.id.goTo:
                dialogBox(3);
                return true;

            case R.id.save:

                if (!isUpdate) {
                    dialogBox(1);
                } else {
                    updateCounterDB();
                }

            default:
                return false;

        }

    }

    public void addTag() {

        EditText tagNameText = rootView.findViewById(R.id.tagName);
        String tagName = String.valueOf(tagNameText.getText()).trim();

        InputMethodManager manager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(tagNameText.getWindowToken(), 0);

        if (tagName.length() == 0) {
            sendMessage("Tag Name Needed");
            return;
        }


        if (!checkTagAvailability(tagName,1)) {
            sendMessage("Tag Already Exists!");
            return;
        }

        if (!checkTagAvailability(String.valueOf(count).trim(),2)) {
            sendMessage("Value Already Tagged");
            return;
        }

        if (isUpdate) {
            addToTagsList(tagName);
            addTagDB(counterName, tagName, count);
        } else {
            addToTagsList(tagName);
        }

        sendMessage("Count Tagged!");

        tagNameText.setText("");

    }

    private Boolean checkTagAvailability(String name, int type) {

        for (int i = 0; i < listTags.size(); i++) {

            if (type == 1) {
                if (listTags.get(i).getTagName().equals(name)) {
                    return false;
                }
            }

            if (type == 2) {
                if (String.valueOf(listTags.get(i).getTagCount()).equals(name)) {
                    return false;
                }
            }
        }

        return true;
    }

    public void reset() {

        count = 0;
        setCount();

    }

    private void dialogBox(final int boxType) {

        final AlertDialog alertBuilder = new AlertDialog.Builder(getContext()).create();

        View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.tag_view, null);

        final EditText nameText = dialogView.findViewById(R.id.tagBoth);
        Button button = dialogView.findViewById(R.id.save);

        final EditText numberText = dialogView.findViewById(R.id.number);

        if (boxType == 1) {

            nameText.setHint("Counter Name");
            button.setText("Save");
            alertBuilder.setIcon(R.drawable.ic_plus);
            alertBuilder.setTitle("Counter");
            numberText.setVisibility(View.GONE);

        }


        if (boxType == 3) {

            numberText.setHint("Number");
            button.setText("SET");
            alertBuilder.setTitle("Counter Value");
            nameText.setVisibility(View.GONE);
        }


        alertBuilder.setView(dialogView);
        alertBuilder.show();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Boolean valid = true;

                switch (boxType) {
                    case (1):

                        String counterName = String.valueOf(nameText.getText()).trim();

                        // Counter Name Blank Check

                        if (counterName.length() == 0) {
                            sendMessage("Counter Name Needed!");
                            valid = false;
                        }

                        // Counter Name Duplicate Check

                        if (valid) {

                            valid = checkCounterAvailability(counterName);

                            if (!valid) {
                                sendMessage("Counter Name Exists!");
                            }
                        }

                        // If valid from all the above cases, update

                        if (valid) {
                            addCounterDB(counterName);
                        }

                        break;


                    case (3):

                        if (String.valueOf(numberText.getText()).trim().length() == 0) {
                            sendMessage("Counter Value Needed");
                            valid = false;
                        }

                        if (valid) {
                            count = Integer.parseInt(String.valueOf(numberText.getText()).trim());
                            setCount();

                        }

                        break;

                    default:
                        break;

                }


                if (valid) {

                    InputMethodManager manager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    manager.hideSoftInputFromWindow(nameText.getWindowToken(), 0);
                    manager.hideSoftInputFromWindow(numberText.getWindowToken(), 0);

                    alertBuilder.cancel();

                }
            }
        });

    }

    private Boolean checkCounterAvailability(String counterName) {

        Cursor cursor = counterDB.rawQuery("SELECT * FROM countTable WHERE counterName = ?", new String[]{counterName});

        cursor.moveToFirst();

        if (cursor.getCount() == 0) {
            return true;
        } else {
            return false;
        }

    }

    private void updateCounterDB() {

        if (contentValues != null) {
            contentValues.clear();
        }

        contentValues.put("counterValue", count);

        counterDB.update("countTable", contentValues, "counterName = ?", new String[]{counterName});

        sendMessage("Counter Saved!");

    }


    private void addToTagsList(String tagName) {

        listTags.add(new TagItem(tagName, count));

    }

    private void addTagDB(String counterName, String tagName, int tagCount) {

        if (contentValues != null) {
            contentValues.clear();
        }

        contentValues.put("counterName", counterName);
        contentValues.put("tagName", tagName);
        contentValues.put("tagValue", tagCount);

        counterDB.insert("tagTable", null, contentValues);

    }


    private void addCounterDB(String counterName) {

        counterNameText.setVisibility(View.VISIBLE);
        counterNameText.setText(counterName);

        if (contentValues != null) {
            contentValues.clear();
        }

        contentValues.put("counterName", counterName);
        contentValues.put("counterValue", count);

        counterDB.insert("countTable", null, contentValues);

        isUpdate = true;

        if (listTags.size() > 0) {

            for (int i = 0; i < listTags.size(); i++) {
                addTagDB(counterName,listTags.get(i).getTagName(), listTags.get(i).getTagCount());
            }
        }

        sendMessage("Counter Saved!");

    }

}

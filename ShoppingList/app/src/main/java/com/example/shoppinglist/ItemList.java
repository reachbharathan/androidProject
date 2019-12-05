package com.example.shoppinglist;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static com.example.shoppinglist.MainActivity.shoppingList;

public class ItemList extends AppCompatActivity {

    String listName;
    ArrayList<item> items;
    String oldItemName = "";
    adapterItem<item> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        shoppingList = this.openOrCreateDatabase("SHOPPINGLIST", Context.MODE_PRIVATE, null);

        listName = getListName();

        if (listName.length() == 0) {
            return;
        }

        loadData(0);

    }

    private String getListName() {

        String result = null;

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if (getIntent().hasExtra("listName")) {

            result = extras.getString("listName", String.valueOf(0));

        }

        return result;
    }

    private String loadData(int status) {

        String itemData = "";

        try {

            items = new ArrayList<item>();

            items.clear();

            shoppingList.execSQL("CREATE TABLE IF NOT EXISTS itemList (itemName VARCHAR , listName VARCHAR , quantity TEXT, strike TEXT )");

            Cursor cursor = shoppingList.rawQuery("SELECT * FROM itemList WHERE listName = ? ", new String[]{listName});

            int nameIndex = cursor.getColumnIndex("itemName");
            int quantityIndex = cursor.getColumnIndex("quantity");
            int strikeIndex = cursor.getColumnIndex("strike");

            cursor.moveToFirst();

            for (int i = 0; i < cursor.getCount(); i++) {

                String itemName = cursor.getString(nameIndex);
                String itemQuantity = cursor.getString(quantityIndex) ;
                Boolean strike = Boolean.parseBoolean(cursor.getString(strikeIndex));
                items.add(new item(itemName, itemQuantity , strike ));

                if (status == 1) {

                    itemData = itemData + itemName + " ( " + itemQuantity + " ) - ";

                    if (strike) {

                        itemData = itemData + "Done\n";

                    } else {

                        itemData = itemData + "Pending\n";

                    }
                }

                cursor.moveToNext();

            }


            if (items.size() > 0) {
                TextView addNewText = findViewById(R.id.addnew);
                addNewText.setVisibility(View.GONE);
            }

            adapter = new adapterItem<item>(ItemList.this, items);


            final ListView listView = findViewById(R.id.list);

            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    updateDBStrike(i);

                }
            });

            listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);

            listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
                @Override
                public void onItemCheckedStateChanged(ActionMode actionMode, int i, long l, boolean b) {
                    final int checkedCount = listView.getCheckedItemCount();
                    actionMode.setTitle(checkedCount + " Selected");
                    adapter.toggleSelection(i);
                }

                @Override
                public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                    actionMode.getMenuInflater().inflate(R.menu.delete_menu, menu);
                    return true;
                }

                @Override
                public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                    return false;
                }

                @Override
                public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {

                    actionBar(menuItem.getItemId());
                    actionMode.finish();
                    return true;

                }

                @Override
                public void onDestroyActionMode(ActionMode actionMode) {
                    adapter.removeSelection();
                }
            });

        } catch (Exception e) {

            e.printStackTrace();

        }

        return itemData;
    }

    private void actionBar(int itemId) {

        switch (itemId) {

            case R.id.edit:
                SparseBooleanArray selectedEdit = adapter.getSelectedListIds();
                for (int i = (selectedEdit.size() - 1); i >= 0; i--) {
                    if (selectedEdit.valueAt(i)) {
                        item selectedListItem = adapter.getItem(selectedEdit.keyAt(i));
                        itemDialog(selectedListItem.getcItemName(), selectedListItem.getcItemQuantity());
                    }
                }

                break;

            case R.id.delete:
                SparseBooleanArray selected = adapter.getSelectedListIds();
                for (int i = 0 ; i <= selected.size() -1  ; i++) {
                    if (selected.valueAt(i)) {
                        item selectedListItem = adapter.getItem(selected.keyAt(i));
                        deleteDB(selectedListItem.getcItemName());
                    }
                }

                items.clear();
                loadData(0);
                Toast.makeText(getApplicationContext(), "Selected Items Deleted!", Toast.LENGTH_SHORT).show();
                break;

        }



    }

    private void itemDialog(final String itemName, final String itemQuantity) {

        final AlertDialog itemBox = new AlertDialog.Builder(this).create();

        View dialogView = LayoutInflater.from(this).inflate(R.layout.add_item_view, null);

        final EditText itemNameText = dialogView.findViewById(R.id.item);
        final EditText itemQuantityText = dialogView.findViewById(R.id.quantity);

        if (!itemName.isEmpty() && !itemQuantity.isEmpty()) {
            itemNameText.setText(itemName);
            itemQuantityText.setText(itemQuantity);
            itemBox.setTitle("Edit");
            itemBox.setIcon(R.drawable.ic_edit);
        } else {
            itemBox.setTitle("Add");
            itemBox.setIcon(R.drawable.ic_plus);
        }


        itemBox.setView(dialogView);
        itemBox.show();

        Button saveButton = dialogView.findViewById(R.id.save);
        Button cancelButton = dialogView.findViewById(R.id.cancel);

        final InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemBox.cancel();
                manager.hideSoftInputFromWindow(itemNameText.getWindowToken(),0);
                manager.hideSoftInputFromWindow(itemQuantityText.getWindowToken(), 0);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String itemNameEdited = String.valueOf(itemNameText.getText());
                String itemQuantityEdited = String.valueOf(itemQuantityText.getText());

                if (itemNameEdited.length() == 0) {
                    Toast.makeText(getApplicationContext(), "Item Name Needed", Toast.LENGTH_SHORT).show();
                } else if (itemQuantityEdited.length() == 0) {
                    Toast.makeText(getApplicationContext(), "Item Quantity Needed", Toast.LENGTH_SHORT).show();
                } else {

                    if (itemName.length() == 0 || itemQuantity.length() == 0) {
                        if ((checkAvailability(itemNameEdited))) {
                            addDB(itemNameEdited, itemQuantityEdited);
                            itemBox.cancel();
                            manager.hideSoftInputFromWindow(itemNameText.getWindowToken(), 0);
                            manager.hideSoftInputFromWindow(itemQuantityText.getWindowToken(), 0);
                        } else {
                            Toast.makeText(getApplicationContext(), "Item Name already Exists", Toast.LENGTH_SHORT).show();
                        }
                    }

                    if (itemName.length() > 0 && itemQuantity.length() > 0) {

                        if ((itemName.equals(itemNameEdited) && !checkAvailability(itemNameEdited)) || (checkAvailability(itemNameEdited))) {
                            oldItemName = itemName;
                            updateDB(itemNameEdited, itemQuantityEdited);
                            itemBox.cancel();
                            manager.hideSoftInputFromWindow(itemNameText.getWindowToken(), 0);
                            manager.hideSoftInputFromWindow(itemQuantityText.getWindowToken(), 0);
                        } else {
                            Toast.makeText(getApplicationContext(), "Item Name Exists", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

            }
        });
    }


    private void updateDBStrike(int i) {

        ContentValues cv = new ContentValues();

        if (items.get(i).getcStrike()) {
            cv.put("strike", "false");
        } else {
            cv.put("strike", "true");
        }

        shoppingList.update("itemList", cv, "itemName = ? AND listName = ? ", new String[]{items.get(i).getcItemName(), listName});

        items.clear();

        loadData(0);

    }



    private void deleteDB(String i) {

        shoppingList.delete("itemList", " itemName = ? AND listName = ? ", new String[]{i, listName});

    }


    private Boolean checkAvailability(String itemName) {

        Cursor cursor = shoppingList.rawQuery("SELECT * FROM itemList WHERE itemName = ? AND listName = ?", new String[]{itemName, listName});

        cursor.moveToFirst();

        if (cursor.getCount() == 0) {
            return true;
        } else {
            return false;
        }

    }

    private void addDB(String itemName, String quantity) {

        shoppingList.execSQL("CREATE TABLE IF NOT EXISTS itemList (itemName VARCHAR , listName VARCHAR , quantity TEXT, strike TEXT )");

        String sql = "INSERT INTO itemList (itemName, listName, quantity, strike) VALUES  ( ? , ? , ? , ? )";

        SQLiteStatement statement = shoppingList.compileStatement(sql);

        statement.bindString(1, itemName);
        statement.bindString(2, listName);
        statement.bindString(3, quantity);
        statement.bindString(4, String.valueOf(false));

        statement.execute();

        Toast.makeText(this, "Item Added", Toast.LENGTH_SHORT).show();

        items.add(new item(itemName, quantity, false));

        adapter.notifyDataSetChanged();

        TextView addNewText = findViewById(R.id.addnew);
        addNewText.setVisibility(View.GONE);

    }

    private void updateDB(String itemName, String quantity) {

        ContentValues cv = new ContentValues();
        cv.put("itemName", itemName);
        cv.put("quantity", quantity);
        cv.put("strike", String.valueOf(false));

        shoppingList.update("itemList", cv, "itemName = ? AND listName = ? ", new String[]{oldItemName, listName});

        Toast.makeText(this, "Item Updated!", Toast.LENGTH_SHORT).show();

        loadData(0);

    }

    public void addNewItem(View view) {

        itemDialog("", "");

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_item, menu);

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {

            case R.id.unstrikeAll:
                strikeAll(false);
                return true;
            case R.id.strikeAll:
                strikeAll(true);
                return true;

            case R.id.deleteAll:
                deleteAll();
                return true;

            case R.id.share:
                shareStatus();
                return true;

            default:
                Log.i("Menu", "None Selected");
                return false;

        }
    }

    private void shareStatus() {

        String result = "";

        result = "List Name  : " + listName + "\n" + " \nItems in the List \n" + "\n" ;

        String itemsData =  loadData(1);

        result = result + itemsData;

        Intent email = new Intent(Intent.ACTION_SENDTO);
        email.setData(Uri.parse("mailto:"));
        email.putExtra(Intent.EXTRA_TEXT, result );
        email.putExtra(Intent.EXTRA_SUBJECT, "Shopping List Status - " + listName);
        if (email.resolveActivity(getPackageManager()) != null) {
            startActivity(email);
        }

    }

    private void strikeAll(boolean action) {

    ContentValues cv = new ContentValues();

    cv.put("strike", String.valueOf(action));

    shoppingList.update("itemList", cv, "listName = ?", new String[]{listName});

    items.clear();

    loadData(0);

    }

    private void deleteAll() {

        shoppingList.delete("itemList", "listName = ?", new String[]{listName});

        Toast.makeText(this, "All item data Deleted!", Toast.LENGTH_SHORT).show();

        items.clear();

        loadData(0);

    }


}

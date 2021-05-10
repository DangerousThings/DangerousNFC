package com.dangerousthings.nfc.pages;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import com.dangerousthings.nfc.R;
import com.dangerousthings.nfc.adapters.SavedImplantRecyclerAdapter;
import com.dangerousthings.nfc.databases.ImplantDatabase;
import com.dangerousthings.nfc.interfaces.IImplantDAO;
import com.dangerousthings.nfc.interfaces.IItemLongClickListener;
import com.dangerousthings.nfc.models.Implant;
import com.dangerousthings.nfc.utilities.HexUtils;

import java.util.List;

public class SavedImplantsActivity extends BaseActivity implements IItemLongClickListener
{
    List<Implant> _savedImplants;

    ImageButton mBackButton;
    RecyclerView mRecyclerView;
    SavedImplantRecyclerAdapter _recyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_implants);

        mRecyclerView = findViewById(R.id.saved_implants_recycler);
        mBackButton = findViewById(R.id.saved_implants_button_back);

        mBackButton.setOnClickListener(v -> onBackPressed());

        ImplantDatabase database = ImplantDatabase.getInstance(this);
        IImplantDAO implantDAO = database.implantDAO();
        _savedImplants = implantDAO.getImplantList();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        _recyclerAdapter = new SavedImplantRecyclerAdapter(this, _savedImplants);
        _recyclerAdapter.setClickListener(this);
        mRecyclerView.setAdapter(_recyclerAdapter);
    }

    @Override
    public void onItemClick(int position)
    {
        Intent displayImplant = new Intent(this, ImplantManagementActivity.class);
        displayImplant.putExtra(getString(R.string.intent_tag_uid), _recyclerAdapter.getImplant(position).getUID());
        displayImplant.putExtra(getString(R.string.intent_oboard_flag), false);
        startActivity(displayImplant);
        overridePendingTransition(0, 0);
    }

    @Override
    public boolean onItemLongClick(int position)
    {
        new AlertDialog.Builder(this)
                .setTitle("Delete Saved Implant")
                .setMessage("Are you sure you want to remove this device from you list of saved implants?")
                .setPositiveButton("Yes", ((dialog, which) ->
                {
                    ImplantDatabase database = ImplantDatabase.getInstance(this);
                    IImplantDAO implantDAO = database.implantDAO();
                    implantDAO.deleteImplant(_recyclerAdapter.getImplant(position));
                    _savedImplants.remove(position);
                    _recyclerAdapter.notifyItemRemoved(position);
                }))
                .setNegativeButton("No", ((dialog, which) -> dialog.cancel()))
                .show();
        return true;
    }
}
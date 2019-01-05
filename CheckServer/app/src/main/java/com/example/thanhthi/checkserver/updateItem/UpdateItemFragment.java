package com.example.thanhthi.checkserver.updateItem;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import com.example.thanhthi.checkserver.R;
import com.example.thanhthi.checkserver.SendDataToMainActivity;
import com.example.thanhthi.checkserver.data.model.ItemCheckServer;
import com.example.thanhthi.checkserver.services.CheckServerService;

public class UpdateItemFragment extends Fragment implements View.OnClickListener
{
    public static final String IS_CREATE = "create";
    public static final String IS_EDIT = "edit";
    private String flag;

    private TextView title;
    private TextInputEditText edtUrl, edtKeyWord, edtMessage, edtFrequency;
    private Switch switchCheck;
    private Button btnDone;

    private ItemCheckServer selectedModel;
    private int position;
    private SendDataToMainActivity sendDataToMainActivity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.activity_update_item, container, false);
        initView(view);
        return view;
    }

    @SuppressLint("SetTextI18n")
    private void initView(View view)
    {
        title = view.findViewById(R.id.title);
        edtUrl = view.findViewById(R.id.edtUrl);
        edtKeyWord = view.findViewById(R.id.edtKeyWord);
        edtMessage = view.findViewById(R.id.edtMessage);
        edtFrequency = view.findViewById(R.id.edtFrequency);
        switchCheck = view.findViewById(R.id.switchCheck);
        btnDone = view.findViewById(R.id.btnDone);

        switchCheck.setChecked(false);

        switch (flag)
        {
            case IS_CREATE:
            {
                title.setText("Tạo mới");
                break;
            }
            case IS_EDIT:
            {
                title.setText("Chỉnh sửa");
                edtUrl.setText(selectedModel.getUrl());
                edtKeyWord.setText(selectedModel.getKeyWord());
                edtMessage.setText(selectedModel.getMessage());
                edtFrequency.setText(selectedModel.getFrequency() + "");
                switchCheck.setChecked(selectedModel.isChecking());
                break;
            }
        }

        btnDone.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btnDone:
            {
                String url = edtUrl.getText().toString().trim();
                String keyWord = edtKeyWord.getText().toString().trim();
                String message = edtMessage.getText().toString().trim();
                Float frequency = Float.parseFloat(edtFrequency.getText().toString());

                selectedModel = new ItemCheckServer(url, keyWord, message, frequency, switchCheck.isChecked());

                checkSwitch();
                updateItemListActivity();
                break;
            }
        }
    }

    private void checkSwitch()
    {
        if (switchCheck.isChecked())
            startCheckServer();
        else
            stopCheckServer();
    }

    private void startCheckServer()
    {
        // start service
        Intent startIntent = new Intent(getActivity(), CheckServerService.class);
        startIntent.putExtra(CheckServerService.INFOR_MODEL, selectedModel.toJson());
        getActivity().startService(startIntent);
    }

    private void stopCheckServer()
    {
        // stop service
        Intent stopService = new Intent(getActivity(), CheckServerService.class);
        getActivity().stopService(stopService);
    }

    private void updateItemListActivity()
    {
        switch(flag)
        {
            case IS_CREATE:
            {
                sendDataToMainActivity.sendNewItem(selectedModel);
                break;
            }
            case IS_EDIT:
            {
                sendDataToMainActivity.sendEditItem(selectedModel, position);
                break;
            }
        }
        getActivity().onBackPressed();
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public void setSelectedModel(ItemCheckServer selectedModel) {
        this.selectedModel = selectedModel;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setSendDataToMainActivity(SendDataToMainActivity sendDataToMainActivity) {
        this.sendDataToMainActivity = sendDataToMainActivity;
    }
}

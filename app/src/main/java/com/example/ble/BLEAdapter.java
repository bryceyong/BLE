package com.example.ble;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;



public class BLEAdapter extends RecyclerView.Adapter<BLEAdapter.BLEViewHolder> {
    private ArrayList<BLEDevice> BLEDeviceList = new ArrayList<BLEDevice>();
    private BLEViewHolder.OnItemClickListener mListener;

    public void setOnItemClickListener(BLEViewHolder.OnItemClickListener listener){
        mListener = listener;
    }

    public static class BLEViewHolder extends RecyclerView.ViewHolder {

        public interface OnItemClickListener {
            void onItemClick(int position);
        }



        public TextView deviceName;
        public TextView macAddress;

        public BLEViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            deviceName = itemView.findViewById(R.id.deviceName);
            macAddress = itemView.findViewById(R.id.macAddress);

            //action when clicked
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });

        }
    }

    public BLEAdapter(ArrayList<BLEDevice> BLEDeviceList){
        this.BLEDeviceList = BLEDeviceList;
    }

    @NonNull
    @Override
    public BLEViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.bledevice, parent, false);
        BLEViewHolder bvh = new BLEViewHolder(v, mListener);
        return bvh;
    }

    @Override
    public void onBindViewHolder(@NonNull BLEViewHolder holder, int position) {
        BLEDevice bleDevice  = BLEDeviceList.get(position);
        holder.deviceName.setText(bleDevice.getDeviceName());
        holder.macAddress.setText(bleDevice.getMacAddress());

    }

    @Override
    public int getItemCount() {
        return BLEDeviceList.size();
    }
}

package demo.lucius.blemodule.ui.view.adapter;

import android.bluetooth.le.ScanResult;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import demo.lucius.blemodule.R;
import demo.lucius.blemodule.databinding.AdapterBleListBinding;
import demo.lucius.blemodule.module.BleDeviceInfo;
import demo.lucius.blemodule.protocol.utils.ProtocolUtil;
import demo.lucius.blemodule.ui.activity.BleInfoActivity;

public class BleListAdapter extends RecyclerView.Adapter<BleListAdapter.ViewHolder> {


    //用于表示点击事件回调
    private OnClickListener onClickListener;

    //用于表示需要显示的BLE信息
    private List<BleDeviceInfo> bleDeviceInfos;

    //用于表示需要显示的BLE
//    private List<ScanResult> scanResults;

//    public BleListAdapter(List<ScanResult> scanResults) {
//        this.scanResults = scanResults;
//    }

    public BleListAdapter(List<BleDeviceInfo> bleDeviceInfos) {
        this.bleDeviceInfos = bleDeviceInfos;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AdapterBleListBinding adapterBleListBinding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.adapter_ble_list, parent,
                        false);

        ViewHolder viewHolder = new ViewHolder(adapterBleListBinding.getRoot());
        viewHolder.setAdapterBleListBinding(adapterBleListBinding);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {


        holder.adapterBleListBinding.setDevice(bleDeviceInfos.get(position).getScanResult().getDevice());
        holder.adapterBleListBinding.setState(bleDeviceInfos.get(position).getState());
        holder.adapterBleListBinding.setName(bleDeviceInfos.get(position).getBleName());
        //用于表示Mac地址 bootVersion  appVersion
        final byte info[] = bleDeviceInfos.get(position).getScanResult().getScanRecord().getBytes();
//        holder.adapterBleListBinding.setMac(ProtocolUtil.byteToHexString(info));
        holder.adapterBleListBinding.setMac(ProtocolUtil.reverseCode(ProtocolUtil.cutByteToHexString(info, 7, 12)));
        holder.adapterBleListBinding.setVersion(ProtocolUtil.reverseCode(ProtocolUtil.cutByteToHexString(info, 13,
                21)));

        holder.adapterBleListBinding.bleDeviceCl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                onClickListener.onClickListener(scanResults.get(position));

//                bluetoothAdapter.getBluetoothLeScanner().stopScan(bleScanCallback);
//                Intent intent = new Intent(this, BleTaskActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putParcelable("SCAN_RESULT", scanResult);
//                intent.putExtras(bundle);
//                startActivity(intent);

                Intent intent=new Intent(holder.adapterBleListBinding.getRoot().getContext(), BleInfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("SCAN_RESULT", bleDeviceInfos.get(position).getScanResult());
                intent.putExtras(bundle);
                holder.adapterBleListBinding.getRoot().getContext().startActivity(new Intent(holder.adapterBleListBinding.getRoot().getContext(), BleInfoActivity.class));
            }
        });


    }


    @Override
    public int getItemCount() {
//        return scanResults.size();
        return bleDeviceInfos.size();
    }

    public void setOnClickListener(BleListAdapter.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    //用于进行点击回调
    public interface OnClickListener {
        void onClickListener(ScanResult scanResult);

    }


    //用于表示ViewHolder
    public static class ViewHolder extends RecyclerView.ViewHolder {

        AdapterBleListBinding adapterBleListBinding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public AdapterBleListBinding getAdapterBleListBinding() {
            return adapterBleListBinding;
        }

        public void setAdapterBleListBinding(AdapterBleListBinding adapterBleListBinding) {
            this.adapterBleListBinding = adapterBleListBinding;
        }
    }

}

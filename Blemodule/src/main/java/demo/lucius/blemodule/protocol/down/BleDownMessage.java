package demo.lucius.blemodule.protocol.down;

import java.util.Arrays;

import demo.lucius.blemodule.log.LogUtils;
import demo.lucius.blemodule.protocol.utils.ByteCryPt;
import demo.lucius.blemodule.protocol.utils.ProtocolUtil;

//用于生成下行报文
public class BleDownMessage {


    //用于生成试探帧
    public static byte[] bleprobe() {
        byte[] payload = new byte[16];
        byte[] address = new byte[6];
        Arrays.fill(address, (byte) 0xff);
        System.arraycopy(address, 0, payload, 0, 6);
        BleDown bleDown = new BleDown.Builder(10, 1).payload(payload).build();
        return bleDown.getBleDownMessaage();
    }

    //用于生成安全认证帧
    public static byte[] safetyCertificate() {
        //用于生成随机数
        byte[] randomByte = ByteCryPt.createRandomByte();
        enK2Crypt(randomByte);
        BleDown bleDown=new BleDown.Builder(1,1).payload(randomByte).build();
        return bleDown.getBleDownMessaage();
    }

    //用于BLE透传78数据帧
    public static byte[] bleTrans78Data(int frameIdx,int followUp,byte[] data){
        BleDown bleDown=new BleDown.Builder(0x0b,2).payload(data).frameIdx(frameIdx).followUp(followUp).build();
        return bleDown.getBleDownMessaage();
    }



    public static void enK2Crypt(byte[] mRandomBytes) {
        try {
            byte[] mBytes_Ek2 = ByteCryPt.Encrypt(mRandomBytes, ByteCryPt.K2);
            ByteCryPt.RANDOM_K2 = mBytes_Ek2;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

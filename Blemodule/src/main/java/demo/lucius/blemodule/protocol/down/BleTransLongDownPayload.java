package demo.lucius.blemodule.protocol.down;

import demo.lucius.blemodule.protocol.utils.CRC16Util;
import demo.lucius.blemodule.protocol.utils.ProtocolUtil;

/***
 * Ble长帧生成报文
 * BLE透传78协议，生成BLE的payload，再将payload切分为16个自己一组
 */
public class BleTransLongDownPayload {


    //用于在78协议外包裹信息
    public static byte[] getPayloadInfo(byte[] message){
        byte[] tmp=new byte[message.length+6];
        byte[] pack_len= ProtocolUtil.hexStringToByte(ProtocolUtil.reverseCode(ProtocolUtil.appendCode(String.valueOf(message.length),4)));
        System.arraycopy(pack_len,0,tmp,0,2);
        System.arraycopy(message,0,tmp,4,message.length);
        System.arraycopy(CRC16Util.CRC_16_X25(message),0,tmp,message.length+4,2);
        return tmp;

    }

    //用于获取断路器地址
    public static byte[] getBreakerAddress(){
        Ble78Down ble78Down=new Ble78Down.Builder(4).build();
        return getPayloadInfo(ble78Down.getBle78message());
    }


}

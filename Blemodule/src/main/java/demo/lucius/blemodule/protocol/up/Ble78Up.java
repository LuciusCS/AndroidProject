package demo.lucius.blemodule.protocol.up;

import demo.lucius.blemodule.protocol.utils.ProtocolUtil;
import demo.lucius.blemodule.protocol.utils.ble.BleSendCtrl;

/**
 * 用于解析78上行报文
 */
public class Ble78Up {

    //用于表示Ble透传上来的的78报文
    private byte data[];

    //用于表示CRC16
    private String crc16;

    //用于表示数据长度
    private int length;

    //用于表示协议版本号
    private int version;

    //用于表示控制码
    private int ctr;

    //用于表示变长数据信息
    private byte[] payload = new byte[]{};


    public Ble78Up(byte[] data) {
        this.data = data;
        length = ProtocolUtil.getCode(data, 1, 2);
        System.arraycopy(data, 6, payload, 0, length);
        ctr = data[4] & 0xff;
        getInfo();
    }

    //用于解析数据
    private void getInfo() {
        if ((ctr >> 6 & 0b1) == 0) {
            //用于表示查询的信息，取消消息重传
            BleSendCtrl.resendTimer=-1;

        } else {
            //用于表示主动上报的信息
        }

        switch (data[5] & 0xff) {
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                break;
            case 0x0f:
                break;
            case 0x10:
                break;
            case 0x11:
                break;
            case 0x12:
                //用于78透传
                break;
            default:
                break;
        }
    }

    //用于处理78透传报文的信息
    private void handleTransparentInfo(byte[] payload) {
        //用于判断透出的是否为645协议
        String pattern = ".*68.{12}68.*";
        String tmpMessage = ProtocolUtil.byteToHexString(ProtocolUtil.cutOutByte(payload, 0,
                payload.length - 1));
        if (tmpMessage.matches(pattern)) {
            //上传645报文处理


        }
    }


}

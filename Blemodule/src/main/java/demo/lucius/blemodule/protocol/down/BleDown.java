package demo.lucius.blemodule.protocol.down;


import demo.lucius.blemodule.log.LogUtils;
import demo.lucius.blemodule.protocol.utils.ByteCryPt;
import demo.lucius.blemodule.protocol.utils.ProtocolUtil;

/**
 * 用于BLE下传协议，长度少于20字节，数据域长度少于16字节
 */
public class BleDown {

    private byte cmdVer = 0;
    private int cmd = 0x0;  //命令标识
    private int ver = 0x0;  //版本

    private byte ctr = 0;
    private int frameIdx = 0x0; //用于表示帧序号
    private int followUp = 0x0; //用于表示是否有后续帧
    private int encMode = 0x0;  //用于表示加密方式
    private int response = 0x1; //用于表示应答模式

    private byte rev = 0;       //保留用于拓展
    private byte[] payload = new byte[16];
    private byte sum = 0;        //对数据域加密前的16进制计算和校验

    //用于表示下行报文
    private byte[] bleDownMessaage = new byte[20];


    public BleDown(Builder builder) {
        cmd=builder.cmd;
        ver=builder.ver;
        frameIdx=builder.frameIdx;
        followUp=builder.followUp;
        encMode=builder.encMode;
        response=builder.response;
        payload=builder.payload;
        generateDownMessage();
    }

    //用于生成Ble下行报文
    private void generateDownMessage() {
        cmdVer = (byte) ((cmd & 0x3f) + (ver << 6));

        bleDownMessaage[0] = cmdVer;
        ctr = (byte) ((frameIdx & 0xf )+ (followUp << 4) +( encMode << 5 )+ (response << 7));
        bleDownMessaage[1] = ctr;
        bleDownMessaage[2] = rev;
        sumCheck();
        switch (encMode) {
            case 0:
                //明文发送
                break;
            case 1:
                payload = enK1Crypt(payload);
                LogUtils.printInfo("加密后报文："+ProtocolUtil.byteToHexStringSpace(payload));
                //认证秘钥加密
                break;
            case 2:
                payload = enK2Crypt(payload);
                //随机秘钥加密
                break;

            default:
                break;
        }
        System.arraycopy(payload,0,bleDownMessaage,3,16);
        bleDownMessaage[19]=sum;
        LogUtils.printInfo("Ble报文"+ProtocolUtil.byteToHexStringSpace(bleDownMessaage));
    }

    //用于计算和校验
    private void sumCheck() {
        int index = payload.length;
        for (int i = 0; i < index; i++) {
            sum +=( payload[i]&0xff);
        }
        sum = (byte) (sum & 0xff);
    }

    //用于表示认证秘钥解密
    private byte[] enK1Crypt(byte[] payload) {
        try {
            byte[] mBytes_Ek1 = ByteCryPt.Encrypt(payload, ByteCryPt.K1);
            return mBytes_Ek1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //用于表示随机秘钥解密
    private byte[] enK2Crypt(byte[] mRandomBytes) {
        try {
            byte[] mBytes_Ek2 = ByteCryPt.Encrypt(mRandomBytes, ByteCryPt.RANDOM_K2);
            return mBytes_Ek2;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public byte[] getBleDownMessaage() {
        return bleDownMessaage;
    }

    public static class Builder {
        int cmd = 0x0;  //命令标识
        private int ver = 0x0;  //版本
        int encMode = 0x0;  //用于表示加密方式
        private int frameIdx = 0x0; //用于表示帧序号
        private int followUp = 0x0; //用于表示是否有后续帧
        private int response = 0x1; //用于表示应答模式
        private byte[] payload = new byte[16];

        public Builder(int cmd, int encode) {
            this.cmd = cmd;
            this.encMode = encode;
        }

        public Builder ver(int val) {
            this.ver = val;
            return this;
        }

        public Builder frameIdx(int val) {
            this.frameIdx = val;
            return this;
        }

        public Builder followUp(int val) {
            this.followUp = val;
            return this;
        }

        public Builder response(int val) {
            this.response = val;
            return this;
        }

        public Builder payload(byte[] val) {
            System.arraycopy(val, 0, this.payload, 0, val.length);
            return this;
        }

        public BleDown build() {
            return new BleDown(this);
        }

    }

}

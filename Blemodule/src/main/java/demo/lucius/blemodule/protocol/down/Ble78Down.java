package demo.lucius.blemodule.protocol.down;

import demo.lucius.blemodule.protocol.utils.ProtocolUtil;

/**
 * 用于78协议
 */
public class Ble78Down {

    //用于表示数据长度
    private byte[] length = new byte[]{0x0, 0x0};

    //用于表示协议版本号
    private byte version = 0x1;
    //用于表示控制码，目前断路器及空气开关
    private byte ctrl = 0x12;
    //用于表示MCU类型
    private int mcuType = 3;

    //用于表示命令标识
    private byte cmdTag = 0x0;

    //用于表示变长数据域
    private byte[] variable = new byte[]{};
    //用于表示头部信息
    private byte[] head78 = new byte[]{0x78, 0x0, 0x0, version, ctrl, cmdTag};

    //用于表示校验码

    //用于表示整条信息
    private byte[] ble78message;

    public Ble78Down(Builder builder) {
        this.cmdTag=builder.cmdTag;
        this.mcuType=builder.mcuType;
        this.variable=builder.variable;
    }


    //用于生成78下行报文
    private void generateBle78Down() {
        length = ProtocolUtil.hexStringToByte(ProtocolUtil.reverseCode(ProtocolUtil.appendCode(variable.length + "",
                4)));
        System.arraycopy(length, 0, head78, 1, 2);
        head78[3] = version;
        ctrl = (byte) (mcuType & 0b11 << 2);
        head78[4] = ctrl;
        head78[5] = cmdTag;
        ble78message = new byte[8 + variable.length];
        System.arraycopy(head78, 0, ble78message, 0, 6);
        System.arraycopy(variable, 0, ble78message, 6, variable.length);
    }

    public byte[] getBle78message() {
        return ble78message;
    }

    public static class Builder {
        //用于表示MCU的类型
        int mcuType = 3;
        //用于表示命令标识
        private byte cmdTag = 0x0;

        //用于变长数据域
        private byte[] variable;

        public Builder(int cmdTag) {
            this.cmdTag = (byte) cmdTag;
        }

        public Builder mcuType(int val){
            this.mcuType=val;
            return this;
        }

        public Builder variable(byte[] val){
            this.variable=val;
            return this;
        }

        public Ble78Down build(){
            return new Ble78Down(this);
        }

    }


}

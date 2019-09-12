package demo.lucius.blemodule.protocol.up;

import demo.lucius.blemodule.log.LogUtils;
import demo.lucius.blemodule.moudle.observe.ProtocolObservable;
import demo.lucius.blemodule.protocol.utils.ByteCryPt;
import demo.lucius.blemodule.protocol.utils.ProtocolUtil;
import demo.lucius.blemodule.protocol.utils.ble.BleSendCtrl;

/**
 * 用于解析Ble上行报文
 */
public class BleUp {

    //用于表示上行报文
    private byte[] data;
    //用于表示本次的payload
    private byte[] payloadItem = new byte[16];
    //用于表示整体额的payload
    private byte[] payload = new byte[]{};

    //用于表示cmd_ver
    private int cmd_ver;
    //用于表示ctr
    private int ctr;
    //用于表示sum
    private int sum;
    //用于表示帧序号
    private int frameOldIdx;

    //单例模式用于拼接，主要拼接payload
    private static BleUp unqueInstance = new BleUp();

    private BleUp() {

    }

    public static BleUp getInstance() {
        return unqueInstance;
    }

    //用于报文解析与payload拼接，唯一入口
    public void setMessage(byte[] data) {
        this.data = data;
        cmd_ver = data[0] & 0xff;  //byte强制类型转换为int需要考虑符号位，如果是int强制类型转换为byte，截取最低八位即可
        ctr = data[1] & 0xff;
        sum = data[19] & 0xff;
        System.arraycopy(data, 3, payloadItem, 0, 16);
        getInfo();
    }

    /**
     * 用于解析数据
     */
    private void getInfo() {
        try {
            decrypt();
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.printInfo(e.getMessage());
        }

        LogUtils.printInfo("解密后报文" + ProtocolUtil.byteToHexStringSpace(payloadItem));
        //验证通过同时正常应答
//        if (sum == sumCheck(payloadItem) && (ctr >> 7 & 0b1) == 1) {  //目前断路器都显示异常应答
        if (sum == sumCheck(payloadItem)) {


            if ((ctr & 0xf) == 0) {
                frameOldIdx = 0;
                payload = new byte[]{};
                ProtocolUtil.combineByte(payload, payloadItem);
//                System.arraycopy(payloadItem, 0, payload, 0, 16);
            } else if ((ctr & 0xf) == ++frameOldIdx) {
                //分帧序号不为零同时加一
                ProtocolUtil.combineByte(payload, payloadItem);
            } else {
                //帧序号错乱
                payload = new byte[]{};
                return;
            }
            //用于判断没有后续帧
            if ((ctr >> 4 & 0b1) == 0) {
                try {
                    BleSendCtrl.resendInfo.put("++");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                switch (cmd_ver & 0x3f) {
                    case 1:
                        LogUtils.printInfo("Ble断路器认证成功");
                        ProtocolObservable.getInstance().notifyObservers("abc","asda");
                        //安全认证
                        break;
                    case 2:
                        //报文透传（645）
                        break;
                    case 3:
                        //启动升级
                        break;
                    case 4:
                        //升级数据帧传输
                        break;
                    case 5:
                        //升级查询
                        break;
                    case 6:
                        //维护地址映射表
                        break;
                    case 7:
                        //远程复位指令
                        break;
                    case 0x0a:
                        //连接试探帧
                        break;
                    case 0x0b:
                        //78透传
                        Ble78Up ble78Up = new Ble78Up(payload);
                        break;
                    default:
                        //有上行报文停止报文重发
//

                        break;

                }

            } else {
                //有后续帧
            }

        } else if (sum != sumCheck(payloadItem)) {
            //校验失败
            LogUtils.printInfo("校验失败");
        } else if ((ctr >> 7 & 0b1) == 0) {
            //异常应答
            LogUtils.printInfo("异常应答");
        }

    }

    //对数据进行解密
    private void decrypt() throws Exception {
        //判断加密类型
//        LogUtils.printInfo("加密类型：" + (ctr >> 5 & 0b11));
        switch (ctr >> 5 & 0b11) {
            case 0:
                //明文传输
                break;
            case 1:
                //认证秘钥加密
                payloadItem = ByteCryPt.Decrypt(payloadItem, ByteCryPt.K1);
                break;
            case 2:
                //随机秘钥加密
//                LogUtils.printInfo("随机加密秘钥：" + ProtocolUtil.byteToHexStringSpace(ByteCryPt.RANDOM_K2));
                payloadItem = ByteCryPt.Decrypt(payloadItem, ByteCryPt.RANDOM_K2);
                break;
            default:
                break;
        }
    }


    //用于计算和校验
    private int sumCheck(byte[] tmp) {
        int sumTmp = 0;
        int index = tmp.length;

        for (int i = 0; i < index; i++) {
            sumTmp += (tmp[i] & 0xff);
        }
        sumTmp = sumTmp & 0xff;

        return sumTmp;
    }


}

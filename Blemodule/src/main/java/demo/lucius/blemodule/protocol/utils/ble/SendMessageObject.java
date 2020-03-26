package demo.lucius.blemodule.protocol.utils.ble;

public class SendMessageObject {

    public final static int BLE_DIRECT_SEND = 1;  // BLE直接下发报文，不需要重新发送
    public final static int BLE_DIRECT_RESEND = 2; //BLE直接下发报文，需要进行重新发送
    public final static int BLE_78_SEND = 3;       //BLE透传78报文，不需要重新发送
    public final static int BLE_78_RESEND = 4;     //BLE透传78报文，需要重新发送

    //用于表示报文类型
    private int type;

    //用于表示报文
    private byte[] message;

    //用于表示报文重发次数
    private int resendTime = 0;

    //用于表示报文重发间隔时间
    private int resendTimer = 1;


    public SendMessageObject(int type, byte[] message) {
        this.type = type;
        this.message = message;
    }

    public SendMessageObject(Builder builder) {
        this.type = builder.type;
        this.message = builder.message;
        this.resendTime=builder.resendTime;
        this.resendTimer=builder.resendTimer;
    }


    public static class Builder {
        //用于表示报文类型
        private int type;

        //用于表示报文
        private byte[] message;

        //用于表示报文重发次数
        private int resendTime = 1;

        //用于表示报文重发间隔时间
        private int resendTimer = 5;

        public Builder(int type, byte[] message) {
            this.type = type;
            this.message = message;
        }

        public Builder resendTime(int val) {
            this.resendTime = val;
            return this;
        }

        public Builder resendTimer(int val) {
            this.resendTimer = val;
            return this;
        }

        public SendMessageObject build() {
            return new SendMessageObject(this);
        }

    }


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public byte[] getMessage() {
        return message;
    }

    public void setMessage(byte[] message) {
        this.message = message;
    }

    public int getResendTime() {
        return resendTime;
    }

    public void setResendTime(int resendTime) {
        this.resendTime = resendTime;
    }

    public int getResendTimer() {
        return resendTimer;
    }

    public void setResendTimer(int resendTimer) {
        this.resendTimer = resendTimer;
    }
}

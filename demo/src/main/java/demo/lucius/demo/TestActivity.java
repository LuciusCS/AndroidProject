//package demo.lucius.demo;
//
//@zourb 恩，我还看到一种方式：使用管道流Pipes
//        “管道”是java.io包的一部分。它是Java的特性，而不是Android特有的。一条“管道”为两个线程建立一个单向的通道。生产者负责写数据，消费者负责读取数据。
//        下面是一个使用管道流进行通信的例子。
//public class PipeExampleActivity extends Activity {
//
//    private static final String TAG = "PipeExampleActivity";
//    private EditText editText;
//
//    PipedReader r;
//    PipedWriter w;
//
//    private Thread workerThread;
//
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        r = new PipedReader();
//        w = new PipedWriter();
//
//        try {
//            w.connect(r);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        setContentView(R.layout.activity_pipe);
//        editText = (EditText) findViewById(R.id.edit_text);
//        editText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
//                try {
//                    if(count > before) {
//                        w.write(charSequence.subSequence(before, count).toString());
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//            }
//        });
//
//        workerThread = new Thread(new TextHandlerTask(r));
//        workerThread.start();
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        workerThread.interrupt();
//        try {
//            r.close();
//            w.close();
//        } catch (IOException e) {
//        }
//    }
//
//    private static class TextHandlerTask implements Runnable {
//        private final PipedReader reader;
//
//        public TextHandlerTask(PipedReader reader){
//            this.reader = reader;
//        }
//        @Override
//        public void run() {
//            while(!Thread.currentThread().isInterrupted()){
//                try {
//                    int i;
//                    while((i = reader.read()) != -1){
//                        char c = (char) i;
//
//                        Log.d(TAG, "char = " + c);
//                    }
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
//}
//在这个例子中，对EditText设置一个TextWatcher监听，一旦EditText的内容发生改变，就向“管道”中输入字符，它就是所谓的生产者。同时，有一个工作线程负责从管道中读取字符，它就是所谓的消费者。这样，就实现了UI线程和工作线程之间的数据通信。
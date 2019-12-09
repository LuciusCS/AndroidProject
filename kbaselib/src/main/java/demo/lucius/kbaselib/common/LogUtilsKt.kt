package demo.lucius.kbaselib.common

import android.util.Log
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


/***
 * 源码地址 https://www.jianshu.com/p/b691d5149763  github上有文件输出
 */
object LogUtilsKt {
    private var debug: Boolean = true//是否打印log
    private val CHUNK_SIZE = 106 //设置字节数
    init {

    }


    fun v(tag: String = "Verbose", msg: String) = debug.debugLog(tag, msg, Log.VERBOSE)
    fun d(tag: String = "Debug", msg: String) = debug.debugLog(tag, msg, Log.DEBUG)
    fun i(tag: String = "Info", msg: String) = debug.debugLog(tag, msg, Log.INFO)
    fun w(tag: String = "Warn", msg: String) = debug.debugLog(tag, msg, Log.WARN)
    fun e(tag: String = "Debug", msg: String) = debug.debugLog(tag, msg, Log.ERROR)


    private fun targetStackTraceMSg(): String {
        val targetStackTraceElement = getTargetStackTraceElement()
        return if (targetStackTraceElement != null) {
            "at ${targetStackTraceElement.className}.${targetStackTraceElement.methodName}(${targetStackTraceElement.fileName}:${targetStackTraceElement.lineNumber})"
        } else {
            ""
        }
    }

    private fun getTargetStackTraceElement(): StackTraceElement? {
        var targetStackTrace: StackTraceElement? = null
        var shouldTrace = false
        val stackTrace = Thread.currentThread().stackTrace
        for (stackTraceElement in stackTrace) {
            val isLogMethod = stackTraceElement.className == LogUtilsKt ::class.java.name
            if (shouldTrace && !isLogMethod) {
                targetStackTrace = stackTraceElement
                break
            }
            shouldTrace = isLogMethod
        }
        return targetStackTrace
    }



    private fun Boolean.debugLog(tag: String, msg: String, type: Int) {
        if (!this) {
            return
        }
        val newMsg = msgFormat(msg)

        when (type) {
            Log.VERBOSE -> Log.v(tag, newMsg)
            Log.DEBUG -> Log.d(tag, newMsg)
            Log.INFO -> Log.i(tag, newMsg)
            Log.WARN -> Log.w(tag, newMsg)
            Log.ERROR -> Log.e(tag, newMsg)
        }

    }

    private fun msgFormat(msg: String): String {
        val bytes: ByteArray = msg.toByteArray()
        val length = bytes.size

        var newMsg = "${targetStackTraceMSg()}"
        if (length > CHUNK_SIZE) {
            var i = 0
            while (i < length) {
                val count = Math.min(length - i, CHUNK_SIZE)
                val tempStr = String(bytes, i, count)
                newMsg += "$tempStr"
                i += CHUNK_SIZE
            }
        } else {
            newMsg += "$msg"
        }
        newMsg += "\n"
        return newMsg

    }

}
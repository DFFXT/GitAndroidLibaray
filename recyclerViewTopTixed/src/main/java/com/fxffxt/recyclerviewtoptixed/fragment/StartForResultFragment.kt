//特定包名：androidx.fragment.app，用以访问内部变量
package androidx.fragment.app

import android.content.Intent
import android.os.Bundle
import android.util.SparseArray

/**
 * 支持任意层级的fragment进行startActivityForResult跳转，并接收返回数据
 *
 */
abstract class StartForResultFragment:Fragment() {
    //记录是谁启动了activity，数据回来时取出
    private val mPendingFragmentActivityResults by lazy { SparseArray<String>() }
    override fun startActivityForResult(intent: Intent, requestCode: Int, options: Bundle?) {
        val f:Fragment = this
        startActivityForResult(intent, requestCode, options, f.mWho)
    }
    private fun startActivityForResult(intent: Intent?, requestCode: Int, options: Bundle?, who:String){
        val parent = parentFragment
        val f:Fragment = this
        when (parent) {
            null -> {//一级fragment，启动activity
                mPendingFragmentActivityResults.put(requestCode, who)
                f.mHost.onStartActivityFromFragment(this,intent,requestCode,options)
            }
            is StartForResultFragment -> {//不是一级fragment，将启动activity的任务交给上一级fragment
                mPendingFragmentActivityResults.put(requestCode, who)
                parent.startActivityForResult(intent, requestCode, options, f.mWho)
            }
            else -> {
                throw Exception("父Fragment必须继承StartForResultFragment")
            }
        }
    }

    override fun startActivityForResult(intent: Intent, requestCode: Int) {
        startActivityForResult(intent,requestCode,null)
    }

    final override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val f:Fragment = this
        mPendingFragmentActivityResults[requestCode]?.let {
            mPendingFragmentActivityResults.remove(requestCode)
            if (it == f.mWho){
                onSupportActivityResult(requestCode, resultCode, data)
                return
            }
            //不是本身，寻找子fragment来处理
            (childFragmentManager as FragmentManagerImpl).findFragmentByWho(it) ?.onActivityResult(requestCode,resultCode, data)
        }
    }
    open fun onSupportActivityResult(requestCode: Int, resultCode: Int, data: Intent?){

    }
}
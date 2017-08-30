import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @Date: 2017/8/30
 */

class TestAdapter : BaseAdapter {
    private var name: Int = 0
    private var type: String =""

    internal constructor(name: Int) {
        this.name = name
    }

    internal constructor(name: Int, type: String) {
        this.name = name
        this.type = type
    }

    override fun getCount(): Int {
        return 0
    }

    override fun getItem(i: Int): Any? {
        return null
    }

    override fun getItemId(i: Int): Long {
        return 0
    }

    override fun getView(i: Int, view: View, viewGroup: ViewGroup): View? {
        return null
    }
}

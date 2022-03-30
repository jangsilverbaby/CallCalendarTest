package com.example.callcalendartest

import android.annotation.SuppressLint
import android.os.Bundle
import android.provider.CallLog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

class WeekLogFragment : Fragment() {
    private lateinit var mAdapter: CallLogAdapter
    var callList = mutableListOf<Call>()
    var sdate = ""

    lateinit var calendarView: MaterialCalendarView
    lateinit var textTypeLog: TextView
    lateinit var recycler: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_weeklog, container, false)
        setContentView(view)
        return view
    }

    fun setContentView(view: View) {
        calendarView = view.findViewById(R.id.calendarView)
        textTypeLog = view.findViewById(R.id.textTypeLog)
        recycler = view.findViewById(R.id.recycler)

        mAdapter = CallLogAdapter(callList)
        recycler.adapter = mAdapter

        recycler.layoutManager = LinearLayoutManager(context)

        setCalendar()
        addDot()
    }

    fun setCalendar() {
        calendarView.setOnDateChangedListener { widget, date, selected ->
            recycler.visibility = View.VISIBLE
            textTypeLog.visibility = View.VISIBLE
            val df = DecimalFormat("00")
            sdate = "${date.year}/${df.format(date.month + 1)}/${date.day}"
            refreshAdapter()
            textTypeLogDataSet()
        }

        calendarView.addDecorator(TodayDecorator())
    }

    fun refreshAdapter() {
        recyclerDataSet()
        textTypeLogDataSet()
        mAdapter.notifyDataSetChanged()
    }

    fun recyclerDataSet() {
        callList.clear()
        callList.addAll(getPhoneNumbers(sdate))
    }

    fun textTypeLogDataSet() {
        var callLogList = mutableListOf<Int>(0, 0, 0)
        for (call in callList) {
            when (call.type) {
                "1" -> ++callLogList[0]
                "2" -> ++callLogList[1]
                "3" -> ++callLogList[2]
            }
        }
        textTypeLog.text = "수신 : ${callLogList[0]}  발신 : ${callLogList[1]}  부재중 : ${callLogList[2]}"
    }

    fun addDot() {
        val callLogUri = CallLog.Calls.CONTENT_URI

        var proj = arrayOf(CallLog.Calls.DATE)
        context?.run {
            val cursor = contentResolver.query(callLogUri, proj, null, null, null)
            while (cursor?.moveToNext() == true) {
                val callLogDate = cursor?.getString(0)

                val calendarDay = CalendarDay.from(Date(callLogDate.toLong()))

                calendarView.addDecorator((EventDecorator(Collections.singleton(calendarDay))))
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun getPhoneNumbers(clickDate: String): List<Call> {
        val list = mutableListOf<Call>()

        val callLogUri = CallLog.Calls.CONTENT_URI
        var proj = arrayOf(
            CallLog.Calls.PHONE_ACCOUNT_ID,
            CallLog.Calls.CACHED_NAME,
            CallLog.Calls.NUMBER,
            CallLog.Calls.TYPE,
            CallLog.Calls.DATE
        )

        context?.run {
            val cursor = contentResolver.query(callLogUri, proj, null, null, null)
            while (cursor?.moveToNext() == true) {
                val id = cursor?.getString(0)
                val name = cursor?.getString(1)
                val number = cursor?.getString(2)
                val type = cursor?.getString(3)
                val callLogDate = cursor?.getString(4)

                val formatter = SimpleDateFormat("yyyy/MM/dd", Locale.KOREA)
                val date = formatter.format(Date(callLogDate.toLong()))

                val call = Call(id, name, number, type, date)

                if (clickDate == date) {
                    list.add(0, call)
                }
            }
        }
        return list
    }
}
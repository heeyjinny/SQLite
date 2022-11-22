package com.heeyjinny.sqlite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.heeyjinny.sqlite.databinding.ItemRecyclerBinding
import java.text.SimpleDateFormat

//리사이클러뷰 어댑터를 상속받는 어댑터클래스 생성
//리사이클러뷰 목록만들기 과정과 동일함...
//Holder클래스 inner클래스로 어댑터 클래스 안에서 생성하기만 바뀜...
//2
class RecyclerAdapter: RecyclerView.Adapter<RecyclerAdapter.Holder>() {

    //8
    //데이터 삭제 구현을 위해 생성하는 helper프로퍼티 생성하고
    //Holder클래스에 클릭리스너 달기...
    var helper: SqliteHelper? = null

    //3
    var listData = mutableListOf<Memo>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        //4
        val binding = ItemRecyclerBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        //6
        val memo = listData.get(position)
        holder.setMemo(memo)

    }

    override fun getItemCount(): Int {
        //3
        return listData.size
    }


    //1
    //10
    //삭제버튼을 클릭하면 어댑터의 helper와 listData에 접근해야함
    //어댑터 클래스의 멤버변수인 helper와 listData에 접근을 위해
    //Holder클래스 전체를 어댑터 클래스 안에 옮기고 class앞에 inner키워드를 붙여줌
    inner class Holder(val binding: ItemRecyclerBinding): RecyclerView.ViewHolder(binding.root){

        //11
        //홀더는 한 화면에 그려지는 개수만큼 만든 후 재사용함 그래서
        //클릭하는 시점에 어떤 데이터가 있는지 알아야 하므로 변수 선언 후 setMemo()를 통해 넘어온 Memo임시 저장
        var mMemo: Memo? = null

        //9
        //삭제버튼을 누르면 반응하는 메서드 만들기
        init {
            binding.btnDelete.setOnClickListener {
                //12
                //SQLite의 데이터를 먼저 삭제하고 listData데이터도 삭제
                //deleteMemo는 null을 허용하지 않지만 mMemo는 null허용설정이 되었기 때문에 !!강제함
                helper?.deleteMemo(mMemo!!)
                listData.remove(mMemo)

                //12-1
                //어댑터 갱신
                notifyDataSetChanged()

            }

        }//init

        //13
        //메모내욜 전체보기를 위한 xml 약간 수정...
        //item_recycler.xml

        //5
        fun setMemo(memo: Memo){
            binding.textNum.text = "${memo.num}"
            binding.textContent.text = memo.content
            val sdf = SimpleDateFormat("yyyy/MM/dd hh:mm:ss")
            binding.textDateTime.text = sdf.format(memo.datetime).toString()

            //11-1
            //setMemo()를 통해 넘어온 Memo임시 저장
            this.mMemo = memo

        }

    }//Holder

}//RecyclerAdapter

//7
//MainActivity.kt 에서 모든 코드를 조합해 동작가능하게 작성
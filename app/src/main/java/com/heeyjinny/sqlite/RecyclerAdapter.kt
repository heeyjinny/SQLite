package com.heeyjinny.sqlite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.heeyjinny.sqlite.databinding.ItemRecyclerBinding
import java.text.SimpleDateFormat

//리사이클러뷰 어댑터를 상속받는 어댑터클래스 생성
//리사이클러뷰 목록만들기 과정과 동일함...
//2
class RecyclerAdapter: RecyclerView.Adapter<Holder>() {
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
}//RecyclerAdapter

//1
class Holder(val binding: ItemRecyclerBinding): RecyclerView.ViewHolder(binding.root){
    //5
    fun setMemo(memo: Memo){
        binding.textNum.text = "${memo.num}"
        binding.textContent.text = memo.content
        val sdf = SimpleDateFormat("yyyy/MM/dd hh:mm:ss")
        binding.textDateTime.text = sdf.format(memo.datetime).toString()
    }

}//Holder

//7
//MainActivity.kt 에서 모든 코드를 조합해 동작가능하게 작성
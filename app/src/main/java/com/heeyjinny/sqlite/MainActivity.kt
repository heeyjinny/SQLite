package com.heeyjinny.sqlite

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.heeyjinny.sqlite.databinding.ActivityMainBinding

//1
//관계형 데이터베이스 SQLite사용하기
//app - java 밑 패키지명 우클릭 - New - Kotlin Class/File 생성
//SqliteHelper.kt 클래스 생성

class MainActivity : AppCompatActivity() {

    //뷰바인딩
    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    //2
    //SqliteHelper클래스 생성하고 변수에 저장
    //설정했던 파라미터: Context, 데이터베이스명, 팩토리(null로 지정하였으므로 적지 않아도 됨), 버전정보
    val helper = SqliteHelper(this, "memo", 1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //뷰바인딩
        setContentView(binding.root)

        //3
        //어댑터 생성
        val adapter = RecyclerAdapter()

        //8
        //데이터 삭제를 위해 생성해둔 helper를 어댑터에 전달
        //RecyclerAdapter.kt에 helper프로퍼티 생성...
        adapter.helper = helper

        //4
        //어댑터에있는 목록(listData)에서 가져온 데이터 세팅(데이터조회메서드 select)
        adapter.listData.addAll(helper.selectMemo())

        //5
        //리사이블러뷰 위젯에 어댑터를 연결하고 레이아웃 매니저 설정
        binding.recyclerMemo.adapter = adapter
        binding.recyclerMemo.layoutManager = LinearLayoutManager(this)

        //6
        //리사이클러뷰 아이템 목록에 있는 저장버튼에 클릭이벤트 설정
        binding.buttonSave.setOnClickListener {

            //6-1
            //조건식을 사용하여 메모입력 위젯인 EditText에 값이 있으면 해당 내용으로 Memo클래스 생성
            if (binding.editText.text.toString().isNotEmpty()){

                //6-2
                //입력한 텍스트 값이 있다면 Memo클래스를 생성해 파라미터로 값을 전달하고 변수에 저장
                val memo = Memo(null, binding.editText.text.toString(), System.currentTimeMillis())
                //6-3
                //helper클래스의 insertMemo()메서드에 변수memo의 값을 전달해 데이터베이스에 저장
                helper.insertMemo(memo)
                //6-4
                //저장이 끝났으면 어댑터의 데이터 모두 초기화
                adapter.listData.clear()
                //6-5
                //데이터베이스에서 새로운 목록을 읽어와 어댑터에 다시 세팅하고 갱신(새로고침 개념...)
                //새로 생성되는 메모에는 번호가 자동 입력되므로 번호를 갱신하기 위해 새로운 데이터를 세팅함
                adapter.listData.addAll(helper.selectMemo())
                //6-6
                //어댑터의 세팅이 끝났다는 것(데이터가 변경되었다는 것) 알려주고 갱신
                adapter.notifyDataSetChanged()
                //5-6
                //editText위젯에 써있는 텍스트 내용 지워서(빈 칸으로) 초기화
                binding.editText.setText("")

            }

        }//클릭리스너...

        //7
        //메모 목록에 삭제버튼 추가하여 메모 삭제 기능 구현
        //item_recycler.xml 수정...

    }//onCreate
}//ManinActivity
package com.heeyjinny.sqlite

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.heeyjinny.sqlite.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    //뷰바인딩
    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //뷰바인딩
        setContentView(binding.root)

        //1
        //관계형 데이터베이스 SQLite사용하기
        //app - java 밑 패키지명 우클릭 - New - Kotlin Class/File 생성
        //SqliteHelper.kt 클래스 생성

    }//onCreate
}//ManinActivity
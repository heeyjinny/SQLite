package com.heeyjinny.sqlite

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

//1
//SQLite 데이터베이스를 사용하기 위해서는 SQLiteOpenHelper클래스를 상속 받아야 함
//SQLiteOpenHelper: 생성 시 파라미터로 Context, 데이터베이스명, 팩토리, 버전정보가 필요함
//팩토리는 사용하지 않아도 되므로 나머지 세 가지 정보를 SqliteHelper클래스의 생성자에 파라미터로 정의 후
//상속받은 SQLiteOpenHelpe에 전달(전달 시 생성자에는 없지만 팩토리는 null값으로 전달함)
open class SqliteHelper(context: Context, name: String, version: Int)
    : SQLiteOpenHelper(context, name, null, version) {

    //2
    //SQLiteOpenHelper의 필수 메서드 Implement

    //3
    //onCreate의 첫 번째 파라미터로 사용할 데이터베이스가 전달됨
    //파라미터명은 변경함: p0 -> db, p1 -> oldVersion, p2 -> newVersion
    //onUgrade: 생성한 SqliteHelper클래스에 전달되는 버전 정보가 변경되었을 때 현재 생성되어있는
    //데이터베이스의 버전과 비교해 더 높으면 포출됨, 버전 변경이 없다면 호출되지 않는 메서드

    override fun onCreate(db: SQLiteDatabase?) {

        //3-1
        //테이블 생성쿼리 작성 후 실행
        //데이터베이스가 생성되어 있으면 테이블 생성쿼리는 더 이상 실행되지 않음
        //val create = "create table memo (num integer primary key, content text, datetime integer)"
        //3-1-1
        //테이블 생성 코드 보기좋게 정리
        val create = "create table memo " +
                "(num integer primary key, " +
                "content text, " +
                "datetime integer)"

        //3-2
        //테이블 생성 실행: db의 execSQL()메서드에
        //테이블생성쿼리를 가지고 있는 변수(create)를 전달해서 실행
        db?.execSQL(create)

    }//onCreate

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }//onUpgrade

    //5
    //데이터 삽입 메서드(INSERT) 구현: insertMemo메서드 생성
    //SQLiteOpenHelper를 이용해 값을 입력할 때 코틀린의 Map처럼
    //키, 값 형태로 사용되는 ContentValues클래스 사용: ContentValues.put("컬럼명","값")
    //num는 null값을 허용했고 자동으로 값이 증가되기 때문에 메서드에 작성할 필요 없음
    fun insertMemo(memo: Memo){

        val values = ContentValues()
        values.put("content", memo.content)
        values.put("datetime", memo.datetime)

        //5-1
        //상속받은 SQLiteOpenHelper에 이미 구현된 writableDatabase사용
        //writableDatabase에 테이블명과 작성한 변수values값을 전달해 insert()하고
        //사용 후 close() 호출하여 닫기 필수
        val wd = writableDatabase
        wd.insert("memo", null, values)
        wd.close()

    }//insertMemo()

    //6
    //데이터 조회 메서드(SELECT) 정의
    //조회 메서드는 반환값이 있어서 함수 내 변수로 반환할 값을 선언하고 반환코드 작성
    fun selectMemo(): MutableList<Memo>{

        //6-1
        //반환할 값 선언
        val list = mutableListOf<Memo>()

        //6-1
        //메모 전체 데이터 조회쿼리 작성
        val select = "select * from memo"

        //6-2
        //상속받은 SQLiteOpenHelper에 이미 구현된 readableDatabase사용
        //읽기전용 데이터베이스를 변수에 담기
        val rd = readableDatabase

        //6-3
        //데이터 베이스의 rawQuery()메서드를 사용하여 작성했던 조회쿼리(select)를 담아 실행 시
        //커서(Cursor)형태로 값이 반환됨
        //Cursor(커서): 데이터셋을 처리할 때 현재 위치를 포함하는 데이터 요소
        //커서사용 시 쿼리를 통해 반환된 데이터 셋으로 반복문으로 반복해 하나씩 처리할 수 있음
        //커서를 사용하면 데이터읽기 -> 다음 줄 이동의 단순 로직으로 데이터 쉽게 처리 가능
        val cursor = rd.rawQuery(select, null)

        //6-4
        //커서 사용하기
        //커서의 moveToNext()메서드 사용
        //moveToNext(): 다음 줄에 사용할 수 있는 레코드가 있는지 여부를 반환하고 해당커서를 다음 위치로 이동시킴
        //레코드가 없으면 반복문을 빠져나가고 모든 레코드를 읽을 때까지 반복함
        while (cursor.moveToNext()){

            //6-5
            //컬럼명으로 조회해서 위치값으로 값 꺼내기
            //반복문을 돌면서 테이블에 정의된 3개 칼럼(num, content, datetime)에서 값을 꺼낸 후 변수에 담기

            //6-6
            //컬럼에서 값을 꺼내기 위해 먼저 몇 번째 컬럼인지를 컬럼명으로 조회하여 변수에 저장
            val numIdx = cursor.getColumnIndex("num") //테이블에서 num컬럼의 순서1 저장
            val contentIdx = cursor.getColumnIndex("content") //테이블에서 content컬럼의 순서2 저장
            val dateIdx = cursor.getColumnIndex("datetime") //테이블에서 datetime컬럼의 순서3 저장

            //6-7
            //위 변수에 저장해둔 컬럼의 위치(순서)에 값 저장
            val num = cursor.getLong(numIdx)
            val content = cursor.getString(contentIdx)
            val datetime = cursor.getLong(dateIdx)

            //6-8
            //변수에 저장해둔 값들로 Memo클래스 생성하고 반환할 목록(list)에 더하기
            list.add(Memo(num, content, datetime))

        }

        //6-9
        //커서와 읽기전용 베이터베이스 모두 닫기
        cursor.close()
        rd.close()

        //6-1-1
        //반환코드
        return list

    }//selectMemo()

    //7
    //데이터 수정 메서드(UPDATE) 정의
    //INSERT와 동일하게 ContentValuse를 사용해 수정할 값 저장
    //ContentValues.put("컬럼명","값")
    fun updateMemo(memo: Memo){
        val values = ContentValues()
        values.put("content", memo.content)
        values.put("datetime", memo.datetime)

        //7-1
        //writeableDatabase의 update()메서드 사용히여 수정 뒤 close()호출
        //update(): 파라미터 4개, 테이블명, 수정할 값, 수정할 조건, 세번째 값에 매핑할 값
        //세 번째 파라미터에 조건인 num칼럼을 넣으면 값도 자동으로 할당되기 때문에 네 번째 파라미터에 null사용
        val wd = writableDatabase
        wd.update("memo", values, "num = ${memo.num}", null)
        wd.close()

    }//updateMeno()

    //8
    //데이터 삭제 메서드(DELETE) 정의
    //복잡한 데이터베이스를 다룰 때 쿼리를 직접 작성하면 데이터를 더 정밀하게 다룰 수 있음
    //삭제 메서드는 앞과 다르게 SqliteHelper클래스에 쿼리를 직접 입력해서 데이터를 삭제하는 코드로 작성
    fun deleteMemo(memo: Memo){

        //8-1
        //삭제쿼리 작성 후 변수 delete에 저장
        //삭제쿼리 구조: DELETE FROM 테이블명 WHERE 조건식("컬럼명 = 값")
        val delete = "delete from memo where num = ${memo.num}"

        //8-2
        //writeableDatabase의 execSQL()메서드 사용히여 쿼리 실행 후 close()호출
        //execSQL()메서드를 사용해 쿼리를 직접 실행할 수 있음
        val db = writableDatabase
        db.execSQL(delete)
        db.close()

    }//deleteMemo()

    //9
    //CRUD생성 완료, 이제 화면 만들어 MainActivity.kt에 연결하기
    //화면 구성: 두개의 XML파일 수정(메인액티비티, 리사이클러뷰 추가한 파일)
    //activity_main.xml 수정하기...

}//SqliteHelper

//4
//데이터클래스(Memo)생성하여 정의
//num와 datetime의 타입: 데이터베이스와 숫자의 범위가 서로 다르기 때문에 Long으로 타입을 변경하여 사용
//항상 특별한 이유가 없으면 SQLite에서 INTEGER로 선언한 것은 소스코드에서 Long으로 사용함
//num의 null(?)허용: primary key의 옵션으로 값이 자동 증가되기 때문에 데이터 삽입 시 필요하지 않아서 허용
data class Memo(var num: Long?, var content: String, var datetime: Long)
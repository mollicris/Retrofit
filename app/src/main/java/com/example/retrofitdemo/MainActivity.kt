package com.example.retrofitdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.liveData
import retrofit2.Response


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val text_view = findViewById<TextView>(R.id.text_view)

        val retService : AlbumService = RetrofitInstance
            .getRetrofitInstance()
            .create(AlbumService::class.java)

        val responseLiveData:LiveData<Response<Albums>> = liveData {
            val response : Response<Albums> = retService.getAlbums()
            emit(response)
        }

        //Code for observe that livedata
        responseLiveData.observe(this, Observer {
            val albumsList = it.body()?.listIterator()
            if (albumsList != null){
                while (albumsList.hasNext()){
                    val albumItem = albumsList.next()
                    //Log.i("MYTAG",albumItem.title)
                    val result :String = " "+"Album Title : ${albumItem.title}"+"\n"+
                    " "+"Album id : ${albumItem.id}"+"\n"+
                    " "+"User id : ${albumItem.userId}"+"\n\n\n"
                    text_view.append(result)
                }
            }
        })
    }
}
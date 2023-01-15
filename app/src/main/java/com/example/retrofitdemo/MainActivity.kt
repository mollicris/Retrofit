package com.example.retrofitdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.liveData
import retrofit2.Response


class MainActivity : AppCompatActivity() {

    private lateinit var retService: AlbumService
    private lateinit var text_view :TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

         text_view = findViewById(R.id.text_view)

         retService  = RetrofitInstance
            .getRetrofitInstance()
            .create(AlbumService::class.java)
        getRequestWithQueryParameters()
        //getRequestWithPathParameters()
        uploadAlbum()

    }

    private fun getRequestWithQueryParameters(){
        val responseLiveData:LiveData<Response<Albums>> = liveData {
            //val response : Response<Albums> = retService.getAlbums()
            val response : Response<Albums> = retService.getSortedAlbums(3)
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

    private fun getRequestWithPathParameters(){
        //path parameterexample
        val pathResponse : LiveData<Response<AlbumItem>> = liveData {
            val response : Response<AlbumItem> = retService.getAlbum(3)
            emit(response)
        }
        pathResponse.observe(this, Observer {
            val title : String? = it.body()?.title
            Toast.makeText(applicationContext,title,Toast.LENGTH_LONG).show()
        })
    }

    private fun uploadAlbum(){
        val album = AlbumItem(101,"Album Tile test",3)

        val postResponse : LiveData<Response<AlbumItem>> = liveData {
            val response : Response<AlbumItem> = retService.uploadAlbum(album)
            emit(response)
        }
        postResponse.observe(this, Observer {
            val receivedAlbumsItem : AlbumItem? = it.body()
            val result :String = " "+"Album Title : ${receivedAlbumsItem?.title}"+"\n"+
                    " "+"Album id : ${receivedAlbumsItem?.id}"+"\n"+
                    " "+"User id : ${receivedAlbumsItem?.userId}"+"\n\n\n"
            text_view.append(result)
        })
    }
}
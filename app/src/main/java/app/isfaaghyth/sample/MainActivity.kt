package app.isfaaghyth.sample

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import app.isfaaghyth.gql.Gql
import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val popularArtistQuery = """
            {
                popular_artists {
                    artists {
                        name
                    }
                }
            }
        """.trimIndent()

        GlobalScope.launch {
            Gql().setUrl("https://metaphysics-production.artsy.net/")
                    .query(popularArtistQuery)
                    .requestTo<Data> {
                        // data
                    }
                    .error {
                        throw it
                    }

        }
    }

}
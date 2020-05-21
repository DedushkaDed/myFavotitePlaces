package gleb.kalinin.myfavoriteplaces

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import gleb.kalinin.myfavoriteplaces.models.Place
import gleb.kalinin.myfavoriteplaces.models.UserMap
import kotlinx.android.synthetic.main.activity_main.*

private const val TAG = "MainActivity"
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val userMaps = generateSampleData()

        // Set layout manager on the recycler view (rv)
        rvMaps.layoutManager = LinearLayoutManager (this)
        // Set adapter on the recycler view (rv)
        rvMaps.adapter = MapsAdapter (this, userMaps, object: MapsAdapter.OnClickListener{
            override fun onItemClick(position: Int) {
                Log.i(TAG, "onItemClick $position")
                // When user taps on view in RV, navigate to new Activity
                val intent = Intent(this@MainActivity, DisplayMapActivity::class.java)
                startActivity(intent)
            }
        })




    }


    private fun generateSampleData(): List<UserMap> {
        return listOf(
            UserMap(
                "Воспоминания номер 1 тайтл",
                listOf(
                    Place("Центр г.Тюмень", "Лучшее место в Тюменской области.",57.149879, 65.539604),
                    Place("Ашан с вкусной выпечкой", "Дешевые цены",57.116692, 65.551523),
                    Place("Мыс, школа", "Школа моего детства",57.116692, 65.551523)
                )
            ),
            UserMap(
                "Номер 2",
                listOf(
                    Place("Кафе <Семейное> ", "Посетить на выходных",57.149879, 65.539604),
                    Place("Ашан с вкусной выпечкой", "Дешевые цены",57.116692, 65.551523),
                    Place("Овощи фрукты", "Мой магазин",57.152467, 65.651256)
                )
            ),
            UserMap(
                "Номер 3",
                listOf(
                    Place("Мыс, школа", "Школа моего детства",57.116692, 65.551523),
                    Place("Центр г.Тюмень", "Лучшее место в Тюменской области.",57.149879, 65.539604),
                    Place("Овощи фрукты", "Мой магазин",57.152467, 65.651256)
                )
            )
        )
    }
}

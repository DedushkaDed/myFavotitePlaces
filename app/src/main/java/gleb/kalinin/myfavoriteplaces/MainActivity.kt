package gleb.kalinin.myfavoriteplaces

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import gleb.kalinin.myfavoriteplaces.models.Place
import gleb.kalinin.myfavoriteplaces.models.UserMap
import kotlinx.android.synthetic.main.activity_main.*

const val EXTRA_MAP_TITLE = "EXTRA_MAP_TITLE"
const val EXTRA_USER_MAP = "EXTRA_USER_MAP"
private const val REQUEST_CODE = 1234
private const val TAG = "MainActivity"
class MainActivity : AppCompatActivity() {

    private lateinit var userMaps: MutableList<UserMap>
    private lateinit var mapAdapter: MapsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        userMaps = generateSampleData().toMutableList()
        // Set layout manager on the recycler view (rv)
        rvMaps.layoutManager = LinearLayoutManager (this)
        // Set adapter on the recycler view (rv)
        mapAdapter =  MapsAdapter (this, userMaps, object: MapsAdapter.OnClickListener{
            override fun onItemClick(position: Int) {
                Log.i(TAG, "onItemClick $position")
                // When user taps on view in RV, navigate to new Activity
                val intent = Intent(this@MainActivity, DisplayMapActivity::class.java)
                intent.putExtra(EXTRA_USER_MAP, userMaps[position])
                startActivity(intent)
            }
        })
        rvMaps.adapter = mapAdapter

        fabCreateMap.setOnClickListener {
            Log.i(TAG, "Taped on FAB")
            showAlertDialog()

        }
    }

    private fun showAlertDialog() {
        val mapFormView = LayoutInflater.from(this).inflate(R.layout.dialog_create_map, null)
        val dialog =
            AlertDialog.Builder(this)
                .setTitle("Заголовок")
                .setView(mapFormView)
                .setNegativeButton("Отменить",null)
                .setPositiveButton("Сохранить", null)
                .show()

        // Если пользователь нажал на СОХРАНИТЬ ->
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener {
            val title = mapFormView.findViewById<EditText>(R.id.etTitle).text.toString()

            if (title.trim().isEmpty()) {
                Toast.makeText(this, "Заголовок не может быть пустым", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            // Направляем пользователя в CreateMapActivity
            val intent = Intent(this@MainActivity, CreateMapActivity::class.java)
            intent.putExtra(EXTRA_MAP_TITLE, title)
            startActivityForResult(intent, REQUEST_CODE)
            dialog.dismiss()
        }
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // Get new map from the data
            // SerializableExtra(Data) -> Получили новые данные о маркерах
            val userMap = data?.getSerializableExtra(EXTRA_USER_MAP) as UserMap
            Log.i(TAG, "onActivityResult сохранил НОВЫЕ МАРКЕРЫ!${userMap.title}")

            userMaps.add(userMap)
            // Notify adapter something has changed in the data.
            mapAdapter.notifyItemInserted(userMaps.size - 1)

        }
        super.onActivityResult(requestCode, resultCode, data)
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

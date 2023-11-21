package ipca.budget.a20731

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import java.util.Date




class MainActivity : AppCompatActivity() {

    val budgetItems: ArrayList<BudgetItem> = arrayListOf<BudgetItem>(

        BudgetItem("Cebolinhs 1kg", 1.50, Date()),
        BudgetItem("batatinhas fritas", 1.0, Date()),
        BudgetItem("Pao de lo de ovar ", 9.80, Date())
    )//adicionar items hardcoded


    lateinit var listviewBudgetItem: ListView //listview
    val adapter = BudgetAdapter() // adaptter que vou usar a listview criada anteriormente
    val resultLauncher =
        registerForActivityResult(
            ActivityResultContracts
            .StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK){
                it.data?.let {intent ->
                    val name = intent.extras?.getString(BudgetItemDetailActivity.DATA_NAME)?:""
                    val value = intent.extras?.getDouble(BudgetItemDetailActivity.DATA_VALUE)?:0.0
                    val date = Date()

                    val budgetItem = BudgetItem(name!!, value!!,date) //Cria
                    budgetItems.add(budgetItem) //cria

                    adapter.notifyDataSetChanged()
                }
            }
        }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        listviewBudgetItem=findViewById<ListView>(R.id.listViewBudgetItems)
        listviewBudgetItem.adapter=adapter

        findViewById<Button>(R.id.ButtonAdd).setOnClickListener(){
            val intent = Intent(this,BudgetItemDetailActivity::class.java )
            resultLauncher.launch(intent)
        }//cria um intent no notao add , para comunicar  activity budgetitemsdetail


        findViewById<Button>(R.id.ButtonSort).setOnClickListener() {
            budgetItems.sortBy{
                it.value
            }
            adapter.notifyDataSetChanged()

        }//quando clicar no botao sort , ordena a lista

        findViewById<Button>(R.id.ButtonTotal).setOnClickListener() {
            // in Activity
            var result=0.0
            for (b in budgetItems){
                result+=b.value

            }
            Toast.makeText(this, "Total${result}", Toast.LENGTH_LONG).show()

        }//quando clico no botao total array list percurrido e usa-se um somatirio para devolver o resultado TOAST FAZ APARECER POPUP-ECRA





    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        return true
    }// FAZ COM QUE APARECE O MENU NO ECRA  e retorna verdadeiro


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection.
        return when (item.itemId) {
            R.id.action_sort -> {
                budgetItems.sortBy{
                    it.value
                }
                adapter.notifyDataSetChanged()
                true
            }
            R.id.action_total -> {
                var result=0.0
                for (b in budgetItems){
                    result+=b.value

                }
                Toast.makeText(this, "Total${result}", Toast.LENGTH_LONG).show()
                true
            }
            R.id.action_add  -> {
                val intent = Intent(this,BudgetItemDetailActivity::class.java )
                resultLauncher.launch(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }//quando e selecionado uma opçao do menu

    inner class BudgetAdapter:BaseAdapter(){
        override fun getCount(): Int {
            return budgetItems.size
        }

        override fun getItem(p0: Int): Any {
            return budgetItems[p0]
        }

        override fun getItemId(p0: Int): Long {
            return 0
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View  {
            val rootView = layoutInflater.inflate(R.layout.row_budget_item,parent, false)
            val textViewDescription = rootView.findViewById<TextView>(R.id.textViewDescription)
            val textViewValue = rootView.findViewById<TextView>(R.id.textViewValue)
            val textViewDate = rootView.findViewById<TextView>(R.id.textViewDate)

            textViewDescription.text=budgetItems[position].description
            textViewValue.text=budgetItems[position].value.toString()
            textViewDate.text=budgetItems[position].date.toString()

            return rootView
        }

    }//permite juntar o budget items com a row.



}
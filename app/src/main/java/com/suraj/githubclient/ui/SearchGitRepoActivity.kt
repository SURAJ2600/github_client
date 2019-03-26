package com.suraj.githubclient.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.UiThread
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.suraj.githubclient.R
import com.suraj.githubclient.Utilities.Util
import kotlinx.android.synthetic.main.search_box_layout.*


/*Created by suraj on 26/03/2019*/
class SearchGitRepoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        val spinner = findViewById<Spinner>(R.id.spinner)
        if (spinner != null) {
            val arrayAdapter = ArrayAdapter(this, R.layout.spinner_textview, Util.spinnerValues)
            spinner.adapter = arrayAdapter

            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                  }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // Code to perform some action when nothing is selected
                }
            }
        }


        val query = savedInstanceState?.getString(LAST_SEARCH_QUERY) ?: DEFAULT_QUERY
        initSearch(query)



    }


    /**
     * @param query
     * perform the query search operation
     */
    private fun initSearch(query: String) {
        et_search.setText(query)

        et_search.setOnEditorActionListener({ _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO) {
                saveSearchResult()
                true
            } else {
                false
            }
        })
        et_search.setOnKeyListener({ _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                saveSearchResult()
                true
            } else {
                false
            }
        })


    }

    private fun saveSearchResult() {
        et_search.text.trim().let {
            if (it.isNotEmpty()) {
                //list.scrollToPosition(0)
               // viewModel.searchRepo(it.toString())
               // adapter.submitList(null)
            }
        }
    }



    @UiThread
    private  fun showVisibileViews(status:Boolean)
    {




    }


    /**
     *
     *override the method for saving the quer value during configuration changes
     */

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(LAST_SEARCH_QUERY, "")
    }


    /**
     * Constant value
     */
    companion object {
        private const val LAST_SEARCH_QUERY: String = "last_search_query"
        private const val DEFAULT_QUERY = "Github"
    }
}

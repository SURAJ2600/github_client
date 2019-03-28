package com.suraj.githubclient.ui.SearchRepo

import android.app.Dialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.arch.paging.PagedList
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.UiThread
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.android.codelabs.paging.model.Repo
import com.suraj.githubclient.Extensions.obtainViewModel
import com.suraj.githubclient.R
import com.suraj.githubclient.Utilities.Injection
import com.suraj.githubclient.Utilities.LogsUtils
import com.suraj.githubclient.Utilities.Util
import com.suraj.githubclient.ui.ViewPullRequest.ViewRepoPullActvity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_view_repo_pull_actvity.*
import kotlinx.android.synthetic.main.search_box_layout.*


/*Created by suraj on 26/03/2019*/
class SearchGitRepoActivity : AppCompatActivity() {
    private lateinit var viewModel: SearchRepoViewModel
    private val adapter = RepositoryAdapter()
    private lateinit var progressDialog:Dialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        progressDialog =Util.ShowProgressView(this)


        // get the view model
        viewModel =obtainViewModels()





        viewModel.apply {

            state.observe(this@SearchGitRepoActivity, Observer {
                when(it)
                {

                    SearchRepoViewModelState.NONE ->
                    {
                        progressDialog.dismiss()


                    }
                    SearchRepoViewModelState.LOADING ->
                    {
                        progressDialog.show()
                    }

                    SearchRepoViewModelState.ERROR ->{
                        progressDialog.dismiss()

                    }
                    SearchRepoViewModelState.SUCESS ->
                    {
                        progressDialog.dismiss()


                    }
                    SearchRepoViewModelState.INTERNET->{
                        LogsUtils.snackBarAction(search_view, getString(R.string.internet))

                    }


                }


            })


        }




        //Init the layout views
        initViews()


        //getting the value from saved instance if device configuration changes happne



            val query = savedInstanceState?.getString(LAST_SEARCH_QUERY) ?: DEFAULT_QUERY
            viewModel.searchRepo(query)
            initSearch(query)


    }


    private fun initViews() {

        //Wire up the recycler

        repo_list.apply {
            val decoration = DividerItemDecoration(this@SearchGitRepoActivity, DividerItemDecoration.VERTICAL)
            addItemDecoration(decoration)
        }

        setupScrollListener()
        initAdapter()






        //Spinner view for choosing the type of serach keyword [Repo] or [User]
        if (spinner != null) {
            val arrayAdapter = ArrayAdapter(this, R.layout.spinner_textview, Util.spinnerValues)
            spinner.adapter = arrayAdapter
            spinner.setSelection(0)
            viewModel.setSpinnerValue(spinner.selectedItemPosition)
            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    if (position != null) {
                        viewModel.setSpinnerValue(position)

                    }

                }

                override fun onNothingSelected(parent: AdapterView<*>) {

                }
            }

        }

    }


    /**
     *
     * initialize the adapter and wire it to the  [repo_list]
     */

    private fun initAdapter() {
        repo_list.adapter = adapter
        viewModel.repos.observe(this, Observer<PagedList<Repo>> {
            Log.d("Activity", "list: ${it?.size}")
            showVisibileViews(it?.size == 0)
            adapter.submitList(it)
        })
        viewModel.networkErrors.observe(this, Observer<String> {
            Toast.makeText(this, "\uD83D\uDE28 Wooops ${it}", Toast.LENGTH_LONG).show()
        })



        adapter.viewPullClicked={

            var repo=adapter.getItemFromPostion(it)
            var data=Bundle()
            data.putString("owner_name",""+repo.owner.login)
            data.putString("repo_name",""+repo.name)
            data.putString("user_avatar",""+repo?.owner.avatar_url)

            val intent = Intent(this, ViewRepoPullActvity::class.java)
            intent.putExtras(data)
            startActivity(intent)




        }

    }


    /**
     *method to listen scroll changes of recylerview to load more data
     */

    private fun setupScrollListener() {

        val layoutManager = repo_list.layoutManager as LinearLayoutManager
        repo_list.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val totalItemCount = layoutManager.itemCount
                val visibleItemCount = layoutManager.childCount
                val lastVisibleItem = layoutManager.findLastVisibleItemPosition()

                viewModel.listScrolled(visibleItemCount, lastVisibleItem, totalItemCount)
            }
        })
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
                repo_list.scrollToPosition(0)
                viewModel.searchRepo(it.toString())
                adapter.submitList(null)
            }
        }
    }



    @UiThread
    private  fun showVisibileViews(status:Boolean)
    {

        if (status) {
            emptyList.visibility = View.VISIBLE
            repo_list.visibility = View.GONE
        } else {
            emptyList.visibility = View.GONE
            repo_list.visibility = View.VISIBLE
        }


    }


    /**
     *
     *override the method for saving the quer value during configuration changes
     */

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(LAST_SEARCH_QUERY, viewModel.lastQueryValue())
    }


    /**
     * Constant value
     */
    companion object {
        private const val LAST_SEARCH_QUERY: String = "last_search_query"
        private const val DEFAULT_QUERY = "Github"
    }



    fun obtainViewModels(): SearchRepoViewModel = obtainViewModel(SearchRepoViewModel::class.java)
}

/*
 * Copyright (C) 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.suraj.githubclient.ui.SearchRepo

import android.arch.paging.PagedListAdapter
import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.android.codelabs.paging.model.Repo
import com.suraj.githubclient.R
import kotlinx.android.synthetic.main.repo_view_item.view.*

/**
 * Adapter for the list of repositories.
 */
class RepositoryAdapter : PagedListAdapter<Repo, RecyclerView.ViewHolder>(REPO_COMPARATOR) {


    var viewPullClicked:(postion:Int)->Unit={

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return RepoViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val repoItem = getItem(position)
        if (repoItem != null) {

            Log.e("repoitems",">>>"+repoItem)
            (holder as RepoViewHolder).bind(repoItem)

            (holder as RepoViewHolder).itemView.bt_pulls.setOnClickListener {
                viewPullClicked.invoke(position)

            }
        }
    }

    fun getItemFromPostion(position: Int) :Repo
    {
     return  getItem(position)!!
    }

    companion object {
        private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<Repo>() {
            override fun areItemsTheSame(oldItem: Repo, newItem: Repo): Boolean =
                    oldItem.fullName == newItem.fullName

            override fun areContentsTheSame(oldItem: Repo, newItem: Repo): Boolean =
                    oldItem == newItem
        }
    }
    /**
     * View Holder for a [Repo] RecyclerView list item.
     */
    class RepoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val name: TextView = view.repo_name
        private val description: TextView = view.repo_description
        private val language: TextView = view.repo_language
        private var repo: Repo? = null

        init {

        }

        fun bind(repo: Repo?) {
            if (repo == null) {
                val resources = itemView.resources
               // name.text = resources.getString()
                description.visibility = View.GONE
                language.visibility = View.GONE
              //  stars.text = resources.getString(R.string.unknown)
               // forks.text = resources.getString(R.string.unknown)
            } else {
                showRepoData(repo)
            }
        }

        private fun showRepoData(repo: Repo) {
            this.repo = repo
            name.text = repo.fullName

            // if the description is missing, hide the TextView
            var descriptionVisibility = View.GONE
            if (repo.description != null) {
                description.text = repo.description
                descriptionVisibility = View.VISIBLE
            }
            description.visibility = descriptionVisibility



            // if the language is missing, hide the label and the value
            var languageVisibility = View.GONE
            if (!repo.language.isNullOrEmpty()) {
                val resources = this.itemView.context.resources
               // language.text = resources.getString(R.string.language, repo.language)
                languageVisibility = View.VISIBLE
            }
            language.visibility = languageVisibility
        }

        companion object {
            fun create(parent: ViewGroup): RepoViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.repo_view_item, parent, false)
                return RepoViewHolder(view)
            }
        }
    }
}
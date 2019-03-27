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

package com.suraj.githubclient.ui.ViewPullRequest

import android.content.Context
import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.android.codelabs.paging.model.Repo
import com.suraj.githubclient.R
import com.suraj.githubclient.model.PullRepoModel
import kotlinx.android.synthetic.main.pullrequest_view_item.view.*

/**
 * Adapter for the list of repositories.
 */
class ViewPullRequestAdapter(var context: Context) : ListAdapter<PullRepoModel, RecyclerView.ViewHolder>(REPO_COMPARATOR) {





    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PullRequestViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val pullRepoData = getItem(position)
        if (pullRepoData != null) {

            (holder as PullRequestViewHolder).bind(pullRepoData,context)
        }
    }

    fun getItemFromPostion(position: Int) :PullRepoModel
    {
     return  getItem(position)
    }

    companion object {
        private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<PullRepoModel>() {
            override fun areItemsTheSame(oldItem: PullRepoModel, newItem: PullRepoModel): Boolean =
                    oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: PullRepoModel, newItem: PullRepoModel): Boolean =
                    oldItem == newItem
        }
    }
    /**
     * View Holder for a [Repo] RecyclerView list item.
     */
    class PullRequestViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val img_user= view.iv_userprofile
        private val tv_username = view.tv_username
        private val tv_pullmessage = view.tv_pullrequestbody
        private var pullRepoDatas: PullRepoModel? = null


        init {

        }

        fun bind(repo: PullRepoModel?, context: Context) {
            if (repo == null) {

            } else {
                showRepoData(repo,context)
            }
        }

        private fun showRepoData(pullRepoData: PullRepoModel,context:Context) {
            this.pullRepoDatas = pullRepoData

            Glide
                .with(context)
                .load(pullRepoDatas?.useravatar)
                .centerCrop()
                .placeholder(R.drawable.ic_user)
                .into(img_user);
            tv_username.text = "Username : "+pullRepoDatas?.username
            tv_pullmessage.text="Pull Request Message : "+pullRepoDatas?.userpullmessage

        }

        companion object {
            fun create(parent: ViewGroup): PullRequestViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.pullrequest_view_item, parent, false)
                return PullRequestViewHolder(view)
            }
        }
    }
}
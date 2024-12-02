package com.example.projectmanager

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment(R.layout.fragment_home) {


    private lateinit var user: User

    private lateinit var recyclerView: RecyclerView
    private lateinit var projectList: ArrayList<Project>
    private lateinit var RecyclerViewProjectsAdapter: RecyclerViewProjectsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            user = it.getSerializable("user") as User
        }
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.recyclerView)

        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = layoutManager

        projectList = java.util.ArrayList(user.projects)
        RecyclerViewProjectsAdapter = RecyclerViewProjectsAdapter(projectList)
        recyclerView.adapter = RecyclerViewProjectsAdapter
    }

    companion object {
        @JvmStatic
        fun newInstance(user: User) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putSerializable("user", user)
                }
            }
    }
}
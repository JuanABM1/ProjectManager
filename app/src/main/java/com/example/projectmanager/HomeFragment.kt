package com.example.projectmanager

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

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

        val textViewName = view.findViewById<TextView>(R.id.textViewName)
        textViewName.text = user.nombre

        recyclerView = view.findViewById(R.id.recyclerView)
        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = layoutManager

        projectList = java.util.ArrayList(user.projects)

        RecyclerViewProjectsAdapter = RecyclerViewProjectsAdapter(projectList,
        { project ->
                Toast.makeText(requireContext(), "Clic en: ${project.name_project}", Toast.LENGTH_SHORT).show()
            },
            {
                Toast.makeText(requireContext(), "Clic en: crear", Toast.LENGTH_SHORT).show()
            }
        )
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
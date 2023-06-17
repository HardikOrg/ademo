package com.example.ademo.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.example.ademo.R
import com.example.ademo.databinding.ActivityMainBinding
import com.example.ademo.databinding.NavHeaderBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration

    private val viewModel by viewModels<MainActivityViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = findNavController(R.id.nav_host_fragment)
        appBarConfiguration = AppBarConfiguration(navController.graph, binding.drawer)

        binding.navView.setupWithNavController(navController)

        val header = NavHeaderBinding.bind(binding.navView.getHeaderView(0)).apply {
            image.setImageResource(R.drawable.ic_launcher_foreground)
            image.setBackgroundResource(R.drawable.drawer_account_background)

            name.text = "Account?"
            email.text = "Email?"

            logButton.apply {
                setCompoundDrawablesWithIntrinsicBounds(
                    0, 0, R.drawable.icon_login, 0
                )
                text = "Login"
                setOnClickListener {
                    navController.navigate(R.id.loginFragment)
                    binding.drawer.close()
                }
            }
        }
        viewModel.account.observe(this) {
            if (it == null) {
                header.image.setImageResource(R.drawable.ic_launcher_foreground)
                header.image.setBackgroundResource(R.drawable.drawer_account_background)

                header.name.text = "Account?"
                header.email.text = "Email?"

                header.logButton.apply {
                    setCompoundDrawablesWithIntrinsicBounds(
                        0, 0, R.drawable.icon_login, 0
                    )
                    text = "Login"
                    setOnClickListener {
                        navController.navigate(R.id.loginFragment)
                        binding.drawer.close()
                    }
                }
            } else {
                Glide.with(this)
                    .load(it.imgLink)
                    .into(header.image)
                header.image.setBackgroundResource(0)

                header.name.text = it.name
                header.email.text = it.email

                header.logButton.apply {
                    setCompoundDrawablesWithIntrinsicBounds(
                        0, 0, R.drawable.icon_logout, 0
                    )
                    text = "Logout"
                    setOnClickListener { viewModel.clearAccount() }
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}
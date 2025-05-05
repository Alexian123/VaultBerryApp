package com.alexianhentiu.vaultberryapp.presentation.utils

import androidx.navigation.NavController
import com.alexianhentiu.vaultberryapp.domain.model.entity.DecryptedKey
import com.alexianhentiu.vaultberryapp.presentation.utils.enums.NavRoute

class NavigationManager(
    private val navController: NavController
) {
    private val vaultKeyStateName = "vaultKey"

    fun navigate(route: NavRoute) {
        navController.navigate(route.path)
    }

    fun navigateWithVaultKey(route: NavRoute, vaultKey: DecryptedKey?) {
        navController.currentBackStackEntry?.savedStateHandle?.set(
            key = vaultKeyStateName,
            value = vaultKey
        )
        navController.navigate(route.path)
    }

    fun retrieveVaultKey(): DecryptedKey? {
        return navController
            .previousBackStackEntry?.savedStateHandle?.get<DecryptedKey>(vaultKeyStateName)
    }

    fun goBack() {
        navController.popBackStack()
    }
}
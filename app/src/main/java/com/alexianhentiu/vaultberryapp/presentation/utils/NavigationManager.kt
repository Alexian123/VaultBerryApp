package com.alexianhentiu.vaultberryapp.presentation.utils

import androidx.navigation.NavController
import com.alexianhentiu.vaultberryapp.domain.model.DecryptedKey
import com.alexianhentiu.vaultberryapp.presentation.utils.enums.NavRoute

class NavigationManager(
    private val nacController: NavController
) {
    private val vaultKeyStateName = "vaultKey"

    fun navigate(route: NavRoute) {
        nacController.navigate(route.path)
    }

    fun navigateWithVaultKey(route: NavRoute, vaultKey: DecryptedKey?) {
        nacController.currentBackStackEntry?.savedStateHandle?.set(
            key = vaultKeyStateName,
            value = vaultKey
        )
        nacController.navigate(route.path)
    }

    fun retrieveVaultKey(): DecryptedKey? {
        return nacController
            .previousBackStackEntry?.savedStateHandle?.get<DecryptedKey>(vaultKeyStateName)
    }
}
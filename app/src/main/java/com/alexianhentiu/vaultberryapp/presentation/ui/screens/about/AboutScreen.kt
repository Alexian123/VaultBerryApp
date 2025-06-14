package com.alexianhentiu.vaultberryapp.presentation.ui.screens.about

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ListAlt
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Copyright
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Language
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.alexianhentiu.vaultberryapp.R
import androidx.core.net.toUri
import com.alexianhentiu.vaultberryapp.presentation.ui.components.misc.AboutInfoRow
import com.alexianhentiu.vaultberryapp.presentation.ui.components.topBars.TopBarWithBackButton
import com.alexianhentiu.vaultberryapp.presentation.ui.common.EmailIntentUtils.createEmailIntent
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(
    navController: NavHostController,
) {
    val context = LocalContext.current
    val currentYear = Calendar.getInstance().get(Calendar.YEAR).toString()

    val versionName = remember {
        try {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            packageInfo.versionName
        } catch (_: Exception) {
            "N/A"
        }.toString()
    }

    val appName = remember { context.getString(R.string.app_name) }
    val appDescription = remember { context.getString(R.string.app_description) }
    val developerName = remember { context.getString(R.string.developer_name) }
    val contactEmail = remember { context.getString(R.string.contact_email) }
    val websiteUrl = remember { context.getString(R.string.website_url) }
    val licenseUrl = remember { context.getString(R.string.license_url) }

    val errorSendingEmailMsg = stringResource(R.string.error_sending_email)
    val errorOpeningWebsiteMsg = stringResource(R.string.error_opening_website)
    val errorOpeningLicenseMsg = stringResource(R.string.error_opening_license)
    val allRightsReserved = stringResource(R.string.all_rights_reserved)

    Scaffold(
        topBar = {
            TopBarWithBackButton(
                navController = navController,
                title = stringResource(R.string.about_screen_title)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp, vertical = 16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.app_icon_foreground),
                contentDescription = "$appName Logo",
                modifier = Modifier
                    .size(100.dp)
                    .padding(bottom = 16.dp)
            )

            Text(
                text = appName,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Version $versionName",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            Text(
                text = appDescription,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

            AboutInfoRow(
                icon = Icons.Filled.Business,
                label = stringResource(R.string.developer_name_label),
                value = developerName
            )
            AboutInfoRow(
                icon = Icons.Filled.Email,
                label = stringResource(R.string.contact_email_label),
                value = contactEmail,
                isClickable = true,
                onClick = {
                    val intent = createEmailIntent(
                        recipientEmail = contactEmail,
                        subject = "Regarding $appName App"
                    )
                    try {
                        context.startActivity(intent)
                    } catch (e: Exception) {
                        Toast.makeText(context, errorSendingEmailMsg, Toast.LENGTH_SHORT).show()
                        Log.e("AboutScreen", errorSendingEmailMsg, e)
                    }
                }
            )
            AboutInfoRow(
                icon = Icons.Filled.Language,
                label = stringResource(R.string.website_label),
                value = websiteUrl,
                isClickable = true,
                onClick = {
                    val intent = Intent(Intent.ACTION_VIEW, websiteUrl.toUri())
                    try {
                        context.startActivity(intent)
                    } catch (e: Exception) {
                        Toast.makeText(context, errorOpeningWebsiteMsg, Toast.LENGTH_SHORT).show()
                        Log.e("AboutScreen", errorOpeningWebsiteMsg, e)
                    }
                }
            )
            AboutInfoRow(
                icon = Icons.AutoMirrored.Filled.ListAlt,
                label = stringResource(R.string.license_label),
                value = stringResource(R.string.view_license_text),
                isClickable = true,
                onClick = {
                    val intent = Intent(Intent.ACTION_VIEW, licenseUrl.toUri())
                    try {
                        context.startActivity(intent)
                    } catch (e: Exception) {
                        Toast.makeText(context, errorOpeningLicenseMsg, Toast.LENGTH_SHORT).show()
                        Log.e("AboutScreen", errorOpeningLicenseMsg, e)
                    }
                }
            )

            Spacer(modifier = Modifier.weight(1f))

            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

            AboutInfoRow(
                icon = Icons.Filled.Copyright,
                label = stringResource(R.string.copyright_label),
                value = "© $currentYear $developerName. $allRightsReserved."
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AboutScreenPreview() {
    MaterialTheme {
        AboutScreen(
            navController = NavHostController(LocalContext.current),
        )
    }
}
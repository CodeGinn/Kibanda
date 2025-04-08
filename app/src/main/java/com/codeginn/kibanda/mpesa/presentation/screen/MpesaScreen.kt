@file:OptIn(ExperimentalMaterial3Api::class)

package com.codeginn.kibanda.mpesa.presentation.screen

import android.R.attr.enabled
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.codeginn.kibanda.cartcheckout.presentation.viewmodel.CartViewModel
import com.codeginn.kibanda.mpesa.presentation.viewmodel.MpesaViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun MpesaScreen(
    modifier: Modifier = Modifier,
    cartViewModel: CartViewModel = koinViewModel<CartViewModel>(),
    mpesaViewModel: MpesaViewModel = koinViewModel<MpesaViewModel>(),
    navigateToOrderScreen: () -> Unit = {}
){
    val transactionStatus by mpesaViewModel.transactionStatus.collectAsState()
    val phoneNumber by mpesaViewModel.phoneNumber.collectAsState()
    val amount by cartViewModel.totalAmount.collectAsState()
    var phoneError by rememberSaveable {
        mutableStateOf("")
    }

    Scaffold(
        modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Checkout",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Card(
                modifier = Modifier
                    .height(200.dp)
                    .width(320.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceAround,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Amount Payable KES $amount",
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                    OutlinedTextField(
                        value = phoneNumber,
                        onValueChange = {
                            mpesaViewModel.onPhoneChange(it)
                        },
                        modifier = Modifier
                            .padding(horizontal = 12.dp)
                            .fillMaxWidth()
                            .height(100.dp),
                        label = {
                            Text("MPESA Phone Number (07/01XXXXXXXX)")
                        },
                        isError = phoneError.isNotEmpty(),
                        supportingText = {
                            if (phoneError.isNotEmpty()){
                                Text(
                                    text = phoneError,
                                    color = MaterialTheme.colorScheme.error,
                                    style = MaterialTheme.typography.labelMedium
                                )
                            }
                        },
                        trailingIcon = {
                            Text(
                                text = "max(10)",
                                style = MaterialTheme.typography.labelMedium,
                                textAlign = TextAlign.End,
                                modifier = Modifier.padding(end = 8.dp)
                            )
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                    )
                    FilledTonalButton(
                        onClick = {
                            if ((phoneNumber.length == 10 && phoneNumber.startsWith("07")) || (phoneNumber.length == 10 && phoneNumber.startsWith("01"))){
                                val formattedPhoneNumber = "254${phoneNumber.substring(1)}"
                                mpesaViewModel.initiateSTKPayment(formattedPhoneNumber.toLong(), amount)
                            }
                            if (transactionStatus == "O"){
                                navigateToOrderScreen()
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = phoneNumber.length == 10 && phoneNumber.startsWith("07")
                    ) {
                        Text("Pay via Mpesa")
                    }
                    transactionStatus?.let {
                        Text(" Transaction Status: $it")
                    }
                }


            }

        }
    }



}
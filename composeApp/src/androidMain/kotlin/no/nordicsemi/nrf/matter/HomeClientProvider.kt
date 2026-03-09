package no.nordicsemi.nrf.matter

import android.accounts.Account
import android.content.Context
import android.util.Log
import com.google.home.Home
import com.google.home.HomeClient
import com.google.home.HomeConfig
import com.google.home.UserAccount
import io.github.aakira.napier.Napier
import kotlin.time.measureTimedValue

/*
 * Copyright (c) 2025, Nordic Semiconductor
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of
 * conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list
 * of conditions and the following disclaimer in the documentation and/or other materials
 * provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its contributors may be
 * used to endorse or promote products derived from this software without specific prior
 * written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY
 * OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
class HomeClientProvider(
    private val applicationContext: Context,
    private val homeConfig: HomeConfig
) {
    private var homeClient: HomeClient? = null

    companion object {
        private const val TAG = "HomeClientProvider"
    }

    /**
     * Returns the current [HomeClient] instance. If the client has not been initialized, it will be
     * created.
     *
     * @return The [HomeClient] instance.
     */
    fun getClient(): HomeClient {
        synchronized(this) {
            if (homeClient == null) {
                Log.d(TAG, "create a new HomeClient instance since homeClient is null")
                homeClient = createHomeClient("", homeConfig)
            }
            return homeClient!!
        }
    }

    private fun createHomeClient(userId: String, config: HomeConfig): HomeClient {
        val client = measureTimedValue {
            if (userId.isEmpty()) {
                Napier.i(tag = TAG) { "createHomeClient without account" }
                Home.getClient(applicationContext, homeConfig = config)
            } else {
                Napier.i(tag = TAG) { "createHomeClient with account $userId" }
                Home.getClient(
                    applicationContext,
                    account = lazy {
                        UserAccount.GoogleAccount(
                            account = Account(
                                userId,
                                "com.google"
                            )
                        )
                    },
                    homeConfig = config,
                )
            }
        }.also {
            Napier.i(tag = TAG) { "HomeSDK construction took ${it.duration.inWholeMilliseconds} ms." }
        }
        return checkNotNull(client.value) { "HomeClient is null. Ensure createClient() succeeded." }
    }
}
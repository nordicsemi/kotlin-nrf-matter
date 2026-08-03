package no.nordicsemi.nrf.matter.commission

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

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

/**
 * A full-screen camera view that scans a Matter onboarding QR code and reports the decoded payload.
 *
 * This is a platform-provided slot rather than an `expect`/`actual`: platforms that support QR
 * scanning install an implementation here (Android does so at startup). Platforms that don't
 * simply leave it `null`, in which case the setup screen hides the scan option and falls back to
 * manual code entry. Keeping it optional avoids forcing a scanner implementation onto every target.
 *
 * The lambda receives:
 * - `onResult`: invoke with the raw decoded string (e.g. `MT:...`) on the first successful scan.
 * - `onCancel`: invoke when the user dismisses the scanner or the camera is unavailable.
 */
typealias QrScannerContent = @Composable (
    modifier: Modifier,
    onResult: (String) -> Unit,
    onCancel: () -> Unit,
) -> Unit

/** Installed by a platform that supports QR scanning; `null` means manual entry only. */
var qrScannerContent: QrScannerContent? = null

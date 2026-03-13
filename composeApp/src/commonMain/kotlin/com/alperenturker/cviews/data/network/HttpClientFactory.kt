package com.alperenturker.cviews.data.network

import io.ktor.client.HttpClient

expect fun createHttpClient(): HttpClient

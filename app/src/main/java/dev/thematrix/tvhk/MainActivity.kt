package dev.thematrix.tvhk

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.security.ProviderInstaller
import java.security.KeyManagementException
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier
import javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setUpSSL()
    }

    private fun setUpSSL() {
        try {
            ProviderInstaller.installIfNeeded(applicationContext)
            var sslContext: SSLContext? = SSLContext.getInstance("TLSv1.2")
            try {
                val trustAllCerts: Array<TrustManager> = arrayOf(object : X509TrustManager {
                    @Throws(CertificateException::class)
                    override fun checkClientTrusted(
                        chain: Array<java.security.cert.X509Certificate>, authType: String) {
                    }

                    @Throws(CertificateException::class)
                    override fun checkServerTrusted(chain: Array<java.security.cert.X509Certificate>, authType: String) {
                    }

                    override fun getAcceptedIssuers(): Array<X509Certificate?> {
                        return arrayOfNulls(0)
                    }
                })

                sslContext!!.init(null, trustAllCerts, SecureRandom())
                val engine = sslContext.createSSLEngine()
                engine.enabledCipherSuites

                val ignoreHostnameVerifier = HostnameVerifier { s, sslsession ->
                    Log.i("HostNameVer", "WARNING: Hostname is not matched for cert.")
                    true
                }
                setDefaultSSLSocketFactory(sslContext.socketFactory)
                setDefaultHostnameVerifier(ignoreHostnameVerifier)

                Log.i("setUpSSL", "強制使用 TLSv1.2")
                //Toast.makeText(this, "強制使用 TLSv1.2", Toast.LENGTH_SHORT).show()
            } catch (e: KeyManagementException) {
                Toast.makeText(this, "強制使用 TLSv1.2 失敗", Toast.LENGTH_SHORT).show()
            }
        } catch (e: NoSuchAlgorithmException) {
            Toast.makeText(this, "系統沒有 TLSv1.2", Toast.LENGTH_SHORT).show()
        } catch (e: GooglePlayServicesNotAvailableException) {
            Toast.makeText(this, "系統沒有 Google Play Service", Toast.LENGTH_SHORT).show()
        } catch (e: GooglePlayServicesRepairableException) {
            Toast.makeText(this, "Google Play Service 錯誤", Toast.LENGTH_SHORT).show()
        }
    }
}
